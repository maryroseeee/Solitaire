package cs3500.klondike;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs3500.klondike.model.hw02.BasicCard;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.Rank;
import cs3500.klondike.model.hw02.Suit;

/**
 * Utils for {@link cs3500.klondike.model.hw02.KlondikeModel}.
 */
public class KlondikeUtils {

  /**
   * Checks if game has started and throws exception if it has not.
   *
   * @param started if the game has started or not
   */
  public static void startException(boolean started) {
    if (!started) {
      throw new IllegalStateException("Game never started");
    }
  }

  /**
   * Makes a standard playing deck with all cards.
   *
   * @return the constructed deck
   */
  public static List<Card> makeStandardDeck() {
    List<Card> standardDeck = new ArrayList<>();
    List<Suit> suits = new ArrayList<>(Arrays.asList(Suit.SPADES, Suit.HEARTS,
            Suit.CLUBS, Suit.DIAMONDS));
    List<Rank> ranks = new ArrayList<>(Arrays.asList(Rank.ACE, Rank.TWO,
            Rank.THREE, Rank.FOUR, Rank.FIVE, Rank.SIX, Rank.SEVEN, Rank.EIGHT,
            Rank.NINE, Rank.TEN, Rank.JACK, Rank.QUEEN, Rank.KING));
    for (Suit s : suits) {
      for (Rank r : ranks) {
        standardDeck.add(new BasicCard(r, s, false));
      }
    }
    return standardDeck;
  }

  /**
   * Checks all exceptions in moving to pile moves.
   *
   * @param src       source pile number
   * @param dest      destination pile number
   * @param model     model to base move after
   * @param modelType type of model
   * @param numCards  number of cards to move
   */
  public static void findErrorsToPMove(int src, int dest, KlondikeModel model, String moveType,
                                       String modelType, int numCards) {
    Card srcCard = null;
    if (dest < 0 || dest >= model.getNumPiles()) {
      throw new IllegalArgumentException("Invalid destination pile");
    }

    if (moveType.equals("p")) {
      if (src == dest) {
        throw new IllegalArgumentException("Cannot move pile to the same pile number");
      } else if (model.getPileHeight(src) - numCards < 0) {
        throw new IllegalArgumentException("Invalid number of cards to be moved");
      }
      srcCard = model.getCardAt(src, model.getPileHeight(src) - numCards);
    } else {
      if (model.getNumDraw() < 1) {
        throw new IllegalStateException("Draw pile is empty");
      }
      srcCard = model.getDrawCards().get(0);
    }

    boolean allSameSuit = false;
    if (modelType.equals("w")) {
      allSameSuit = sameSuit(model, src, numCards);
    }

    try {
      if (!(BasicCard.validPileMove(srcCard, model.getCardAt(dest,
              model.getPileHeight(dest) - 1), modelType, allSameSuit))) {
        throw new IllegalStateException("Move is not allowable");
      }
    } catch (IllegalArgumentException e) {
      if (modelType.equals("b") && !(BasicCard.validEmptyPileMove(srcCard))) {
        throw new IllegalStateException("Move is not allowable");
      }
    }
  }

  /**
   * Checks if all cards to be moved are in the same suit.
   *
   * @param model    the model it is based on
   * @param src      the source pile number
   * @param numCards the number of cards to be moved
   * @return true if they are all in the same suit, false otherwise
   */
  public static boolean sameSuit(KlondikeModel model, int src, int numCards) {
    Suit s = model.getCardAt(src, model.getPileHeight(src) - 1).getSuit();
    for (int i = 1; i <= numCards; i++) {
      if (!model.getCardAt(src, model.getPileHeight(src) - i).getSuit().equals(s)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks all exceptions in moving to foundation moves.
   *
   * @param src      source pile number
   * @param dest     destination pile number
   * @param model    model to base move after
   * @param moveType type of move (draw or pile)
   */
  public static void findErrorsToFMove(int src, int dest, KlondikeModel model, String moveType) {
    Card srcCard = null;
    if (dest < 0 || dest >= model.getNumFoundations()) {
      throw new IllegalArgumentException("Invalid Foundation Number");
    }
    if (moveType.equals("p")) {
      if (model.getPileHeight(src) < 1) {
        throw new IllegalStateException("Can't move card from an empty source pile");
      }
      srcCard = model.getCardAt(src, model.getPileHeight(src) - 1);
    } else {
      if (model.getDrawCards().isEmpty()) {
        throw new IllegalStateException("Draw pile is empty");
      } else {
        srcCard = model.getDrawCards().get(0);
      }
    }
    if (!(BasicCard.validFoundationMove(srcCard, model.getCardAt(dest)))) {
      throw new IllegalStateException("Move is not allowable");
    }
  }

  /**
   * Signal if the game pile number is valid.
   *
   * @param pileNum the 0-based index (from the left) of the pile
   * @param length  length of the pile
   * @return true if pile number is valid, false otherwise
   */
  public static boolean invalidPile(int pileNum, int length) {
    return pileNum < 0 || pileNum >= length || length < 1;
  }

  /**
   * Gets a card from deck based on string.
   *
   * @param deck deck to find the card in
   * @param card string of card to find
   * @return the string as a card or null if it is not found
   */
  public static Card getCardFromDeck(List<Card> deck, String card) {
    for (int i = 0; i < deck.size(); i++) {
      if (deck.get(i).toString().equals(card)) {
        return deck.get(i);
      }
    }
    return null;
  }

  /**
   * Makes a deck out of a list of card strings.
   *
   * @param cards list of card strings
   * @param deck  deck to get the cards from
   * @return a new deck of cards that matches the list of string cards
   */
  public static List<Card> makeSmallDeck(List<String> cards, List<Card> deck) {
    List<Card> sDeck = new ArrayList<>();
    for (String card : cards) {
      sDeck.add(getCardFromDeck(deck, card));
    }
    return sDeck;
  }

  /**
   * Gets last card in a pile.
   *
   * @param piles   the list of card lists the pile of the card needed to be found is in
   * @param pileNum column of the desired card (0-indexed from the left)
   * @return the last card in a given pile
   */
  public static Card getLastInAPile(List<List<Card>> piles, int pileNum) {
    return piles.get(pileNum).get(piles.get(pileNum).size() - 1);
  }

  /**
   * Gets top card need to move.
   *
   * @param piles    the list of card lists the pile of the card needed to be found is in
   * @param pileNum  column of the desired card (0-indexed from the left)
   * @param numCards the number of cards to be moved
   * @return the top card needed to move
   */
  public static Card getCardToMove(List<List<Card>> piles, int pileNum, int numCards) {
    return piles.get(pileNum).get(piles.get(pileNum).size() - numCards);
  }

  /**
   * Gets last card in a list.
   *
   * @param pile column of the desired card (0-indexed from the left)
   * @return the last card in a given pile
   */
  public static Card getLastInList(List<Card> pile) {
    return pile.get(pile.size() - 1);
  }

  /**
   * Makes last card in a list visible.
   *
   * @param pile column of the desired card (0-indexed from the left)
   * @return the last card in a given pile but made visible
   */
  public static Card makeLastVisible(List<Card> pile) {
    return pile.set(pile.size() - 1, getLastInList(pile).updateVisible(true));
  }

  /**
   * Makes a given card visible.
   *
   * @param pile column of the desired card (0-indexed from the left)
   * @param card row of the desired card (0-indexed from the top)
   * @return the last card in a given pile
   */
  public static Card makeCardVisible(List<Card> pile, int card) {
    return pile.set(card, pile.get(card).updateVisible(true));
  }

  /**
   * Checks if there are no available moves in the draw deck.
   *
   * @param dd the draw deck
   * @param fd the foundation decks
   * @param pd the pile decks
   * @return true if there's no moves available, false if otherwise.
   */
  public static boolean checkIfOverDraw(List<Card> dd, List<List<Card>> fd, List<List<Card>> pd,
                                        String modelType) {
    for (List<Card> f : fd) {
      for (Card draw : dd) {
        if (f.isEmpty()) {
          if (draw.getRank().equals(Rank.ACE)) {
            return false;
          }
        } else if (BasicCard.validFoundationMove(draw, getLastInList(f))) {
          return false;
        }
      }
    }
    for (List<Card> p : pd) {
      for (Card draw : dd) {
        if (p.isEmpty()) {
          if (draw.getRank().equals(Rank.KING)) {
            return false;
          }
        } else if (BasicCard.validPileMove(draw, getLastInList(p), modelType, false)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Checks if there are no available moves from pile to foundation deck.
   *
   * @param fd the foundation decks
   * @param pd the pile decks
   * @return true if there's no moves available, false if otherwise.
   */
  public static boolean checkIfOverPileToF(List<List<Card>> fd, List<List<Card>> pd) {
    for (List<Card> f : fd) {
      for (List<Card> p : pd) {
        if (!p.isEmpty()) {
          if (f.isEmpty()) {
            if (getLastInList(p).getRank().equals(Rank.ACE)) {
              return false;
            }
          } else if (BasicCard.validFoundationMove(getLastInList(p), getLastInList(f))) {
            return false;
          }
        }
      }
    }
    return true;
  }

  /**
   * Checks if there are no available moves from pile to another pile.
   *
   * @param src  the 0-based index (from the left) of the pile to be moved
   * @param dest the 0-based index (from the left) of the destination pile for the
   *             moved cards
   * @return true if there's no moves available, false if otherwise.
   */
  public static boolean checkIfOverPiletoP(List<Card> src, List<List<Card>> dest, String type) {
    for (List<Card> destP : dest) {
      for (Card p : src) {
        if (p.getVisibility()) {
          if (destP.isEmpty()) {
            if (p.getRank().equals(Rank.KING) && (src.indexOf(p) != 0)) {
              return false;
            }
          } else {
            if ((src.indexOf(p)) != 0 && !(src.get(src.indexOf(p) - 1).getVisibility())
                    && BasicCard.validPileMove(p, getLastInList(destP), type, false)) {
              return false;
            } else if ((src.indexOf(p)) == 0 && BasicCard.validPileMove(p,
                    getLastInList(destP), type, false)) {
              return false;
            } else if (BasicCard.validPileMove(p, getLastInList(destP), type, false)) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  /**
   * Counts the amount of aces in a deck.
   *
   * @param deck deck to find the card in
   * @return the amount of aces in a deck.
   */
  public static int getAmountOfAces(List<Card> deck) {
    int count = 0;
    for (Card card : deck) {
      if (card.getRank().equals(Rank.ACE)) {
        count++;
      }
    }
    return count;
  }

  /**
   * Makes sure a deck is valid with cards that can be grouped into equal-length,
   * consecutive runs of cards (each one starting at an Ace, and each of a single
   * suit).
   *
   * @param deck the given deck to check the validity of
   * @return true if valid, false otherwise
   */
  public static boolean evenDeck(List<Card> deck) {
    List<Card> spades = new ArrayList<>();
    List<Card> hearts = new ArrayList<>();
    List<Card> clubs = new ArrayList<>();
    List<Card> diamonds = new ArrayList<>();
    for (Card c : deck) {
      if (c.getSuit() == Suit.SPADES) {
        spades.add(c);
      } else if (c.getSuit() == Suit.HEARTS) {
        hearts.add(c);
      } else if (c.getSuit() == Suit.CLUBS) {
        clubs.add(c);
      } else if (c.getSuit() == Suit.DIAMONDS) {
        diamonds.add(c);
      }

    }
    List<Integer> allCards = new ArrayList<>(Arrays.asList(spades.size(),
            hearts.size(), clubs.size(), diamonds.size()));

    List<Integer> nonEmptySuits = new ArrayList<>();
    for (int count : allCards) {
      if (count != 0) {
        nonEmptySuits.add(count);
      }
    }
    int num = nonEmptySuits.get(0);
    for (int finalCount : nonEmptySuits) {
      if (finalCount != num) {
        return false;
      }
    }
    return true;
  }


  /**
   * Checks to see if a game has been won.
   *
   * @param model the model to go through
   * @return true if won, false otherwise
   */
  public static boolean isGameWon(KlondikeModel model) {
    if (model.getDrawCards().isEmpty()) {
      int count = 0;
      for (int i = 0; i < model.getNumPiles(); i++) {
        if (model.getPileHeight(i) != 0) {
          count++;
        }
      }
      if (count == 0) {
        return true;
      }
    }
    return false;
  }

  /**
   * Throws exceptions in start game method.
   *
   * @param deck        the deck of cards
   * @param numPiles    the number of piles
   * @param numDraw     the number of draw cards
   * @param started     boolean if game has started
   * @param cascadeSize number of cards in the cascade piles
   */
  public static void startGameExceptions(List<Card> deck, int numPiles, int numDraw,
                                         boolean started, int cascadeSize) {
    try {
      if (started) {
        throw new IllegalStateException("Cannot start a game that has already started");
      } else if (deck.isEmpty() || deck.contains(null) || deck == null || numPiles < 0
              || numDraw < 0 || deck.size() < cascadeSize) {
        throw new IllegalArgumentException("invalid deck 1");
      } else if (!KlondikeUtils.evenDeck(deck)) {
        throw new IllegalArgumentException("invalid deck2");
      }
    } catch (NullPointerException e) {
      throw new IllegalArgumentException("Invalid deck3");
    }
  }

  /**
   * Makes the cascade piles in a Klondike game.
   *
   * @param cascadeSize number of cards in the cascade piles
   * @param numPiles    number of piles
   * @param visibility  if card is visible or not
   * @param deck        the whole deck of cards
   * @return the cascade piles
   */
  public static List<List<Card>> makeCascadePiles(int cascadeSize, int numPiles,
                                                  boolean visibility, List<Card> deck) {
    List<List<Card>> piles = new ArrayList<List<Card>>();
    List<Card> cascadeDeck = deck.subList(0, cascadeSize);
    int startingPoint = 0;
    int currentCard = 0;

    for (int i = 0; i < numPiles; i++) {
      piles.add(new ArrayList<>());
    }

    while (currentCard < cascadeSize) {
      while (startingPoint < numPiles) {
        for (int j = startingPoint; j < numPiles; j++) {
          piles.get(j).add(cascadeDeck.get(currentCard));
          currentCard++;
        }
        startingPoint++;
      }
    }

    int pileNum = 1;
    for (List<Card> p : piles) {
      for (Card c : p) {
        c.updateVisible(visibility);
      }
      if (p.size() != pileNum) {
        throw new IllegalArgumentException("Too many piles for the amount of cards");
      }
      pileNum++;
    }

    return piles;
  }

  /**
   * Makes the draw deck in a Klondike game.
   *
   * @param undoneDrawDeck the deck of cards with un-updated
   * @param numDraw        the number of visible cards
   * @return the draw deck
   */
  public static List<Card> makeDraw(List<Card> undoneDrawDeck, int numDraw) {
    for (int i = 0; i < undoneDrawDeck.size(); i++) {
      if (i < numDraw) {
        undoneDrawDeck.set(i, undoneDrawDeck.get(i).updateVisible(true));
      } else {
        undoneDrawDeck.set(i, undoneDrawDeck.get(i).updateVisible(false));
      }
    }
    return undoneDrawDeck;
  }


}




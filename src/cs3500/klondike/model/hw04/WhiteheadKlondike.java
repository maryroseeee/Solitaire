package cs3500.klondike.model.hw04;

import cs3500.klondike.KlondikeUtils;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This is a stub implementation of the {@link cs3500.klondike.model.hw02.KlondikeModel} interface.
 * You may assume that the actual implementation of WhiteheadKlondike will have a zero-argument
 * (i.e. default) constructor, and that all the methods below will be implemented.  You may not make
 * any other assumptions about the implementation of this class (e.g. what fields it might have,
 * or helper methods, etc.).
 */
public class WhiteheadKlondike implements KlondikeModel {

  private List<Card> mainDeck;
  private List<List<Card>> foundationDeck;
  private List<Card> drawDeck;
  private List<List<Card>> piles;
  private boolean started;
  private int visibleDraw;


  /**
   * Constructs a basic klondike.
   */
  public WhiteheadKlondike() {
    this.mainDeck = KlondikeUtils.makeStandardDeck();
    this.foundationDeck = new ArrayList<>();
    this.drawDeck = new ArrayList<>();
    this.piles = new ArrayList<>();
    this.started = false;
    this.visibleDraw = 0;


  }

  @Override
  public List<Card> getDeck() {
    return this.mainDeck;
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw) {
    int cascadeSize = 0;
    for (int i = 1; i <= numPiles; i++) {
      cascadeSize += i;
    }

    KlondikeUtils.startGameExceptions(deck, numPiles, numDraw, this.started, cascadeSize);
    this.visibleDraw = numDraw;
    this.started = true;
    if (shuffle) {
      Collections.shuffle(deck);
      this.mainDeck = deck;
    } else {
      this.mainDeck = deck;
    }

    this.piles = KlondikeUtils.makeCascadePiles(cascadeSize, numPiles, true, this.mainDeck);

    for (int i = 0; i < KlondikeUtils.getAmountOfAces(this.mainDeck); i++) {
      this.foundationDeck.add(new ArrayList<Card>());
    }

    this.drawDeck = new ArrayList<>(deck.subList(cascadeSize, deck.size()));
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile) {
    KlondikeUtils.startException(this.started);
    KlondikeUtils.findErrorsToPMove(srcPile, destPile, this, "p", "w", numCards);
    for (int i = numCards; i > 0; i--) {
      this.piles.get(destPile).add(KlondikeUtils.getCardToMove(this.piles, srcPile, i));
      this.piles.get(srcPile).remove(KlondikeUtils.getCardToMove(this.piles, srcPile, i));
    }
  }

  @Override
  public void moveDraw(int destPile) {
    KlondikeUtils.startException(this.started);
    KlondikeUtils.findErrorsToPMove(0, destPile, this, "d", "w", 0);

    this.piles.get(destPile).add(this.drawDeck.get(0));
    this.drawDeck.remove(0);
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile) {
    KlondikeUtils.startException(this.started);
    KlondikeUtils.findErrorsToFMove(srcPile, foundationPile, this, "p");

    this.foundationDeck.get(foundationPile).add(KlondikeUtils.getLastInAPile(this.piles, srcPile));
    this.piles.get(srcPile).remove(KlondikeUtils.getLastInAPile(this.piles, srcPile));
  }

  @Override
  public void moveDrawToFoundation(int foundationPile) {
    KlondikeUtils.startException(this.started);
    KlondikeUtils.findErrorsToFMove(0, foundationPile, this, "d");

    this.foundationDeck.get(foundationPile).add(this.drawDeck.get(0));
    this.drawDeck.remove(0);
  }

  @Override
  public void discardDraw() {
    KlondikeUtils.startException(this.started);

    if (this.drawDeck.isEmpty()) {
      throw new IllegalStateException("Move is not allowable");
    }
    if (this.getNumDraw() >= this.drawDeck.size()) {
      this.drawDeck.add(this.drawDeck.get(0));
    } else {
      this.drawDeck.add((this.drawDeck.get(0)).updateVisible(false));
    }
    this.drawDeck.remove(0);
    if (this.getNumDraw() < this.drawDeck.size()) {
      KlondikeUtils.makeCardVisible(this.drawDeck, this.getNumDraw() - 1);
    }
  }

  @Override
  public int getNumRows() {
    KlondikeUtils.startException(this.started);
    int numRows = 0;
    for (List<Card> piles : this.piles) {
      int length = piles.size();
      if (length > numRows) {
        numRows = length;
      }
    }
    return numRows;
  }

  @Override
  public int getNumPiles() {
    KlondikeUtils.startException(this.started);
    return this.piles.size();
  }

  @Override
  public int getNumDraw() {
    KlondikeUtils.startException(this.started);
    return this.getDrawCards().size();

  }

  @Override
  public boolean isGameOver() {
    KlondikeUtils.startException(this.started);

    int pCount = 0;
    List<List<Card>> pileRemove = new ArrayList<>(this.piles);
    Iterator<List<Card>> iterator = pileRemove.iterator();
    while (iterator.hasNext()) {
      List<Card> src = iterator.next();
      iterator.remove();
      if (!KlondikeUtils.checkIfOverPiletoP(src, pileRemove, "w")) {
        pCount++;
      }
      pileRemove = new ArrayList<>(this.piles);
    }
    if (!this.drawDeck.isEmpty()) {
      return KlondikeUtils.checkIfOverDraw(this.drawDeck, this.foundationDeck, this.piles, "w")
              && KlondikeUtils.checkIfOverPileToF(this.foundationDeck, this.piles) && (pCount == 0);
    } else {
      return KlondikeUtils.checkIfOverPileToF(this.foundationDeck, this.piles) && (pCount == 0);
    }
  }

  @Override
  public int getScore() {
    KlondikeUtils.startException(this.started);
    int result = 0;
    for (int i = 0; i < this.getNumFoundations(); i++) {
      if (!this.foundationDeck.get(i).isEmpty()) {
        result += this.getCardAt(i).getValue();
      }
    }
    return result;
  }

  @Override
  public int getPileHeight(int pileNum) {
    KlondikeUtils.startException(this.started);
    if (this.piles.size() - 1 < pileNum || pileNum < 0) {
      throw new IllegalArgumentException("Invalid pile number");
    }
    return this.piles.get(pileNum).size();
  }

  @Override
  public boolean isCardVisible(int pileNum, int card) {
    KlondikeUtils.startException(this.started);
    if (this.piles.size() - 1 < pileNum || pileNum < 0) {
      throw new IllegalArgumentException("Invalid pile number");
    } else if (this.piles.get(pileNum).size() - 1 < card || card < 0) {
      throw new IllegalArgumentException("Invalid card number");
    }
    return true;
  }

  @Override
  public Card getCardAt(int pileNum, int card) {
    KlondikeUtils.startException(this.started);
    if (KlondikeUtils.invalidPile(pileNum, this.piles.size())) {
      throw new IllegalArgumentException("Invalid pile number");
    } else if (KlondikeUtils.invalidPile(card, this.getPileHeight(pileNum))) {
      throw new IllegalArgumentException("Invalid card number");
    } else if (getPileHeight(pileNum) < 1) {
      throw new IllegalArgumentException("Pile is empty");
    }

    return this.piles.get(pileNum).get(card);
  }

  @Override
  public Card getCardAt(int foundationPile) {
    KlondikeUtils.startException(this.started);
    if (KlondikeUtils.invalidPile(foundationPile, this.getNumFoundations())) {
      throw new IllegalArgumentException("Invalid foundation pile number");
    } else if (this.foundationDeck.get(foundationPile).isEmpty()) {
      return null;
    }
    return KlondikeUtils.getLastInAPile(this.foundationDeck, foundationPile);
  }

  @Override
  public List<Card> getDrawCards() {
    KlondikeUtils.startException(this.started);
    if (this.drawDeck.size() <= this.visibleDraw) {
      return this.drawDeck;
    } else {
      List<Card> visDraw = new ArrayList<>();
      for (int i = 0; i < this.visibleDraw; i++) {
        visDraw.add(this.drawDeck.get(i).updateVisible(true));
      }
      return visDraw;
    }
  }

  @Override
  public int getNumFoundations() {
    KlondikeUtils.startException(this.started);
    return this.foundationDeck.size();
  }
}

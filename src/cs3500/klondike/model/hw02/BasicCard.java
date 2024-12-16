package cs3500.klondike.model.hw02;

/**
 * This is an implementation of the {@link Card}
 * represents a basic card.
 */
public class BasicCard implements Card {
  private final Suit suit;
  private final Rank rank;
  private final char color;
  private boolean visible;
  private int redrawCount;


  /**
   * Constructs a basic card that is not visible.
   *
   * @param rank the card's rank
   * @param suit the card's suit
   */
  public BasicCard(Rank rank, Suit suit) {
    this.suit = suit;
    this.rank = rank;
    this.color = suit.getColor();
    this.visible = false;
    this.redrawCount = 0;
  }

  /**
   * Constructs a basic card.
   *
   * @param rank    the card's rank
   * @param suit    the card's rank
   * @param visible the card's visibility
   */
  public BasicCard(Rank rank, Suit suit, boolean visible) {
    this.suit = suit;
    this.rank = rank;
    this.color = suit.getColor();
    this.visible = visible;
    this.redrawCount = 0;
  }

  /**
   * Constructs a basic card.
   *
   * @param rank      the card's rank
   * @param suit      the card's rank
   * @param visible   the card's visibility
   * @param numRedraw the number of redraws on a card so far
   */
  public BasicCard(Rank rank, Suit suit, boolean visible, int numRedraw) {
    this.suit = suit;
    this.rank = rank;
    this.color = suit.getColor();
    this.visible = visible;
    this.redrawCount = numRedraw;
  }


  /**
   * Checks if a card can move to an empty pile.
   *
   * @param src the source card
   * @return true if the move is allowable, false otherwise
   */
  public static boolean validEmptyPileMove(Card src) {
    return src.getValue() == 13;
  }

  /**
   * Checks if a card can move on to another pile card.
   *
   * @param src         the source card
   * @param dest        the destination of the card
   * @param modelType   the type of model
   * @param allSameSuit true if they are all the cards to be moved
   *                    are the same suit.
   * @return true if the move is allowable, false otherwise
   */
  public static boolean validPileMove(Card src, Card dest, String modelType, boolean allSameSuit) {
    if (modelType.equals("w")) {
      return allSameSuit && (src.getColor() == dest.getColor());
    }
    if (dest.getColor() == 'r') {
      return (src.getColor() == 'b') && (src.getValue() == dest.getValue() - 1);
    } else if (dest.getColor() == 'b') {
      return (src.getColor() == 'r') && (src.getValue() == dest.getValue() - 1);
    } else {
      return false;
    }
  }

  /**
   * Checks if a card can move on to a foundation pile card.
   *
   * @param src  the source card
   * @param dest the destination foundation pile
   * @return true if the move is allowable, false otherwise
   */
  public static boolean validFoundationMove(Card src, Card dest) {
    if (dest == null) {
      return src.getValue() == 1;
    } else {
      return (dest.getSuit().equals(src.getSuit())
              && src.getValue() == (dest.getValue() + 1));
    }
  }

  @Override
  public int getValue() {
    return this.rank.getValue();
  }

  @Override
  public boolean getVisibility() {
    return this.visible;
  }

  @Override
  public Card increaseRedrawCount() {
    return new BasicCard(this.rank, this.suit, this.visible, this.redrawCount + 1);
  }

  @Override
  public int getRedrawCount() {
    return this.redrawCount;
  }

  @Override
  public Card updateVisible(boolean v) {
    return new BasicCard(this.rank, this.suit, v);
  }

  @Override
  public String toString() {
    return rank.toString() + suit.toString();
  }

  @Override
  public Rank getRank() {
    return this.rank;
  }

  @Override
  public Suit getSuit() {
    return this.suit;
  }

  @Override
  public char getColor() {
    return this.color;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof BasicCard)) {
      return false;
    } else {
      Card c = (Card) o;
      return this.suit == c.getSuit() && this.rank == c.getRank();
    }
  }

  @Override
  public int hashCode() {
    switch (this.suit) {
      case SPADES:
        return this.getValue() * 53 + 1;
      case HEARTS:
        return this.getValue() * 53 + 2;
      case CLUBS:
        return this.getValue() * 53 + 3;
      case DIAMONDS:
        return this.getValue() * 53 + 4;
      default:
        throw new IllegalArgumentException("Bad Basic Card");
    }
  }


}

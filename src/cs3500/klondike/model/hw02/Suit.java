package cs3500.klondike.model.hw02;

/**
 * This is an implementation of the {@link Card}
 * represents a card's suit.
 */
public enum Suit implements Card {
  SPADES, CLUBS, HEARTS, DIAMONDS;

  @Override
  public String toString() {
    if (this == SPADES) {
      return "♠";
    } else if (this == CLUBS) {
      return "♣";
    } else if (this == HEARTS) {
      return "♡";
    } else {
      return "♢";
    }
  }


  @Override
  public int getValue() {
    throw new IllegalArgumentException("Cannot get value of a suit");
  }

  @Override
  public Rank getRank() {
    throw new IllegalArgumentException("Cannot get rank of a suit");
  }

  @Override
  public boolean getVisibility() {
    throw new IllegalArgumentException("Cannot get visibility of a suit");
  }

  @Override
  public Suit getSuit() {
    return this;
  }

  @Override
  public Card updateVisible(boolean v) {
    throw new IllegalArgumentException("Cannot change visibility of suit");
  }

  @Override
  public char getColor() {
    if (this == SPADES || this == CLUBS) {
      return 'b';
    } else {
      return 'r';
    }
  }

  @Override
  public Card increaseRedrawCount() {
    throw new IllegalArgumentException("Cannot change redraw amount of suit");
  }

  @Override
  public int getRedrawCount() {
    throw new IllegalArgumentException("Cannot get redraw amount of a suit");
  }


}

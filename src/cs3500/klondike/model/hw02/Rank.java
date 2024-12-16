package cs3500.klondike.model.hw02;

/**
 * This is an implementation of the {@link Card}
 * represents a card rank.
 */
public enum Rank implements Card {
  TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
  JACK(11), QUEEN(12), KING(13), ACE(1);

  private final int value;

  Rank(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  @Override
  public String toString() {
    if (this == TWO) {
      return "2";
    } else if (this == THREE) {
      return "3";
    } else if (this == FOUR) {
      return "4";
    } else if (this == FIVE) {
      return "5";
    } else if (this == SIX) {
      return "6";
    } else if (this == SEVEN) {
      return "7";
    } else if (this == EIGHT) {
      return "8";
    } else if (this == NINE) {
      return "9";
    } else if (this == TEN) {
      return "10";
    } else if (this == JACK) {
      return "J";
    } else if (this == QUEEN) {
      return "Q";
    } else if (this == KING) {
      return "K";
    } else {
      return "A";
    }
  }

  @Override
  public Rank getRank() {
    return this;
  }

  @Override
  public Suit getSuit() {
    throw new IllegalArgumentException("Cannot get suit of a rank");
  }

  @Override
  public char getColor() {
    throw new IllegalArgumentException("Cannot get color of a rank without the suit");
  }

  @Override
  public Card updateVisible(boolean v) {
    throw new IllegalArgumentException("Cannot change visibility of rank");
  }

  @Override
  public boolean getVisibility() {
    throw new IllegalArgumentException("Cannot get visibility of a rank");
  }

  @Override
  public Card increaseRedrawCount() {
    throw new IllegalArgumentException("Cannot change redraw amount of rank");
  }

  @Override
  public int getRedrawCount() {
    throw new IllegalArgumentException("Cannot get redraw amount of a rank");
  }

}

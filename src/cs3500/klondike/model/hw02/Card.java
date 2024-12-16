package cs3500.klondike.model.hw02;

/**
 * This (essentially empty) interface marks the idea of cards.  You will need to
 * implement this interface in order to use your model.
 * 
 * <p>The only behavior guaranteed by this class is its {@link Card#toString()} method,
 * which will render the card as specified in the assignment.
 * 
 * <p>In particular, you <i>do not</i> know what implementation of this interface is
 * used by the Examplar wheats and chaffs, so your tests must be defined sufficiently
 * broadly that you do not rely on any particular constructors or methods of cards.
 */
public interface Card {

  /**
   * Gets the value of a card.
   *
   * @return the card value
   * @throws IllegalArgumentException if called on a Suit
   */
  int getValue();

  /**
   * Renders a card with its value followed by its suit as one of
   * the following symbols (♣, ♠, ♡, ♢).
   * For example, the 3 of Hearts is rendered as {@code "3♡"}.
   *
   * @return the formatted card
   */
  String toString();

  /**
   * Gets the rank of a card.
   *
   * @return the rank of a card
   * @throws IllegalArgumentException if called on a Suit
   */
  Rank getRank();

  /**
   * Gets the suit of a card.
   *
   * @return the card suit
   * @throws IllegalArgumentException if called on a rank
   */
  Suit getSuit();

  /**
   * Gets the color of a card.
   *
   * @return the card color
   * @throws IllegalArgumentException if called on a rank
   */
  char getColor();

  /**
   * Gets the visibility of a card.
   *
   * @return the card's visibility
   * @throws IllegalArgumentException if called on a rank or suit
   */
  boolean getVisibility();

  /**
   * See's if two card equal one another.
   *
   * @return true if they equal, false otherwise.
   */
  boolean equals(Object o);

  /**
   * Increases the redraw amount by one.
   * @return the updated card
   */
  Card increaseRedrawCount();

  /**
   * Returns the number of redraws on a card.
   * @return the number of redraws on a card
   */
  int getRedrawCount();

  /**
   * Updates the visibility of a card.
   *
   * @return the card with an updated visibility.
   * @throws IllegalArgumentException if called on a rank or suit
   */
  Card updateVisible(boolean v);


}
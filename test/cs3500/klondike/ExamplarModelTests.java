package cs3500.klondike;

import cs3500.klondike.model.hw02.BasicCard;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.Rank;
import cs3500.klondike.model.hw02.Suit;
import cs3500.klondike.view.KlondikeTextualView;
import cs3500.klondike.view.TextualView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link KlondikeModel}.
 */
public class ExamplarModelTests {


  KlondikeModel bigTester;
  List<Card> smallDeck;
  List<Card> longDeck;

  List<String> smallDeckString;
  List<String> smallerDeckString;
  List<String> longDeckString;
  List<String> smallDeckStringDifferent;


  List<Card> smallerDeck;
  KlondikeModel solve;

  KlondikeModel tester;
  KlondikeModel longer;
  KlondikeModel testerDifferent;

  /**
   * initializes examples used in tests.
   */
  @Before
  public void init() {
    bigTester = new BasicKlondike();
    tester = new BasicKlondike();
    solve = new BasicKlondike();
    longer = new BasicKlondike();
    testerDifferent = new BasicKlondike();
    smallDeckString = new ArrayList<String>(Arrays.asList("2♠", "A♠", "A♡",
            "4♡", "4♠", "3♡", "3♠", "2♡"));
    List<Card> smallDeck = KlondikeUtils.makeSmallDeck(smallDeckString,
            bigTester.getDeck());
    tester.startGame(smallDeck, false, 3, 1);
    smallerDeckString = new ArrayList<String>(Arrays.asList("2♡", "A♡", "2♠", "A♠"));
    List<Card> smallerDeck = KlondikeUtils.makeSmallDeck(smallerDeckString,
            bigTester.getDeck());
    solve.startGame(smallerDeck, false, 2, 1);

    longDeckString = new ArrayList<String>(Arrays.asList("A♡", "9♠", "Q♠", "J♠", "K♡",
            "10♠", "5♠", "4♠", "6♠", "7♠", "A♠", "3♡", "2♠", "3♠", "K♠", "Q♡", "J♡",
            "10♡", "9♡", "8♡", "7♡", "6♡", "5♡", "4♡", "8♠", "2♡"));
    longDeck = KlondikeUtils.makeSmallDeck(longDeckString, bigTester.getDeck());
    longer.startGame(longDeck, false, 4, 1);


    List<Card> smallDeckDiff = KlondikeUtils.makeSmallDeck(new ArrayList<String>(Arrays.asList(
                    "4♠", "A♠", "A♡", "3♡", "4♡", "2♠", "3♠", "2♡")), bigTester.getDeck());
    testerDifferent.startGame(smallDeckDiff,
            false, 3, 1);

  }


  // Tests moving wrong number card from draw to a foundation pile.
  @Test
  public void testMoveWrongNumberFromDrawToFoundation() {
    init();
    TextualView longView = new KlondikeTextualView(longer);
    System.out.println(longView.toString());

    longer.moveToFoundation(0, 0);
    longer.discardDraw();
    Assert.assertThrows(Exception.class, () ->
            longer.moveDrawToFoundation(0));
  }

  // Tests moving wrong suit card from draw to a foundation pile
  @Test
  public void testMoveWrongSuitFromDrawToFoundation() {
    init();
    longer.moveToFoundation(0, 0);
    longer.discardDraw();
    longer.discardDraw();
    Assert.assertThrows(Exception.class, () ->
            longer.moveDrawToFoundation(0));
  }

  // Tests moving wrong suit card from a cascade pile to a foundation pile
  @Test
  public void testMoveWrongSuitFromPileToFoundation() {
    init();
    solve.moveDrawToFoundation(0);
    Assert.assertThrows(Exception.class, () ->
            solve.moveToFoundation(0, 0));
  }

  // Tests moving wrong number
  // card from a cascade pile to a foundation pile
  @Test
  public void testMoveWrongNumberFromPileToFoundation() {
    init();
    longer.moveDrawToFoundation(0);
    Assert.assertThrows(Exception.class, () ->
            longer.moveToFoundation(1, 0));
  }

  // Tests moving ace to empty foundation pile
  @Test
  public void testMoveAceFromPile() {
    init();
    longer.moveToFoundation(0, 0);
    Assert.assertTrue(longer.getCardAt(0).equals(new BasicCard(Rank.ACE,
            Suit.HEARTS, true)));
  }

  // Tests moving king to empty pile
  @Test
  public void testMoveKing() {
    init();
    TextualView longerView = new KlondikeTextualView(longer);

    longer.moveToFoundation(0, 0);
    System.out.println(longerView.toString());
    Assert.assertEquals(0, longer.getPileHeight(0));
    Assert.assertEquals(0, longer.getPileHeight(0));
    longer.movePile(1, 1, 0);

    Assert.assertTrue(longer.getCardAt(0, 0).equals(new BasicCard(Rank.KING,
            Suit.HEARTS, true)));
    System.out.println(longerView.toString());

  }

  // Tests moving card the same number on one another fails
  @Test
  public void testMoveSameNumber() {
    init();
    Assert.assertThrows(Exception.class, () ->
            solve.movePile(0, 1, 1));
  }

  // Tries to move pile using too many cards to see if it fails
  @Test
  public void testMovePileTooManyCards() {
    init();
    Assert.assertThrows(Exception.class, () ->
            tester.movePile(0, 2, 2));
  }

  // Tests putting a non-ace card from a pile into an empty foundation
  @Test
  public void testMovePileCardToEmptyFoundationWorksCorrectly() {
    init();
    Assert.assertThrows(Exception.class, () ->
            tester.moveToFoundation(0, 0));
  }

  // Tests putting a non-king card from a pile to an empty pile
  @Test
  public void testNonKingFromPileToEmptyPile() {
    init();
    KlondikeTextualView testerModel = new KlondikeTextualView(tester);

    System.out.println(testerModel.toString());

    tester.movePile(0, 1, 2);
    System.out.println(testerModel.toString());
    Assert.assertThrows(Exception.class, () ->
            tester.movePile(2, 1, 0));
  }

  // Test putting a card the same color on another pile
  @Test
  public void testMoveSameColorPileToPile() {
    init();
    Assert.assertThrows(Exception.class, () ->
            tester.movePile(2, 1, 1));
  }

  // Tests putting a non-king card from the draw pile to an empty pile
  @Test
  public void testNonKingFromDrawToEmptyPile() {
    init();
    tester.movePile(0, 1, 2);
    Assert.assertThrows(Exception.class, () -> tester.moveDraw(0));
  }


  // Tests that only the top draw card is available and that discardDraw cycles
  @Test
  public void testDiscardDraw() {
    Assert.assertEquals(1, tester.getDrawCards().size());
    Assert.assertTrue(tester.getDrawCards().get(0).equals(new BasicCard(Rank.THREE,
            Suit.SPADES)));
    tester.discardDraw();
    Assert.assertEquals(1, tester.getDrawCards().size());
    Assert.assertTrue(tester.getDrawCards().get(0).equals(new BasicCard(Rank.TWO,
            Suit.HEARTS)));
    tester.discardDraw();
    Assert.assertEquals(1, tester.getDrawCards().size());
    Assert.assertTrue(tester.getDrawCards().get(0).equals(new BasicCard(Rank.THREE,
            Suit.SPADES)));
  }


  // Tests moving multiple cards at once
  @Test
  public void testMoveMultiple() {
    init();
    KlondikeTextualView testerModel = new KlondikeTextualView(testerDifferent);
    System.out.println(testerModel.toString());

    testerDifferent.movePile(2, 1, 1);
    System.out.println(testerModel.toString());

    testerDifferent.movePile(1, 2, 0);
    Assert.assertTrue(testerDifferent.getCardAt(0,
            1).toString().equals(new BasicCard(Rank.THREE, Suit.HEARTS).toString()));
    Assert.assertTrue(testerDifferent.getCardAt(0,
            2).toString().equals(new BasicCard(Rank.TWO, Suit.SPADES).toString()));
    System.out.println(testerModel.toString());

  }
}


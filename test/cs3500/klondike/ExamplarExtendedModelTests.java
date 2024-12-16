package cs3500.klondike;

import cs3500.klondike.model.hw02.BasicCard;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.Rank;
import cs3500.klondike.model.hw02.Suit;
import cs3500.klondike.model.hw04.WhiteheadKlondike;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;
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
public class ExamplarExtendedModelTests {

  KlondikeModel fullDeck;
  KlondikeModel whTester;
  List<Card> whDeck;
  KlondikeModel whDiamondTest;
  List<Card> whDiamondDeck;

  KlondikeModel ldTest;
  List<Card> ldDeck;
  TextualView ldView;

  KlondikeModel ldGameOver;
  List<Card> ldGameOverDeck;
  KlondikeModel ldNotOver;
  List<Card> ldNotOverDeck;

  /**
   * initializes examples used in tests.
   */
  @Before
  public void init() {
    fullDeck = new WhiteheadKlondike();

    whTester = new WhiteheadKlondike();
    whDeck = KlondikeUtils.makeSmallDeck(new ArrayList<String>(Arrays.asList(
            "2♠", "A♠", "3♠", "2♡", "A♡", "3♡")), fullDeck.getDeck());
    this.whTester.startGame(whDeck, false, 2, 1);

    whDiamondTest = new WhiteheadKlondike();
    whDiamondDeck = KlondikeUtils.makeSmallDeck(new ArrayList<String>(Arrays.asList(
            "2♢", "A♢", "3♡", "2♡", "A♡", "3♢")), fullDeck.getDeck());
    this.whDiamondTest.startGame(whDiamondDeck, false, 2, 1);

    ldTest = new LimitedDrawKlondike(1);
    ldDeck = KlondikeUtils.makeSmallDeck(new ArrayList<String>(Arrays.asList(
            "2♠", "A♠", "3♠", "2♡", "A♡", "3♡")), fullDeck.getDeck());
    this.ldTest.startGame(ldDeck, false, 2, 3);
    ldView = new KlondikeTextualView(ldTest);

    ldGameOver = new LimitedDrawKlondike(2);
    ldGameOverDeck = KlondikeUtils.makeSmallDeck(new ArrayList<String>(Arrays.asList(
            "6♡", "A♡", "A♢", "2♡", "2♢", "3♢", "A♠",
                  "7♡", "4♢", "4♡", "5♢", "6♢", "10♡",
                        "8♡", "2♠", "3♠", "4♠", "10♢",
                              "9♡", "5♠", "6♠", "7♠",
                                    "7♢", "8♠", "9♠",
                                          "8♢", "10♠",
                                                "9♢", "3♡", "5♡")), fullDeck.getDeck());
    ldGameOver.startGame(ldGameOverDeck,false,7, 1);

    ldNotOver = new LimitedDrawKlondike(2);
    ldNotOverDeck = KlondikeUtils.makeSmallDeck(new ArrayList<String>(Arrays.asList(
            "6♡", "A♡", "A♢", "2♡", "2♢", "3♢", "A♠",
                  "7♡", "4♢", "4♡", "5♢", "6♢", "10♡",
                        "8♡", "2♠", "3♠", "4♠", "10♢",
                               "9♡", "5♡", "6♠", "7♠",
                                     "7♢", "8♠", "9♠",
                                           "8♢", "10♠",
                                                  "9♢",
            "3♡", "J♡", "J♢", "J♠", "5♠")), fullDeck.getDeck());
    ldNotOver.startGame(ldNotOverDeck,false,7, 1);
  }

  // Tests that the game is over with no visible moves
  @Test
  public void gameOverWithDraw() {
    Assert.assertTrue(ldGameOver.isGameOver());
  }

  // Tests that the game is not over with no immediately visible moves
  @Test
  public void gameNotOverWithDraw() {
    Assert.assertFalse(ldNotOver.isGameOver());
  }

  // Tests that moving a card a same color works in a whitehead klondike
  @Test
  public void testSameColorPileWHK() {
    whTester.movePile(0, 1, 1);
    Assert.assertEquals(new BasicCard(Rank.TWO, Suit.SPADES),
            whTester.getCardAt(1, whTester.getPileHeight(1) - 1));
  }

  // Tests that moving a card a same color works in a whitehead klondike
  @Test(expected = IllegalStateException.class)
  public void testDiffColorDrawWHK() {
    whTester.moveDraw(1);
  }

  // Tests moving multiple non-king same suit cards to an empty whitehead board works
  @Test
  public void testNonKingToEmptyWHK() {
    whTester.movePile(0, 1, 1);
    Assert.assertEquals(0, whTester.getPileHeight(0));
    whTester.movePile(1,2,0);
  }

  // Tests discard draw fails when you go above redraw amount
  @Test(expected = IllegalStateException.class)
  public void testldFails() {
    ldTest.discardDraw();
    ldTest.discardDraw();
    ldTest.discardDraw();
    ldTest.discardDraw();
    ldTest.discardDraw();
    ldTest.discardDraw();
    ldTest.discardDraw();
  }

  // Tests that Limited Draw Klondike draw deck is empty after you discard all
  @Test
  public void testldEmpties() {
    ldTest.discardDraw();
    ldTest.discardDraw();
    ldTest.discardDraw();
    Assert.assertEquals(3, ldTest.getNumDraw());
    ldTest.discardDraw();
    Assert.assertEquals(2, ldTest.getNumDraw());
    ldTest.discardDraw();
    Assert.assertEquals(1, ldTest.getNumDraw());
    ldTest.discardDraw();
    Assert.assertEquals(0, ldTest.getNumDraw());
  }

  // Tests that Limited Draw Klondike draw deck changes in view
  @Test
  public void testldEmptiesView() {
    ldTest.discardDraw();
    ldTest.discardDraw();
    ldTest.discardDraw();
    Assert.assertTrue(ldView.toString().contains("2♡, A♡, 3♡"));
    ldTest.discardDraw();
    Assert.assertTrue(ldView.toString().contains("A♡"));
    ldTest.discardDraw();
    Assert.assertTrue(ldView.toString().contains("3♡"));
    ldTest.discardDraw();
    Assert.assertTrue(ldView.toString().contains("Draw: \n"));
  }

  // Tests moving draw card to invalid pile throws an argument exception
  @Test(expected = IllegalArgumentException.class)
  public void testDrawToInvalidPile() {
    ldTest.moveDraw(10);
  }

  // Tests that a null deck in Whitehead Klondike startgame fails
  @Test(expected = IllegalArgumentException.class)
  public void testBadStart() {
    KlondikeModel badStart = new WhiteheadKlondike();
    badStart.startGame(null, false, 1,1);
  }

  // Tests that starting a game with too many piles in Whitehead Klondike fails
  @Test(expected = IllegalArgumentException.class)
  public void testTooManyPiles() {
    KlondikeModel badStart = new LimitedDrawKlondike(1);
    badStart.startGame(null, false, 10,1);
  }

  // Tests that a negative amount of redraws in limited game fails
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeRedraws() {
    KlondikeModel badRedraw = new LimitedDrawKlondike(-1);
  }
}

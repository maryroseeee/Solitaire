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
public class KlondikeTest {

  KlondikeModel tester;
  KlondikeModel fullDeck;
  KlondikeModel sameSuitGame;
  List<Card> smallDeck;
  List<Card> drawDeck;
  TextualView testerView;
  KlondikeModel gameOverDraw;
  KlondikeModel gameOverNoDraw;
  List<String> deckGameOverNoDraw;

  KlondikeModel notOverNoDraw;
  List<Card> deckNoDraw;
  List<Card> deckWDraw;
  List<Card> sameSuitDeck;
  List<Card> noDrawDeck;

  List<Card> notInOrder;
  List<Card> differentSuitAmount;

  KlondikeModel solve;
  List<Card> solveDeck;

  /**
   * initializes examples used in tests.
   */
  @Before
  public void init() {
    fullDeck = new BasicKlondike();

    tester = new BasicKlondike();
    List<Card> smallDeck = KlondikeUtils.makeSmallDeck(new ArrayList<String>(Arrays.asList(
            "2♠", "A♠", "A♡", "4♡", "4♠", "3♡", "3♠", "2♡")), fullDeck.getDeck());
    tester.startGame(smallDeck, false, 3, 1);
    testerView = new KlondikeTextualView(tester);

    notOverNoDraw = new BasicKlondike();
    List<Card> noDrawDeck = KlondikeUtils.makeSmallDeck(new ArrayList<String>(Arrays.asList(
            "A♠", "2♠", "A♡", "4♡", "4♠", "3♡", "3♠", "2♡")), fullDeck.getDeck());
    notOverNoDraw.startGame(smallDeck, false, 3, 1);

    drawDeck = new ArrayList<Card>(Arrays.asList(new BasicCard(Rank.FOUR, Suit.SPADES, true),
            new BasicCard(Rank.FOUR, Suit.SPADES, true),
            new BasicCard(Rank.FOUR, Suit.SPADES, false)));

    notInOrder = KlondikeUtils.makeSmallDeck(new ArrayList<String>(Arrays.asList(
            "2♠", "4♠", "3♠")), fullDeck.getDeck());

    differentSuitAmount = KlondikeUtils.makeSmallDeck(new ArrayList<String>(Arrays.asList(
            "2♠", "A♠", "3♠", "2♡", "A♡")), fullDeck.getDeck());

    sameSuitGame = new BasicKlondike();
    sameSuitDeck = KlondikeUtils.makeSmallDeck(new ArrayList<String>(Arrays.asList(
                    "A♠", "2♠", "4♠", "3♠", "5♠", "6♠", "7♠", "8♠", "9♠", "10♠", "J♠", "Q♠", "K♠")),
            fullDeck.getDeck());
    sameSuitGame.startGame(sameSuitDeck, false, 3, 1);

    gameOverNoDraw = new BasicKlondike();
    deckNoDraw = KlondikeUtils.makeSmallDeck(new ArrayList<String>(Arrays.asList("2♡", "A♠",
            "A♡", "3♡", "3♠", "2♠")), fullDeck.getDeck());
    gameOverNoDraw.startGame(deckNoDraw, false, 3, 1);

    gameOverDraw = new BasicKlondike();
    deckWDraw = KlondikeUtils.makeSmallDeck(new ArrayList<String>(Arrays.asList("2♡",
            "A♠", "A♡", "3♡", "3♠", "2♠", "4♡", "4♠")), fullDeck.getDeck());
    gameOverDraw.startGame(deckWDraw, false, 3, 1);

    solve = new BasicKlondike();
    solveDeck = KlondikeUtils.makeSmallDeck((new ArrayList<String>(Arrays.asList("2♡", "A♡",
            "A♠", "2♠"))), fullDeck.getDeck());
    solve.startGame(solveDeck, false, 2, 1);
  }

  // Checks solving a game
  @Test
  public void testSolve() {
    solve.moveToFoundation(1,0);
    Assert.assertEquals("A♠", solve.getCardAt(0).toString());
    solve.moveToFoundation(1,1);
    Assert.assertEquals("A♡", solve.getCardAt(1).toString());
    solve.moveToFoundation(0,1);
    Assert.assertEquals("2♡", solve.getCardAt(1).toString());
    solve.moveDrawToFoundation(0);
    Assert.assertEquals("2♠", solve.getCardAt(0).toString());
    Assert.assertTrue(solve.isGameOver());
    Assert.assertTrue(KlondikeUtils.isGameWon(solve));
    Assert.assertEquals(4, solve.getScore());
  }

  // Checks to see if an ongoing game isn't over with draw cards left over
  @Test
  public void testOngoingGameOverDraw() {
    Assert.assertFalse(tester.isGameOver());
  }

  // Checks to see if game is over with draw cards left over
  @Test
  public void testGameOverDraw() {
    Assert.assertFalse(gameOverDraw.isGameOver());
    gameOverDraw.movePile(2, 1, 1);
    Assert.assertFalse(gameOverDraw.isGameOver());
    gameOverDraw.movePile(0, 1, 2);
    Assert.assertTrue(gameOverDraw.isGameOver());
    Assert.assertTrue(!gameOverDraw.getDrawCards().isEmpty());
  }

  // Tests to see if initial building of list is correct after a game started
  @Test
  public void testInitialListBuilding() {
    Assert.assertEquals(new BasicCard(Rank.TWO, Suit.SPADES).toString(),
            tester.getCardAt(0, 0).toString());
    Assert.assertTrue(new BasicCard(Rank.TWO, Suit.SPADES).equals(
            tester.getCardAt(0, 0)));
  }

  // Tests trying to move a card works
  @Test
  public void testMovePileWorks() {
    tester.movePile(0, 1, 2);
    Assert.assertEquals(0, tester.getPileHeight(0));
    Assert.assertEquals(new BasicCard(Rank.TWO, Suit.SPADES).toString(),
            tester.getCardAt(2, 3).toString());
  }

  // Tests trying to move a draw card works
  @Test
  public void testMoveDrawWorks() {
    System.out.println(testerView.toString());

    tester.moveDraw(1);
    System.out.println(testerView.toString());

    Assert.assertEquals(new BasicCard(Rank.THREE, Suit.SPADES).toString(),
            (tester.getCardAt(1, 2)).toString());
  }

  // Test to see if method that checks if a foundation move is valid
  @Test
  public void testValidFoundationMoves() {
    BasicCard aHeart = new BasicCard(Rank.ACE, Suit.HEARTS);
    BasicCard twoHeart = new BasicCard(Rank.TWO, Suit.HEARTS);
    Assert.assertTrue(BasicCard.validFoundationMove(twoHeart, aHeart));
  }


  // Test to see if using the wrong card number in the getCardAt
  // throws an IllegalArgumentException
  @Test
  public void testInvalidPile() {
    System.out.println(testerView.toString());
    Assert.assertThrows(IllegalArgumentException.class, () ->
            tester.getCardAt(0, 2));
    Assert.assertTrue(KlondikeUtils.invalidPile(0, 0));
    Assert.assertFalse(KlondikeUtils.invalidPile(0, 1));
    Assert.assertFalse(KlondikeUtils.invalidPile(4, 5));
    Assert.assertTrue(KlondikeUtils.invalidPile(5, 5));
    Assert.assertTrue(KlondikeUtils.invalidPile(-2, -3));
  }

  // Test to see if using the wrong card number in the getCardAt
  // throws an IllegalArgumentException
  @Test
  public void testNullDeck() {
    KlondikeModel model = new BasicKlondike();
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.startGame(new ArrayList<Card>(), false, 2, 1));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.startGame(new ArrayList<Card>(Arrays.asList(new BasicCard(Rank.ACE,
                    Suit.SPADES), null)), false, 2, 1));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.startGame(null, false, 2, 1));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.startGame(smallDeck, false, -5, 1));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            model.startGame(smallDeck, false, 10, 1));
  }

  // Test moving  card to invalid foundation pile
  @Test
  public void testMoveToWrongFoundation() {
    Assert.assertThrows(IllegalArgumentException.class, () ->
            sameSuitGame.moveToFoundation(0, -1));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            sameSuitGame.moveToFoundation(0, 1));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            sameSuitGame.moveDrawToFoundation(-1));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            sameSuitGame.moveDrawToFoundation(5));
  }



}

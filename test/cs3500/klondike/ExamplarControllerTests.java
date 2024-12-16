package cs3500.klondike;

import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.view.KlondikeTextualView;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.StringReader;

/**
 * Tests for {@link KlondikeController}.
 */
public class ExamplarControllerTests {
  KlondikeModel fullDeck;
  KlondikeModel tester;
  List<Card> smallDeck;
  List<Card> aceReadyPileDeck;
  List<Card> aceReadyDrawDeck;
  List<Card> solve;
  List<Card> gameIsOver;


  @Before
  public void init() {
    this.fullDeck = new BasicKlondike();
    this.tester = new BasicKlondike();
    this.smallDeck = KlondikeUtils.makeSmallDeck((new ArrayList<String>(Arrays.asList("2♠", "A♠",
            "A♡", "4♡", "4♠", "3♡", "3♠", "2♡"))), fullDeck.getDeck());
    this.aceReadyPileDeck = KlondikeUtils.makeSmallDeck((new ArrayList<String>(Arrays.asList(
            "4♡", "2♠", "A♡", "A♠", "4♠", "3♡", "3♠", "2♡"))), fullDeck.getDeck());
    this.aceReadyDrawDeck = KlondikeUtils.makeSmallDeck((new ArrayList<String>(Arrays.asList(
            "2♠", "3♠", "2♡", "4♡", "4♠", "3♡", "A♠", "A♡"))), fullDeck.getDeck());
    this.solve = KlondikeUtils.makeSmallDeck((new ArrayList<String>(Arrays.asList("2♡", "A♡",
            "A♠", "2♠"))), fullDeck.getDeck());
    this.gameIsOver = KlondikeUtils.makeSmallDeck((new ArrayList<String>(Arrays.asList("2♠",
            "A♠", "A♡", "3♠", "3♡", "4♠", "2♡", "4♡"))), fullDeck.getDeck());

  }

  // Tests that the initial board shows before any input is read
  @Test
  public void testShowsInitialBoard() {
    StringReader in = new StringReader("q");
    StringBuilder out = new StringBuilder();
    KlondikeController kc = new KlondikeTextualController(in, out);
    kc.playGame(tester, smallDeck, false, 3, 1);
    Assert.assertTrue(out.toString().contains(
            (new KlondikeTextualView(tester)).toString() + "\nScore: 0\n" + "Game quit!"));
  }

  // Tests that when you quit early the final game board is displayed
  @Test
  public void testEarlyQuit() {
    StringReader in = new StringReader("q");
    StringBuilder out = new StringBuilder();
    KlondikeController kc = new KlondikeTextualController(in, out);
    kc.playGame(tester, smallDeck, false, 3, 1);
    Assert.assertTrue(out.toString().contains("State of game when quit:\n" +
            (new KlondikeTextualView(tester)).toString()
            + "\nScore: 0"));
  }

  // Tests that moving last card in a pile makes the pile empty
  @Test
  public void testMovePile() {
    StringReader in = new StringReader("mpp 1 1 3\nq");
    StringBuilder out = new StringBuilder();
    KlondikeController kc = new KlondikeTextualController(in, out);
    kc.playGame(tester, smallDeck, false, 3, 1);
    Assert.assertTrue(out.toString().contains("X"));
    Assert.assertTrue(out.toString().contains("  X  ?  ?\n" +
            "    4♡  ?\n" +
            "       3♡\n" +
            "       2♠"));
  }

  // Tests that moving draw card adds card to pile
  @Test
  public void testMoveDraw() {
    StringReader in = new StringReader("md 2\nq");
    StringBuilder out = new StringBuilder();
    KlondikeController kc = new KlondikeTextualController(in, out);
    kc.playGame(tester, smallDeck, false, 3, 1);
    System.out.println(out.toString());
    Assert.assertTrue(out.toString().contains("Draw: 2♡"));
    Assert.assertTrue(out.toString().contains(" 2♠  ?  ?\n" +
            "    4♡  ?\n" +
            "    3♠ 3♡"));
  }

  // Tests winning game displays You win! message
  @Test
  public void testWin() {
    StringReader in = new StringReader("mpf 2 1 mpf 2 2 mpf 1 2 mdf 1");
    StringBuilder out = new StringBuilder();
    KlondikeController kc = new KlondikeTextualController(in, out);
    kc.playGame(tester, solve, false, 2, 1);
    System.out.println(out.toString());
    Assert.assertTrue(out.toString().contains("You win!"));
    Assert.assertTrue(out.toString().contains("X"));
  }

  // Tests that putting a null model in play game throws an exception
  @Test
  public void testNullModelForPlayGame() {
    StringReader in = new StringReader("mdf 1");
    StringBuilder out = new StringBuilder();
    KlondikeController kc = new KlondikeTextualController(in, out);
    Assert.assertThrows(IllegalArgumentException.class, () ->
            kc.playGame(null, solve, false, 2, 1));
  }

  // Tests invalid moves display a message
  @Test
  public void testInvalidMovePile() {
    StringReader in = new StringReader("mpp 0 1 2\nq");
    StringBuilder out = new StringBuilder();
    KlondikeController kc = new KlondikeTextualController(in, out);
    kc.playGame(tester, smallDeck, false, 3, 1);
    Assert.assertTrue(out.toString().contains("Invalid move. Play again."));
  }

  // Tests moving from invalid pile displays a message
  @Test
  public void testInvalidMoveDrawPile() {
    StringReader in = new StringReader("md 0\nq");
    StringBuilder out = new StringBuilder();
    KlondikeController kc = new KlondikeTextualController(in, out);
    kc.playGame(tester, smallDeck, false, 3, 1);
    Assert.assertTrue(out.toString().contains("Invalid move. Play again."));
  }


  // Tests moving a non king from pile to empty pile
  @Test
  public void testNonPileKingToEmpty() {
    StringReader in = new StringReader("mpp 1 1 3 mpp 3 1 1\nq");
    StringBuilder out = new StringBuilder();
    KlondikeController kc = new KlondikeTextualController(in, out);
    kc.playGame(tester, smallDeck, false, 3, 1);
    Assert.assertTrue(out.toString().contains("Invalid move. Play again."));
  }

  // Tests adding random characters causes invalid move message
  @Test
  public void testBadInput() {
    StringReader in = new StringReader("!#$ md 2\nq");
    StringBuilder out = new StringBuilder();
    KlondikeController kc = new KlondikeTextualController(in, out);
    kc.playGame(tester, smallDeck, false, 3, 1);
    Assert.assertTrue(out.toString().contains("Invalid move. Play again."));
    Assert.assertTrue(out.toString().contains("Draw: 2♡"));
    Assert.assertTrue(out.toString().contains(" 2♠  ?  ?\n" +
            "    4♡  ?\n" +
            "    3♠ 3♡"));
  }

  @Test(expected = IllegalStateException.class)
  public void testMultipleInvalidMoves() {
    StringReader in = new StringReader("mpp 1 3 2 mpp 1 1 1 mdf 5 md 5");
    StringBuilder out = new StringBuilder();
    KlondikeController kc = new KlondikeTextualController(in, out);
    kc.playGame(tester, smallDeck, false, 3, 1);
  }

  @Test(expected = IllegalStateException.class)
  public void testNoFullInput() {
    StringReader in = new StringReader("mpp 1 3");
    StringBuilder out = new StringBuilder();
    KlondikeController kc = new KlondikeTextualController(in, out);
    kc.playGame(tester, smallDeck, false, 3, 1);
  }

  @Test
  public void testRandomCharDuringInput() {
    StringReader in = new StringReader("mpp 1 f 1 3 q");
    StringBuilder out = new StringBuilder();
    KlondikeController kc = new KlondikeTextualController(in, out);
    kc.playGame(tester, smallDeck, false, 3, 1);
    Assert.assertTrue(out.toString().contains((new KlondikeTextualView(tester)).toString()));
    Assert.assertTrue(out.toString().contains("X"));
  }

  // Tests that moving ace from pile to draw works
  @Test
  public void testMoveDrawToFoundation() {
    StringReader in = new StringReader("mdf 1\nq");
    StringBuilder out = new StringBuilder();
    KlondikeController kc = new KlondikeTextualController(in, out);
    kc.playGame(tester, aceReadyDrawDeck, false, 3, 1);
    Assert.assertTrue(out.toString().contains("Foundation: A♠, <none>\n"));
    Assert.assertTrue(out.toString().contains("Score: 1"));
  }

  // Tests that moving ace from pile to draw works
  @Test
  public void testMovePileToFoundation() {
    StringReader in = new StringReader("mpf 2 1 mpf 2 1\nq");
    StringBuilder out = new StringBuilder();
    KlondikeController kc = new KlondikeTextualController(in, out);
    kc.playGame(tester, aceReadyPileDeck, false, 3, 1);
    Assert.assertTrue(out.toString().contains("Foundation: 2♠"));
  }


}

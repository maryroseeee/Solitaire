package cs3500.klondike;

import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.controller.KlondikeController;
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
public class ControllerTest {
  StringReader quit;
  StringBuilder out;
  KlondikeModel tester;
  List<Card> smallDeck;
  List<Card> invalidDeck;
  List<Card> noDrawOver;
  List<Card> moveMultiple;
  List<Card> immediateOver;


  @Before
  public void init() {
    this.quit = new StringReader("q");
    this.out = new StringBuilder();
    this.tester = new BasicKlondike();
    this.smallDeck = KlondikeUtils.makeSmallDeck(new ArrayList<String>(Arrays.asList("2♠", "A♠",
            "A♡", "4♡", "4♠", "3♡", "3♠", "2♡")), tester.getDeck());
    this.invalidDeck = KlondikeUtils.makeSmallDeck(new ArrayList<String>(Arrays.asList("A♠",
            "A♡", "4♡", "4♠", "3♡", "3♠", "2♡")), tester.getDeck());
    this.noDrawOver = KlondikeUtils.makeSmallDeck(new ArrayList<String>(Arrays.asList("2♡", "A♠",
            "A♡", "3♡", "3♠", "2♠")), tester.getDeck());
    this.moveMultiple = KlondikeUtils.makeSmallDeck(new ArrayList<String>(Arrays.asList(
            "4♠", "A♠", "A♡", "3♡", "4♡","2♠","3♠", "2♡")), tester.getDeck());


  }


  // tests that using a model with an invalid deck throws a IllegalStateException
  @Test(expected = IllegalStateException.class)
  public void testInvalidDeck() {
    KlondikeController kc = new KlondikeTextualController(quit, out);
    kc.playGame(tester, invalidDeck, false, 3, 1);
  }

  // tests that using a null model throws a IllegalArgumentException
  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() {
    KlondikeController kc = new KlondikeTextualController(quit, out);
    kc.playGame(null, smallDeck, false, 3, 1);
  }

  // Tests that the intial board shows before any input is read
  @Test
  public void testShowsInitialBoard() {
    StringReader in = new StringReader("q");
    StringBuilder out = new StringBuilder();
    KlondikeController kc = new KlondikeTextualController(in, out);
    kc.playGame(tester, smallDeck, false, 3, 1);
    Assert.assertTrue(out.toString().contains(
            (new KlondikeTextualView(tester)) + "\nScore: 0\n" + "Game quit!"));
  }

  // Tests draw card displayed it rotated
  @Test
  public void testDiscardDraw() {
    StringReader in = new StringReader("dd\nq");
    StringBuilder out = new StringBuilder();
    KlondikeController kc = new KlondikeTextualController(in, out);
    kc.playGame(tester, smallDeck, false, 3, 1);
    Assert.assertTrue(out.toString().contains("Draw: 2♡"));
  }

  // Tests moving pile to invalid foundation pile number displays a message
  @Test
  public void testInvalidMoveToFPile() {
    StringReader in = new StringReader("mpf 1 0\nq");
    StringBuilder out = new StringBuilder();
    KlondikeController kc = new KlondikeTextualController(in, out);
    kc.playGame(tester, smallDeck, false, 3, 1);
    Assert.assertTrue(out.toString().contains("Invalid move. Play again."));
  }

  // Tests that moving multiple cards displays properly
  @Test
  public void testMoveMultiple() {
    StringReader in = new StringReader("mpp 3 1 2 mpp 2 2 1\nq");
    StringBuilder out = new StringBuilder();
    KlondikeController kc = new KlondikeTextualController(in, out);
    kc.playGame(tester, moveMultiple, false, 3, 1);
    System.out.println(out.toString());
    Assert.assertTrue(out.toString().contains("Foundation: <none>, <none>\n" +
            " 4♠ A♠  ?\n" +
            " 3♡    4♡\n" +
            " 2♠"));
  }

  // Tests moving too many cards from a pile moves display a message
  @Test
  public void testTooManyCards() {
    StringReader in = new StringReader("mpp 1 5 2\nq");
    StringBuilder out = new StringBuilder();
    KlondikeController kc = new KlondikeTextualController(in, out);
    kc.playGame(tester, smallDeck, false, 3, 1);
    Assert.assertTrue(out.toString().contains("Invalid move. Play again."));
  }

}

package cs3500.klondike;

import java.io.IOException;
import java.io.InputStreamReader;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * Demonstrates a simple command-line-based Klondike game.
 */
public final class Klondike {
  /**
   * Main class for Klondike.
   * @param args command-line arguments provided when running the program.
   */
  public static void main(String[] args) {
    KlondikeModel model = KlondikeCreator.GameType.create(KlondikeCreator.GameType.BASIC);
    KlondikeController c = new KlondikeTextualController(new InputStreamReader(System.in), System.out);
    c.playGame(model, KlondikeUtils.makeStandardDeck(), true, 7,
            1);
    /*try {
      new KlondikeCreator(new InputStreamReader(System.in), System.out).playGame();
    } catch (IOException e) {
      e.printStackTrace();
    } */
  }
}
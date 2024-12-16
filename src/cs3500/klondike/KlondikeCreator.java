package cs3500.klondike;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;
import cs3500.klondike.model.hw04.WhiteheadKlondike;

/**
 * Creates a Klondike game.
 */
public final class KlondikeCreator {

  private Readable readable;
  private Appendable appendable;
  private List<Integer> args;
  private KlondikeModel model;

  /**
   * constructs a Klondike Controller.
   *
   * @param r input
   * @param a output
   */
  public KlondikeCreator(Readable r, Appendable a) {
    if ((r == null) || (a == null)) {
      throw new IllegalArgumentException("Readable or appendable is null");
    }
    this.readable = r;
    this.appendable = a;
    this.args = new ArrayList<>();
  }

  /**
   * Represents the types of game modes.
   */
  public enum GameType {
    BASIC, LIMITED, WHITEHEAD;


    @Override
    public String toString() {
      if (this == BASIC) {
        return "basic";
      } else if (this == LIMITED) {
        return "limited";
      } else {
        return "whitehead";
      }
    }

    /**
     * Creates a game based on given mode.
     * @param mode the mode of the game to be played
     * @return the game board.
     */
    public static KlondikeModel create(GameType mode) {
      switch (mode) {
        case BASIC:
          return new BasicKlondike();
        case LIMITED:
          return new LimitedDrawKlondike(2);
        case WHITEHEAD:
          return new WhiteheadKlondike();
        default:
          throw new IllegalArgumentException("Unsupported game type");
      }
    }
  }

  /**
   * The primary method for beginning and playing a game.
   * @throws IOException If the Appendable object is unable to transmit output
   */
  public void playGame() throws IOException {
    Scanner sc = new Scanner(this.readable);
    int p = 0;
    int d = 0;
    KlondikeTextualController c = null;
    if (sc.hasNextLine()) {
      String line = sc.nextLine().toLowerCase();
      Scanner lineScanner = new Scanner(line);
      if (lineScanner.hasNext()) {
        String command = lineScanner.next();
        addInt(lineScanner);
        switch (command) {
          case "basic":
            this.model = GameType.create(GameType.BASIC);
            addInt(lineScanner);
            c = new KlondikeTextualController(this.readable, this.appendable, this.args);
            c.playGame(this.model, KlondikeUtils.makeStandardDeck(), true, c.getPileNum(),
                    c.getVisDraw());
            break;
          case "limited":
            if (!this.args.isEmpty()) {
              this.model = new LimitedDrawKlondike(this.args.get(0));
              this.args.remove(0);
            } else {
              this.model = GameType.create(GameType.LIMITED);
            }
            c = new KlondikeTextualController(this.readable, this.appendable, this.args);
            c.playGame(this.model, KlondikeUtils.makeStandardDeck(), true, c.getPileNum(),
                    c.getVisDraw());
            break;
          case "whitehead":
            this.model = GameType.create(GameType.WHITEHEAD);
            c = new KlondikeTextualController(this.readable, this.appendable, this.args);
            c.playGame(this.model, KlondikeUtils.makeStandardDeck(), true, c.getPileNum(),
                    c.getVisDraw());
            break;
          default:

        }
      }
    }
  }

  /**
   * Gets the next int in a scanner and adds it to arguments list.
   *
   * @param sc the command line command
   */
  public void addInt(Scanner sc) {
    while (sc.hasNext()) {
      if (sc.hasNextInt()) {
        this.args.add(sc.nextInt());
      }
    }
  }
}



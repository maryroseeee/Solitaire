package cs3500.klondike.controller;

import cs3500.klondike.KlondikeUtils;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.KlondikeTextualView;
import cs3500.klondike.view.TextualView;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Represents a Controller for Klondike: handle user moves by executing them using the model
 * convey move outcomes to the user in some form. Implements {@link KlondikeController}
 */
public class KlondikeTextualController implements cs3500.klondike.controller.KlondikeController {
  private Readable readable;
  private Appendable appendable;
  private KlondikeModel board;
  private final int pileNum;
  private final int visDraw;


  private boolean quit;


  /**
   * constructs a Klondike Controller.
   *
   * @param r input
   * @param a output
   */
  public KlondikeTextualController(Readable r, Appendable a) {
    if ((r == null) || (a == null)) {
      throw new IllegalArgumentException("Readable or appendable is null");
    }
    this.readable = r;
    this.appendable = a;
    this.board = new BasicKlondike();
    this.quit = false;
    this.pileNum = 7;
    this.visDraw = 3;
  }

  /**
   * Constructs a KlondikeTextualController.
   *
   * @param r input
   * @param a output
   * @param l list of arguments for pileNum and visDraw
   */
  public KlondikeTextualController(Readable r, Appendable a, List<Integer> l) {
    if ((r == null) || (a == null)) {
      throw new IllegalArgumentException("Readable or appendable is null");
    }
    this.readable = r;
    this.appendable = a;
    this.board = new BasicKlondike();
    this.quit = false;
    if (l.isEmpty()) {
      this.pileNum = 7;
      this.visDraw = 3;
    } else if (l.size() == 2) {
      this.pileNum = l.get(0);
      this.visDraw = l.get(1);
    } else {
      this.pileNum = l.get(0);
      this.visDraw = 3;
    }
  }

  @Override
  public void playGame(KlondikeModel model, List<Card> deck, boolean shuffle,
                       int numPiles, int numDraw) {
    if ((model == null)) {
      throw new IllegalArgumentException("Model is null");
    }

    this.board = model;
    try {
      this.board.startGame(deck, shuffle, numPiles, numDraw);
      appendBoardWithScore();
    } catch (IllegalArgumentException | IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
    this.quit = false;
    Scanner sc = new Scanner(readable);

    while (!quit) {
      if (sc.hasNext()) {
        String move = sc.next().toLowerCase();
        if (move.equals("quit") || move.equals("q")) {
          try {
            if (board.isGameOver()) {
              gameOverMessage();
            } else {
              gameQuit();
            }
          } catch (IOException e) {
            invalidMessage(e.getMessage() + "\n");
          }

        } else {
          compute(move, sc);
        }
      } else {
        throw new IllegalStateException();
      }
    }
  }


  /**
   * The main method that computes the actions of the application to the controller.
   */
  protected void compute(String move, Scanner sc) {
    int src;
    int dest;
    switch (move) {
      case "mpp":
        try {
          src = getNextIntOrQuit(sc) - 1;
          int numCards = getNextIntOrQuit(sc);
          dest = getNextIntOrQuit(sc) - 1;
          this.board.movePile(src, numCards, dest);
          checkIfOver();
        } catch (IllegalArgumentException | IllegalStateException | IOException e) {
          mutationErrorChecking(e.getMessage());
        }
        break;
      case "md":
        try {
          dest = getNextIntOrQuit(sc) - 1;
          this.board.moveDraw(dest);
          checkIfOver();
        } catch (IllegalArgumentException | IllegalStateException | IOException e) {
          mutationErrorChecking(e.getMessage());
        }
        break;
      case "mpf":
        try {
          src = getNextIntOrQuit(sc) - 1;
          dest = getNextIntOrQuit(sc) - 1;
          this.board.moveToFoundation(src, dest);
          checkIfOver();
        } catch (IllegalArgumentException | IllegalStateException | IOException e) {
          mutationErrorChecking(e.getMessage());
        }
        break;
      case "mdf":
        try {
          dest = getNextIntOrQuit(sc) - 1;
          this.board.moveDrawToFoundation(dest);
          checkIfOver();
        } catch (IllegalArgumentException | IllegalStateException | IOException e) {
          mutationErrorChecking(e.getMessage());
        }
        break;
      case "dd":
        try {
          this.board.discardDraw();
          appendBoardWithScore();
        } catch (IllegalArgumentException | IllegalStateException | IOException e) {
          invalidMessage(e.getMessage() + "\n");
        }
        break;
      default:
        if (this.board.isGameOver()) {
          this.quit = true;
          gameOverMessage();
        }
        invalidMessage("Not a valid move option\n");
    }
  }

  /**
   * Checks moves errors at the end so game does not get stuck if it is in a loop.
   *
   * @param message the error message to throw if game is not over
   */
  public void mutationErrorChecking(String message) {
    if (!this.quit) {
      invalidMessage(message + "\n");
    }
  }

  /**
   * Gets pile numbers.
   *
   * @return amount of piles
   */
  public int getPileNum() {
    return this.pileNum;
  }

  /**
   * Gets amount of visible draw cards.
   *
   * @return amount of visible draw cards.
   */
  public int getVisDraw() {
    return this.visDraw;
  }

  /**
   * Checks if game is over.
   */
  public void checkIfOver() {
    try {
      if (this.board.isGameOver()) {
        gameOverMessage();
        this.quit = true;
      } else if (!this.quit) {
        appendBoardWithScore();
      }
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  /**
   * Adds view of current board and score to the appendable.
   *
   * @throws IOException If the Appendable object is unable to transmit output
   */
  public void appendBoardWithScore() throws IOException {
    TextualView view = new KlondikeTextualView(this.board, this.appendable);
    this.appendable.append(view.toString() + "\nScore: " + this.board.getScore() + "\n");

  }

  /**
   * Adds view of the game over screen, win or loss, to the appendable.
   *
   * @throws IOException If the Appendable object is unable to transmit output
   */
  public void gameOverMessage() {
    try {
      TextualView view = new KlondikeTextualView(this.board, this.appendable);
      this.appendable.append(view.toString() + "\n");
      if (KlondikeUtils.isGameWon(this.board)) {
        this.appendable.append("\nYou win!");
      } else {
        this.appendable.append("\nGame over. Score: " + this.board.getScore() + "\n");

      }
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }


  private void invalidMessage(String message) {
    try {
      appendBoardWithScore();
      appendable.append("Invalid move. Play again. " + message);
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  /**
   * Adds view of game when quit.
   *
   * @throws IOException If the Appendable object is unable to transmit output
   */
  private void gameQuit() throws IOException {
    this.quit = true;
    this.appendable.append("Game quit!\n" + "State of game when quit:\n");
    appendBoardWithScore();
    this.appendable.append("\n");
  }

  /**
   * Adds view of game when quit.
   *
   * @throws IOException If the Appendable object is unable to transmit output
   */
  private void reenterNewValueMsg() throws IOException {
    this.quit = true;
    this.appendable.append("Re-enter value\n");
  }

  /**
   * Gets the next int in a scanner and updates the controller's quit
   * to true if q is found.
   *
   * @return the first int in a scanner.
   * @throws IOException           If the Appendable object is unable to transmit output
   * @throws IllegalStateException If there is no number found
   */
  public int getNextIntOrQuit(Scanner s) throws IOException {
    while (s.hasNext()) {
      if (s.hasNextInt()) {
        return s.nextInt();
      } else {
        String q = s.next().toLowerCase();
        if (q.equals("q")) {
          this.quit = true;
          gameQuit();
          break;
        }
      }
    }
    throw new IllegalStateException("Invalid Input");
  }


}

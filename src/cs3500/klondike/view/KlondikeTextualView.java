package cs3500.klondike.view;

import java.io.IOException;

import cs3500.klondike.model.hw02.KlondikeModel;


/**
 * A simple text-based rendering of the Klondike game.
 */
public class KlondikeTextualView implements TextualView {
  private final KlondikeModel model;
  private Appendable appendable;

  public KlondikeTextualView(KlondikeModel model) {
    this.model = model;
    this.appendable = System.out;
  }

  public KlondikeTextualView(KlondikeModel model, Appendable appendable) {
    this.model = model;
    this.appendable = appendable;
  }

  @Override
  public void render() throws IOException {
    this.appendable.append(this.toString());
  }

  @Override
  public String toString() {
    String ans = "Draw: ";
    if (model.getDrawCards().size() > 0) {
      if (model.getNumDraw() > 1) {
        for (int i = 0; i < model.getDrawCards().size() - 1; i++) {
          ans += this.model.getDrawCards().get(i) + ", ";
        }
        ans += this.model.getDrawCards().get(model.getDrawCards().size() - 1);
      } else {
        ans += this.model.getDrawCards().get(0);
      }
    }
    ans += "\nFoundation: ";
    for (int i = 0; i < model.getNumFoundations() - 1; i++) {
      if (this.model.getCardAt(i) == null) {
        ans += "<none>, ";
      } else {
        ans += this.model.getCardAt(i).toString() + ", ";
      }
    }
    if (this.model.getCardAt(model.getNumFoundations() - 1) == null) {
      ans += "<none>\n";
    } else {
      ans += this.model.getCardAt(model.getNumFoundations() - 1).toString() + "\n";
    }
    if (model.getNumRows() == 0) {
      for (int i = 0; i < model.getNumPiles(); i++) {
        ans += "  X";
      }
    } else {
      for (int i = 0; i < model.getNumRows(); i++) {
        for (int j = 0; j < model.getNumPiles(); j++) {
          if (model.getPileHeight(j) < 1 && i < 1) {
            ans += "  X";
          } else if (model.getPileHeight(j) - 1 < i && i != 0) {
            ans += "   ";
          } else if (model.getPileHeight(j) > i && !model.isCardVisible(j, i)) {
            ans += "  ?";
          } else if (model.getPileHeight(j) > i && model.isCardVisible(j, i)) {
            if (model.getCardAt(j, i).toString().length() < 3) {
              ans += " " + model.getCardAt(j, i).toString();
            } else {
              ans += model.getCardAt(j, i).toString();
            }
          }
        }
        if (i < model.getNumRows() - 1) {
          ans += "\n";
        }
      }
    }
    return ans;
  }
}

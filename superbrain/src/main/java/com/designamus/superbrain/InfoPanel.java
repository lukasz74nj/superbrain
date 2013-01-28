/**
 * Copyright 1998 Lukasz Szelag
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package com.designamus.superbrain;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
  * This class implements a component that is used to display the game status.
  *
  * @author  Lukasz Szelag (luk@alice.ci.pwr.wroc.pl)
  * @version 1.0 12/23/98
  */
class InfoPanel extends JPanel implements SBConstants, BoardListener {
  private Board board;

  private GridBagLayout      gb = new GridBagLayout();
  private GridBagConstraints c  = new GridBagConstraints();

  private JLabel player, humanPow, compPow, move;

  /*
  This class implements a custom titled border. The base class version doesn't
  provide enough empty space around it.
  */
  private class MyTitledBorder extends TitledBorder {
    MyTitledBorder(String title) {
      super(title);
    }

    public Insets getBorderInsets(Component c) {
      return new Insets(20, 10, 10, 10);
    }
  }

  InfoPanel(Board board) {
    super();
    this.board = board;
    board.addBoardListener(this);

    setLayout(gb);
    setBorder(new MyTitledBorder("Game Info"));

    player      = new JLabel();
    c.gridx     = 0; 
    c.gridy     = 0;
    c.anchor    = c.CENTER;
    c.gridwidth = c.REMAINDER;
    c.insets    = new Insets(0, 0, 20, 0);
    gb.setConstraints(player, c);
    add(player);

    JLabel lHumanPow = new JLabel("HUMAN Power:");
    c.gridx          = 0; 
    c.gridy          = 1;
    c.anchor         = c.EAST;
    c.gridwidth      = c.RELATIVE;
    c.insets         = new Insets(0, 0, 10, 0);
    gb.setConstraints(lHumanPow, c);
    add(lHumanPow);
    humanPow         = new JLabel();
    c.gridx          = 1; 
    c.gridy          = 1;
    c.anchor         = c.WEST;
    c.insets         = new Insets(0, 5, 10, 0);
    gb.setConstraints(humanPow, c);
    add(humanPow);

    JLabel lCompPow = new JLabel("COMPUTER Power:");
    c.gridx         = 0; 
    c.gridy         = 2;
    c.anchor        = c.EAST;
    c.insets        = new Insets(0, 0, 10, 0);
    gb.setConstraints(lCompPow, c);
    add(lCompPow);
    compPow         = new JLabel();
    c.gridx         = 1; 
    c.gridy         = 2;
    c.anchor        = c.WEST;
    c.insets        = new Insets(0, 5, 10, 0);
    gb.setConstraints(compPow, c);
    add(compPow);

    JLabel lMove = new JLabel("Move:");
    c.gridx      = 0; 
    c.gridy      = 3;
    c.anchor     = c.EAST;
    c.insets     = new Insets(0, 0, 0, 0);
    gb.setConstraints(lMove, c);
    add(lMove);
    move         = new JLabel();
    c.gridx      = 1; 
    c.gridy      = 3;
    c.anchor     = c.WEST;
    c.insets     = new Insets(0, 5, 0, 0);
    gb.setConstraints(move, c);
    add(move);
  }

  /*
  BoardListener implementation. These callback methods are invoked when the
  situation on the board changes.
  */

  public void boardBoardCleared(BoardEvent e){
    Board board = (Board)e.getSource();

    player.setText("HUMAN to move");
    humanPow.setText(String.valueOf(board.getWhitePower()));
    compPow.setText(String.valueOf(board.getBlackPower()));
    move.setText("1");
  }

  public void boardPlayerMoved(BoardEvent e) {
    Board board = (Board)e.getSource();

    if ((board.isWhiteWon() || board.isBlackWon()) ||
        ((board.getWhitePower() == 0) && (board.getBlackPower() == 0))) {
      player.setText("Game over!");
    }
    else {
      if (e.getPawnType() == WHITE_PAWN) {
        player.setText("COMPUTER to move");
      }
      else {
        player.setText("HUMAN to move");
      }
    }

    humanPow.setText(String.valueOf(board.getWhitePower()));
    compPow.setText(String.valueOf(board.getBlackPower()));
    move.setText(String.valueOf((board.getPawnsNo() / 2) + 1));
  }
}

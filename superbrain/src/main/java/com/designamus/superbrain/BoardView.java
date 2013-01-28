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

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
  * This class implements a component that is used to visualize the game board.
  *
  * @author  Lukasz Szelag (luk@alice.ci.pwr.wroc.pl)
  * @version 1.0 12/22/98
  */
class BoardView extends JComponent implements SBConstants, BoardListener {
  private JFrame owner;
  private Board  board;

  // the game board consists of tiles
  private JButton tiles[][] = new JButton[BOARD_SIZE][BOARD_SIZE];

  // icons for tiles
  private Icon emptyTile, whiteTile, blackTile;

  // indicates the computer turn
  private boolean busy = false;

  /*
  This class implements a component that is used to visualize tiles on the
  board and to get input from the user.
  */
  private class Tile extends JButton implements ActionListener, Runnable {
    int row, col;

    Tile(int row, int col) {
      this.row = row;
      this.col = col;

      setPreferredSize(new Dimension(25, 25));
      setRequestFocusEnabled(false);
      addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
      if (busy) {
        Toolkit.getDefaultToolkit().beep();
        return;
      }

      Tile tile = (Tile)e.getSource();

      try { 
        // human move
        board.putPawn(WHITE_PAWN, tile.row, tile.col);

        // computer move (called asynchronously)
        Thread thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
      }
      catch (IllegalMoveException ex) {
        Toolkit.getDefaultToolkit().beep();
      }
      catch (GameOverException ex) {
        JOptionPane.showMessageDialog(owner, ex.getMessage(), "GameOver",
                                      JOptionPane.INFORMATION_MESSAGE);
        board.clear();
      }
    }

    public void run() {
      busy = true;

      try {     
        BoardHelper.makeComputerMove(board);
      }
      catch (final GameOverException ex) {
        Runnable code = new Runnable() {
          public void run() {
            JOptionPane.showMessageDialog(owner, ex.getMessage(), "GameOver",
                                          JOptionPane.INFORMATION_MESSAGE);
            board.clear();
          }
        };
        SwingUtilities.invokeLater(code);
      }

      busy = false;
    } 
  }
      
  BoardView(JFrame owner, Board board) {
    this.owner = owner;
    this.board = board;

    emptyTile = new ImageIcon(getClass().getResource("images/tile_empty.gif"));
    whiteTile = new ImageIcon(getClass().getResource("images/tile_cross.gif"));
    blackTile = new ImageIcon(getClass().getResource("images/tile_circle.gif"));

    board.addBoardListener(this);
    setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));

    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        Tile tile = new Tile(row, col);
        add(tile);
        tiles[row][col] = tile;
      }
    }
  }

  /*
  BoardListener implementation. These callback methods are invoked when the
  situation on the board changes.
  */

  public void boardBoardCleared(BoardEvent e){
    BoardHelper.abortComputerMove();

    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        tiles[row][col].setIcon(emptyTile);
      }
    }
  }

  public void boardPlayerMoved(BoardEvent e) {
    Icon icon = emptyTile;

    switch (e.getPawnType()) {
      case WHITE_PAWN : icon = whiteTile; break;
      case BLACK_PAWN : icon = blackTile; break;
    }

    // show move
    tiles[e.getPawnRow()][e.getPawnCol()].setIcon(icon);
  }
}

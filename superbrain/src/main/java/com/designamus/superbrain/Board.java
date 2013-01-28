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

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
  * This class implements the game board.
  *
  * @author  Lukasz Szelag (luk@alice.ci.pwr.wroc.pl)
  * @version 1.0 12/02/98
  */
class Board implements Cloneable, SBConstants {
  private int pawnsNo = 0;  // number of pawns on the board

  // these veriables are used in computing the best move
  int depth, val, row, col;

  /*
  Positions of white and black pawns. A position is represented by a white and
  black bitmask. A bit is set if a position is ocupied. There are
  1 << BOARD_SIZE^2 possible positions for each side.
  */
  private BitSet white = new BitSet(BOARD_SIZE * BOARD_SIZE);
  private BitSet black = new BitSet(BOARD_SIZE * BOARD_SIZE);

  /*
  A collection of winning bitmasks. It is computed once to eliminate overhead
  during a game.
  */
  private static ArrayList wonMasks = new ArrayList();

  static {
    computeMasks();
  }

  Board() {
  }

  // Implements cloneability.
  public Object clone() {
    Board board = null;

    try {
      board = (Board)super.clone();
    }
    // this should never happen
    catch (CloneNotSupportedException e) {
    }

    board.white = (BitSet)board.white.clone();
    board.black = (BitSet)board.black.clone();

    // don't clone listeners
    board.listeners = new ArrayList();

    return board;
  }

  // Clears the board.
  void clear() {
    white   = new BitSet(BOARD_SIZE * BOARD_SIZE);
    black   = new BitSet(BOARD_SIZE * BOARD_SIZE);
    pawnsNo = 0;

    notifyBoardCleared();
  }

  // Places a new pawn at the specified position.
  void putPawn(int type, int row, int col) throws IllegalMoveException,
                                                  GameOverException {
    // bit position 
    int bitNo = row * BOARD_SIZE + col;

    if (white.get(bitNo) || black.get(bitNo) ||
        (row < 0) || (row > BOARD_SIZE - 1) ||
        (col < 0) || (col > BOARD_SIZE - 1)) {
      // position is already ocupied or illegal
      throw new IllegalMoveException();
    } 
    else {
      if (type == WHITE_PAWN) {
        white.set(bitNo);
      }
      else {
        black.set(bitNo);
      }

      pawnsNo++;
      notifyPlayerMoved(type, row, col);

      if (isWhiteWon()) {
        throw new GameOverException("HUMAN won!");
      }
      else if (isBlackWon()) {
        throw new GameOverException("COMPUTER won!");
      }
      else {
        if ((getWhitePower() == 0) && (getBlackPower() == 0)) {
          throw new GameOverException("NOBODY won!");
        }
      }
    }
  } 

  // Returns the number of pawns on the board.
  int getPawnsNo() {
    return pawnsNo;
  }

  // Returns the type of the pawn at the specified position.
  int getPawnType(int row, int col) {
    // bit position 
    int bitNo = row * BOARD_SIZE + col;

    if (white.get(bitNo)) {
      return WHITE_PAWN;
    }
    else if (black.get(bitNo)) {
      return BLACK_PAWN;
    }
    return EMPTY_PAWN;
  }

  // Checks if the next turn is taken by white pawns.
  boolean isWhiteMove() {
    return (pawnsNo % 2 == 0);
  }

  // Checks if white pawns won. 
  boolean isWhiteWon() {
    for (int i = 0; i < wonMasks.size(); i++) {
      BitSet bs = (BitSet)white.clone();

      bs.or((BitSet)wonMasks.get(i));

      // if a winning mask is a subset of white pawns mask
      if (bs.equals(white)) {
        return true;
      }
    }

    return false;
  } 

  // Checks if black pawns won. 
  boolean isBlackWon() {
    for (int i = 0; i < wonMasks.size(); i++) {
      BitSet bs = (BitSet)black.clone();

      bs.or((BitSet)wonMasks.get(i));

      // if a winning mask is a subset of black pawns mask
      if (bs.equals(black)) {
        return true;
      }
    }

    return false;
  } 

  // Returns the number of possible winning combinations for white pawns.
  int getWhitePower() {
    BitSet empty = new BitSet();
    int    power = 0;

    for (int i = 0; i < wonMasks.size(); i++) {
      BitSet bs = (BitSet)black.clone();

      bs.and((BitSet)wonMasks.get(i));

      // if a winning mask is not ocupied by black pawns
      if (bs.equals(empty)) {
        power++;
      }
    }

    return power;
  }

  // Returns the number of possible winning combinations for black pawns.
  int getBlackPower() {
    BitSet empty = new BitSet();
    int    power = 0;

    for (int i = 0; i < wonMasks.size(); i++) {
      BitSet bs = (BitSet)white.clone();

      bs.and((BitSet)wonMasks.get(i));

      // if a winning mask is not ocupied by white pawns
      if (bs.equals(empty)) {
        power++;
      }
    }

    return power;
  }

  // Computes all winning bitmasks.
  private static void computeMasks() {
    // compute masks for each row
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int i = 0; i <= BOARD_SIZE - WON_SIZE; i++) {
        BitSet bs = new BitSet(BOARD_SIZE * BOARD_SIZE);

        for (int col = i; col < i + WON_SIZE; col++) {
          bs.set(row * BOARD_SIZE + col);
        }
        wonMasks.add(bs);
      }
    }

    // compute masks for each column
    for (int col = 0; col < BOARD_SIZE; col++) {
      for (int i = 0; i <= BOARD_SIZE - WON_SIZE; i++) {
        BitSet bs = new BitSet(BOARD_SIZE * BOARD_SIZE);

        for (int row = i; row < i + WON_SIZE; row++) {
          bs.set(row * BOARD_SIZE + col);
        }
        wonMasks.add(bs);
      }
    }

    // compute masks for diagonals
    for (int d = 0; d <= BOARD_SIZE - WON_SIZE; d++) { 
      for (int i = 0; i <= BOARD_SIZE - WON_SIZE - d; i++) {
        BitSet bs1 = new BitSet(BOARD_SIZE * BOARD_SIZE);
        BitSet bs2 = new BitSet(BOARD_SIZE * BOARD_SIZE);
        BitSet bs3 = new BitSet(BOARD_SIZE * BOARD_SIZE);
        BitSet bs4 = new BitSet(BOARD_SIZE * BOARD_SIZE);

        for (int row = i; row < i + WON_SIZE; row++) {
          // bit positions for left oriented diagonals  
          int bitNo1 = row * BOARD_SIZE + row + d;
          int bitNo2 = (row + d) * BOARD_SIZE + row;

          // bit positions for right oriented diagonals  
          int bitNo3 = row * BOARD_SIZE + BOARD_SIZE - row - 1 - d;
          int bitNo4 = (row + d) * BOARD_SIZE + (BOARD_SIZE - 1 - row);

          bs1.set(bitNo1);
          bs2.set(bitNo2);
          bs3.set(bitNo3);
          bs4.set(bitNo4);
        }

        wonMasks.add(bs1);
        wonMasks.add(bs3);

        // add masks for symmetric diagonals
        if (d > 0) {       
          wonMasks.add(bs2);
          wonMasks.add(bs4);
        }
      }
    }
  }

  /*
  Code for managing the listeners.
  */

  private ArrayList listeners = new ArrayList();

  synchronized void addBoardListener(BoardListener l) {
    listeners.add(l);
  }

  synchronized void removeBoardListener(BoardListener l) {
    listeners.remove(l);
  }

  // Notifies all listeners that the game board has been cleared.
  void notifyBoardCleared() {
    BoardEvent e = new BoardEvent(this);
    List l;

    synchronized(this) {
      l = (List)listeners.clone();
    }

    for (int i = 0; i < l.size(); i++) {
      ((BoardListener)l.get(i)).boardBoardCleared(e);
    }
  } 

  /* 
  Notifies all listeners that the given player has placed a pawn on the
  specified position.
  */
  void notifyPlayerMoved(int type, int row, int col) {
    BoardEvent e = new BoardEvent(this, type, row, col);
    List l;

    synchronized(this) {
      l = (List)listeners.clone();
    }

    for (int i = 0; i < l.size(); i++) {
      ((BoardListener)l.get(i)).boardPlayerMoved(e);
    }
  }
}

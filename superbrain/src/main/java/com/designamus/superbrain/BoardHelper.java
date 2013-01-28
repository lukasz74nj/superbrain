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

import java.util.TreeMap;

/**
  * This class contains static methods that implement MINIMAX algorithm.
  *
  * @author  Lukasz Szelag (luk@alice.ci.pwr.wroc.pl)
  * @version 1.0 12/29/98
  */
class BoardHelper implements SBConstants {
  private static boolean abort = false;

  // Makes the best computer move.
  static void makeComputerMove(Board board) throws GameOverException {
    abort = false;

    // mark this board as the root of the search tree
    board.depth = 0;

    // compute the best situation
    Board best = BoardHelper.miniMax(board);

    // operation aborted
    if (abort) {
      return;
    }

    try {
      if (board.isWhiteMove()) { 
        board.putPawn(WHITE_PAWN, best.row, best.col);
      }
      else {
        board.putPawn(BLACK_PAWN, best.row, best.col);
      }
      return;
    }
    // this should never happen
    catch (IllegalMoveException e) {
    }
  }

  // Stops the computation of the best computer move.
  static void abortComputerMove() {
    abort = true;
  }

  // Returns the best situation reachable from a given situation.
  private static Board miniMax(Board board) {
    if (abort) {
      return board;
    }

    // for leaves return the situation
    if (board.depth == MAX_DEPTH) {
      return board;
    }
    else {
      Board nodes[] = getNodes(board);
      TreeMap maps  = new TreeMap();

      // evaluate nodes
      for (int i = 0; i < nodes.length; i++) {
        Board node = miniMax(nodes[i]);
        maps.put(new Integer(node.val), node);
      }

      if (board.isWhiteMove()) {
        // the situation associated with the max value
        Board max = (Board)maps.get(maps.lastKey());

        // evaluate the current node
        board.val = max.val;

        // remember the best move in the root
        if (board.depth == 0) {
          board.row = max.row;
          board.col = max.col;
        }

        return board;
      }
      else {
        // the situation associated with the min value
        Board min = (Board)maps.get(maps.firstKey());

        // evaluate the current node
        board.val = min.val;

        // remember the best move in the root
        if (board.depth == 0) {
          board.row = min.row;
          board.col = min.col;
        }

        return board;
      }
    }
  }

  // Returns the array of all situations reachable from the given situation.
  private static Board[] getNodes(Board board) {
    Board nodes[] = new Board[(BOARD_SIZE * BOARD_SIZE) - board.getPawnsNo()];
    int   idx     = 0;

    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        // it is necessary to make a new copy
        Board node = (Board)board.clone();
        node.depth++;

        // construct a new situation
        try {
          if (node.isWhiteMove()) { 
            node.putPawn(WHITE_PAWN, row, col);
          }
          else {
            node.putPawn(BLACK_PAWN, row, col);
          }
        }
        catch (IllegalMoveException e) {
          // the position is already ocupied
          continue;
        }
        catch (GameOverException e) {
          // mark this situation as a leave
          node.depth = MAX_DEPTH;
        }

        // remember the move
        node.row = row;
        node.col = col;

        // evaluate the situation
        if (node.isWhiteWon()) {
          node.val = 100000;
        }
        else if (node.isBlackWon()) {
          node.val = -100000;
        }
        else {
          node.val = node.getWhitePower() - node.getBlackPower(); 
        }
       
        nodes[idx++] = node;
      }
    }

    return nodes;
  }
}

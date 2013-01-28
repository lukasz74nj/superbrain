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

import java.util.EventObject;

/**
  * This class implements event state object for the game board.
  *
  * @author  Lukasz Szelag (luk@alice.ci.pwr.wroc.pl)
  * @version 1.0 12/24/98
  */
class BoardEvent extends EventObject {
  private int pawnType;
  private int pawnRow;
  private int pawnCol;

  BoardEvent(Object source) {
    super(source);
  }

  BoardEvent(Object source, int pawnType, int pawnRow, int pawnCol) {
    super(source);

    this.pawnType   = pawnType;
    this.pawnRow    = pawnRow;
    this.pawnCol    = pawnCol;
  }

  int getPawnType() {
    return pawnType;
  }

  int getPawnRow() {
    return pawnRow;
  }

  int getPawnCol() {
    return pawnCol;
  }
}

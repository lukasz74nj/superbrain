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

/**
  * This interface encapsulates various constants.
  *
  * @author  Lukasz Szelag (luk@alice.ci.pwr.wroc.pl)
  * @version 1.0 12/02/98
  */
interface SBConstants {
  static final int BOARD_SIZE = 8;
  static final int WON_SIZE   = 5;
  static final int MAX_DEPTH  = 3;

  // tiles
  static final int EMPTY_PAWN = 0;
  static final int WHITE_PAWN = 1;
  static final int BLACK_PAWN = 2;
}

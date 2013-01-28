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
  * This interface encapsulates constants for the application actions.
  *
  * @author  Lukasz Szelag (luk@hades.itma.pwr.wroc.pl)
  * @version 1.0 12/23/98
  */
interface AppActions {
  // constants for action commands
  String NEW_GAME_ACTION = "New Game";
  String EXIT_ACTION     = "Exit";
  String ABOUT_ACTION    = "About...";

  /*
  Constants that can be used as the storage-retrieval keys when setting or
  getting an action properties.
  */
  String TOOLTIP_TEXT = "tooltip-text";
  String MNEMONIC     = "mnemonic";
  String TOOLBAR_ICON = "toolbar-icon";
}

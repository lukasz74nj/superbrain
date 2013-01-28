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
  * @author  Lukasz Szelag (luk@alice.ci.pwr.wroc.pl)
  * @version 1.0 12/26/98
  */
public class GameOverException extends Exception {
  public GameOverException() {
  }

  public GameOverException(String str) {
    super(str);
  }
}

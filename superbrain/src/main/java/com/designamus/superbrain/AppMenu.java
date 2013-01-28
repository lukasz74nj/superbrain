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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
  * The application menu.
  *
  * @author  Lukasz Szelag (luk@hades.itma.pwr.wroc.pl)
  * @version 1.0 12/23/98
  */
class AppMenu extends JMenuBar implements AppActions {
  /*
  This class encapsulates a menu item component that is bound to a given action.
  The menu item is registered as the state-change listener for the action.
  Enabling/disabling the action will result in the menu item to update itself.
  */
  private class MItem extends JMenuItem implements PropertyChangeListener {
    MItem(Action a) {
      setText((String)a.getValue(Action.NAME));
      
      Icon icon = (Icon)a.getValue(Action.SMALL_ICON);
      if (icon != null) {
        setIcon(icon);
        setHorizontalTextPosition(JMenuItem.RIGHT);
      }

      setMnemonic(((Character)a.getValue(MNEMONIC)).charValue());
      this.setEnabled(a.isEnabled());
      addActionListener(a);
      a.addPropertyChangeListener(this);
    }

    public void propertyChange(PropertyChangeEvent e) {
      if (e.getPropertyName().equals("enabled")) {
        if (e.getNewValue() != e.getOldValue()) {
          this.setEnabled(((Boolean)e.getNewValue()).booleanValue());
        }
      }
    }
  }

  AppMenu(Hashtable commands) {
    JMenu fileMenu = new JMenu();
    fileMenu.setText("File");
    fileMenu.setMnemonic('F');
    fileMenu.add(new MItem((Action)commands.get(NEW_GAME_ACTION)));
    fileMenu.add(new MItem((Action)commands.get(EXIT_ACTION)));

    JMenu helpMenu = new JMenu();
    helpMenu.setText("Help");
    helpMenu.setMnemonic('H');
    helpMenu.add(new MItem((Action)commands.get(ABOUT_ACTION)));

    add(fileMenu);
    add(helpMenu);
  }
}

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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
  * The main container frame for the application.
  *
  * @author  Lukasz Szelag (luk@hades.itma.pwr.wroc.pl)
  * @version 1.0 12/23/98
  */
class SBFrame extends JFrame implements AppActions, SBConstants {
  private static final String APP_NAME = "Super Brain";

  // the game board
  private Board board = new Board();

  // application actions
  private Action[] actions = {
    new NewGameAction(),
    new ExitAction(),
    new AboutAction()
  };

  // command table for actions
  private Hashtable commands = new Hashtable();

  SBFrame() {
    super(APP_NAME);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        exit();
      }
    });

    // build command table for actions 
    for (int i = 0; i < actions.length; i++) {
      Action a = actions[i];
      commands.put(a.getValue(Action.NAME), a);
    }

    setJMenuBar(new AppMenu(commands));

    // set up the container
    Container content = getContentPane();
    content.setLayout(new BorderLayout());
    content.add(new AppToolBar(commands), BorderLayout.NORTH);
    content.add(new JScrollPane(new BoardView(this, board)));
    content.add(new InfoPanel(board), BorderLayout.EAST); 
    board.clear();
    pack();

    // center the frame on the screen
    Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frm = getSize();
    setLocation((scr.width - frm.width) / 2, (scr.height - frm.height) / 2);    
    setVisible(true);
  }

  // Return the action associated with a given action command.
  private Action getAction(String cmd) {
    return (Action)commands.get(cmd);
  }

  private void exit() {
    dispose();
    System.exit(0);
  }

  /*
  Inner classes that implement actions.
  */

  // creates a new game
  private class NewGameAction extends AbstractAction {
    NewGameAction() {
      super(NEW_GAME_ACTION);

      putValue(TOOLTIP_TEXT, "New Game");
      putValue(MNEMONIC, new Character('n'));
      putValue(TOOLBAR_ICON, new ImageIcon(getClass().getResource(
               "images/toolbar/new.gif")));
      putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource(
               "images/small/new.gif")));
    }

    public void actionPerformed(ActionEvent e) {
      board.clear();
    }
  }

  // terminates the application
  private class ExitAction extends AbstractAction {
    ExitAction() {
      super(EXIT_ACTION);

      putValue(TOOLTIP_TEXT, "Exit");
      putValue(MNEMONIC, new Character('x'));
      putValue(TOOLBAR_ICON, new ImageIcon(getClass().getResource(
               "images/toolbar/exit.gif")));
      putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource(
               "images/small/exit.gif")));
    }

    public void actionPerformed(ActionEvent e) {
      exit();
    }
  }

  // shows the about dialog
  private class AboutAction extends AbstractAction {
    AboutAction() {
      super(ABOUT_ACTION);

      putValue(MNEMONIC, new Character('A'));
      putValue(TOOLTIP_TEXT, "About");
      putValue(TOOLBAR_ICON, new ImageIcon(getClass().getResource(
               "images/toolbar/info.gif")));
      putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource(
               "images/small/help.gif")));
    }

    public void actionPerformed(ActionEvent e) {
      AboutDlg dlg = new AboutDlg(SBFrame.this);
      dlg.setVisible(true);
    }
  }
}

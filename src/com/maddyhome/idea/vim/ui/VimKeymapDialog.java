package com.maddyhome.idea.vim.ui;

import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;

/**
 * @author oleg
 */
public class VimKeymapDialog extends DialogWrapper {

  private VimKeymapPanel myVimKeymapPanel;

  public VimKeymapDialog(final String parentKeymap) {
    super(ProjectManager.getInstance().getDefaultProject());
    myVimKeymapPanel = new VimKeymapPanel(parentKeymap);
    setTitle("Vim Keymap settings");
    init();
  }

  protected Action[] createActions() {
    return new Action[]{getOKAction(), getCancelAction()};
  }

  protected JComponent createCenterPanel() {
    return myVimKeymapPanel.getPanel();
  }

  public Keymap getSelectedKeymap(){
    return myVimKeymapPanel.getSelectedKeyMap();
  }
}

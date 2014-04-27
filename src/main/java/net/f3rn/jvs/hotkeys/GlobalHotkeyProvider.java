package net.f3rn.jvs.hotkeys;

import java.awt.PopupMenu;
import java.awt.event.ActionListener;

public interface GlobalHotkeyProvider {
    public void init(PopupMenu popup);

    public void setOpenConsole(final int idx, final ActionListener al);
}

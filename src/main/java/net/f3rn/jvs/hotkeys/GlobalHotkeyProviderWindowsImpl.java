package net.f3rn.jvs.hotkeys;

import java.awt.Frame;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Map.Entry;

import net.f3rn.jvs.JavaVersionSwitcher;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

public class GlobalHotkeyProviderWindowsImpl implements GlobalHotkeyProvider {

    public void init(final PopupMenu popup) {
        JIntellitype.getInstance();
        JIntellitype.getInstance().registerHotKey(-1,
                JIntellitype.MOD_CONTROL + JIntellitype.MOD_SHIFT, '0');
        JIntellitype.getInstance().registerHotKey(0,
                JIntellitype.MOD_CONTROL + JIntellitype.MOD_SHIFT, 'J');
        JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
            public void onHotKey(int id) {
                switch (id) {
                case -1:

                    String sysJavaHome = System
                            .getenv(JavaVersionSwitcher.JAVA_HOME);
                    for (Entry<String, String> entry : JavaVersionSwitcher
                            .getConfiguration().getHomes().entrySet()) {
                        if (entry.getValue().equals(sysJavaHome)) {
                            JavaVersionSwitcher.getCommandRunner().run(
                                    JavaVersionSwitcher.getConfiguration()
                                            .getConsole(), entry.getKey());
                            break;
                        }
                    }

                    break;
                case 0:
                    openPopup();
                    break;
                }
            }

            private void openPopup() {
                final Frame frame = new Frame();
                frame.setUndecorated(true);
                frame.setResizable(false);
                frame.setVisible(true);
                frame.add(popup);
                frame.addFocusListener(new FocusListener() {

                    public void focusLost(FocusEvent e) {
                        frame.dispose();
                    }

                    public void focusGained(FocusEvent e) {

                    }
                });
                popup.show(frame, 0, 0);
            }
        });

    }

    public void setOpenConsole(final int idx, final ActionListener al) {
        JIntellitype.getInstance().registerHotKey(idx,
                JIntellitype.MOD_CONTROL + JIntellitype.MOD_SHIFT,
                ("" + idx).codePointAt(0));
        JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
            public void onHotKey(int id) {
                if (idx == id) {
                    al.actionPerformed(new ActionEvent(new Object(), 0, ""));
                }
            }
        });

    }

}

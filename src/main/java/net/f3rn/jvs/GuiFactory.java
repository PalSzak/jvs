package net.f3rn.jvs;

import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.melloware.jintellitype.JIntellitype;

public class GuiFactory {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(JavaVersionSwitcher.class);

    private static final String EXIT_LABEL = "Exit";
    private static final String TRAY_LABEL = "Java Version Switcher";
    private static final String RUN_LABEL = "Run With";
    private static final String OPEN_CONSOLE_LABEL = "Open Console";

    public static TrayIcon getSystemTray() {

        final TrayIcon trayIcon = getTray();
        final PopupMenu popup = new PopupMenu();

        // build menu from config
        MenuItem exit = new MenuItem(EXIT_LABEL);
        popup.add(exit);
        popup.addSeparator();
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    JavaVersionSwitcher.getCommandRunner().close();
                } catch (Exception e1) {
                    LOGGER.error("", e);
                }
                SystemTray.getSystemTray().remove(trayIcon);
                JIntellitype.getInstance().cleanUp();
                LOGGER.info("aplication exit");
                System.exit(0);
            }
        });

        String sysJavaHome = System.getenv(JavaVersionSwitcher.JAVA_HOME);
        final List<CheckboxMenuItem> javaVersions = new LinkedList<CheckboxMenuItem>();
        for (final Map.Entry<String, String> javaVersion : JavaVersionSwitcher
                .getConfiguration().getHomes().entrySet()) {
            final CheckboxMenuItem cmi = new CheckboxMenuItem(
                    javaVersion.getKey());
            javaVersions.add(cmi);
            if (javaVersion.getValue().equals(sysJavaHome)) {
                cmi.setState(true);
            }
            popup.add(cmi);
            cmi.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    for (CheckboxMenuItem checkboxMenuItem : javaVersions) {
                        checkboxMenuItem.setState(false);
                    }
                    JavaVersionSwitcher.getCommandRunner().switchGlobalVersion(
                            javaVersion.getKey());
                    cmi.setState(true);
                }
            });
        }
        popup.addSeparator();

        if (JavaVersionSwitcher.getConfiguration().hasScripts()) {

            Menu runWith = new Menu(RUN_LABEL);
            popup.add(runWith);
            for (final String javaVersion : JavaVersionSwitcher
                    .getConfiguration().getHomes().keySet()) {
                Menu m = new Menu(javaVersion);
                runWith.add(m);
                for (final Map.Entry<String, String> script : JavaVersionSwitcher
                        .getConfiguration().getScriptsFor(javaVersion)
                        .entrySet()) {
                    MenuItem mi = new MenuItem(script.getKey());
                    m.add(mi);
                    mi.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            JavaVersionSwitcher.getCommandRunner().run(
                                    script.getValue(), javaVersion);
                        }
                    });
                }
            }

        }

        Menu openConsole = new Menu(OPEN_CONSOLE_LABEL);
        popup.add(openConsole);
        int i = 1;
        for (final String javaVersion : JavaVersionSwitcher.getConfiguration()
                .getHomes().keySet()) {
            final int ii = i++;
            MenuItem mi = new MenuItem((javaVersion + ((ii < 10) ? (" #" + ii)
                    : "")));
            openConsole.add(mi);
            final ActionListener al = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JavaVersionSwitcher.getCommandRunner()
                            .run(JavaVersionSwitcher.getConfiguration()
                                    .getConsole(), javaVersion);
                }
            };
            mi.addActionListener(al);
            if (ii < 10) {
                JavaVersionSwitcher.getGlobalHotkeyProvider().setOpenConsole(
                        ii, al);
            }
        }

        JavaVersionSwitcher.getGlobalHotkeyProvider().init(popup);

        trayIcon.setPopupMenu(popup);
        return trayIcon;
    }

    private static TrayIcon getTray() {
        TrayIcon trayIcon = new TrayIcon(getImage("jvs.png"), TRAY_LABEL);
        trayIcon.setImageAutoSize(true);
        return trayIcon;
    }

    public static Image getImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        return icon.getImage();
    }
}

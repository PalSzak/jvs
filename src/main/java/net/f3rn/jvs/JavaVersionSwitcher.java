package net.f3rn.jvs;

import java.awt.SystemTray;
import java.io.IOException;

import net.f3rn.jvs.commandrunner.CommandRunner;
import net.f3rn.jvs.commandrunner.CommandRunnerWindowsImpl;
import net.f3rn.jvs.hotkeys.GlobalHotkeyProvider;
import net.f3rn.jvs.hotkeys.GlobalHotkeyProviderWindowsImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.melloware.jintellitype.JIntellitype;

public class JavaVersionSwitcher {
    public static final String JAVA_HOME = "JAVA_HOME";

    private static final Logger LOGGER = LoggerFactory
            .getLogger(JavaVersionSwitcher.class);
    private static ConfigurationManager config;
    private static CommandRunner commandRunner;

    private static GlobalHotkeyProvider hotkeyProvider;

    public static ConfigurationManager getConfiguration() {
        return config;
    }

    public static CommandRunner getCommandRunner() {
        return commandRunner;
    }

    public static void main(String[] args) throws IOException {
        LOGGER.info("application start");

        if (!SystemTray.isSupported()) {
            LOGGER.warn("System tray not supported");
            LOGGER.warn("aplication exit");
            System.exit(1);
        } else {
            try {
                LOGGER.info("System tray supported");

                config = new ConfigurationManager();

                // TODO detect os version
                commandRunner = new CommandRunnerWindowsImpl();
                hotkeyProvider = new GlobalHotkeyProviderWindowsImpl();
                // /////

                SystemTray.getSystemTray().add(GuiFactory.getSystemTray());

            } catch (Exception e) {
                JIntellitype.getInstance().cleanUp();
                LOGGER.error("", e);
                System.exit(1);
            }

        }

    }

    public static GlobalHotkeyProvider getGlobalHotkeyProvider() {
        return hotkeyProvider;

    }

}

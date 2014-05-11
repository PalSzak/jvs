package net.f3rn.jvs;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;

public class ConfigurationManager {
    private static final String CONFIG_FILE_NAME = "config.xml";

    private static final String DEFAULT_CONSOLE = "cmd.exe /k start";

    private static final String X_RUN = "run";
    private static final String X_CONSOLE = "console";
    private static final String X_JH_NAMES = "java/home/name";
    private static final String X_JH_PATHS = "java/home/path";

    private static final String X_SCRIPT_NAMES_PATTERN = "run/%s/task/name";
    private static final String X_SCRIPT_PATHS_PATTERN = "run/%s/task/command";

    private XMLConfiguration config;

    private Map<String, String> homes = null;
    private Map<String, Map<String, String>> scriptContainer = new HashMap<String, Map<String, String>>();
    private String console = null;

    public ConfigurationManager() throws ConfigurationException {
        config = new XMLConfiguration(CONFIG_FILE_NAME);
        config.setExpressionEngine(new XPathExpressionEngine());
    }

    public Map<String, String> getHomes() {
        return (homes == null) ? homes = getMap(X_JH_NAMES, X_JH_PATHS) : homes;
    }

    public boolean hasScripts() {
        return config.getStringArray(X_RUN) != null;
    }

    public Map<String, String> getScriptsFor(String name) {
        if (scriptContainer.get(name) == null) {
            scriptContainer.put(
                    name,
                    getMap(String.format(X_SCRIPT_NAMES_PATTERN, name),
                            String.format(X_SCRIPT_PATHS_PATTERN, name)));
        }
        return scriptContainer.get(name);
    }

    private Map<String, String> getMap(String keyQuery, String valueQuery) {
        HashMap<String, String> map = new HashMap<String, String>();

        String[] keys = config.getStringArray(keyQuery);
        String[] values = config.getStringArray(valueQuery);

        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }

        return map;
    }

    public String getConsole() {
        return (console == null) ? console = config.getString(X_CONSOLE,
                DEFAULT_CONSOLE) : console;
    }

}

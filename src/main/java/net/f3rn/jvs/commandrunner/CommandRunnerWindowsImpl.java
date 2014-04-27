package net.f3rn.jvs.commandrunner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import net.f3rn.jvs.JavaVersionSwitcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandRunnerWindowsImpl implements CommandRunner {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(CommandRunner.class);

    private static final String CMD = "cmd.exe";
    private static final String EXIT = "exit";

    private static final String JAVA_GLOBAL_VERSION_SWITCH_PATTERN = "SETX "
            + JavaVersionSwitcher.JAVA_HOME + " \"%s\"";

    private static final String JAVA_LOCAL_HOME_UPDATE = "SET PATH=%s\\bin;%%PATH%%";

    private Process cmd;

    private BufferedWriter writer;

    private LogLoop stdOut;
    private LogLoop stdErr;

    public CommandRunnerWindowsImpl() throws IOException {
        start();
    }

    private void start() throws IOException {
        ProcessBuilder builder = new ProcessBuilder(CMD);
        cmd = builder.start();

        writer = new BufferedWriter(new OutputStreamWriter(
                cmd.getOutputStream()));

        stdOut = new LogLoop(new BufferedReader(new InputStreamReader(
                cmd.getInputStream())), false);
        stdErr = new LogLoop(new BufferedReader(new InputStreamReader(
                cmd.getErrorStream())), true);

        // TODO keep output in blocks
        (new Thread(stdOut)).start();
        (new Thread(stdErr)).start();
    }

    private void stop() throws IOException, InterruptedException {
        runCommand(EXIT);
        writer.close();

        cmd.waitFor();
        cmd.destroy();

        stdOut.close();
        stdErr.close();
    }

    public void close() {
        try {
            stop();
        } catch (IOException e) {
            LOGGER.error("", e);
        } catch (InterruptedException e) {
            LOGGER.error("", e);
        }
    }

    public void switchGlobalVersion(String name) {
        try {
            runCommand(String
                    .format(JAVA_GLOBAL_VERSION_SWITCH_PATTERN,
                            JavaVersionSwitcher.getConfiguration().getHomes()
                                    .get(name)));
        } catch (IOException e) {
            LOGGER.error("", e);
        }
    }

    public void run(String command, String javaVersion) {
        try {
            runCommand(command, javaVersion);
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    private void runCommand(String command, String javaVersion)
            throws IOException, InterruptedException {
        runCommand(String.format(JAVA_LOCAL_HOME_UPDATE, JavaVersionSwitcher
                .getConfiguration().getHomes().get(javaVersion)));

        runCommand(command);
    }

    private void runCommand(String command) throws IOException {
        writer.write(command + "\n");
        writer.flush();
    }

    private static class LogLoop implements Runnable {
        private String line = null;
        private BufferedReader input;
        private boolean error;
        private boolean keepGoing = true;

        public void close() {
            keepGoing = false;
        }

        public LogLoop(BufferedReader input, boolean error) {
            this.input = input;
            this.error = error;
        }

        public void run() {
            try {
                while ((line = input.readLine()) != null && keepGoing) {
                    if (error) {
                        LOGGER.error(line);
                    } else {
                        LOGGER.info(line);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        LOGGER.error("", e);
                    }
                }
                input.close();
            } catch (IOException e) {
                LOGGER.error("", e);
            }
        }
    }

}

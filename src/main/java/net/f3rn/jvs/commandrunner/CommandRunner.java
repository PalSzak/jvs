package net.f3rn.jvs.commandrunner;

public interface CommandRunner {

    void close();

    void switchGlobalVersion(String name);

    void run(String command, String javaVersion);
}

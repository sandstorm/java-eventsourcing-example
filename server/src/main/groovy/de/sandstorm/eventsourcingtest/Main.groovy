package de.sandstorm.eventsourcingtest

import groovy.util.logging.Slf4j

/**
 * Bootstrap for Fusion
 */
@Slf4j
public class Main {

    /**
     * Static initializer; which is executed when the application is deployed.
     *
     * @param args
     */
    public static void main(String[] args) {
        def mainApplication = new ServerMain()
        mainApplication.startApplication()
    }

    /**
     * Executes {@link ServerMain#run(boolean, boolean)} with the given command line arguments.
     *
     * @param arguments command line arguments
     */
    public void run(String... arguments) {

    }

}

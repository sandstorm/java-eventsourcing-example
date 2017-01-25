import de.sandstormmedia.groovyrunner.ApplicationDescriptor
import de.sandstorm.eventsourcingtest.Main

/**
 * Bootstrap for fusion.
 *
 * Allows the execution of different fusion commands.
 */
class ScriptMain extends ApplicationDescriptor {

    void help() {
        println "COMMAND REFERENCE"
        println "================="
        println "run             Runs the example"

        super.help()
    }

    void runStartCommand() {
        println "HAHA"
        def main = new Main()
        main.run()
    }

    void runStopCommand() {
    }
}

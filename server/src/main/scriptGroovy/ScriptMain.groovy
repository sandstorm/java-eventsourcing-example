import de.sandstormmedia.groovyrunner.ApplicationDescriptor

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

    }

    void runStopCommand() {
    }
}

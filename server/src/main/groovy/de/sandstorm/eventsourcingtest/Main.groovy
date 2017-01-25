package de.sandstorm.eventsourcingtest

import de.sandstorm.eventsourcingtest.dnsIpAddress.DnsIpAddressCommandHandler
import de.sandstorm.eventsourcingtest.dnsIpAddress.projection.TableProjection
import groovy.util.logging.Slf4j
import org.axonframework.config.Configuration
import org.axonframework.config.Configurer
import org.axonframework.config.DefaultConfigurer
import org.axonframework.config.EventHandlingConfiguration

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
        def mainApplication = new Main()
        mainApplication.run()
    }

    public void run() {
        Configurer configurer = DefaultConfigurer.defaultConfiguration();

        EventHandlingConfiguration ehConfiguration = new EventHandlingConfiguration()
                .registerEventHandler({conf -> new TableProjection()});

        configurer
                //.configureEmbeddedEventStore({ c -> new InMemoryEventStorageEngine() })
                .registerModule(ehConfiguration);

        Configuration config = configurer.buildConfiguration();

        def theEventBus = config.eventBus()
        config.start()

        println theEventBus

        new DnsIpAddressCommandHandler(eventBus: theEventBus).ipHostMappingWasReadFromDns("127.0.0.1", "localhost")
        new DnsIpAddressCommandHandler(eventBus: theEventBus).ipHostMappingWasReadFromDns("11.12.11.11", "sandstorm.de")
        new DnsIpAddressCommandHandler(eventBus: theEventBus).ipHostMappingWasReadFromDns("11.12.11.15", "sandstorm.de")
    }

}

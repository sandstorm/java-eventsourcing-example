package de.sandstorm.eventsourcingtest

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource
import de.sandstorm.eventsourcingtest.dnsIpAddress.DnsIpAddressCommandHandler
import de.sandstorm.eventsourcingtest.dnsIpAddress.projection.TableProjection
import groovy.sql.Sql
import groovy.util.logging.Slf4j
import org.axonframework.common.jdbc.DataSourceConnectionProvider
import org.axonframework.common.transaction.NoTransactionManager
import org.axonframework.config.Configuration
import org.axonframework.config.Configurer
import org.axonframework.config.DefaultConfigurer
import org.axonframework.config.EventHandlingConfiguration
import org.axonframework.eventsourcing.eventstore.jdbc.JdbcEventStorageEngine
import org.axonframework.eventsourcing.eventstore.jdbc.MySqlEventTableFactory

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

        def db = [url:'jdbc:mysql://127.0.0.1:3306/es-example', user:'root', password:'', driver:'com.mysql.jdbc.Driver']

        def dataSource = new MysqlDataSource()
        dataSource.setURL(db.url)
        dataSource.setUser(db.user)
        dataSource.setPassword(db.password)

        Configurer configurer = DefaultConfigurer.defaultConfiguration();

        EventHandlingConfiguration ehConfiguration = new EventHandlingConfiguration()
                .registerEventHandler({conf -> new TableProjection()});

        configurer.configureEmbeddedEventStore({ c ->
            def engine = new CustomJdbcEventStorageEngine(new DataSourceConnectionProvider(dataSource), NoTransactionManager.INSTANCE)
            engine.createSchema(CustomMySqlEventTableFactory.instance)
            return engine
        })

        configurer.registerModule(ehConfiguration);

        Configuration config = configurer.buildConfiguration();

        def theEventBus = config.eventBus()
        config.start()

        println theEventBus

        new DnsIpAddressCommandHandler(eventBus: theEventBus).ipHostMappingWasReadFromDns("127.0.0.1", "localhost")
        new DnsIpAddressCommandHandler(eventBus: theEventBus).ipHostMappingWasReadFromDns("11.12.11.11", "sandstorm.de")
        new DnsIpAddressCommandHandler(eventBus: theEventBus).ipHostMappingWasReadFromDns("11.12.11.15", "sandstorm.de")
    }

}

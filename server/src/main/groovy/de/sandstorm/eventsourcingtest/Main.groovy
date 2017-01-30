package de.sandstorm.eventsourcingtest

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import de.sandstorm.eventsourcingtest.dnsIpAddress.DnsIpAddressCommandHandler
import de.sandstorm.eventsourcingtest.dnsIpAddress.projection.async.AsynchronousProjection
import de.sandstorm.eventsourcingtest.dnsIpAddress.projection.secondAsync.SecondAsynchronousProjection
import de.sandstorm.eventsourcingtest.dnsIpAddress.projection.table.TableProjection
import de.sandstorm.eventsourcingtest.internal.CustomJdbcEventStorageEngine
import de.sandstorm.eventsourcingtest.internal.CustomMySqlEventTableFactory
import groovy.util.logging.Slf4j
import org.axonframework.common.jdbc.DataSourceConnectionProvider
import org.axonframework.common.jpa.EntityManagerProvider
import org.axonframework.common.transaction.NoTransactionManager
import org.axonframework.config.Configuration
import org.axonframework.config.Configurer
import org.axonframework.config.DefaultConfigurer
import org.axonframework.config.EventHandlingConfiguration
import org.axonframework.eventhandling.saga.repository.SagaStore
import org.axonframework.eventhandling.saga.repository.jdbc.GenericSagaSqlSchema
import org.axonframework.eventhandling.saga.repository.jdbc.JdbcSagaStore
import org.axonframework.eventhandling.tokenstore.TokenStore
import org.axonframework.eventhandling.tokenstore.jdbc.GenericTokenTableFactory
import org.axonframework.eventhandling.tokenstore.jdbc.JdbcTokenStore
import org.axonframework.eventhandling.tokenstore.jpa.JpaTokenStore
import org.axonframework.serialization.Serializer
import org.axonframework.serialization.json.JacksonSerializer

import javax.sql.DataSource

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
        configureDatabaseEventStorage(configurer)

        EventHandlingConfiguration ehConfiguration = new EventHandlingConfiguration()
                .registerEventHandler({ conf -> new TableProjection() })
                .registerEventHandler({ conf -> new AsynchronousProjection() })
                .registerTrackingProcessor('de.sandstorm.eventsourcingtest.dnsIpAddress.projection.async')



        configurer.registerModule(ehConfiguration);

        Configuration config = configurer.buildConfiguration();
        config.start()
        def theEventBus = config.eventBus()

        new DnsIpAddressCommandHandler(eventBus: theEventBus).ipHostMappingWasReadFromDns("127.0.0.1", "localhost")
        new DnsIpAddressCommandHandler(eventBus: theEventBus).ipHostMappingWasReadFromDns("11.12.11.11", "sandstorm.de")
        new DnsIpAddressCommandHandler(eventBus: theEventBus).ipHostMappingWasReadFromDns("11.12.11.15", "sandstorm.de")
        sleep(1000)
    }

    public void runSecond() {
        Configurer configurer = DefaultConfigurer.defaultConfiguration();
        configureDatabaseEventStorage(configurer)

        EventHandlingConfiguration ehConfiguration = new EventHandlingConfiguration()
                .registerEventHandler({ conf -> new SecondAsynchronousProjection() })
                .registerTrackingProcessor('de.sandstorm.eventsourcingtest.dnsIpAddress.projection.secondAsync')

        configurer.registerModule(ehConfiguration);

        Configuration config = configurer.buildConfiguration();
        config.start()
    }

    private void configureDatabaseEventStorage(Configurer configurer) {
        def dataSource = createDataSource(true)
        def dataSourceWithoutAutocommit = createDataSource(false)
        configurer.configureEmbeddedEventStore({ c ->
            def engine = new CustomJdbcEventStorageEngine(new DataSourceConnectionProvider(dataSource), NoTransactionManager.INSTANCE)
            engine.createSchema(CustomMySqlEventTableFactory.instance)
            return engine
        })

        configurer.registerComponent(TokenStore.class, {c ->
            def tokenStore = new JdbcTokenStore(new DataSourceConnectionProvider(dataSourceWithoutAutocommit), c.serializer())
            tokenStore.createSchema(new GenericTokenTableFactory())
            return tokenStore
        })

        configurer.registerComponent(SagaStore.class, { c ->
            def store = new JdbcSagaStore(dataSource, new GenericSagaSqlSchema())
            println "SAGA STORE"
            store.createSchema()
            return store
        });
    }

    private DataSource createDataSource(boolean autoCommit) {
        // TODO: relaxAutoCommit
        def db = [url: 'jdbc:mysql://127.0.0.1:3306/es-example', user: 'root', password: '']

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(db.url);
        config.setUsername(db.user);
        config.setPassword(db.password);
        config.setAutoCommit(autoCommit)
        //config.addDataSourceProperty("autoCommit", false);

        return new HikariDataSource(config);

    }
}

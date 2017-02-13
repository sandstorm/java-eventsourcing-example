package de.sandstorm.eventsourcingtest

import de.sandstorm.eventsourcingtest.dnsIpAddress.DnsIpAddressCommandHandler
import de.sandstorm.eventsourcingtest.internal.DataSourceProvider
import de.sandstorm.eventsourcingtest.dnsIpAddress.projection.async.AsynchronousProjection
import de.sandstorm.eventsourcingtest.dnsIpAddress.projection.secondAsync.SecondAsynchronousProjection
import de.sandstorm.eventsourcingtest.dnsIpAddress.projection.table.TableProjection
import de.sandstorm.eventsourcingtest.internal.CustomJdbcEventStorageEngine
import de.sandstorm.eventsourcingtest.internal.CustomMySqlEventTableFactory
import de.sandstorm.eventsourcingtest.internal.EventBusProvider
import de.sandstorm.eventsourcingtest.internal.TestProjectionTable
import de.sandstormmedia.dependencyinjection.Inject
import groovy.util.logging.Slf4j
import org.axonframework.common.jdbc.DataSourceConnectionProvider
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

    @Inject DataSourceProvider dataSourceProvider
    @Inject TestProjectionTable projectionTableFactory
    @Inject EventBusProvider eventBusProvider

    public void run() {
        initializeAxom()
        initializeProjectionTables()

        new DnsIpAddressCommandHandler().ipHostMappingWasReadFromDns("127.0.0.1", "localhost")
        new DnsIpAddressCommandHandler().ipHostMappingWasReadFromDns("11.12.11.11", "sandstorm.de")
        new DnsIpAddressCommandHandler().ipHostMappingWasReadFromDns("11.12.11.15", "sandstorm.de")
        new DnsIpAddressCommandHandler().ipHostMappingWasReadFromDns("127.0.0.1", "localhost")
        new DnsIpAddressCommandHandler().ipHostMappingWasReadFromDns("11.12.11.11", "sandstorm.de")
        new DnsIpAddressCommandHandler().ipHostMappingWasReadFromDns("11.12.11.15", "sandstorm.de")
        new DnsIpAddressCommandHandler().ipHostMappingWasReadFromDns("127.0.0.1", "localhost")
        new DnsIpAddressCommandHandler().ipHostMappingWasReadFromDns("11.12.11.11", "sandstorm.de")
        new DnsIpAddressCommandHandler().ipHostMappingWasReadFromDns("11.12.11.15", "sandstorm.de")
        sleep(1000)
    }

    protected void initializeAxom() {
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
        eventBusProvider.eventBus = theEventBus
    }

    private initializeProjectionTables() {
        projectionTableFactory.createTestProjectionTable()
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
        def dataSourceWithoutAutocommit = DataSourceProvider.createDataSource(false)
        configurer.configureEmbeddedEventStore({ c ->
            def engine = new CustomJdbcEventStorageEngine(new DataSourceConnectionProvider(dataSourceProvider.dataSource), NoTransactionManager.INSTANCE)
            engine.createSchema(CustomMySqlEventTableFactory.instance)
            return engine
        })

        configurer.registerComponent(TokenStore.class, { c ->
            def tokenStore = new JdbcTokenStore(new DataSourceConnectionProvider(dataSourceWithoutAutocommit), c.serializer())
            tokenStore.createSchema(new GenericTokenTableFactory())
            return tokenStore
        })

        configurer.registerComponent(SagaStore.class, { c ->
            def store = new JdbcSagaStore(dataSourceProvider.dataSource, new GenericSagaSqlSchema())
            println "SAGA STORE"
            store.createSchema()
            return store
        });
    }

}

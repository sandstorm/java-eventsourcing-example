package de.sandstorm.eventsourcingtest

import de.sandstorm.eventsourcingtest.dnsIpAddress.projection.secondAsync.SecondAsynchronousProjection
import de.sandstorm.eventsourcingtest.internal.*
import de.sandstorm.eventsourcingtest.neosFundingProcess.AccountBalanceCommandHandler
import de.sandstorm.eventsourcingtest.neosFundingProcess.command.DebitMoneyCommand
import de.sandstorm.eventsourcingtest.neosFundingProcess.projection.async.AsynchronousProjection
import de.sandstorm.eventsourcingtest.neosFundingProcess.projection.table.AccountBalanceProjectionTable
import de.sandstorm.eventsourcingtest.neosFundingProcess.projection.table.SubscriptionOverviewProjectionTable
import de.sandstormmedia.dependencyinjection.Inject
import groovy.util.logging.Slf4j
import org.axonframework.commandhandling.AggregateAnnotationCommandHandler
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
import org.axonframework.eventsourcing.eventstore.EventStore

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
    @Inject SubscriptionOverviewProjectionTable subscriptionProjectionTable
    @Inject AccountBalanceProjectionTable accountBalanceProjectionTable
    @Inject EventBusProvider eventBusProvider

    Configuration config
    EventStore eventStore

    public void run() {
        initializeAxom()
        initializeProjectionTables()

        /*new DnsIpAddressCommandHandler().ipHostMappingWasReadFromDns("127.0.0.1", "localhost")
        new DnsIpAddressCommandHandler().ipHostMappingWasReadFromDns("11.12.11.11", "sandstorm.de")
        new DnsIpAddressCommandHandler().ipHostMappingWasReadFromDns("11.12.11.15", "sandstorm.de")
        new DnsIpAddressCommandHandler().ipHostMappingWasReadFromDns("127.0.0.1", "localhost")
        new DnsIpAddressCommandHandler().ipHostMappingWasReadFromDns("11.12.11.11", "sandstorm.de")
        new DnsIpAddressCommandHandler().ipHostMappingWasReadFromDns("11.12.11.15", "sandstorm.de")
        new DnsIpAddressCommandHandler().ipHostMappingWasReadFromDns("127.0.0.1", "localhost")
        new DnsIpAddressCommandHandler().ipHostMappingWasReadFromDns("11.12.11.11", "sandstorm.de")
        new DnsIpAddressCommandHandler().ipHostMappingWasReadFromDns("11.12.11.15", "sandstorm.de")


        // Create dummy data
        def user = new User( firstName : "John",
            lastName: "Doe",
            eMail: "foo@bar.com",
            streetName: "Foostreet 42",
            zipCode: 555,
            city: "FooBar"
        )

        // Create user and accept payment method
        new NeosFundingProcessEventHandler().userPaymentAccepted(user,"This is a fancy label", PaymentMethod.MONTHLY)

        // Acknowledge payment
        new NeosFundingProcessEventHandler().paymentAcknowledged("FsyiWPPMDsHi8Aftt7zQCw", user)

        // Debit money w/o constraint
        new NeosFundingProcessEventHandler().debitFromAccount(40.0f)
        new NeosFundingProcessEventHandler().debitFromAccount(60.0f)
        new NeosFundingProcessEventHandler().debitFromAccount(100.0f)
        */

        // Debit money w/ constraint
        new AccountBalanceCommandHandler(eventStore).handleDebitMoney(new DebitMoneyCommand(1,20.0f))

        sleep(1000)
        config.shutdown()
    }

    protected void initializeAxom() {
        Configurer configurer = DefaultConfigurer.defaultConfiguration();
        configureDatabaseEventStorage(configurer)

       /* EventHandlingConfiguration ehConfiguration = new EventHandlingConfiguration()
            .registerEventHandler({ conf -> new TableProjection() })
            .registerEventHandler({ conf -> new AsynchronousProjection() })
            .registerTrackingProcessor('de.sandstorm.eventsourcingtest.dnsIpAddress.projection.async')
*/
        EventHandlingConfiguration ehConfiguration = new EventHandlingConfiguration()
            .registerEventHandler({ conf -> new SubscriptionOverviewProjectionTable() })
            .registerEventHandler({ conf -> new AccountBalanceProjectionTable()})
            .registerEventHandler({ conf -> new AsynchronousProjection() })
            .registerTrackingProcessor('de.sandstorm.eventsourcingtest.neosfundingprocess.projection.async')


        configurer.registerModule(ehConfiguration);

        config = configurer.buildConfiguration()
        config.start()
        def theEventBus = config.eventBus()
        eventBusProvider.eventBus = theEventBus
        eventStore = config.eventStore()


    }

   /* private initializeRepositories() {
        EventCountSnapshotTriggerDefinition snapshotTriggerDefinition = new EventCountSnapshotTriggerDefinition(
            snapshotter,
            50);

        CachingEventSourcingRepository<AccountBalancePositiveAggregate> repository = new CachingEventSourcingRepository<>(accountBalancePositiveAggregateFactory(),
            config.eventStore(),
            cache,
            snapshotTriggerDefinition);
    }

    public AggregateFactory<AccountBalancePositiveAggregate> accountBalancePositiveAggregateFactory() {
        GenericAggregateFactory<AccountBalancePositiveAggregate> aggregateFactory = new GenericAggregateFactory<>();
        aggregateFactory

        return aggregateFactory;
    }*/

    private initializeProjectionTables() {
        projectionTableFactory.createTestProjectionTable()
        subscriptionProjectionTable.createSubscriptionOverviewTable()
        accountBalanceProjectionTable.createAccountBalanceProjectionTable()
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

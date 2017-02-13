package de.sandstorm.eventsourcingtest.internal

import de.sandstormmedia.dependencyinjection.Inject
import de.sandstormmedia.dependencyinjection.ScopeSingleton

@ScopeSingleton
class ProjectionTableFactory {

    static final String projectionTableName = "test_projection"
    static final String ipAddressColumn = "ip_address"
    static final String counterColumn = "counter"

    @Inject DataSourceProvider dataSourceProvider

    public void createTestProjectionTable() {
        dataSourceProvider.withSql { sql ->
            sql.execute '''
                CREATE TABLE IF NOT EXISTS test_projection (
                    ip_address VARCHAR(64) NOT NULL,
                    counter INT NOT NULL DEFAULT 0,
                    PRIMARY KEY (ip_address)
                );
            '''
        }
    }

}

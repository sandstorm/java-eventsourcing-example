package de.sandstorm.eventsourcingtest.internal

import de.sandstormmedia.dependencyinjection.Inject
import de.sandstormmedia.dependencyinjection.ScopeSingleton
import groovy.sql.Sql

@ScopeSingleton
class TestProjectionTable {

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

    public void updateProjectionTable(String ipAddress) {
        println "update projection table"
        dataSourceProvider.withSql { Sql sql ->
            sql.execute '''
                INSERT INTO test_projection (ip_address, counter) VALUES (?, 1)
                ON DUPLICATE KEY UPDATE counter = counter + 1;
            ''', [ipAddress]
        }
    }

}

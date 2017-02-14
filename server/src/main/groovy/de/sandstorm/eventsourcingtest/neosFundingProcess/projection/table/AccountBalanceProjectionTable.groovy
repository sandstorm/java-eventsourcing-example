package de.sandstorm.eventsourcingtest.neosFundingProcess.projection.table

import de.sandstorm.eventsourcingtest.internal.DataSourceProvider
import de.sandstorm.eventsourcingtest.neosFundingProcess.model.User
import de.sandstormmedia.dependencyinjection.Inject
import de.sandstormmedia.dependencyinjection.ScopeSingleton
import groovy.sql.Sql

/**
 * Created by Sven on 13.02.2017.
 */

@ScopeSingleton
class AccountBalanceProjectionTable {

    @Inject DataSourceProvider dataSourceProvider

    public void createAccountBalanceProjectionTable() {
        dataSourceProvider.withSql { sql ->
            sql.execute '''
                CREATE TABLE IF NOT EXISTS accountbalance_projection (
                    accountID INT,
                    totalBalance FLOAT DEFAULT '1000.0',
                    PRIMARY KEY (accountID)
                );
            '''
        }
    }

    public void updateAccountBalanceProjectionTable(float amount) {
        println "update projection table"

        dataSourceProvider.withSql { Sql sql ->
            sql.execute '''
                INSERT INTO accountbalance_projection (accountID, totalBalance) VALUES (?, '1000.0')
                ON DUPLICATE KEY UPDATE totalBalance = totalBalance - (?);
            ''', [1,amount]
        }
    }

}

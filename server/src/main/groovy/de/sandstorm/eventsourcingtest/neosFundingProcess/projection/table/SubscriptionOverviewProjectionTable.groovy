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
class SubscriptionOverviewProjectionTable {

    @Inject DataSourceProvider dataSourceProvider

    public void createSubscriptionOverviewTable() {
        dataSourceProvider.withSql { sql ->
            sql.execute '''
                CREATE TABLE IF NOT EXISTS subscription_projection (
                    firstName VARCHAR(64) NOT NULL,
                    LastName VARCHAR(64) NOT NULL,
                    paymentMethod VARCHAR(64) NOT NULL,
                    PRIMARY KEY (firstName)
                );
            '''
        }
    }

    public void updateSubscriptionOverviewTable(User user, String paymentMethod) {
        println "update projection table"
        dataSourceProvider.withSql { Sql sql ->
            sql.execute '''
                INSERT INTO subscription_projection (firstName, lastName, paymentMethod) VALUES (?, ?, ?);
            ''', [user.firstName,user.lastName, paymentMethod]
        }
    }
}

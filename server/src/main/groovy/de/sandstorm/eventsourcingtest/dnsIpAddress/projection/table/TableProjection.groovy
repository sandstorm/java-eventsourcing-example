package de.sandstorm.eventsourcingtest.dnsIpAddress.projection.table

import de.sandstorm.eventsourcingtest.dnsIpAddress.event.IpHostMappingWasReadFromDns
import de.sandstorm.eventsourcingtest.internal.DataSourceProvider
import de.sandstormmedia.dependencyinjection.Inject
import groovy.sql.Sql
import org.axonframework.eventhandling.EventHandler

/**
 * Created by sebastian on 25.01.17.
 */
class TableProjection {

    @Inject DataSourceProvider dataSourceProvider

    @EventHandler
    public void handle(IpHostMappingWasReadFromDns event) {
        updateProjectionTable(event.ip)
    }

    private void updateProjectionTable(String ipAddress) {
        dataSourceProvider.withSql { Sql sql ->
            sql.execute '''
                REPLACE INTO test_projection SET
                    ip_address = ?,
                    counter = counter + 1
            ''', [ipAddress]
        }
    }
}

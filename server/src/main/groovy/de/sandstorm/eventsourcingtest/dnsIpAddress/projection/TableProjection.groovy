package de.sandstorm.eventsourcingtest.dnsIpAddress.projection

import de.sandstorm.eventsourcingtest.dnsIpAddress.event.IpHostMappingWasReadFromDns
import org.axonframework.eventhandling.EventHandler

/**
 * Created by sebastian on 25.01.17.
 */
class TableProjection {

    @EventHandler
    public void handle(IpHostMappingWasReadFromDns event) {
        println "TODO: update projection"
    }
}

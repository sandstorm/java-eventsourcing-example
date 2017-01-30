package de.sandstorm.eventsourcingtest.dnsIpAddress.projection.async

import de.sandstorm.eventsourcingtest.dnsIpAddress.event.IpHostMappingWasReadFromDns
import org.axonframework.eventhandling.EventHandler

/**
 * Created by sebastian on 25.01.17.
 */
class AsynchronousProjection {

    @EventHandler
    public void handleIpHostMappingWasReadFromDns(IpHostMappingWasReadFromDns event) {
        println "TODO: asynchronous projection"
    }
}

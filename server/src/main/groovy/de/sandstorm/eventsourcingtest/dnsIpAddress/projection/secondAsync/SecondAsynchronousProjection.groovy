package de.sandstorm.eventsourcingtest.dnsIpAddress.projection.secondAsync

import de.sandstorm.eventsourcingtest.dnsIpAddress.event.IpHostMappingWasReadFromDns
import org.axonframework.eventhandling.EventHandler

/**
 * Created by sebastian on 25.01.17.
 */
class SecondAsynchronousProjection {

    @EventHandler
    public void handleIpHostMappingWasReadFromDns(IpHostMappingWasReadFromDns event) {
        println "TODO: SECOND asynchronous projection"
    }
}

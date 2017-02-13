package de.sandstorm.eventsourcingtest.dnsIpAddress.projection.async

import de.sandstorm.eventsourcingtest.dnsIpAddress.event.IpHostMappingWasReadFromDns
import org.axonframework.eventhandling.EventHandler

/**
 * Created by sebastian on 25.01.17.
 */
class AsynchronousProjection {

    int counter

    @EventHandler
    public void handleIpHostMappingWasReadFromDns(IpHostMappingWasReadFromDns event) {
        counter++
        println "TODO: asynchronous projection $counter"
    }
}

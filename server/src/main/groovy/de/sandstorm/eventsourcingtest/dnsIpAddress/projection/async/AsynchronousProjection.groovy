package de.sandstorm.eventsourcingtest.dnsIpAddress.projection.async

import de.sandstorm.eventsourcingtest.dnsIpAddress.event.IpHostMappingWasReadFromDns
import de.sandstorm.eventsourcingtest.internal.TestProjectionTable
import de.sandstormmedia.dependencyinjection.Inject
import org.axonframework.eventhandling.EventHandler

class AsynchronousProjection {

    @Inject TestProjectionTable testProjectionTable

    @EventHandler
    public void handleIpHostMappingWasReadFromDns(IpHostMappingWasReadFromDns event) {
        // in this projection we count the IP changes
        testProjectionTable.updateProjectionTable(event.ip)
    }
}

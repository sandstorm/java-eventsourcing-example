package de.sandstorm.eventsourcingtest.dnsIpAddress

import de.sandstorm.eventsourcingtest.dnsIpAddress.event.IpHostMappingWasReadFromDns
import de.sandstorm.eventsourcingtest.internal.EventBusProvider
import de.sandstormmedia.dependencyinjection.Inject
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.GenericEventMessage

/**
 * Created by sebastian on 25.01.17.
 */
class DnsIpAddressCommandHandler {

    @Inject
    EventBusProvider eventBusProvider

    public void ipHostMappingWasReadFromDns(String ip, String hostname) {
        eventBusProvider.eventBus.publish(GenericEventMessage.asEventMessage(new IpHostMappingWasReadFromDns(
                ip: ip,
                hostname: hostname
        )))
        println "PUBLISHED"
    }


}

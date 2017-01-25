package de.sandstorm.eventsourcingtest.dnsIpAddress

import de.sandstorm.eventsourcingtest.dnsIpAddress.event.IpHostMappingWasReadFromDns
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.GenericEventMessage

/**
 * Created by sebastian on 25.01.17.
 */
class DnsIpAddressCommandHandler {

    EventBus eventBus

    public void ipHostMappingWasReadFromDns(String ip, String hostname) {
        eventBus.publish(GenericEventMessage.asEventMessage(new IpHostMappingWasReadFromDns(
                ip: ip,
                hostname: hostname
        )))
        println "PUBLISHED"
    }


}

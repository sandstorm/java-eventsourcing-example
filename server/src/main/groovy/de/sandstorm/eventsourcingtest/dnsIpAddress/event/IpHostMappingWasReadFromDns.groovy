package de.sandstorm.eventsourcingtest.dnsIpAddress.event

import groovy.transform.Immutable

/**
 * Created by sebastian on 25.01.17.
 */
@Immutable
class IpHostMappingWasReadFromDns {
    String ip
    String hostname
}

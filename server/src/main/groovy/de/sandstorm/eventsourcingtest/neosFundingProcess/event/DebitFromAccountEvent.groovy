package de.sandstorm.eventsourcingtest.neosFundingProcess.event

import groovy.transform.Immutable

/**
 * Created by Sven on 13.02.2017.
 */

@Immutable
class DebitFromAccountEvent {
    float amount
}

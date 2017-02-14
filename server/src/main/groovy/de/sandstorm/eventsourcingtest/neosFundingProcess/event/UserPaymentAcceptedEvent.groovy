package de.sandstorm.eventsourcingtest.neosFundingProcess.event

import de.sandstorm.eventsourcingtest.neosFundingProcess.model.User
import groovy.transform.Immutable

/**
 * Created by Sven on 13.02.2017.
 */

@Immutable
class UserPaymentAcceptedEvent {
    User user
    String label
    String paymentMethod
}





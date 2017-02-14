package de.sandstorm.eventsourcingtest.neosFundingProcess.event

import de.sandstorm.eventsourcingtest.neosFundingProcess.model.User

/**
 * Created by Sven on 13.02.2017.
 */
class PaymentAcknowledgedEvent {
    String token
    User user
}

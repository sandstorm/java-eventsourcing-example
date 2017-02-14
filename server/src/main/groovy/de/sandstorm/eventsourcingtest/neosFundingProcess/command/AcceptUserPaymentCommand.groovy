package de.sandstorm.eventsourcingtest.neosFundingProcess.command

import de.sandstorm.eventsourcingtest.neosFundingProcess.model.User
import groovy.transform.Immutable

/**
 * Created by Sven on 14.02.2017.
 */
@Immutable
class AcceptUserPaymentCommand {
    User user
    String label
    String paymentMethod
}

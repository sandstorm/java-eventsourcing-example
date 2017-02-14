package de.sandstorm.eventsourcingtest.neosFundingProcess.command

import groovy.transform.Immutable

/**
 * Created by Sven on 13.02.2017.
 */
@Immutable
class DebitMoneyCommand {
    int accountID
    float amount
}

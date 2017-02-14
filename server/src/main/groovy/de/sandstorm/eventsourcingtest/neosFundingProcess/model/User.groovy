package de.sandstorm.eventsourcingtest.neosFundingProcess.model

import groovy.transform.Immutable

/**
 * Created by Sven on 13.02.2017.
 */
@Immutable
class User {
    String firstName
    String lastName
    String eMail
    String streetName
    int zipCode
    String city
}

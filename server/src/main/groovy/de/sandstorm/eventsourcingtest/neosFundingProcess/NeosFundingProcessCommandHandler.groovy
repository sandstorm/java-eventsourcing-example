package de.sandstorm.eventsourcingtest.neosFundingProcess

import de.sandstorm.eventsourcingtest.neosFundingProcess.command.AcceptUserPaymentCommand
import de.sandstorm.eventsourcingtest.neosFundingProcess.model.User
import org.axonframework.commandhandling.CommandHandler

/**
 * Created by Sven on 14.02.2017.
 */
class NeosFundingProcessCommandHandler {


    def neosFundingProcessEventHandler = new NeosFundingProcessEventHandler()

    @CommandHandler
    handleAcceptUserPayment(AcceptUserPaymentCommand cmd) {

        // Dummy data
        def user = new User( firstName : "John",
            lastName: "Doe",
            eMail: "foo@bar.com",
            streetName: "Foostreet 42",
            zipCode: 555,
            city: "FooBar"
        )

        neosFundingProcessEventHandler.userPaymentAccepted(user,"This is a fancy label", "Monthly")

    }
}

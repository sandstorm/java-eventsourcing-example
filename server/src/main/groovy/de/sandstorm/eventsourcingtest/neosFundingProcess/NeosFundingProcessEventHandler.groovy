package de.sandstorm.eventsourcingtest.neosFundingProcess

import de.sandstorm.eventsourcingtest.internal.EventBusProvider
import de.sandstorm.eventsourcingtest.neosFundingProcess.event.DebitFromAccountEvent
import de.sandstorm.eventsourcingtest.neosFundingProcess.event.PaymentAcknowledgedEvent
import de.sandstorm.eventsourcingtest.neosFundingProcess.event.UserPaymentAcceptedEvent
import de.sandstorm.eventsourcingtest.neosFundingProcess.model.User
import de.sandstormmedia.dependencyinjection.Inject
import org.axonframework.eventhandling.GenericEventMessage

/**
 * Created by Sven on 13.02.2017.
 */
class NeosFundingProcessEventHandler {

    @Inject
    EventBusProvider eventBusProvider

    public void userPaymentAccepted(User user, String label, String paymentMethod) {
        eventBusProvider.eventBus.publish(GenericEventMessage.asEventMessage(new UserPaymentAcceptedEvent(
            user: user,
            label: label,
            paymentMethod: paymentMethod
        )))
        println "PUBLISHED PAYMENT ACCEPTED"
    }

    public void paymentAcknowledged(String token, User user) {
        eventBusProvider.eventBus.publish(GenericEventMessage.asEventMessage(new PaymentAcknowledgedEvent(
            token: token,
            user: user
        )))
        println "PUBLISHED PAYMENT ACKNOWLEDGED"
    }


    public void debitFromAccount(float amount) {
        eventBusProvider.eventBus.publish(GenericEventMessage.asEventMessage(new DebitFromAccountEvent(
            amount: amount
        )))
        println "PUBLISHED DEBITED FROM ACCOUNT"
    }

}

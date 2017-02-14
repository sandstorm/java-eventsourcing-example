package de.sandstorm.eventsourcingtest.neosFundingProcess.projection.async

import de.sandstorm.eventsourcingtest.neosFundingProcess.event.DebitFromAccountEvent
import de.sandstorm.eventsourcingtest.neosFundingProcess.event.UserPaymentAcceptedEvent
import de.sandstorm.eventsourcingtest.neosFundingProcess.projection.table.AccountBalanceProjectionTable
import de.sandstorm.eventsourcingtest.neosFundingProcess.projection.table.SubscriptionOverviewProjectionTable
import de.sandstormmedia.dependencyinjection.Inject
import org.axonframework.eventhandling.EventHandler

class AsynchronousProjection {

    @Inject
    SubscriptionOverviewProjectionTable subscriptionOverviewProjectionTable

    @Inject
    AccountBalanceProjectionTable accountBalanceProjectionTable

    @EventHandler
    public void handleUserPaymentAccepted(UserPaymentAcceptedEvent event) {

        subscriptionOverviewProjectionTable.updateSubscriptionOverviewTable(event.user)

    }

    @EventHandler
    public void handleManualMoneyWithdrawn(DebitFromAccountEvent event) {

        accountBalanceProjectionTable.updateAccountBalanceProjectionTable(event.amount)

    }


}

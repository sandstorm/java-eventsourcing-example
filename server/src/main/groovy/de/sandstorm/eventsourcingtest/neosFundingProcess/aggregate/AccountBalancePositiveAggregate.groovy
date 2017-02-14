package de.sandstorm.eventsourcingtest.neosFundingProcess.aggregate

import de.sandstorm.eventsourcingtest.neosFundingProcess.event.DebitFromAccountEvent
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateRoot
import org.axonframework.eventhandling.EventHandler

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

/**
 * Created by Sven on 13.02.2017.
 */
@AggregateRoot
class AccountBalancePositiveAggregate {

    @AggregateIdentifier
    public int accountID
    private float balance = 1000.0f

    // Needed for Axon
    AccountBalancePositiveAggregate(){}

    public AccountBalancePositiveAggregate(int accountID, float balance) {
        this.accountID = accountID
        this.balance = balance
    }

    // Business logic here. Hard constraint check, but soft constraint would suffice too
    public boolean debit(float amount) {
        boolean success = ((balance - amount) < 0.0)
        if(success) {
            apply(new DebitFromAccountEvent(amount))
        }
        return success
    }

    @EventHandler
    public void onAccountDebited(DebitFromAccountEvent event) {
        this.balance -= event.amount
    }

}

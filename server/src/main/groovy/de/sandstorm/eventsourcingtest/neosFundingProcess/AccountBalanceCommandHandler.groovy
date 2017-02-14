package de.sandstorm.eventsourcingtest.neosFundingProcess

import de.sandstorm.eventsourcingtest.neosFundingProcess.aggregate.AccountBalancePositiveAggregate
import de.sandstorm.eventsourcingtest.neosFundingProcess.command.DebitMoneyCommand
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateNotFoundException
import org.axonframework.commandhandling.model.Repository
import org.axonframework.eventsourcing.EventSourcingRepository
import org.axonframework.eventsourcing.eventstore.EventStore
import org.axonframework.messaging.unitofwork.DefaultUnitOfWork
import org.axonframework.messaging.unitofwork.UnitOfWork

/**
 * Created by Sven on 13.02.2017.
 */
class AccountBalanceCommandHandler {

    private Repository<AccountBalancePositiveAggregate> repository

    AccountBalanceCommandHandler(EventStore eventStore){

        this.repository = new EventSourcingRepository<>(AccountBalancePositiveAggregate.class, eventStore)

    }

    @CommandHandler
    public void handleDebitMoney(DebitMoneyCommand cmd) {

        UnitOfWork uow = DefaultUnitOfWork.startAndGet();
        try {
            // business logic comes herex
            def account
            try {
                account = repository.load(String.valueOf(cmd.accountID))
            } catch (AggregateNotFoundException e) {
                // In a real application we would probably implement a CreateAccountCommand // CreateAccountEvent
                account = repository.newInstance({ new AccountBalancePositiveAggregate(cmd.accountID, 1000.0f) })
            }

            if (account != null) {
                    if(!account.invoke({ a -> a.debit(cmd.amount)})) {
                        println "Insufficient Fund!"
                    } else {
                        println "AccountBalancePositiveAggregate debited!"
                    }
                }
            // Command handling is finished, commit work
            uow.commit();
            }
     catch (Exception e) {
        uow.rollback(e);
        // maybe rethrow...
    }

}


}

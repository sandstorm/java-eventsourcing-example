package de.sandstorm.eventsourcingtest.internal

import de.sandstormmedia.dependencyinjection.ScopeSingleton
import org.axonframework.eventhandling.EventBus

@ScopeSingleton
class EventBusProvider {

    EventBus eventBus

}

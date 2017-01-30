package de.sandstorm.eventsourcingtest.internal

import groovy.transform.InheritConstructors
import org.axonframework.common.jdbc.ConnectionProvider
import org.axonframework.common.jdbc.PersistenceExceptionResolver
import org.axonframework.common.transaction.TransactionManager
import org.axonframework.eventhandling.EventMessage
import org.axonframework.eventsourcing.DomainEventMessage
import org.axonframework.eventsourcing.eventstore.jdbc.EventSchema
import org.axonframework.eventsourcing.eventstore.jdbc.JdbcEventStorageEngine
import org.axonframework.eventsourcing.eventstore.jdbc.JdbcSQLErrorCodesResolver
import org.axonframework.serialization.SerializedObject
import org.axonframework.serialization.Serializer
import org.axonframework.serialization.upcasting.event.EventUpcaster

import java.sql.PreparedStatement

import static org.axonframework.common.ObjectUtils.getOrDefault
import static org.axonframework.common.ObjectUtils.getOrDefault
import static org.axonframework.common.ObjectUtils.getOrDefault
import static org.axonframework.common.jdbc.JdbcUtils.executeBatch
import static org.axonframework.serialization.MessageSerializer.serializeMetaData
import static org.axonframework.serialization.MessageSerializer.serializePayload

class CustomJdbcEventStorageEngine extends JdbcEventStorageEngine {

    private EventSchema eventSchema
    private Class<?> dataType
    private TransactionManager transactionManager

    public CustomJdbcEventStorageEngine(ConnectionProvider connectionProvider, TransactionManager transactionManager) {
        this(null, null, null, null, connectionProvider, transactionManager, byte[].class, new EventSchema(), null, null);
    }

    public CustomJdbcEventStorageEngine(Serializer serializer, EventUpcaster upcasterChain,
                                  PersistenceExceptionResolver persistenceExceptionResolver, Integer batchSize,
                                  ConnectionProvider connectionProvider, TransactionManager transactionManager,
                                  Class<?> dataType, EventSchema schema,
                                  Integer maxGapOffset, Long lowestGlobalSequence) {
        super(serializer, upcasterChain, persistenceExceptionResolver, batchSize, connectionProvider, transactionManager, dataType, schema, maxGapOffset, lowestGlobalSequence)
        this.eventSchema = schema;
        this.dataType = dataType;
        this.transactionManager = transactionManager
    }

    @Override
    protected void appendEvents(List<? extends EventMessage<?>> events, Serializer serializer) {
        if (events.isEmpty()) {
            return;
        }
        def schema = eventSchema
        final String table = schema.domainEventTable();
        final String sql = "INSERT INTO " + table + " (" +
                String.join(", ", schema.eventIdentifierColumn(), schema.aggregateIdentifierColumn(),
                        schema.sequenceNumberColumn(), schema.typeColumn(), schema.timestampColumn(),
                        schema.payloadTypeColumn(), schema.payloadRevisionColumn(), schema.payloadColumn(),
                        schema.metaDataColumn()) + ") VALUES (?,?,?,?,?,?,?,?,?)";
        transactionManager.executeInTransaction({ ->
                executeBatch(getConnection(), {connection ->
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);

                    for (EventMessage<?> eventMessage : events) {
                        SerializedObject<?> payload = serializePayload(eventMessage, serializer, dataType);
                        SerializedObject<?> metaData = serializeMetaData(eventMessage, serializer, dataType);
                        preparedStatement.setString(1, eventMessage.getIdentifier());
                        if (eventMessage instanceof DomainEventMessage) {
                            DomainEventMessage<?> event = (DomainEventMessage<?>) eventMessage;
                            preparedStatement.setString(2, event.getAggregateIdentifier());
                            preparedStatement.setLong(3, event.getSequenceNumber());
                            preparedStatement.setString(4, event.getType());
                        } else {
                            preparedStatement.setString(2, "");
                            preparedStatement.setLong(3, 0);
                            preparedStatement.setString(4, "");
                        }
                        writeTimestamp(preparedStatement, 5, eventMessage.getTimestamp());
                        preparedStatement.setString(6, payload.getType().getName());
                        preparedStatement.setString(7, payload.getType().getRevision());
                        preparedStatement.setObject(8, payload.getData());
                        preparedStatement.setObject(9, metaData.getData());
                        preparedStatement.addBatch();
                    }
                    return preparedStatement;
                }, {e -> handlePersistenceException(e, events.get(0))})
    });
    }
}

package de.sandstorm.eventsourcingtest.internal

import org.axonframework.eventsourcing.eventstore.jdbc.EventSchema
import org.axonframework.eventsourcing.eventstore.jdbc.MySqlEventTableFactory

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * Created by sebastian on 25.01.17.
 */
@Singleton
class CustomMySqlEventTableFactory extends MySqlEventTableFactory {

    @Override
    public PreparedStatement createDomainEventTable(Connection connection,
                                                    EventSchema schema) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS " + schema.domainEventTable() + " (\n" +
                schema.globalIndexColumn() + " " + idColumnType() + " NOT NULL,\n" +
                schema.aggregateIdentifierColumn() + " VARCHAR(255) NOT NULL,\n" +
                schema.sequenceNumberColumn() + " BIGINT NOT NULL,\n" +
                schema.typeColumn() + " VARCHAR(255) NOT NULL,\n" +
                schema.eventIdentifierColumn() + " VARCHAR(255) NOT NULL,\n" +
                schema.metaDataColumn() + " " + payloadType() + ",\n" +
                schema.payloadColumn() + " " + payloadType() + " NOT NULL,\n" +
                schema.payloadRevisionColumn() + " VARCHAR(255),\n" +
                schema.payloadTypeColumn() + " VARCHAR(255) NOT NULL,\n" +
                schema.timestampColumn() + " VARCHAR(255) NOT NULL,\n" +
                "PRIMARY KEY (" + schema.globalIndexColumn() + "),\n" +
                // REMOVE UNIQUENESS HERE
                //"UNIQUE (" + schema.aggregateIdentifierColumn() + ", " +
                //schema.sequenceNumberColumn()+ "),\n" +
                "UNIQUE (" + schema.eventIdentifierColumn() + ")\n" +
                ")";
        return connection.prepareStatement(sql);
    }
}

package de.sandstorm.eventsourcingtest.internal

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import de.sandstormmedia.dependencyinjection.ScopeSingleton
import groovy.sql.Sql

import javax.sql.DataSource

@ScopeSingleton
class DataSourceProvider {

    DataSource dataSource

    DataSourceProvider() {
        dataSource = createDataSource(true)
    }

    public withSql(Closure<?> sqlAction) {
        Sql sql = new Sql(dataSource)
        sqlAction.call(sql)
        sql.close()
    }

    public static DataSource createDataSource(boolean autoCommit) {
        // TODO: relaxAutoCommit
        def db = [url: 'jdbc:mysql://127.0.0.1:3306/es-example', user: 'root', password: '']

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(db.url);
        config.setUsername(db.user);
        config.setPassword(db.password);
        config.setAutoCommit(autoCommit)
        //config.addDataSourceProperty("autoCommit", false);

        return new HikariDataSource(config);
    }
}

package de.hhu.cs.dbs.propra.application.configurations;

import de.hhu.cs.dbs.propra.application.services.BasicHTTPAuthenticationService;
import de.hhu.cs.dbs.propra.application.services.CustomAuthorizationService;
import de.hhu.cs.dbs.propra.domain.model.UserRepository;
import de.hhu.cs.dbs.propra.infrastructure.repositories.SQLiteUserRepository;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Properties;

public class AbstractBinder extends org.glassfish.hk2.utilities.binding.AbstractBinder {
    @Override
    protected void configure() {
        bind(getDataSource()).to(DataSource.class);
        bind(SQLiteUserRepository.class).to(UserRepository.class);
        bindAsContract(BasicHTTPAuthenticationService.class);
        bindAsContract(CustomAuthorizationService.class);
    }

    private SQLiteDataSource getDataSource() {
        Properties properties = new Properties();
        properties.put("auto_vacuum", "full");
        SQLiteConfig config = new SQLiteConfig(properties);
        config.setEncoding(SQLiteConfig.Encoding.UTF8);
        config.enforceForeignKeys(true);
        config.setJournalMode(SQLiteConfig.JournalMode.WAL);
        config.setSynchronous(SQLiteConfig.SynchronousMode.NORMAL);
        SQLiteDataSource dataSource = new SQLiteDataSource(config);
        try {
            dataSource.setLogWriter(new PrintWriter(System.out));
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
        }
        dataSource.setUrl("jdbc:sqlite:data" + File.separator + "database.db");
        return dataSource;
    }
}

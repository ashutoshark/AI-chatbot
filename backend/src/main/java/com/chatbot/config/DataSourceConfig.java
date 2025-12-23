package com.chatbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.net.URI;

@Configuration
@Profile("production")
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");
        
        if (databaseUrl == null || databaseUrl.isEmpty()) {
            throw new RuntimeException("DATABASE_URL environment variable is required");
        }

        try {
            URI dbUri = new URI(databaseUrl);
            String[] userInfo = dbUri.getUserInfo().split(":");
            
            String jdbcUrl = String.format(
                "jdbc:postgresql://%s:%d%s?sslmode=require",
                dbUri.getHost(),
                dbUri.getPort() != -1 ? dbUri.getPort() : 5432,
                dbUri.getPath()
            );
            
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(userInfo[0]);
            config.setPassword(userInfo[1]);
            config.setMaximumPoolSize(5);
            config.setMinimumIdle(1);
            
            return new HikariDataSource(config);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse DATABASE_URL: " + e.getMessage(), e);
        }
    }
}

package org.example.pool;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class MyDataSource {

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;

    static {
        // URL correcta apuntando a la base de datos "jbdc" en el puerto 3306
        // El puerto 8081 es para phpMyAdmin (interfaz web), mientras que 3306 es el puerto real de MySQL
        config.setJdbcUrl("jdbc:mysql://localhost:3306/jbdc?allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true&serverTimezone=Europe/Madrid");
        
        // Credenciales según la configuración de Docker
        config.setUsername("user");
        config.setPassword("password");
        
        // Configuración del tamaño del pool (como método en lugar de propiedad)
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        
        // Timeouts recomendados
        config.setConnectionTimeout(30000); // 30 segundos
        config.setIdleTimeout(600000); // 10 minutos
        config.setMaxLifetime(1800000); // 30 minutos

        // Propiedades recomendadas para MySQL
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
    }

    private MyDataSource() {}

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
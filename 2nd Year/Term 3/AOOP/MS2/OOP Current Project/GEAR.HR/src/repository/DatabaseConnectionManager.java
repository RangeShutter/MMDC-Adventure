package repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Opens and closes JDBC connections to the GEAR.HR MySQL database.
 * [ENCAPSULATION] Centralizes connection configuration (URL, user, password) so
 * repository classes do not duplicate JDBC boilerplate or know connection details.
 * Configuration is read once from database.properties at the project root;
 * defaults are used when the file or a key is missing.
 */
public final class DatabaseConnectionManager {

    private static final String CONFIG_FILE = "database.properties";

    private static final String DEFAULT_URL =
        "jdbc:mysql://localhost:3306/gear.hr?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    /** [ENCAPSULATION] Loaded once; callers never see raw properties. */
    private static final Properties CONFIG = loadConfig();

    private DatabaseConnectionManager() {
        // utility class; no instances
    }

    /**
     * Returns a new JDBC connection to MySQL using the configured URL and credentials.
     * @return open {@link Connection}; caller is responsible for closing it
     * @throws SQLException if the connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            CONFIG.getProperty("db.url", DEFAULT_URL),
            CONFIG.getProperty("db.user", DEFAULT_USER),
            CONFIG.getProperty("db.password", DEFAULT_PASSWORD)
        );
    }

    /**
     * Closes the given connection safely (null-safe, swallows close errors).
     * @param conn connection to close; may be null
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignored) {
                // closing failures are not actionable for callers
            }
        }
    }

    private static Properties loadConfig() {
        Properties props = new Properties();
        Path path = Paths.get(CONFIG_FILE);
        if (!Files.exists(path)) {
            // Tolerate launches from the repo root instead of the GEAR.HR project folder.
            path = Paths.get("GEAR.HR", CONFIG_FILE);
        }
        if (Files.exists(path)) {
            try (InputStream in = Files.newInputStream(path)) {
                props.load(in);
            } catch (IOException ignored) {
                // fall through to defaults
            }
        }
        return props;
    }
}

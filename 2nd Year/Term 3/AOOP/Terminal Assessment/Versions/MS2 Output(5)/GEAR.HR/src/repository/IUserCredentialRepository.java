package repository;

import model.UserCredential;

import java.util.List;

/**
 * [INTERFACE] JDBC persistence contract for user credentials.
 * Production implementation: {@link UserCredentialJdbcRepository} (MySQL table {@code user_credentials}).
 * Alternative implementations may be supplied for unit tests.
 */
public interface IUserCredentialRepository {
    /** [INTERFACE] Loads all credentials from the database via JDBC. */
    List<UserCredential> load();

    /** [INTERFACE] Persists credentials to the database via JDBC (full table replace). */
    void save(List<UserCredential> credentials);
}

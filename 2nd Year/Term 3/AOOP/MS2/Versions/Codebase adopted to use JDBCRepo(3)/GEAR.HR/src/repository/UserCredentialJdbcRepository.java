package repository;

import model.UserCredential;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * JDBC persistence for user credentials (MySQL table: user_credentials). Load/save only; no business logic.
 * [INTERFACE] Implements IUserCredentialRepository.
 * [INHERITANCE] Extends AbstractJdbcRepository for shared JDBC load/save; provides UserCredential-specific SQL and mapping.
 * [POLYMORPHISM] Can be used as IUserCredentialRepository by callers.
 */
public class UserCredentialJdbcRepository extends AbstractJdbcRepository<UserCredential> implements IUserCredentialRepository {

    /** [ABSTRACTION] [INHERITANCE] Returns the user_credentials table name. */
    @Override
    protected String getTableName() {
        return "user_credentials";
    }

    /** [ABSTRACTION] [INHERITANCE] Returns the parameterized INSERT for one credential row. */
    @Override
    protected String getInsertSql() {
        return "INSERT INTO user_credentials (user_id, password, role, email) VALUES (?,?,?,?)";
    }

    /** [ABSTRACTION] [INHERITANCE] Maps one result row to a UserCredential. */
    @Override
    protected UserCredential mapRow(ResultSet rs) throws SQLException {
        return new UserCredential(
            rs.getString("user_id"),
            rs.getString("password"),
            rs.getString("role"),
            rs.getString("email")
        );
    }

    /** [ABSTRACTION] [INHERITANCE] Binds one UserCredential onto the INSERT parameters. */
    @Override
    protected void bindInsert(PreparedStatement ps, UserCredential cred) throws SQLException {
        ps.setString(1, cred.getUserId());
        ps.setString(2, cred.getPassword());
        ps.setString(3, cred.getRole());
        ps.setString(4, cred.getEmail());
    }

    /** [INTERFACE] Implements IUserCredentialRepository.load. [INHERITANCE] Delegates to AbstractJdbcRepository.loadAll. */
    @Override
    public List<UserCredential> load() {
        return loadAll();
    }

    /** [INTERFACE] Implements IUserCredentialRepository.save. [INHERITANCE] Delegates to AbstractJdbcRepository.replaceAll. */
    @Override
    public void save(List<UserCredential> credentials) {
        replaceAll(credentials);
    }
}

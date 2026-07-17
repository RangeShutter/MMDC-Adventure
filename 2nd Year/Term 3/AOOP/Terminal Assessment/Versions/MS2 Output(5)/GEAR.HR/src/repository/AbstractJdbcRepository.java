package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * [ABSTRACTION] Abstract base for JDBC repositories that persist a list of entities
 * in a single MySQL table. Subclasses provide table name, insert SQL, row mapping,
 * and parameter binding; this class implements the common load/save logic.
 * [INHERITANCE] Concrete JDBC repos extend and implement the abstract hooks.
 */
public abstract class AbstractJdbcRepository<T> {

    /**
     * [ABSTRACTION] Subclass returns the table name (e.g. "employees").
     */
    protected abstract String getTableName();

    /**
     * [ABSTRACTION] Subclass returns the parameterized INSERT statement
     * (e.g. "INSERT INTO employees VALUES (?,?,?,?,?,?,?,?,?,?,?,?)").
     */
    protected abstract String getInsertSql();

    /**
     * [ABSTRACTION] Subclass maps the current ResultSet row to an entity; returns null to skip.
     */
    protected abstract T mapRow(ResultSet rs) throws SQLException;

    /**
     * [ABSTRACTION] Subclass binds one entity's values onto the INSERT statement parameters.
     */
    protected abstract void bindInsert(PreparedStatement ps, T item) throws SQLException;

    /**
     * [ABSTRACTION] [INHERITANCE] Template method: JDBC SELECT * from the table and map
     * each row via {@code mapRow}. Returns an empty list on {@link SQLException} so the
     * application can continue without crashing when the database is unavailable.
     */
    public List<T> loadAll() {
        List<T> list = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName();
        Connection conn = null;
        try {
            conn = DatabaseConnectionManager.getConnection();
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    T item = mapRow(rs);
                    if (item != null) list.add(item);
                }
            }
        } catch (SQLException e) {
            // return empty list on JDBC failure
        } finally {
            DatabaseConnectionManager.closeConnection(conn);
        }
        return list;
    }

    /**
     * [ABSTRACTION] [INHERITANCE] Template method: JDBC full-table replace — DELETE all rows
     * then batched INSERTs inside one transaction via {@link DatabaseConnectionManager}.
     * Rolls back the transaction on {@link SQLException}.
     */
    public void replaceAll(List<T> items) {
        if (items == null) return;
        Connection conn = null;
        try {
            conn = DatabaseConnectionManager.getConnection();
            conn.setAutoCommit(false);
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("DELETE FROM " + getTableName());
            }
            try (PreparedStatement ps = conn.prepareStatement(getInsertSql())) {
                for (T item : items) {
                    bindInsert(ps, item);
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            conn.commit();
        } catch (SQLException e) {
            rollbackQuietly(conn);
        } finally {
            DatabaseConnectionManager.closeConnection(conn);
        }
    }

    private static void rollbackQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ignored) {
                // nothing further to do
            }
        }
    }
}

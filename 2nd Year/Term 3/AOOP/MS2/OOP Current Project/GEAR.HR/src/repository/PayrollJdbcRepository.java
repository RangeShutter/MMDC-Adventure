package repository;

import model.PayrollData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * JDBC persistence for payroll data (MySQL table: {@code payroll_settings}; employeeId -> PayrollData).
 * Load/save only; no business logic. Implements the map-based {@link IPayrollRepository} contract
 * directly since the list-oriented {@link AbstractJdbcRepository} does not fit a keyed map.
 * Connections are obtained from {@link DatabaseConnectionManager}.
 * [INTERFACE] Implements IPayrollRepository.
 * [POLYMORPHISM] Can be used as IPayrollRepository by callers.
 * [ENCAPSULATION] All payroll SQL and row mapping is hidden inside this class.
 */
public class PayrollJdbcRepository implements IPayrollRepository {

    private static final String TABLE = "payroll_settings";
    private static final String INSERT_SQL =
        "INSERT INTO payroll_settings (employee_id, base_salary, hourly_rate, sss_amount, "
        + "phil_health_amount, pag_ibig_amount, withholding_tax, rice_subsidy, phone_allowance, "
        + "clothing_allowance) VALUES (?,?,?,?,?,?,?,?,?,?)";

    /** [INTERFACE] Implements IPayrollRepository.load: reads all rows into an employeeId -> PayrollData map. */
    @Override
    public Map<String, PayrollData> load() {
        Map<String, PayrollData> map = new HashMap<>();
        Connection conn = null;
        try {
            conn = DatabaseConnectionManager.getConnection();
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT * FROM " + TABLE)) {
                while (rs.next()) {
                    map.put(rs.getString("employee_id"), new PayrollData(
                        rs.getDouble("base_salary"),
                        rs.getDouble("hourly_rate"),
                        rs.getDouble("sss_amount"),
                        rs.getDouble("phil_health_amount"),
                        rs.getDouble("pag_ibig_amount"),
                        (float) rs.getDouble("withholding_tax"),
                        (float) rs.getDouble("rice_subsidy"),
                        (float) rs.getDouble("phone_allowance"),
                        (float) rs.getDouble("clothing_allowance")
                    ));
                }
            }
        } catch (SQLException e) {
            // return empty map on JDBC failure
        } finally {
            DatabaseConnectionManager.closeConnection(conn);
        }
        return map;
    }

    /** [INTERFACE] Implements IPayrollRepository.save: replaces table contents in one transaction. */
    @Override
    public void save(Map<String, PayrollData> data) {
        if (data == null) return;
        Connection conn = null;
        try {
            conn = DatabaseConnectionManager.getConnection();
            conn.setAutoCommit(false);
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("DELETE FROM " + TABLE);
            }
            try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
                for (Map.Entry<String, PayrollData> e : data.entrySet()) {
                    PayrollData d = e.getValue();
                    ps.setString(1, e.getKey());
                    ps.setDouble(2, d.getBaseSalary());
                    ps.setDouble(3, d.getHourlyRate());
                    ps.setDouble(4, d.getSssAmount());
                    ps.setDouble(5, d.getPhilHealthAmount());
                    ps.setDouble(6, d.getPagIbigAmount());
                    ps.setDouble(7, d.getTaxDeduction());
                    ps.setDouble(8, d.getRiceSubsidy());
                    ps.setDouble(9, d.getPhoneAllowance());
                    ps.setDouble(10, d.getClothingAllowance());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            conn.commit();
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ignored) {
                // nothing further to do
            }
        } finally {
            DatabaseConnectionManager.closeConnection(conn);
        }
    }
}

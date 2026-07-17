package repository;

import model.ItTicket;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * JDBC persistence for IT support tickets (MySQL table: it_tickets). Load/save only; no business logic.
 * [INTERFACE] Implements IItTicketRepository.
 * [INHERITANCE] Extends AbstractJdbcRepository for shared JDBC load/save; provides ItTicket-specific SQL and mapping.
 * [POLYMORPHISM] Can be used as IItTicketRepository by callers.
 */
public class ItTicketJdbcRepository extends AbstractJdbcRepository<ItTicket> implements IItTicketRepository {

    /** [ABSTRACTION] [INHERITANCE] Returns the it_tickets table name. */
    @Override
    protected String getTableName() {
        return "it_tickets";
    }

    /** [ABSTRACTION] [INHERITANCE] Returns the parameterized INSERT for one ticket row. */
    @Override
    protected String getInsertSql() {
        return "INSERT INTO it_tickets (ticket_id, user_id_requestor, type_of_request, status) "
            + "VALUES (?,?,?,?)";
    }

    /** [ABSTRACTION] [INHERITANCE] Maps one result row to an ItTicket. */
    @Override
    protected ItTicket mapRow(ResultSet rs) throws SQLException {
        return new ItTicket(
            rs.getString("ticket_id"),
            rs.getString("user_id_requestor"),
            rs.getString("type_of_request"),
            rs.getString("status")
        );
    }

    /** [ABSTRACTION] [INHERITANCE] Binds one ItTicket onto the INSERT parameters. */
    @Override
    protected void bindInsert(PreparedStatement ps, ItTicket ticket) throws SQLException {
        ps.setString(1, ticket.getTicketId());
        ps.setString(2, ticket.getUserIdRequestor());
        ps.setString(3, ticket.getTypeOfRequest());
        ps.setString(4, ticket.getStatus());
    }

    /** [INTERFACE] Implements IItTicketRepository.load. [INHERITANCE] Delegates to AbstractJdbcRepository.loadAll. */
    @Override
    public List<ItTicket> load() {
        return loadAll();
    }

    /** [INTERFACE] Implements IItTicketRepository.save. [INHERITANCE] Delegates to AbstractJdbcRepository.replaceAll. */
    @Override
    public void save(List<ItTicket> tickets) {
        replaceAll(tickets);
    }
}

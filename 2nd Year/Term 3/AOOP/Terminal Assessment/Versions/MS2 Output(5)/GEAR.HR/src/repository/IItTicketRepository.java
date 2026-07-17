package repository;

import model.ItTicket;

import java.util.List;

/**
 * [INTERFACE] JDBC persistence contract for IT support tickets.
 * Production implementation: {@link ItTicketJdbcRepository} (MySQL table {@code it_tickets}).
 * Alternative implementations may be supplied for unit tests.
 */
public interface IItTicketRepository {
    /** [INTERFACE] Loads all IT tickets from the database via JDBC. */
    List<ItTicket> load();

    /** [INTERFACE] Persists IT tickets to the database via JDBC (full table replace). */
    void save(List<ItTicket> tickets);
}

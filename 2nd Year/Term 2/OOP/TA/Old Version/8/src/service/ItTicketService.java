package service;

import model.ItTicket;
import repository.IItTicketRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ItTicketService implements IItTicketService {
    private final IItTicketRepository repository;
    private final AuthenticationService authenticationService;
    private final List<ItTicket> tickets = new ArrayList<>();

    public ItTicketService(IItTicketRepository repository, AuthenticationService authenticationService) {
        this.repository = repository;
        this.authenticationService = authenticationService;
        reload();
    }

    @Override
    public void reload() {
        tickets.clear();
        tickets.addAll(repository.load());
    }

    @Override
    public List<ItTicket> getAllTickets() {
        List<ItTicket> copy = new ArrayList<>(tickets);
        copy.sort(Comparator.comparing(ItTicket::getTicketId));
        return copy;
    }

    @Override
    public String createTicket(String userIdRequestor, String typeOfRequest) {
        String userId = userIdRequestor != null ? userIdRequestor.trim() : "";
        String requestType = typeOfRequest != null ? typeOfRequest.trim() : "";
        if (userId.isEmpty()) return "User ID Requestor is required.";
        if (requestType.isEmpty()) return "Type of Request is required.";
        if (!"Forgot Password".equals(requestType)) return "Unsupported request type.";
        if (authenticationService == null || !authenticationService.hasUser(userId)) {
            return "User ID Requestor is invalid.";
        }

        String nextId = generateNextTicketId();
        ItTicket ticket = new ItTicket(nextId, userId, requestType, ItTicket.STATUS_PENDING);
        if (!ticket.isValid()) return "Ticket data is invalid.";

        tickets.add(ticket);
        repository.save(tickets);
        return null;
    }

    @Override
    public boolean updateTicketStatus(String ticketId, String newStatus) {
        if (ticketId == null || newStatus == null) return false;
        String status = newStatus.trim();
        if (!ItTicket.STATUS_PENDING.equals(status) && !ItTicket.STATUS_RESOLVED.equals(status)) return false;
        for (ItTicket ticket : tickets) {
            if (ticketId.trim().equals(ticket.getTicketId())) {
                ticket.setStatus(status);
                repository.save(tickets);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteTicket(String ticketId) {
        if (ticketId == null) return false;
        boolean removed = tickets.removeIf(t -> ticketId.trim().equals(t.getTicketId()));
        if (removed) repository.save(tickets);
        return removed;
    }

    private String generateNextTicketId() {
        int max = 0;
        for (ItTicket ticket : tickets) {
            if (ticket == null || ticket.getTicketId() == null) continue;
            String id = ticket.getTicketId().trim();
            if (id.startsWith("T")) {
                try {
                    max = Math.max(max, Integer.parseInt(id.substring(1)));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return String.format("T%04d", max + 1);
    }
}

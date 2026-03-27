package service;

import model.ItTicket;

import java.util.List;

public interface IItTicketService {
    void reload();
    List<ItTicket> getAllTickets();
    String createTicket(String userIdRequestor, String typeOfRequest);
    boolean updateTicketStatus(String ticketId, String newStatus);
    boolean deleteTicket(String ticketId);
}

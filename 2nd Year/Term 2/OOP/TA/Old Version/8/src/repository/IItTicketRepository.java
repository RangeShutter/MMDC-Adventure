package repository;

import model.ItTicket;

import java.util.List;

public interface IItTicketRepository {
    List<ItTicket> load();
    void save(List<ItTicket> tickets);
}

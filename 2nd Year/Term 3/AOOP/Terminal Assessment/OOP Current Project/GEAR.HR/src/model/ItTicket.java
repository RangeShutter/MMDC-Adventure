package model;

/**
 * Domain model for IT support tickets.
 * [INHERITANCE] Child class extends AbstractEntity.
 */
public class ItTicket extends AbstractEntity {

    private final String ticketId;
    private String userIdRequestor;
    private String typeOfRequest;
    /** [ENCAPSULATION] Status held as a type-safe {@link TicketStatus} enum. */
    private TicketStatus status;

    public ItTicket(String ticketId, String userIdRequestor, String typeOfRequest, String status) {
        super(ticketId != null ? ticketId.trim() : "");
        this.ticketId = ticketId != null ? ticketId.trim() : "";
        this.userIdRequestor = userIdRequestor != null ? userIdRequestor.trim() : "";
        this.typeOfRequest = typeOfRequest != null ? typeOfRequest.trim() : "";
        this.status = TicketStatus.fromStringOrDefault(status, TicketStatus.PENDING);
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getUserIdRequestor() {
        return userIdRequestor;
    }

    public void setUserIdRequestor(String userIdRequestor) {
        this.userIdRequestor = userIdRequestor != null ? userIdRequestor.trim() : this.userIdRequestor;
    }

    public String getTypeOfRequest() {
        return typeOfRequest;
    }

    public void setTypeOfRequest(String typeOfRequest) {
        this.typeOfRequest = typeOfRequest != null ? typeOfRequest.trim() : this.typeOfRequest;
    }

    /** [ENCAPSULATION] Returns the canonical status label for storage/UI. */
    public String getStatus() {
        return status != null ? status.getLabel() : TicketStatus.PENDING.getLabel();
    }

    /** Returns the status as a type-safe {@link TicketStatus} enum. */
    public TicketStatus getStatusEnum() {
        return status;
    }

    public void setStatus(String status) {
        this.status = TicketStatus.fromStringOrDefault(status, this.status);
    }

    /** [POLYMORPHISM - Overloading] Sets the status directly from the enum. */
    public void setStatus(TicketStatus status) {
        if (status != null) {
            this.status = status;
        }
    }

    /** [INTERFACE] Implements Validatable.isValid. [INHERITANCE] Overrides AbstractEntity.isValid. */
    @Override
    public boolean isValid() {
        return !ticketId.isEmpty() && !userIdRequestor.isEmpty()
            && !typeOfRequest.isEmpty() && status != null;
    }
}

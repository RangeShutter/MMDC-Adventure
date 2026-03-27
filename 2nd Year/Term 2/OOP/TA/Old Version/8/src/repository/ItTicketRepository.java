package repository;

import model.ItTicket;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ItTicketRepository extends AbstractCsvListRepository<ItTicket> implements IItTicketRepository {
    private static final String FILE = "csv/ITtickets.csv";
    private static final String HEADER = "ticketId,userIdRequestor,typeOfRequest,status";

    public ItTicketRepository() {
        ensureFileWithHeader();
    }

    @Override
    protected String getFilePath() {
        return FILE;
    }

    @Override
    protected String getHeader() {
        return HEADER;
    }

    @Override
    protected ItTicket parseLine(String[] parts) {
        if (parts == null || parts.length < 4) return null;
        return new ItTicket(parts[0], parts[1], parts[2], parts[3]);
    }

    @Override
    protected String[] toCsvRow(ItTicket item) {
        return new String[]{
            item.getTicketId(),
            item.getUserIdRequestor(),
            item.getTypeOfRequest(),
            item.getStatus()
        };
    }

    @Override
    public List<ItTicket> load() {
        ensureFileWithHeader();
        return super.load();
    }

    @Override
    public void save(List<ItTicket> tickets) {
        ensureFileWithHeader();
        super.save(tickets);
    }

    private void ensureFileWithHeader() {
        try {
            Path path = Paths.get(FILE);
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            if (!Files.exists(path)) {
                Files.write(path, (HEADER + System.lineSeparator()).getBytes());
            }
        } catch (IOException e) {
            // ignore and allow callers to continue gracefully
        }
    }
}

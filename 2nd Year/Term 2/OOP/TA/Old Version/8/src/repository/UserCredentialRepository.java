package repository;

import model.UserCredential;

import java.util.List;

public class UserCredentialRepository extends AbstractCsvListRepository<UserCredential> implements IUserCredentialRepository {
    private static final String FILE = "csv/user_credentials.csv";
    private static final String HEADER = "userId,password,role,email";

    @Override
    protected String getFilePath() {
        return FILE;
    }

    @Override
    protected String getHeader() {
        return HEADER;
    }

    @Override
    protected UserCredential parseLine(String[] parts) {
        if (parts == null || parts.length < 4) return null;
        return new UserCredential(parts[0], parts[1], parts[2], parts[3]);
    }

    @Override
    protected String[] toCsvRow(UserCredential item) {
        return new String[]{
            item.getUserId(),
            item.getPassword(),
            item.getRole(),
            item.getEmail()
        };
    }

    @Override
    public List<UserCredential> load() {
        return super.load();
    }

    @Override
    public void save(List<UserCredential> credentials) {
        super.save(credentials);
    }
}

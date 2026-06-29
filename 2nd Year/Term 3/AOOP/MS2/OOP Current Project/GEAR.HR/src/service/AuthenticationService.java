package service;

import model.Employee;
import model.UserCredential;
import repository.IUserCredentialRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * [INTERFACE][POLYMORPHISM] Concrete authentication implementation used via IAuthenticationService.
 * [ABSTRACTION] Loads and validates credentials independent of UI.
 * Credentials are read through the injected {@link IUserCredentialRepository} (JDBC/MySQL).
 */
public class AuthenticationService implements IAuthenticationService {

    /** [POLYMORPHISM] Storage abstraction the service depends on. */
    private final IUserCredentialRepository credentialRepository;
    private final Map<String, String[]> userCredentials = new HashMap<>();

    /**
     * [ENCAPSULATION] Initializes credential cache on service creation.
     * [INTERFACE] Depends on the repository abstraction, not a concrete storage class.
     */
    public AuthenticationService(IUserCredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
        loadUserCredentials();
    }

    /**
     * Loads credentials from the repository into the in-memory cache.
     * [INTERFACE] Implements IAuthenticationService.loadUserCredentials.
     */
    @Override
    public void loadUserCredentials() {
        userCredentials.clear();
        if (credentialRepository == null) {
            return;
        }
        for (UserCredential cred : credentialRepository.load()) {
            if (cred != null && cred.getUserId() != null) {
                userCredentials.put(cred.getUserId().trim(),
                    new String[]{cred.getPassword(), cred.getRole(), cred.getEmail()});
            }
        }
    }

    /**
     * Validates credentials and returns an Employee (with employeeNumber set) if successful.
     */
    /** [OVERRIDING][ABSTRACTION] Implements interface authentication contract. */
    @Override
    public Employee authenticate(String userId, String password) {
        if (userId == null || password == null) return null;
        String key = userId.trim();
        String[] cred = userCredentials.get(key);
        if (cred == null || cred.length < 1) return null;
        if (!password.trim().equals(cred[0])) return null;
        // cred[] is { password, role, email }
        return new Employee(key, "", "", "", "", "", "", cred.length > 2 ? cred[2] : "", cred.length > 1 ? cred[1] : "", "regular", "", "");
    }

    /**
     * Returns [role, email] for the given userId, or ["", ""] if not found.
     */
    /** [OVERRIDING][ABSTRACTION] Returns immutable auth context instead of string arrays. */
    @Override
    public AuthContext getAuthContext(String userId) {
        if (userId == null) return new AuthContext("", "");
        String[] cred = userCredentials.get(userId.trim());
        if (cred == null || cred.length < 2) return new AuthContext("", "");
        // cred[] is { password, role, email }
        String role = cred.length > 1 ? cred[1] : "";
        String email = cred.length > 2 ? cred[2] : "";
        return new AuthContext(role, email);
    }

    /**
     * [ABSTRACTION] Backward-compatible accessor for legacy callers.
     * Prefer getAuthContext(userId) for new usage.
     */
    public String[] getRoleAndEmail(String userId) {
        AuthContext context = getAuthContext(userId);
        return new String[]{context.getRole(), context.getEmail()};
    }

    /** [OVERRIDING][ABSTRACTION] Implements existence check contract. */
    @Override
    public boolean hasUser(String userId) {
        return userId != null && userCredentials.containsKey(userId.trim());
    }
}

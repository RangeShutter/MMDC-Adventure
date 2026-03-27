package service;

import model.UserCredential;
import repository.IUserCredentialRepository;
import util.CredentialValidationUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class UserCredentialService implements IUserCredentialService {
    private final IUserCredentialRepository repository;
    private final AuthenticationService authenticationService;
    private final List<UserCredential> credentials = new ArrayList<>();

    public UserCredentialService(IUserCredentialRepository repository, AuthenticationService authenticationService) {
        this.repository = repository;
        this.authenticationService = authenticationService;
        reload();
    }

    @Override
    public void reload() {
        credentials.clear();
        credentials.addAll(repository.load());
    }

    @Override
    public List<UserCredential> getAllCredentials() {
        return new ArrayList<>(credentials);
    }

    @Override
    public Set<String> getDistinctRoles() {
        Set<String> roles = new LinkedHashSet<>();
        for (UserCredential credential : credentials) {
            if (credential != null && credential.getRole() != null && !credential.getRole().trim().isEmpty()) {
                roles.add(credential.getRole().trim());
            }
        }
        return roles;
    }

    @Override
    public UserCredential findByUserId(String userId) {
        if (userId == null) return null;
        String key = userId.trim();
        for (UserCredential credential : credentials) {
            if (credential != null && key.equals(credential.getUserId())) {
                return credential;
            }
        }
        return null;
    }

    @Override
    public String updateCredential(String userId, String password, String role, String email) {
        UserCredential existing = findByUserId(userId);
        if (existing == null) {
            return "User ID not found.";
        }

        String emailError = CredentialValidationUtil.validateEmail(email);
        if (emailError != null) return emailError;

        String roleError = CredentialValidationUtil.validateRole(role);
        if (roleError != null) return roleError;

        existing.setPassword(password);
        existing.setRole(role);
        existing.setEmail(email);

        String validationError = existing.getValidationError();
        if (validationError != null) return validationError;

        repository.save(credentials);
        if (authenticationService != null) {
            authenticationService.loadUserCredentials();
        }
        return null;
    }
}

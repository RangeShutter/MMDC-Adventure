package service;

import model.UserCredential;

import java.util.List;
import java.util.Set;

public interface IUserCredentialService {
    void reload();
    List<UserCredential> getAllCredentials();
    Set<String> getDistinctRoles();
    UserCredential findByUserId(String userId);
    String updateCredential(String userId, String password, String role, String email);
}

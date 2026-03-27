package repository;

import model.UserCredential;

import java.util.List;

public interface IUserCredentialRepository {
    List<UserCredential> load();
    void save(List<UserCredential> credentials);
}

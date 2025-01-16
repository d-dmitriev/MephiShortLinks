package home.work.links.repo;

import home.work.links.services.UserRepository;

import java.util.*;

public class UserRepositoryImpl implements UserRepository {

    private final List<UUID> storage = new ArrayList<>();

    public UUID add(UUID user) {
        storage.add(user);
        return user;
    }

    public void delete(UUID uuid) {
        storage.remove(uuid);
    }

    public List<UUID> findAll() {
        return storage;
    }
}

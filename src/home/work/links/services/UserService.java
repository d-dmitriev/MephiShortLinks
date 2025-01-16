package home.work.links.services;

import java.util.List;
import java.util.UUID;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID createUser() {
        return userRepository.add(UUID.randomUUID());
    }

    public List<UUID> listUsers() {
        return userRepository.findAll();
    }
}
package home.work.links.services;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    UUID add(UUID user);

    List<UUID> findAll();
}

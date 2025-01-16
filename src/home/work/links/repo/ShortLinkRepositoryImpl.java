package home.work.links.repo;

import home.work.links.data.ShortLink;
import home.work.links.services.ShortLinkRepository;

import java.util.*;

public class ShortLinkRepositoryImpl implements ShortLinkRepository {
    private final Map<String, ShortLink> storage = new HashMap<>();

    public void add(ShortLink link) {
        storage.put(link.getShortId(), link);
    }

    public ShortLink find(String linkShortId) {
        return storage.get(linkShortId);
    }

    public void delete(ShortLink link) {
        storage.remove(link.getShortId());
    }

    public List<ShortLink> findAllByUuid(UUID userUuid) {
        return storage.values().stream().filter(link -> link.getUserUuid().equals(userUuid)).toList();
    }
}

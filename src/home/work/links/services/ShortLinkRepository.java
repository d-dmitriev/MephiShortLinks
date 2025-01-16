package home.work.links.services;

import home.work.links.data.ShortLink;

import java.util.List;
import java.util.UUID;

public interface ShortLinkRepository {
    void add(ShortLink link);

    ShortLink find(String linkShortId);

    void delete(ShortLink link);

    List<ShortLink> findAllByUuid(UUID userUuid);
}

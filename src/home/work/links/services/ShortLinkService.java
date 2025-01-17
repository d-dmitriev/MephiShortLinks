package home.work.links.services;

import home.work.links.Config;
import home.work.links.data.ShortLink;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ShortLinkService {
    public static final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public static final String ERROR_EMPTY_URL = "URL не может быть пустым";
    public static final String ERROR_EMPTY_USER = "UUID пользователя не может быть null";

    private final ShortLinkRepository shortLinkRepository;

    public ShortLinkService(ShortLinkRepository shortLinkRepository) {
        this.shortLinkRepository = shortLinkRepository;
    }

    public String createShortLink(String originalUrl, UUID userUuid, int ttl, int limit) {

        if (originalUrl == null || originalUrl.isEmpty()) {
            throw new IllegalArgumentException(ERROR_EMPTY_URL);
        }

        if (userUuid == null) {
            throw new IllegalArgumentException(ERROR_EMPTY_USER);
        }

        String shortId = generateId();
        long expiryTime = System.currentTimeMillis() + ttl * Config.getDivider();

        shortLinkRepository.add(new ShortLink(shortId, originalUrl, System.currentTimeMillis(), expiryTime, limit, userUuid));
        return shortId;
    }

    public String getOriginalUrl(String shortId) {
        ShortLink link = shortLinkRepository.find(shortId);

        if (link == null) {
            throw new RuntimeException("Ссылка с идентификатором " + shortId + " не найдена.");
        }

        if (System.currentTimeMillis() > link.getExpiryTime()) {
            shortLinkRepository.delete(link);
            throw new RuntimeException("Срок действия ссылки с идентификатором " + shortId + " истёк.");
        }

        link.setCurrentCount(link.getCurrentCount() + 1);

        if (link.getCurrentCount() > link.getLimit()) {
            shortLinkRepository.delete(link);
            throw new RuntimeException("Ссылка с идентификатором " + shortId + " превысила лимит переходов.");
        }
        return link.getOriginalUrl();
    }

    public void deleteLink(String shortId, UUID userId) {
        ShortLink link = shortLinkRepository.find(shortId);
        if (link.getUserUuid().equals(userId)) {
            shortLinkRepository.delete(link);
        } else {
            throw new RuntimeException("Пользователь " + userId + " не является владельцем ссылки с идентификатором " + shortId + ".");
        }
    }

    public List<ShortLink> getUserLinks(UUID userUuid) {
        return shortLinkRepository.findAllByUuid(userUuid);
    }

    private String generateId() {
        StringBuilder shortId = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < Config.getLength(); i++) {
            shortId.append(chars.charAt(random.nextInt(chars.length())));
        }

        return shortId.toString();
    }
}
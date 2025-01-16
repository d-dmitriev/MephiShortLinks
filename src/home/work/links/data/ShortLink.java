package home.work.links.data;

import java.util.UUID;

public class ShortLink {
    private final String shortId;
    private final String originalUrl;
    private final long createdAt;
    private final long expiryTime;
    private final int limit;
    private int currentCount;
    private final UUID userUuid;

    public ShortLink(String shortId,
                     String originalUrl,
                     long createdAt,
                     long expiryTime,
                     int limit,
                     UUID userUuid) {
        this.shortId = shortId;
        this.originalUrl = originalUrl;
        this.createdAt = createdAt;
        this.expiryTime = expiryTime;
        this.limit = limit;
        this.userUuid = userUuid;
    }

    public String getShortId() {
        return shortId;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public int getLimit() {
        return limit;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    @Override
    public String toString() {
        return "ShortLink{" +
                "shortId='" + shortId + '\'' +
                ", originalUrl='" + originalUrl + '\'' +
                ", createdAt=" + createdAt +
                ", expiryTime=" + expiryTime +
                ", limit=" + limit +
                ", currentCount=" + currentCount +
                ", userUuid=" + userUuid +
                '}';
    }
}

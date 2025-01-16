package home.work.links;

import java.io.IOException;
import java.util.Properties;

public class Config {
    public static final String PROP_TTL = "ttl";
    public static final String PROP_LIMIT = "limit";
    public static final String PROP_DIVIDER = "divider";
    public static final String PROP_LENGTH = "length";

    public static final String ERROR_FILE = "Не удалось загрузить конфигурационный файл: ";
    public static final String APPLICATION_PROPERTIES = "application.properties";

    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(Config.class.getClassLoader().getResourceAsStream(APPLICATION_PROPERTIES));
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException(ERROR_FILE + e.getMessage(), e);
        }
    }

    public static int getTtl() {
        return Integer.parseInt(properties.getProperty(PROP_TTL, "10"));
    }

    public static int getLimit() {
        return Integer.parseInt(properties.getProperty(PROP_LIMIT, "10"));
    }

    public static long getDivider() {
        return Long.parseLong(properties.getProperty(PROP_DIVIDER, "60000"));
    }

    public static int getLength() {
        return Integer.parseInt(properties.getProperty(PROP_LENGTH, "6"));
    }
}


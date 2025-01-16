package home.work;

import home.work.links.*;
import home.work.links.repo.ShortLinkRepositoryImpl;
import home.work.links.repo.UserRepositoryImpl;
import home.work.links.services.ShortLinkService;
import home.work.links.services.UserService;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static final String USAGE = """
            Добро пожаловать в сервис сокращения ссылок!
            Доступные команды:
            - shorten <url> [time] [limit]: Создание короткой ссылки
            - list: Просмотр всех ссылок пользователя
            - open <shortId>: Переход по ссылке
            - whoami: Вывести текущего пользователя
            - login <uuid>: Установить текущего пользователя
            - logout: Сбросить текущего пользователя
            - users: Список пользователей
            - exit: Завершение работы приложения""";
    public static final String UNAUTHORIZED = "Необходимо авторизоваться для выполнения команды.";
    public static final String EXIT = "Завершение работы программы. До свидания!";
    public static final String OPEN_ERROR = "Введите идентификатор короткой ссылки.";
    public static final String UNKNOWN = "Неизвестная команда. Введите 'help' для списка доступных команд.";
    public static final String SHORTEN_ERROR = "Введите URL.";
    public static final String CURRENT_USER = "Текущий пользователь: ";
    public static final String SHORTEN_MESSAGE = """
            Текущий пользователь: %s
            Создана ссылка: %s, время жизни: %s минут(а), Лимит переходов: %s
            """;
    public static final String LINK_MESSAGE = "- %s -> %s (лимит: %d, переходов: %d, истекает через: %d минут)%n";
    public static final String MESSAGE = "Сообщение: ";
    public static final String ERROR_OPEN_LINK = "Ошибка при попытке открыть ссылку: ";
    public static final String ERROR_DESKTOP_NOT_SUPPORTED = "Операция Desktop не поддерживается на данной системе.";

    private static final ShortLinkService ShortLinkService = new ShortLinkService(new ShortLinkRepositoryImpl());
    private static final UserService UserService = new UserService(new UserRepositoryImpl());

    private static UUID currentUser;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(USAGE);

        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine();

            if (command.equals("exit")) {
                System.out.println(EXIT);
                break;
            }
            handleCommand(command);
        }
        scanner.close();
    }

    private static void handleCommand(String command) {
        String[] parts = command.split(" ");
        String action = parts[0];

        try {
            switch (action) {
                case "shorten":
                    shortenUrl(parts);
                    break;
                case "list":
                    checkLogin();
                    ShortLinkService.getUserLinks(currentUser).forEach(link -> System.out.printf(LINK_MESSAGE,
                            link.getShortId(), link.getOriginalUrl(), link.getLimit(), link.getCurrentCount(),
                            (link.getExpiryTime() - System.currentTimeMillis()) / Config.getDivider()));
                    break;
                case "open":
                    if (parts.length < 2) {
                        throw new IllegalArgumentException(OPEN_ERROR);
                    }
                    openInBrowser(ShortLinkService.getOriginalUrl(parts[1]));
                    break;
                case "whoami":
                    checkLogin();
                    System.out.println(CURRENT_USER + currentUser);
                    break;
                case "login":
                    currentUser = UUID.fromString(parts[1]);
                    break;
                case "logout":
                    currentUser = null;
                    break;
                case "users":
                    UserService.listUsers().forEach(System.out::println);
                    break;
                default:
                    System.out.println(UNKNOWN);
            }
        } catch (Exception e) {
            System.out.println(MESSAGE + e.getMessage());
        }
    }

    private static void checkLogin() {
        if (currentUser == null) {
            throw new IllegalStateException(UNAUTHORIZED);
        }
    }

    private static void shortenUrl(String[] parts) {
        if (parts.length < 2) {
            throw new IllegalArgumentException(SHORTEN_ERROR);
        }

        if (currentUser == null) currentUser = UserService.createUser();

        int userTtl = Integer.MAX_VALUE;
        int userLimit = 0;

        if (parts.length > 2) {
            userTtl = Integer.parseInt(parts[2]);
        }

        if (parts.length > 3) {
            userLimit = Integer.parseInt(parts[3]);
        }

        userTtl = Math.min(Config.getTtl(), userTtl);
        userLimit = Math.max(Config.getLimit(), userLimit);

        String shortId = ShortLinkService.createShortLink(parts[1], currentUser, userTtl, userLimit);
        System.out.printf(SHORTEN_MESSAGE, currentUser, shortId, userTtl, userLimit);
    }

    private static void openInBrowser(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(ERROR_OPEN_LINK + e.getMessage());
            }
        } else {
            throw new RuntimeException(ERROR_DESKTOP_NOT_SUPPORTED);
        }
    }
}

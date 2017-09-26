package ku.cs.duckdealer.warehouse_manager.controllers;

import java.util.HashMap;

public class AuthenticationService {
    private static String currentUsername = null;

    public static boolean LOGGED_IN_AS_STOCK = "Stock".equals(currentUsername);
    public static boolean LOGGED_IN_AS_OWNER = "Owner".equals(currentUsername);
    public static boolean NOT_LOGGED_IN = currentUsername == null;

    private static HashMap<String, String> users = new HashMap<String, String>();

    static {
        users.put("Owner", "1234");
        users.put("Stock", "12345");
    }

    public static boolean login(String username, String password) {
        if (users.containsKey(username))
            if (users.get(username).equals(password)) {
                setCurrentUsername(username);
                return true;
            }
        return false;
    }

    public static void logout() {
        setCurrentUsername(null);
    }

    private static void setCurrentUsername(String username) {
        currentUsername = username;
        LOGGED_IN_AS_STOCK = "Stock".equals(currentUsername);
        LOGGED_IN_AS_OWNER = "Owner".equals(currentUsername);
        NOT_LOGGED_IN = currentUsername == null;
    }

}

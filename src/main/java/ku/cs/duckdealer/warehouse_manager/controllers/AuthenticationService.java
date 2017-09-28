package ku.cs.duckdealer.warehouse_manager.controllers;

import java.util.HashMap;

public class AuthenticationService {
    private String currentUsername;

    public static boolean LOGGED_IN_AS_STOCK = false;
    public static  boolean LOGGED_IN_AS_OWNER = false;
    public static  boolean NOT_LOGGED_IN = true;

    private HashMap<String, String> users;

    public AuthenticationService() {
        users = new HashMap<String, String>();
        users.put("Owner", "1234");
        users.put("Stock", "12345");
    }

    public boolean login(String username, String password) {
        if (users.containsKey(username))
            if (users.get(username).equals(password)) {
                setCurrentUsername(username);
                return true;
            }
        return false;
    }

    public void logout() {
        setCurrentUsername(null);
    }

    private void setCurrentUsername(String username) {
        currentUsername = username;
        LOGGED_IN_AS_STOCK = "Stock".equals(currentUsername);
        LOGGED_IN_AS_OWNER = "Owner".equals(currentUsername);
        NOT_LOGGED_IN = currentUsername == null;
    }

}

package model;

import java.util.ArrayList;

public class User {
    private static ArrayList<User> users;

    static {
        users = new ArrayList<>();
    }

    private final String username;
    private String password;
    private int maxPoint;
    private final ArrayList<Map> selectedMaps;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.maxPoint  = 0;
        selectedMaps = new ArrayList<>();
        users.add(this);
    }

    public static void setUsers(ArrayList<User> users) {
        User.users = users;
    }

    public static User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Map> getSelectedMaps() {
        return selectedMaps;
    }

    public String getUsername() {
        return username;
    }

    public void addMap(Map map) {
        selectedMaps.add(map);
    }

    public void setMaxPoint(int maxPoint) {
        this.maxPoint = maxPoint;
    }

    public int getMaxPoint() {
        return maxPoint;
    }
}

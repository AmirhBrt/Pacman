package control;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.com.google.gson.reflect.TypeToken;
import model.Map;
import model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SaverAndLoader {

    public static void saveAllUsers() throws IOException {
        for (User user : User.getUsers()){
            for (Map map : user.getSelectedMaps()){
                map.setMap(null);
            }
        }
        YaGson yaGson = new YaGson();
        Files.deleteIfExists(Paths.get("src/main/resources/data/users.json"));
        FileWriter fileWriter = new FileWriter("src/main/resources/data/users.json" , false);
        fileWriter.write(yaGson.toJson(User.getUsers()));
        fileWriter.close();
    }

    public static ArrayList<User> loadAllUsers() {
        String users = null;
        try {
            users = new String(Files.readAllBytes(Paths.get("src/main/resources/data/users.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<User> allUsers;
        YaGson yaGson = new YaGson();
        allUsers = yaGson.fromJson(users , new TypeToken<List<User>>(){}.getType());
        return allUsers;
    }
}

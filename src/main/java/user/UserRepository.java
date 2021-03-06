package user;

import parser.JsonParser;
import java.io.*;
import java.util.Scanner;


public class UserRepository {
    private final String pathDB;
    private final String extension;

    public UserRepository(String pathDB, String extension) {
        this.pathDB = pathDB;
        this.extension = extension;
    }

    public User getUserById(String id) throws IOException {
        File file = getOrCreateIfNone(id);
        if (file.length() == 0)
            return null;
        String json = readJson(file);

        return JsonParser.fromJsonToUser(json);
    }

    public static String readJson(File file) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        StringBuilder json = new StringBuilder();
        while(sc.hasNext())
            json.append(sc.next());

        return json.toString();
    }

    public void saveUser(String id, User user) throws IOException {
        delFile(id);
        File file = getOrCreateIfNone(id);
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(JsonParser.fromUserToJson(user));
        }
    }

    public void dellUser(String id) {
        delFile(id);
    }

    private File getOrCreateIfNone(String id) throws IOException {
        File file = new File(pathDB + id + extension);
        if (!file.exists())
            file.createNewFile();

        return file;
    }

    private void delFile(String id){
        File file = new File(pathDB + id + extension);
        if (file.exists())
            file.delete();
    }
}

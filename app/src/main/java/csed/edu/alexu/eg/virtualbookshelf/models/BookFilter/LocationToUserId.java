package csed.edu.alexu.eg.virtualbookshelf.models.BookFilter;

import java.util.HashMap;

public class LocationToUserId {

    private static final HashMap<String, String> locationToUserId = new HashMap<>();

    // Todo update the user id to a real one
    static {
        locationToUserId.put("alf", "115795862021150203743");
        locationToUserId.put("bibalex", "116150945014472813554");
    }

    private LocationToUserId() {}

    public static String getUserId(String location) {
        return locationToUserId.get(location);
    }

}

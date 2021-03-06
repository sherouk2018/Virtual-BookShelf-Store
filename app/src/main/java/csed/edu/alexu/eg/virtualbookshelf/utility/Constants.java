package csed.edu.alexu.eg.virtualbookshelf.utility;

public final class Constants {
    public static final long MAX_RESULTS = 10L;
    // Filter
    public static String[] search_fields = { "Title", "Category", "Location"};

    public static final String FAVOURITE = "0";
    public static final String WISH_LIST = "3";
    public static final String READ = "2";
    public static final String NO_SHELF = "-1";

    public static final int NO_FILTER = -1;
    public static final int TITLE = 0;
    public static final int SUBJECT = 1;
    public static final int LOCATION = 2;

    public static final String KEYAPI = "AIzaSyCik4DYGQfsRcBp1a-cB6seTwuA6cqxCKY";

    private Constants(){
        throw new AssertionError();
    }
}

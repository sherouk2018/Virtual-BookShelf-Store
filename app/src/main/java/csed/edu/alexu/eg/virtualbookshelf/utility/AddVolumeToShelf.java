package csed.edu.alexu.eg.virtualbookshelf.utility;

import android.util.Log;

import com.google.api.services.books.Books;

import com.google.api.services.books.model.Volumes;

import java.util.HashMap;

import csed.edu.alexu.eg.virtualbookshelf.models.UserFunctionality.EditShelf;

public class AddVolumeToShelf extends UserUtils {
    private final String TAG = AddVolumeToShelf.class.getSimpleName();
    private HashMap<String, String> shelfID;
    public AddVolumeToShelf(Books books) {
        super(books);
                /*
    BookShelf ShelfID: 1 Name: Purchased
    BookShelf ShelfID: 5 Name: Reviewed
    BookShelf ShelfID: 6 Name: Recently viewed
    BookShelf ShelfID: 9 Name: Browsing history
    BookShelf ShelfID: 0 Name: Favorites
    BookShelf ShelfID: 3 Name: Reading now
    BookShelf ShelfID: 2 Name: read
    */
        shelfID = new HashMap<>();
        shelfID.put("Favorites", "0");
        shelfID.put("WishList", "3");
        shelfID.put("Read", "2");
    }

    @Override
    // params[0]: ShelfID
    // params[1]: volumeID
    public Volumes doFunctionality(String... params) {
        Log.d(TAG, "Begin AddVolume async task");
        EditShelf shelf = new EditShelf();
        shelf.AddVolumeToShelf(shelfID.get(params[0]), params[1], books);
        Log.d(TAG, "End AddVolume async task");
        return null;
    }
}

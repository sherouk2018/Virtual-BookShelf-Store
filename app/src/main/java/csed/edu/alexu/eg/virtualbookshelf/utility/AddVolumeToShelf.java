package csed.edu.alexu.eg.virtualbookshelf.utility;

import android.util.Log;

import com.google.api.services.books.Books;

import com.google.api.services.books.model.Volumes;
import csed.edu.alexu.eg.virtualbookshelf.models.UserFunctionality.EditShelf;

public class AddVolumeToShelf extends UserUtils {
    private final String TAG = AddVolumeToShelf.class.getSimpleName();

    public AddVolumeToShelf(Books books) {
        super(books);
    }

    @Override
    // params[0]: ShelfID
    // params[1]: volumeID
    public Volumes doFunctionality(String... params) {
        Log.d(TAG, "Begin AddVolume async task");
        EditShelf shelf = new EditShelf();
        shelf.AddVolumeToShelf(params[0], params[1], books);
        Log.d(TAG, "End AddVolume async task");
        return null;
    }
}

package csed.edu.alexu.eg.virtualbookshelf.utility;

import android.util.Log;

import com.google.api.services.books.Books;
import csed.edu.alexu.eg.virtualbookshelf.models.UserFunctionality.EditShelf;
import com.google.api.services.books.model.Volumes;

public class RemoveVolumeFromShelf extends UserUtils {
    private final String TAG = RemoveVolumeFromShelf.class.getSimpleName();

    public RemoveVolumeFromShelf(Books books) {
        super(books);
    }

    @Override
    public Volumes doFunctionality(String... params) {
        if ((params.length < 1 || params.length >= 3)) throw new RuntimeException("Invalid parameters");
        Log.d(TAG, "Begin removing volume from shelf with shelf id: " + params[0] );
        //params[0]: ShelfID, params[1]: volumeID
        EditShelf shelf = new EditShelf();
        if(params.length == 1) shelf.clearShelf(params[0], books);
        else shelf.deleteFromShelf(params[0], params[1], books);
        Log.d(TAG, "Finish removing volume from shelf");
        return null;
    }
}

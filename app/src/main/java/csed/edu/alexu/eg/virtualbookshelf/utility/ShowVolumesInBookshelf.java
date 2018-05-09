package csed.edu.alexu.eg.virtualbookshelf.utility;

import android.util.Log;
import com.google.api.services.books.Books;
import com.google.api.services.books.model.Volume;
import java.io.IOException;
import csed.edu.alexu.eg.virtualbookshelf.models.UserFunctionality.EditShelf;
import com.google.api.services.books.model.Volumes;
//import com.google.api.services.books.Books.Mylibrary.Bookshelves.Volumes;

public class ShowVolumesInBookshelf extends UserUtils {
    private final String TAG = AddVolumeToShelf.class.getSimpleName();
    public ShowVolumesInBookshelf(Books books) {super(books);}

    @Override
    public Volumes doFunctionality(String... params) {
        //params[0] = shelfID
        Log.d(TAG, "Begin showVolume async task");
        EditShelf shelf = new EditShelf();
        Books.Mylibrary.Bookshelves.Volumes
                volumes = shelf.ShowVolumesInBookshelf(books, params[0]);
        Log.d("Soso ", "invistigate volumes ");
        try {
            return volumes.list(params[0]).execute();
        } catch (IOException e) {
            throw new RuntimeException("No books available for that shelf");
        }
    }
}

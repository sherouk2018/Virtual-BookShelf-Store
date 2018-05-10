package csed.edu.alexu.eg.virtualbookshelf.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.model.Bookshelf;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import csed.edu.alexu.eg.virtualbookshelf.R;
import csed.edu.alexu.eg.virtualbookshelf.utility.BookListAdapter;
import csed.edu.alexu.eg.virtualbookshelf.utility.Constants;
import csed.edu.alexu.eg.virtualbookshelf.utility.EditFactory;
import csed.edu.alexu.eg.virtualbookshelf.utility.FilterData;
import csed.edu.alexu.eg.virtualbookshelf.utility.UserUtils;

public class BookActivity extends AppCompatActivity {
    private static final String SEPARATOR = "\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        Log.d(this.getClass().getName(), "open individual book number ");
        String volumeId = (String) this.getIntent().getExtras().get("book_id");
        new BookActivity.LoadSingleBookTask().execute(volumeId);
    }


    /**
     *
     */
    private class LoadSingleBookTask extends AsyncTask<String, Void, Volume> {

        @Override
        protected Volume doInBackground(String... volumeId) {
            Volume volume = null;
            try {
                // TODO : REMOVE THIS
                // get book volume
                Books.Volumes.Get listBooksInst = EditFactory.getInstance().getBooks().volumes().get(volumeId[0]);
                volume = listBooksInst.execute();


            } catch (IOException e) {
                e.printStackTrace();
            }
            return volume;
        }

        @Override
        protected void onPostExecute(final Volume volume) {

            Button submitRateBtn = findViewById(R.id.submit_rate_btn);
            Button addToFavBtn = findViewById(R.id.add_to_fav_btn);
            Button addToWishlistBtn = findViewById(R.id.add_to_wishlist_btn);
            Button addToReadBtn = findViewById(R.id.add_to_read_btn);

            super.onPostExecute(volume);
            if (volume != null) {
                // ui items
                ImageView bookImage = findViewById(R.id.ind_book_image);
                TextView bookTitleTxt = findViewById(R.id.ind_book_title);
                TextView authorsTxt = findViewById(R.id.ind_book_authors);
                TextView pageCountTxt = findViewById(R.id.ind_book_page_count);
                TextView descriptionTxt = findViewById(R.id.ind_book_description);

                // image
                Volume.VolumeInfo.ImageLinks imageLinks = volume.getVolumeInfo().getImageLinks();
                if (imageLinks != null) {

                    Log.d("VIEW-BOOK"," Book image link : " + imageLinks.getThumbnail());
                    //Picasso.with(BookActivity.this).load(imageLinks.getMedium()).resize(500,500).into(bookImage);
                    Picasso.with(BookActivity.this).load(imageLinks.getThumbnail()).resize(500,500).into(bookImage);
                }

                // title
                bookTitleTxt.setText(volume.getVolumeInfo().getTitle());
                Log.d("VIEW-BOOK"," Book title : " + volume.getVolumeInfo().getTitle() );
                List<String> authors = volume.getVolumeInfo().getAuthors();

                // authors
                if (authors != null && !authors.isEmpty()) {
                    Log.d("VIEW-BOOK",authors.size()+ ": " );
                    StringBuilder authorsJoined = new StringBuilder();
                    for (String author : authors) {
                        Log.d("VIEW-BOOK", " author : "+author );
                        authorsJoined.append(author);
                        authorsJoined.append(SEPARATOR);
                    }
                    String authorsStr = authorsJoined.toString();
                    //Remove last comma
                    authorsStr = authorsStr.substring(0, authorsStr.length() - SEPARATOR.length());
                    Log.d("VIEW-BOOK", " ALL authors : "+ authorsStr );
                    authorsTxt.setText(authorsStr);
                }
                StringBuilder pageCountStr = new StringBuilder();
                pageCountStr.append(volume.getVolumeInfo().getPageCount());
                pageCountStr.append(" pages");
                pageCountTxt.setText(pageCountStr);
                descriptionTxt.setText(volume.getVolumeInfo().getDescription());
                submitRateBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        RatingBar ratingBar = findViewById(R.id.rating_bar);
                        Toast.makeText(getApplicationContext(),
                                String.valueOf(ratingBar.getRating()),
                                Toast.LENGTH_SHORT).show();

                    }

                });
                addToFavBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addToShelf(volume.getId() , "Favorites");
                        Toast.makeText(BookActivity.this, "Added to Favourite List", Toast.LENGTH_SHORT).show();
                    }
                });
                addToWishlistBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addToShelf(volume.getId(), "WishList");
                        Toast.makeText(BookActivity.this, "Added to Wish List", Toast.LENGTH_SHORT).show();
                    }
                }

                );
                addToReadBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addToShelf(volume.getId(), "Read");
                        Toast.makeText(BookActivity.this, "Added to Read List", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }


        public void addToShelf(String id, String shelfName){
            new UserUtilsAsyncTask(null, null,
                    BookActivity.this,
                    "AddVolumeToShelf", Constants.NO_SHELF).execute(shelfName, id);

        }
    }

}



package csed.edu.alexu.eg.virtualbookshelf.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import csed.edu.alexu.eg.virtualbookshelf.R;
import csed.edu.alexu.eg.virtualbookshelf.ui.MainActivity;
import csed.edu.alexu.eg.virtualbookshelf.ui.UserUtilsAsyncTask;

public class BookListAdapter extends ArrayAdapter<Volume> {
    private static final String SEPARATOR = " , ";

    private List<Volume> volumes;
    private Context context;
    private String shelfId;
    private ListView booksListView;

    public BookListAdapter(Context context, int resource, Volumes volumes, String shelfId, ListView bookListView) {
        super(context, resource);
        this.context = context;
        this.volumes = volumes.getItems();
        this.shelfId = shelfId;
        this.booksListView = bookListView;
    }


    public void setBooksListView(ListView booksListView) {
        this.booksListView = booksListView;
    }

    public void setShelfId(String shelfId) {
        this.shelfId = shelfId;
    }

    public void setVolumes(Volumes volumes) {
        this.volumes = volumes.getItems();
    }

    @Override
    public int getCount() {
        if (volumes == null) return 0;
        return volumes.size();
    }

    @Override
    public void clear() {
        volumes.clear();
    }

    @Nullable
    @Override
    public Volume getItem(int position) {
        return volumes.get(position);
    }
//
//    public List<Volume> getVolumes() {
//        return volumes;
//    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View listView;

        if (convertView == null) {
            convertView =  LayoutInflater.from(getContext()).inflate(R.layout.books_list_item, parent, false);
        }
            final Volume volume = getItem(position);
//            listView = inflater.inflate(R.layout.books_list_item, null);

            // image
            ImageView imageView =  convertView.findViewById(R.id.book_image);
            Volume.VolumeInfo.ImageLinks imageLinks = volume.getVolumeInfo().getImageLinks();
            if (imageLinks != null) {
                Picasso.with(this.context).load(imageLinks.getSmallThumbnail()).into(imageView);
            }
            // title
            TextView title_tv = convertView.findViewById(R.id.book_title);
            title_tv.setText(volume.getVolumeInfo().getTitle());

            // authors
            TextView authorsTxt = convertView.findViewById(R.id.authors);
            List<String> authors = volume.getVolumeInfo().getAuthors();

            if (authors != null && !authors.isEmpty()) {
                StringBuilder authorsJoined = new StringBuilder();
                for (String author : authors) {
                    authorsJoined.append(author);
                    authorsJoined.append(SEPARATOR);
                }
                String authorsStr = authorsJoined.toString();
                //Remove last comma
                authorsStr = authorsStr.substring(0, authorsStr.length() - SEPARATOR.length());
                authorsTxt.setText(authorsStr);
            }

            ImageButton removeBtn = convertView.findViewById(R.id.remove_from_list);
            if(shelfId.equals(Constants.NO_SHELF))
                removeBtn.setVisibility(View.INVISIBLE);
            else {
                removeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("BookListAdapterBefore: ", volume.getVolumeInfo().getTitle());

//                        BookListAdapter.this.remove(volume);
                        volumes.remove(volume);
                        new UserUtilsAsyncTask(null, null,
                                context,
                                "RemoveVolumeFromShelf", Constants.NO_SHELF)
                                .execute(shelfId, volume.getId());
                        new UserUtilsAsyncTask(BookListAdapter.this, booksListView,
                                context,
                                "ShowVolumesInBookshelf", shelfId).execute(shelfId);

                        Log.d("BookListAdapterAfter: ", volume.getVolumeInfo().getTitle());
                    }
                });

        }
        return convertView;
    }
}


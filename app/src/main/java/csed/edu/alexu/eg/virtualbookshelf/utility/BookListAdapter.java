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

public class BookListAdapter extends ArrayAdapter {
    private static final String SEPARATOR = " , ";

    private List<Volume> volumes;
    private Context context;
    private String shelfId;

    public BookListAdapter(Context context, int resource, Volumes volumes, String shelfId) {
        super(context, resource);
        this.context = context;
        this.volumes = volumes.getItems();
        this.shelfId = shelfId;
    }

    @Override
    public int getCount() {
        return volumes.size();
    }

    @Nullable
    @Override
    public Volume getItem(int position) {
        return volumes.get(position);
    }

    public List<Volume> getVolumes() {
        return volumes;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView;

        if (convertView == null) {
            final Volume volume = volumes.get(position);
            listView = inflater.inflate(R.layout.books_list_item, null);

            // image
            ImageView imageView = (ImageView) listView.findViewById(R.id.book_image);
            Volume.VolumeInfo.ImageLinks imageLinks = volume.getVolumeInfo().getImageLinks();
            if (imageLinks != null) {
                Picasso.with(this.context).load(imageLinks.getSmallThumbnail()).into(imageView);
            }
            // title
            TextView title_tv = (TextView) listView.findViewById(R.id.book_title);
            title_tv.setText(volume.getVolumeInfo().getTitle());

            // authors
            TextView authorsTxt = listView.findViewById(R.id.authors);
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

            ImageButton removeBtn = listView.findViewById(R.id.remove_from_list);
            if(shelfId == Constants.NO_SHELF)
                removeBtn.setVisibility(View.INVISIBLE);
            else {
                removeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("BookListAdapter: ", volume.getId());
                        new UserUtilsAsyncTask(null, null,
                                context,
                                "RemoveVolumeFromShelf", Constants.NO_SHELF)
                                .execute(shelfId, volume.getId());
                        volumes.remove(volume);
                        BookListAdapter.this.remove(position);
                        BookListAdapter.this.notifyDataSetChanged();

                    }
                });
            }
        } else {
            listView = convertView;
        }
        return listView;
    }
}


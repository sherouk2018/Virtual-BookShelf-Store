package csed.edu.alexu.eg.virtualbookshelf.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;

import csed.edu.alexu.eg.virtualbookshelf.R;
import csed.edu.alexu.eg.virtualbookshelf.utility.BookListAdapter;
import csed.edu.alexu.eg.virtualbookshelf.utility.Constants;
import csed.edu.alexu.eg.virtualbookshelf.utility.EditFactory;
import csed.edu.alexu.eg.virtualbookshelf.utility.UserUtils;

public class UserUtilsAsyncTask extends AsyncTask<String , Void, Volumes>  {
    private ListView booksListView;
    private BookListAdapter adapter;
    private Context mainActivity;
    private String className;

    protected UserUtilsAsyncTask(BookListAdapter adapter, ListView booksListView, Context mainActivity, String className){
        this.adapter = adapter;
        this.booksListView = booksListView;
        this.mainActivity = mainActivity;
        this.className = className;
    }


    @Override
    protected Volumes doInBackground(String ... params) {

        EditFactory factory = EditFactory.getInstance();
        UserUtils utils = factory.getEditFun(className);
        Log.d("MainActivity" , "filtering "+ params[0]);
        Log.d("MainActivity" , "filtering with subject in home activity");
        Volumes volumes = utils.doFunctionality(params);

        if (volumes == null || volumes.isEmpty()) {
            //TODO: Print Toast instead.
            Log.d("MainActivity", "volumes is empty");
            return  new Volumes();
        }
        return volumes;
    }

    @Override
    protected void onPostExecute(Volumes volumes) {
        super.onPostExecute(volumes);
        Log.d("MainActivity in post ", volumes.size()+"");
        if (volumes == null || volumes.isEmpty()) {
            //TODO: Print Toast instead.
            Log.d("MainActivity", "volumes is empty");
            return;
        }
        adapter = new BookListAdapter(mainActivity, R.layout.books_list_item, volumes);
        booksListView.setAdapter(adapter);

        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(this.getClass().getName(), "open indivual book number " + i + " in list");
                Intent intent = new Intent(mainActivity, BookActivity.class);
                Volume volume = (Volume) booksListView.getAdapter().getItem(i);
                intent.putExtra("book_id", volume.getId());
                mainActivity.startActivity(intent);
            }
        });
    }

}

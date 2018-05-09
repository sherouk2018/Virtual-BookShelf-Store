package csed.edu.alexu.eg.virtualbookshelf.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.api.services.books.Books;
import com.google.api.services.books.model.Volumes;

import csed.edu.alexu.eg.virtualbookshelf.R;
import csed.edu.alexu.eg.virtualbookshelf.utility.BookListAdapter;
import csed.edu.alexu.eg.virtualbookshelf.utility.Constants;
import csed.edu.alexu.eg.virtualbookshelf.utility.EditFactory;
import csed.edu.alexu.eg.virtualbookshelf.utility.UserUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static int filterOption = 0;
    private ListView booksListView;
    private BookListAdapter adapter;
    private Button filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //---------------------
        // books
        booksListView = findViewById(R.id.books_list);

        // filter text field
        final EditText filter_String = findViewById(R.id.search_text);
        // filter fields menu
        final Spinner spinner = findViewById(R.id.search_choices);
        ArrayAdapter spinner_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Constants.search_fields);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                          @Override
                          public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                              filterOption = i;
                          }

                          @Override
                          public void onNothingSelected(AdapterView<?> adapterView) {
                              filterOption = 0;
                          }
                      }
        );

        // books filtering button
        filter = findViewById(R.id.do_filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Mirnaaaa", "Filtering" + filterOption + "  " + filter_String.getText().toString());
                if (adapter != null) {
                    Log.d("Mirnaaa 222", "Filtering" + filterOption + "  " + filter_String.getText().toString());
                    // clear data
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                    // according to filter
                }

                new UserUtilsAsyncTask(adapter, booksListView,
                        MainActivity.this,
                        "FilterData").execute(getFilter(String.valueOf(filterOption),
                        filter_String.getText().toString()));

            }

            private String [] getFilter(String ... params) {
                //params[0] = filterType
                //params[1] = query

                Volumes volumes = null;
                String query;
                String [] array = new String[2];
                switch (Integer.parseInt(params[0])) {

                    case Constants.TITLE:
                        array[0] = "FilterDataByAttribute";
                        array[1] = "intitle:" + params[1];
                        break;
                    case Constants.SUBJECT:
                        array[0] = "FilterDataByAttribute";
                        array[1] = "subject:" + params[1];
                        break;
                    case Constants.LOCATION:
                        array[0] = "FilterDataByAttribute";
                        array[1] = params[1];
                        break;
                    case 0:
                        //TODO: Print Toast instead.
                        query = "intitle:harry";
                        break;
                }
                return array;
            }
        });
        //------------------
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        final Context context = this;

        // Get the layout inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int id = item.getItemId();

        if (id == R.id.profile) {

        /*}else if (id == R.id.edit_shelf) {
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.edit_shelf)
                        .setItems(R.array.edit_shelf_array, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // Get the layout inflater
                                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                switch(which){
                                    case 0:
                                        builder.setTitle("Add a volume to a shelf");

                                        // Inflate and set the layout for the dialog
                                        // Pass null as the parent view because its going in the dialog layout
                                        builder.setView(inflater.inflate(R.layout.add_or_delete_book_layout, null))
                                                // Add action buttons
                                                .setPositiveButton(R.string.add_book, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // call add volume to shelf

                                                    }
                                                })
                                                .setNegativeButton(R.string.cancel_book, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        builder.create();
                                        builder.show();
                                        break;

                                    case 1:
                                        builder.setTitle("Delete a volume from a shelf");
                                        // Inflate and set the layout for the dialog
                                        // Pass null as the parent view because its going in the dialog layout
                                        builder.setView(inflater.inflate(R.layout.add_or_delete_book_layout, null))
                                                // Add action buttons
                                                .setPositiveButton(R.string.clear_Shelf, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        UserUtils user = EditFactory.getInstance().getEditFun("RemoveVolumeFromShelf");
                                                        String shelf_id = getResources().getString(R.string.shelf_ID);
                                                        String vol_id = getResources().getString(R.string.vol_ID);
                                                        Log.d("Soso", "shelfId: " + shelf_id + " " + vol_id);
                                                        user.execute(new String[]{shelf_id, vol_id});
                                                    }
                                                })
                                                .setNegativeButton(R.string.cancel_book, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        builder.create();
                                        builder.show();
                                        break;

                                    case 2:
                                        builder.setTitle("Clear a shelf");
                                        // Inflate and set the layout for the dialog
                                        // Pass null as the parent view because its going in the dialog layout
                                        builder.setView(inflater.inflate(R.layout.clear_shelf_layout, null))
                                                // Add action buttons
                                                .setPositiveButton(R.string.delete_book, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // call clear shelf
                                                    }
                                                })
                                                .setNegativeButton(R.string.cancel_book, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        builder.create();
                                        builder.show();
                                        break;
                                }

                            }
                        });
                builder.create();
                builder.show();
    } */
    }else if (id == R.id.favourite_list) {
            new UserUtilsAsyncTask(adapter, booksListView,
                    MainActivity.this,
                    "ShowVolumesInBookshelf").execute(Constants.FAVOURITE);

    } else if (id == R.id.to_be_read_list) {
            new UserUtilsAsyncTask(adapter, booksListView,
                    MainActivity.this,
                    "ShowVolumesInBookshelf").execute(Constants.WISH_LIST);

    } else if (id == R.id.read_list) {
            new UserUtilsAsyncTask(adapter, booksListView,
                    MainActivity.this,
                    "ShowVolumesInBookshelf").execute(Constants.READ);

    } else if (id == R.id.books_view) {
            //TODO View the whole Library for the user

    } else if (id == R.id.shelf) {

    }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    /**
     *

    private class LoadBooksTask extends AsyncTask<String , Void, Volumes> {

        @Override
        protected Volumes doInBackground(String ... params) {
            //params[0] = getFilterField
            //params[1] = getFilterString

            EditFactory factory = EditFactory.getInstance();
            UserUtils filterData = factory.getEditFun("FilterData");
           // FilterData filterData = new FilterData();
            Log.d("MainActivity" , "filtering "+ params[0] + "  " + params[1]);
            Volumes volumes = null;
            String query;
            Log.d("MainActivity" , "filtering with subject in home activity");
            switch (Integer.parseInt(params[0])) {

                case Constants.TITLE:
                    query = "intitle:" + params[1];
                    volumes = filterData.doFunctionality("FilterDataByAttribute", query);
                    break;
                case Constants.SUBJECT:
                    query = "subject:" + params[1];
                    volumes = filterData.doFunctionality("FilterDataByAttribute", query);
                    break;
                case Constants.LOCATION:
                    query = params[1];
                    volumes = filterData.doFunctionality("FilterDataByAttribute", query);
                    break;
                case 0:
                    //TODO: Print Toast instead.
                    query = "intitle:harry";
                    volumes = filterData.doFunctionality("FilterDataByAttribute", query);
                    break;
            }

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

            adapter = new BookListAdapter(MainActivity.this, R.layout.books_list_item, volumes);
            booksListView.setAdapter(adapter);

            booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d(this.getClass().getName(), "open indivual book number " + i + " in list");
                    Intent intent = new Intent(MainActivity.this, BookActivity.class);
                    Volume volume = (Volume) booksListView.getAdapter().getItem(i);
                    intent.putExtra("book_id", volume.getId());
                    startActivity(intent);
                }
            });
        }

    }
*/

    /**
     * public class Filter {
     private int filterField;
     private String filterString;

     Filter(int filterField, String filterString) {
     this.filterField = filterField;
     this.filterString = filterString;
     }

     public int getFilterField() {
     return filterField;
     }

     public String getFilterString() {
     return filterString;
     }
     }
     */

}

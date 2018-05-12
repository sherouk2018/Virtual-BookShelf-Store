package csed.edu.alexu.eg.virtualbookshelf.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.services.books.model.Volumes;
import com.squareup.picasso.Picasso;

import csed.edu.alexu.eg.virtualbookshelf.R;
import csed.edu.alexu.eg.virtualbookshelf.utility.BookListAdapter;
import csed.edu.alexu.eg.virtualbookshelf.utility.Constants;
import csed.edu.alexu.eg.virtualbookshelf.utility.EditFactory;
import csed.edu.alexu.eg.virtualbookshelf.utility.FilterData;
import csed.edu.alexu.eg.virtualbookshelf.utility.ShowVolumesInBookshelf;
import csed.edu.alexu.eg.virtualbookshelf.utility.UserUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static MainActivity mainActivity;
    private final static String TAG = "MAIN-ACTIVITY";
    private static int filterOption = Constants.NO_FILTER;
    private ListView booksListView;
    private BookListAdapter adapter;
    private Button filter;
    private NavigationView navigationView;
    private boolean signedIn = false;
    private Toolbar toolbar;
    private String currentShelf;


    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity=this;
        //
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("840899473814-ggog8u302tcsri4otg29424dp420omi2.apps.googleusercontent.com")
                .requestScopes(new Scope("https://www.googleapis.com/auth/books"))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // top toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // books
        booksListView = findViewById(R.id.books_list);

        currentShelf=Constants.NO_SHELF;
        final Button clearBtn = findViewById(R.id.clear_list_btn);
        clearBtn.setVisibility(View.INVISIBLE);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage(R.string.clearing_list_alert_dialog);
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new UserUtilsAsyncTask(adapter, booksListView,
                                        MainActivity.this,
                                        "RemoveVolumeFromShelf",currentShelf).execute(currentShelf);
                                Toast.makeText(MainActivity.this, R.string.clearing_list_toast, Toast.LENGTH_SHORT).show();
                                // clear data
                                adapter.clear();
                                adapter.notifyDataSetChanged();
                                Log.e(TAG, "Clearing list : successful");
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        // filter text field
        final EditText filter_String = findViewById(R.id.search_text);
        // filter fields menu
//        adapter = new BookListAdapter(mainActivity, R.layout.books_list_item, new Volumes(), Constants.NO_SHELF, booksListView);
////        adapter.setNotifyOnChange(true);
//        booksListView.setAdapter(adapter);
//        ((MainActivity)mainActivity).setAdapter(adapter);
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
                clearBtn.setVisibility(View.INVISIBLE);
                Log.d(TAG, "Filtering" + filterOption + "  " + filter_String.getText().toString());
                if (adapter != null) {
                    Log.d(TAG, "Filtering" + filterOption + "  " + filter_String.getText().toString());
                    // clear data
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                }
                new UserUtilsAsyncTask(adapter, booksListView,
                        MainActivity.this,
                        "FilterData", Constants.NO_SHELF).execute(getFilter(String.valueOf(filterOption),
                        filter_String.getText().toString()));
            }

            private String[] getFilter(String... params) {
                //params[0] = filterType
                //params[1] = query

                String[] array = new String[2];
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
                        array[0] = "FilterDataByLocation";
                        array[1] = params[1];
                        break;
                    case Constants.NO_FILTER:
                        //TODO: Print Toast instead.
                        array[1] = "intitle:harry";
                        break;
                }
                return array;
            }
        });
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        Menu menuNav = navigationView.getMenu();

        // show logged in user lists
        menuNav.findItem(R.id.view_favourite_list).setVisible(signedIn);
        menuNav.findItem(R.id.view_wish_list).setVisible(signedIn);
        menuNav.findItem(R.id.view_read_list).setVisible(signedIn);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            signIn();
            item.setEnabled(false);
            item.setVisible(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        final Context context = this;

        // Get the layout inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int id = item.getItemId();

        Button clearBtn = findViewById(R.id.clear_list_btn);
        clearBtn.setVisibility(View.VISIBLE);
        switch (id) {
            case R.id.view_favourite_list:
                toolbar.setTitle(R.string.favourite_list);
                currentShelf=Constants.FAVOURITE;
                break;
            case R.id.view_wish_list:
                toolbar.setTitle(R.string.wish_list);
                currentShelf=Constants.WISH_LIST;
                break;
            case R.id.view_read_list:
                toolbar.setTitle(R.string.read_list);
                currentShelf=Constants.READ;
                break;
            case R.id.books_view:
                clearBtn.setVisibility(View.INVISIBLE);
                toolbar.setTitle("Books");
                currentShelf=Constants.NO_SHELF;
                //TODO View the whole Library for the user
                break;
            case R.id.shelf:
                toolbar.setTitle("Shelf");
                currentShelf=Constants.FAVOURITE;
                break;
        }
        if (currentShelf != Constants.NO_SHELF)
            new UserUtilsAsyncTask(adapter, booksListView,
                MainActivity.this,
                "ShowVolumesInBookshelf",currentShelf).execute(currentShelf);
        else {
            if(adapter!=null) {
//                adapter.getVolumes().clear();
                adapter.clear();
                adapter.notifyDataSetChanged();
            }
        }
        //
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 100;

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        signedIn = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("requestCode is ", requestCode + "");
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        Log.d(TAG, "12345");
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            Toast.makeText(this, R.string.signing_in_success_msg, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "signInResult:successful");
            updateUI(account);
        } catch (ApiException e) {
            Toast.makeText(this, R.string.signing_in_failure_msg, Toast.LENGTH_LONG).show();
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    protected void updateUI(GoogleSignInAccount account) {
        Log.d(TAG, "UI updated");
        EditFactory.init(MainActivity.this, account.getAccount());
        //TODO :
        Menu menuNav = navigationView.getMenu();
        ((TextView) findViewById(R.id.user_name)).setText(account.getDisplayName());
        ((TextView) findViewById(R.id.user_email)).setText(account.getEmail());
        Picasso.with(this).load(account.getPhotoUrl()).into((ImageView) findViewById(R.id.user_image));

        menuNav.findItem(R.id.view_favourite_list).setVisible(signedIn);
        menuNav.findItem(R.id.view_wish_list).setVisible(signedIn);
        menuNav.findItem(R.id.view_read_list).setVisible(signedIn);
    }

    public void setAdapter(BookListAdapter adapter) {
        this.adapter = adapter;
    }

    public boolean isSignedInUser(){
        return signedIn;
    }
}


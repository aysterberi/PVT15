package grupp2.satansdemocracy;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements WikiFragment.OnFragmentInteractionListener,
        InformationFragment.OnFragmentInteractionListener, NyheterFragment.OnFragmentInteractionListener{

    private Button beaconButton;
    private ImageSwitcher lampSwitcher;
    private boolean beaconMode;
    private Fragment fragment;
    private Toolbar toolbar;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private final String TAG = MainActivity.class.getSimpleName();

    /**
     * Sets up an instance oc the mainActivity class upon first creation.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.navList);
        addDrawerItems();
        setUpDrawer();
        beaconButton = (Button) findViewById(R.id.beacons_button);
        beaconButton.setText("AKTIVERA FÖRESTÄLLNINGSLÄGE");
        lampSwitcher = (ImageSwitcher) findViewById(R.id.lamp_switcher);

    }

    /**
     * Called upon finishing the application or shutting down.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     * Called upon completion of onCreate().
     * Syncs the navigation drawer.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        Animation in = AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.fade_out);
        lampSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView lampView = new ImageView(getApplicationContext());
                return lampView;
            }
        });

        lampSwitcher.setImageResource(R.drawable.lamp_off);
        lampSwitcher.setInAnimation(in);
        lampSwitcher.setOutAnimation(out);

        beaconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!beaconMode){
                    lampSwitcher.setImageResource(R.drawable.lamp_on);
                    beaconButton.setText("AVAKTIVERA FÖRESTÄLLNINGLÄGE");
                    beaconMode = true;
                } else {
                    lampSwitcher.setImageResource(R.drawable.lamp_off);
                    beaconButton.setText("AKTIVERA FÖRESTÄLLNINGLÄGE");
                    beaconMode = false;
                }

            }
        });
        mDrawerToggle.syncState();
        super.onPostCreate(savedInstanceState);
    }

    /**
     * Called when the application is opened again after being paused
     */
    @Override
    protected void onResume() {
        // till för framtiden kanske
        super.onResume();
    }

    /**
     * Called when the application is inactive (minimized).
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Creates an array for the navigation drawer in which to add headers.
     * Then using a switch statement to set up the headers.
     */
    private void addDrawerItems() {
        String[] drawerArray = {"START", "#SATANSDEMOKRATI", "INFORMATION ", "WIKI+", "LOGGA UT"};
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drawerArray);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStackImmediate();
                        }
                        mDrawerLayout.closeDrawers();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, new NyheterFragment())
                                .addToBackStack(null).commit();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, new InformationFragment())
                                .addToBackStack(null).commit();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 3:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, new WikiFragment())
                                .addToBackStack(null).commit();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 4:
                        LoginManager.getInstance().logOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        mDrawerLayout.closeDrawers();
                        break;
                }
            }
        });
    }

    /**
     * Called when user presses back
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /**
     * Sets up the navigation drawer
     */
    private void setUpDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    /**
     * TODO: Förklara när den här metoden kallas.
     * @param item
     * @return TODO: Vad är det som returneras? Drawern som blev tryckt på?
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param uri
     */
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

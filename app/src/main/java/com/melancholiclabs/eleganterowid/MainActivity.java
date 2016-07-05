package com.melancholiclabs.eleganterowid;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.mikepenz.aboutlibraries.LibsBuilder;

import java.util.Arrays;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;
import com.melancholiclabs.eleganterowid.model.IndexItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        IndexFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener {

    /**
     * Tag for logging.
     */
    private static final String TAG = MainActivity.class.getName();

    /**
     * ErowidDB to recieve data about the Erowid site.
     */
    private static final ErowidDB EROWID_DB = ErowidDB.getInstance();

    /**
     * DrawerLayout of MainActivity.
     */
    private DrawerLayout mDrawerLayout;

    /**
     * AsyncTask to load the chemIndex.
     */
    private FetchIndexTask mFetchChemIndexTask = new FetchIndexTask();
    /**
     * AsyncTask to load the plantIndex.
     */
    private FetchIndexTask mFetchPlantIndexTask = new FetchIndexTask();
    /**
     * AsyncTask to load the herbIndex.
     */
    private FetchIndexTask mFetchHerbIndexTask = new FetchIndexTask();
    /**
     * AsyncTask to load the pharmIndex.
     */
    private FetchIndexTask mFetchPharmIndexTask = new FetchIndexTask();
    /**
     * AsyncTask to load the smartIndex.
     */
    private FetchIndexTask mFetchSmartIndexTask = new FetchIndexTask();
    /**
     * AsyncTask to load the animalIndex.
     */
    private FetchIndexTask mFetchAnimalIndexTask = new FetchIndexTask();
    /**
     * AsyncTask to load the vaultIndex.
     */
    private FetchIndexTask mFetchVaultIndexTask = new FetchIndexTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!EROWID_DB.isChemIndexLoaded()) mFetchChemIndexTask.execute(ErowidDB.Index.CHEMICALS.toString());
        if (!EROWID_DB.isPlantIndexLoaded()) mFetchPlantIndexTask.execute(ErowidDB.Index.PLANTS.toString());
        if (!EROWID_DB.isHerbIndexLoaded()) mFetchHerbIndexTask.execute(ErowidDB.Index.HERBS.toString());
        if (!EROWID_DB.isPharmIndexLoaded()) mFetchPharmIndexTask.execute(ErowidDB.Index.PHARMS.toString());
        if (!EROWID_DB.isSmartIndexLoaded()) mFetchSmartIndexTask.execute(ErowidDB.Index.SMARTS.toString());
        if (!EROWID_DB.isAnimalIndexLoaded()) mFetchAnimalIndexTask.execute(ErowidDB.Index.ANIMALS.toString());
        if (!EROWID_DB.isVaultIndexLoaded()) mFetchVaultIndexTask.execute(ErowidDB.Index.VAULT.toString());

        // Calls the "Rate this app library"
        AppRate.with(this)
                .setInstallDays(5)
                .setLaunchTimes(5)
                .setRemindInterval(2)
                .setShowLaterButton(true)
                .setDebug(false)
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        Log.d(MainActivity.class.getName(), Integer.toString(which));
                    }
                })
                .monitor();
        AppRate.showRateDialogIfMeetsConditions(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = HomeFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            getSupportActionBar().setTitle(R.string.app_name);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Displays the respective view to the item id
        displayView(item.getItemId());
        // Close the navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Utils.gracefullyStop(mFetchChemIndexTask);
        Utils.gracefullyStop(mFetchPlantIndexTask);
        Utils.gracefullyStop(mFetchHerbIndexTask);
        Utils.gracefullyStop(mFetchPharmIndexTask);
        Utils.gracefullyStop(mFetchSmartIndexTask);
        Utils.gracefullyStop(mFetchAnimalIndexTask);
        Utils.gracefullyStop(mFetchVaultIndexTask);
    }

    public void displayView(int viewId) {
        Fragment fragment = null;
        String title = null;

        switch (viewId) {
            case R.id.nav_chem_index:
                fragment = new IndexFragment().newInstance(ErowidDB.Index.CHEMICALS.getIndexName());
                title = "Chemicals";
                break;
            case R.id.nav_plant_index:
                fragment = new IndexFragment().newInstance(ErowidDB.Index.PLANTS.getIndexName());
                title = "Plants";
                break;
            case R.id.nav_herb_index:
                fragment = new IndexFragment().newInstance(ErowidDB.Index.HERBS.getIndexName());
                title = "Herbs";
                break;
            case R.id.nav_pharm_index:
                fragment = new IndexFragment().newInstance(ErowidDB.Index.PHARMS.getIndexName());
                title = "Pharms";
                break;
            case R.id.nav_smart_index:
                fragment = new IndexFragment().newInstance(ErowidDB.Index.PLANTS.getIndexName());
                title = "Smarts";
                break;
            case R.id.nav_animal_index:
                fragment = new IndexFragment().newInstance(ErowidDB.Index.ANIMALS.getIndexName());
                title = "Animals";
                break;
            case R.id.nav_exp_vault:
                fragment = new IndexFragment().newInstance(ErowidDB.Index.VAULT.getIndexName());
                title = "Experience Vault";
                break;
            case R.id.nav_about:
                fragment = new LibsBuilder().supportFragment();
                title = "About this App";
                break;
        }

        // Transitions to the new fragment while replacing the fragment_container
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            }
            ft.addToBackStack("nav_selection");
            ft.commit();
        }

        // Set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        // Closes the navigation drawer to go to new view
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onFragmentInteraction(IndexItem item) {
        Log.d(TAG, item.toString());
        Intent intent = null;
        if (item.getId() != null) {
            intent = SubstanceActivity.newIntent(this, item);
        } else {
            intent = VaultActivity.newIntent(this, item);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }

    @Override
    public void onFragmentInteraction(String query, boolean[] options) {
        Log.d(TAG, query);
        Log.d(TAG, Arrays.toString(options));
        Fragment fragment = SearchFragment.newInstance(query, options);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack("search");
        ft.commit();

        getSupportActionBar().setTitle(query);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public class FetchIndexTask extends AsyncTask<String, Void, Void> {

        String indexType = null;

        @Override
        protected Void doInBackground(String... params) {
            // Returns null if no value is passed through for indexType
            if (params[0] == null) return null;
            indexType = params[0];
            // Make-shift String switch statement
            if (indexType.equals(ErowidDB.Index.CHEMICALS.toString())) {
                EROWID_DB.loadChemIndex();
            } else if (indexType.equals(ErowidDB.Index.PLANTS.toString())) {
                EROWID_DB.loadPlantIndex();
            } else if (indexType.equals(ErowidDB.Index.HERBS.toString())) {
                EROWID_DB.loadHerbIndex();
            } else if (indexType.equals(ErowidDB.Index.PHARMS.toString())) {
                EROWID_DB.loadPharmIndex();
            } else if (indexType.equals(ErowidDB.Index.SMARTS.toString())) {
                EROWID_DB.loadSmartIndex();
            } else if (indexType.equals(ErowidDB.Index.ANIMALS.toString())) {
                EROWID_DB.loadAnimalIndex();
            } else if (indexType.equals(ErowidDB.Index.VAULT.toString())) {
                EROWID_DB.loadVaultIndex();
            } else {
                Log.d(TAG, "params[0] was not null but didn't match an indexType");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(TAG, indexType + " has finished.");
        }
    }
}

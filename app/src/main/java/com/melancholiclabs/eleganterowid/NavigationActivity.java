package com.melancholiclabs.eleganterowid;

import android.net.Uri;
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
import android.view.Menu;
import android.view.MenuItem;

import com.melancholiclabs.eleganterowid.index.IndexFragment;

import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IndexFragment.OnFragmentInteractionListener {

    public static final String chemIndex = "http://104.131.56.118/erowid/api.php/chemIndex?columns=id,name,effectsClassification,category&transform=1";
    public static final String plantIndex = "http://104.131.56.118/erowid/api.php/plantIndex?columns=id,name,effectsClassification,category&transform=1";
    public static final String herbIndex = "http://104.131.56.118/erowid/api.php/herbIndex?columns=id,name,botanicalClassification,category&transform=1";
    public static final String pharmIndex = "http://104.131.56.118/erowid/api.php/pharmIndex?columns=id,name,effectsClassification,category&transform=1";
    public static final String smartIndex = "http://104.131.56.118/erowid/api.php/smartIndex?columns=id,name,effectsClassification,category&transform=1";
    public static final String animalIndex = "http://104.131.56.118/erowid/api.php/animalIndex?columns=id,name,effectsClassification,category&transform=1";

    public static ArrayList<IndexItem> mainIndex = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        HomeFragment homeFragment = HomeFragment.newInstance();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, homeFragment);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.navigation, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_chem_index) {
            displayView(id);
        } else if (id == R.id.nav_plant_index) {
            displayView(id);
        } else if (id == R.id.nav_herb_index) {
            displayView(id);
        } else if (id == R.id.nav_pharm_index) {
            displayView(id);
        } else if (id == R.id.nav_smart_index) {
            displayView(id);
        } else if (id == R.id.nav_animal_index) {
            displayView(id);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Displays a view based off what viewId is passed
    public void displayView(int viewId) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        // Need to figure out how to call a certain index or if needed create all the separate fragments
        switch (viewId) {
            case R.id.nav_chem_index:
                fragment = new IndexFragment().newInstance("chemIndex");
                title = "Chemicals";
                break;
            case R.id.nav_plant_index:
                fragment = new IndexFragment().newInstance("plantIndex");
                title = "Plants";
                break;
            case R.id.nav_herb_index:
                fragment = new IndexFragment().newInstance("herbIndex");
                title = "Herbs";
                break;
            case R.id.nav_pharm_index:
                fragment = new IndexFragment().newInstance("pharmIndex");
                title = "Pharms";
                break;
            case R.id.nav_smart_index:
                fragment = new IndexFragment().newInstance("smartIndex");
                title = "Smarts";
                break;
            case R.id.nav_animal_index:
                fragment = new IndexFragment().newInstance("animalIndex");
                title = "Animals";
                break;
        }

        // Transitions to the new fragment while replacing the fragment_container
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack("index");
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
    public void onFragmentInteraction(Uri uri) {
        System.out.println(uri);
    }

    public class IndexItem {
        public String id;
        public String name;
        public String caption;
        public String category;

        public IndexItem(String id, String name, String caption, String category) {
            this.id = id;
            this.name = name;
            this.caption = caption;
            this.category = category;
        }
    }

    public class FetchMainIndexTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            return null;
        }
    }
}

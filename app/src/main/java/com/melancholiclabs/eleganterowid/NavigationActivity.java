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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IndexFragment.OnFragmentInteractionListener {

    public static final String CHEM_URL = "http://104.131.56.118/erowid/api.php/chemIndex?columns=id,name,effectsClassification,category&transform=1";
    public static final String PLANT_URL = "http://104.131.56.118/erowid/api.php/plantIndex?columns=id,name,effectsClassification,category&transform=1";
    public static final String HERB_URL = "http://104.131.56.118/erowid/api.php/herbIndex?columns=id,name,botanicalClassification,category&transform=1";
    public static final String PHARM_URL = "http://104.131.56.118/erowid/api.php/pharmIndex?columns=id,name,effectsClassification,category&transform=1";
    public static final String SMART_URL = "http://104.131.56.118/erowid/api.php/smartIndex?columns=id,name,effectsClassification,category&transform=1";
    public static final String ANIMAL_URL = "http://104.131.56.118/erowid/api.php/animalIndex?columns=id,name,effectsClassification,category&transform=1";

    public static ArrayList<IndexItem> mainIndex = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FetchMainIndexTask fetchMainIndexTask = new FetchMainIndexTask();
        fetchMainIndexTask.execute();

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

    public class FetchMainIndexTask extends AsyncTask<Void, Void, Void> {
        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * <p/>
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private void loadIndexFromJSON(String forecastJsonStr, String indexType)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray substanceArray = forecastJson.getJSONArray(indexType);

            for (int i = 0; i < substanceArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                String substanceID;
                String substanceName;
                String substanceCaption;
                String substanceCategory;

                // Get the JSON object representing the day
                JSONObject substance = substanceArray.getJSONObject(i);

                substanceID = substance.getString("id");
                substanceName = substance.getString("name");
                if (!indexType.equals("herbIndex")) {
                    substanceCaption = substance.getString("effectsClassification");
                } else {
                    substanceCaption = substance.getString("botanicalClassification");
                }
                substanceCategory = substance.getString("category");

                mainIndex.add(new IndexItem(substanceID, substanceName, substanceCaption, substanceCategory));
            }
        }

        private Void loadIndex(String urlString, String indexType) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String indexJsonStr = null;

            try {

                URL url = new URL(urlString);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line);
                    buffer.append("\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                indexJsonStr = buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                loadIndexFromJSON(indexJsonStr, indexType);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadIndex(CHEM_URL, "chemIndex");
            loadIndex(PLANT_URL, "plantIndex");
            loadIndex(HERB_URL, "herbIndex");
            loadIndex(PHARM_URL, "pharmIndex");
            loadIndex(SMART_URL, "smartIndex");
            loadIndex(ANIMAL_URL, "animalIndex");
            return null;
        }
    }
}

package com.melancholiclabs.eleganterowid.substance;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.melancholiclabs.eleganterowid.R;
import com.melancholiclabs.eleganterowid.pages.BasicsFragment;
import com.melancholiclabs.eleganterowid.pages.ChemistryFragment;
import com.melancholiclabs.eleganterowid.pages.DoseFragment;
import com.melancholiclabs.eleganterowid.pages.EffectsFragment;
import com.melancholiclabs.eleganterowid.pages.HealthFragment;
import com.melancholiclabs.eleganterowid.pages.ImagesFragment;
import com.melancholiclabs.eleganterowid.pages.LawFragment;
import com.melancholiclabs.eleganterowid.pages.MainFragment;
import com.melancholiclabs.eleganterowid.pages.ResearchChemicalFragment;

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

public class SubstanceActivity extends AppCompatActivity {

    private static final String URL_PREFIX = "http://104.131.56.118/erowid/api.php/";
    private static final String URL_MIDDLE = "?filter=id,eq,";
    private static final String URL_SUFFIX = "&comlumns=&columns=effectsClassification,botanicalClassification,commonNames,chemicalName,uses,description,imageURL,basicsURL,effectsURL,imagesURL,healthURL,lawURL,doseURL,chemistryURL,researchChemicalsURL&transform=1";

    private static final String ARG_ID = "id";
    private static final String ARG_NAME = "name";
    private static final String ARG_CATEGORY = "category";
    private static final String ARG_PAGES = "pages";

    private static String mId;
    private static String mName;
    private static String mCategory;
    private static String[] mPages;
    private static String mIndexType;

    private static String imageURL;
    private static String effectsClassification;
    private static String botanicalClassification;
    private static String chemicalName;
    private static String commonNames;
    private static String uses;
    private static String description;

    private Bitmap substanceImage;

    private ArrayList<String> pageTitles = new ArrayList<>();

    private FetchSubstanceTask fetchSubstanceTask = new FetchSubstanceTask();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_substance);

        Bundle b = getIntent().getExtras();
        mId = b.getString(ARG_ID);
        mName = b.getString(ARG_NAME);
        mCategory = b.getString(ARG_CATEGORY);
        mPages = b.getStringArray(ARG_PAGES);

        for (String url : mPages) {
            if (url.contains("basics")) pageTitles.add("BASICS");
            if (url.contains("effects")) pageTitles.add("EFFECTS");
            if (url.contains("images")) pageTitles.add("IMAGES");
            if (url.contains("health")) pageTitles.add("HEALTH");
            if (url.contains("law")) pageTitles.add("LAW");
            if (url.contains("dose")) pageTitles.add("DOSE");
            if (url.contains("chemistry")) pageTitles.add("CHEMISTRY");
            if (url.contains("research_chems")) pageTitles.add("RESEARCH CHEMICAL");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mName);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(8);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        if (mCategory.equals("Chemicals")) {
            mIndexType = "chemIndex";
        } else if (mCategory.equals("Plants")) {
            mIndexType = "plantIndex";
        } else if (mCategory.equals("Herbs")) {
            mIndexType = "herbIndex";
        } else if (mCategory.equals("Pharms")) {
            mIndexType = "pharmIndex";
        } else if (mCategory.equals("Smarts")) {
            mIndexType = "smartIndex";
        } else if (mCategory.equals("Animals")) {
            mIndexType = "animalIndex";
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_substance, menu);
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

    @Override
    public void onStop() {
        super.onStop();

        if (fetchSubstanceTask != null && fetchSubstanceTask.getStatus() == AsyncTask.Status.RUNNING) {
            fetchSubstanceTask.cancel(true);
        }

        if (fetchSubstanceTask != null && fetchSubstanceTask.getStatus() == AsyncTask.Status.RUNNING) {
            fetchSubstanceTask.cancel(true);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return MainFragment.newInstance(URL_PREFIX + mIndexType + URL_MIDDLE + mId + URL_SUFFIX, mIndexType);
            if (pageTitles.get(position - 1).equals("BASICS"))
                return BasicsFragment.newInstance(mId);
            if (pageTitles.get(position - 1).equals("EFFECTS"))
                return EffectsFragment.newInstance(mId);
            if (pageTitles.get(position - 1).equals("IMAGES"))
                return ImagesFragment.newInstance(mId);
            if (pageTitles.get(position - 1).equals("HEALTH"))
                return HealthFragment.newInstance(mId);
            if (pageTitles.get(position - 1).equals("LAW")) return LawFragment.newInstance(mId);
            if (pageTitles.get(position - 1).equals("DOSE")) return DoseFragment.newInstance(mId);
            if (pageTitles.get(position - 1).equals("CHEMISTRY"))
                return ChemistryFragment.newInstance(mId);
            if (pageTitles.get(position - 1).equals("RESEARCH CHEMICAL"))
                return ResearchChemicalFragment.newInstance(mId);
            return null;
        }

        @Override
        public int getCount() {
            int count = 1;
            for (String url : mPages) {
                if (!url.equals("null")) count++;
        }
            return count;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return "MAIN";
            return pageTitles.get(position - 1);
    }
    }

    public class FetchSubstanceTask extends AsyncTask<Void, Void, Void> {
        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * <p/>
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private void loadIndexFromJSON(String substanceJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            JSONObject forecastJson = new JSONObject(substanceJsonStr);
            JSONArray substanceArray = forecastJson.getJSONArray(mIndexType);

            // Get the JSON object representing the day
            JSONObject substance = substanceArray.getJSONObject(0);

            try {
                imageURL = substance.getString("imagesURL");
            } catch (JSONException e) {
                // Do nothing
            }
            try {
                effectsClassification = substance.getString("effectsClassification");
            } catch (JSONException e) {
                // Do nothing
            }
            try {
                botanicalClassification = substance.getString("botanicalClassification");
            } catch (JSONException e) {
                // Do nothing
            }
            try {
                commonNames = substance.getString("commonNames");
            } catch (JSONException e) {
                // Do nothing
            }
            try {
                uses = substance.getString("uses");
            } catch (JSONException e) {
                // Do nothing
            }
            try {
                description = substance.getString("description");
            } catch (JSONException e) {
                // Do nothing
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String substanceJsonStr = null;

            try {

                URL url = new URL(URL_PREFIX + mIndexType + URL_MIDDLE + mId + URL_SUFFIX);

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
                substanceJsonStr = buffer.toString();
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
                loadIndexFromJSON(substanceJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
    }
}
}

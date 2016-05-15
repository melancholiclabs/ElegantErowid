package com.melancholiclabs.eleganterowid.substance;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TabWidget;

import com.melancholiclabs.eleganterowid.R;
import com.melancholiclabs.eleganterowid.pages.PageFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubstanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubstanceFragment extends Fragment {

    private static final String URL_PREFIX = "http://104.131.56.118/erowid/api.php/";
    private static final String URL_MIDDLE = "?filter=id,eq,";
    private static final String URL_SUFFIX = "&comlumns=&columns=effectsClassification,botanicalClassification,commonNames,chemicalName,uses,description,imageURL,basicsURL,effectsURL,imagesURL,healthURL,lawURL,doseURL,chemistryURL,researchChemicalsURL&transform=1";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "id";
    private static final String ARG_NAME = "name";
    private static final String ARG_CATEGORY = "category";
    private FragmentTabHost mTabHost;
    // TODO: Rename and change types of parameters
    private String mId;
    private String mName;
    private String mCategory;
    private String mIndexType;

    private String imageURL;
    private String effectsClassification;
    private String botanicalClassification;
    private String chemicalName;
    private String commonNames;
    private String uses;
    private String description;

    private String basicsURL;
    private String effectsURL;
    private String imagesURL;
    private String healthURL;
    private String lawURL;
    private String doseURL;
    private String chemistryURL;
    private String researchChemicalURL;

    private Bitmap substanceImage;

    public SubstanceFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id       Parameter 1.
     * @param name     Parameter 2.
     * @param category Parameter 3.
     * @return A new instance of fragment SubstanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubstanceFragment newInstance(String id, String name, String category) {
        SubstanceFragment fragment = new SubstanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        args.putString(ARG_NAME, name);
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getString(ARG_ID);
            mName = getArguments().getString(ARG_NAME);
            mCategory = getArguments().getString(ARG_CATEGORY);

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

            FetchSubstanceTask fetchSubstanceTask = new FetchSubstanceTask();
            fetchSubstanceTask.execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_substance, container, false);

        FetchSubstanceTask fetchSubstanceTask = new FetchSubstanceTask();
        fetchSubstanceTask.execute();

        mTabHost = (FragmentTabHost) myView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("Basics").setIndicator("Basics"),
                PageFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Effects").setIndicator("Effects"),
                PageFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Images").setIndicator("Images"),
                PageFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Health").setIndicator("Health"),
                PageFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Health").setIndicator("Health"),
                PageFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Law").setIndicator("Law"),
                PageFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Dose").setIndicator("Dose"),
                PageFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Chemistry").setIndicator("Chemistry"),
                PageFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Research Chemical").setIndicator("Research Chemical"),
                PageFragment.class, null);

        TabWidget tw = (TabWidget) myView.findViewById(android.R.id.tabs);
        LinearLayout ll = (LinearLayout) tw.getParent();
        HorizontalScrollView hs = new HorizontalScrollView(this.getContext());
        hs.setBackgroundColor(Color.rgb(102, 192, 183));
        hs.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        ll.addView(hs, 0);
        ll.removeView(tw);
        hs.addView(tw);
        hs.setHorizontalScrollBarEnabled(false);

        return myView;
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
                imagesURL = substance.getString("imagesURL");
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

            basicsURL = substance.getString("basicsURL");
            effectsURL = substance.getString("effectsURL");
            imagesURL = substance.getString("imagesURL");
            healthURL = substance.getString("healthURL");
            lawURL = substance.getString("lawURL");
            doseURL = substance.getString("doseURL");
            chemistryURL = substance.getString("chemistryURL");
            try {
                researchChemicalURL = substance.getString("researchChemicalURL");
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
    }
}

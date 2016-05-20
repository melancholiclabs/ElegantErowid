package com.melancholiclabs.eleganterowid.pages;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.melancholiclabs.eleganterowid.R;

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
 * Use the {@link HealthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealthFragment extends Fragment {

    private static final String URL_PREFIX = "http://104.131.56.118/erowid/api.php/healthIndex?filter=id,eq,";
    private static final String URL_SUFFIX = "&columns=notes,deaths,warnings,cautions,benefits&transform=1";

    private static final String ARG_ID = "id";

    private String mID;

    private String notes;
    private String deaths;
    private String warnings;
    private String cautions;
    private String benefits;

    private FetchHealthTask fetchHealthTask = new FetchHealthTask();

    public HealthFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id substance id.
     * @return A new instance of fragment HealthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HealthFragment newInstance(String id) {
        HealthFragment fragment = new HealthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mID = getArguments().getString(ARG_ID);
        }
        fetchHealthTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_health, container, false);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (fetchHealthTask != null && fetchHealthTask.getStatus() == AsyncTask.Status.RUNNING) {
            fetchHealthTask.cancel(true);
        }
    }

    public class FetchHealthTask extends AsyncTask<Void, Void, Void> {
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
            JSONArray substanceArray = forecastJson.getJSONArray("healthIndex");

            // Get the JSON object representing the day
            JSONObject substance = substanceArray.getJSONObject(0);

            notes = substance.getString("notes");
            deaths = substance.getString("deaths");
            warnings = substance.getString("warnings");
            cautions = substance.getString("cautions");
            benefits = substance.getString("benefits");
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

                URL url = new URL(URL_PREFIX + mID + URL_SUFFIX);

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
            View rootView = getView();

            LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.health_linear_layout);

            if (!notes.equals("null")) {
                TextView titleTextView = (TextView) getLayoutInflater(null).inflate(R.layout.title_text_view, null);
                titleTextView.setText("Notes");

                TextView paragraphTextView = new TextView(getContext());
                paragraphTextView.setText(notes);
                paragraphTextView.setPadding(10, 0, 0, 10);

                linearLayout.addView(titleTextView);
                linearLayout.addView(paragraphTextView);
            }

            if (!deaths.equals("null")) {
                TextView titleTextView = (TextView) getLayoutInflater(null).inflate(R.layout.title_text_view, null);
                titleTextView.setText("Deaths");

                TextView paragraphTextView = new TextView(getContext());
                paragraphTextView.setText(deaths);
                paragraphTextView.setPadding(10, 0, 0, 10);

                linearLayout.addView(titleTextView);
                linearLayout.addView(paragraphTextView);
            }

            if (!warnings.equals("null")) {
                TextView titleTextView = (TextView) getLayoutInflater(null).inflate(R.layout.title_text_view, null);
                titleTextView.setText("Warnings");

                TextView paragraphTextView = new TextView(getContext());
                paragraphTextView.setText(warnings);
                paragraphTextView.setPadding(10, 0, 0, 10);

                linearLayout.addView(titleTextView);
                linearLayout.addView(paragraphTextView);
            }

            if (!cautions.equals("null")) {
                TextView titleTextView = (TextView) getLayoutInflater(null).inflate(R.layout.title_text_view, null);
                titleTextView.setText("Cautions");

                TextView paragraphTextView = new TextView(getContext());
                paragraphTextView.setText(cautions);
                paragraphTextView.setPadding(10, 0, 0, 10);

                linearLayout.addView(titleTextView);
                linearLayout.addView(paragraphTextView);
            }

            if (!benefits.equals("null")) {
                TextView titleTextView = (TextView) getLayoutInflater(null).inflate(R.layout.title_text_view, null);
                titleTextView.setText("Benefits");

                TextView paragraphTextView = new TextView(getContext());
                paragraphTextView.setText(benefits);
                paragraphTextView.setPadding(10, 0, 0, 10);

                linearLayout.addView(titleTextView);
                linearLayout.addView(paragraphTextView);
            }

            rootView.invalidate();

            super.onPostExecute(aVoid);
        }
    }

}

package com.melancholiclabs.eleganterowid.pages;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.melancholiclabs.eleganterowid.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

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
 * Use the {@link ResearchChemicalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResearchChemicalFragment extends Fragment {

    private static final String URL_PREFIX = "http://104.131.56.118/erowid/api.php/researchChemicalIndex?filter=id,eq,";
    private static final String URL_SUFFIX = "&columns=summaryText,imageURL&transform=1";

    private static final String ARG_ID = "id";

    private String mID;

    private String summaryText;
    private String imageURL;

    private FetchResearchChemicalTask fetchResearchChemicalTask = new FetchResearchChemicalTask();

    public ResearchChemicalFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id substance id.
     * @return A new instance of fragment ResearchChemicalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResearchChemicalFragment newInstance(String id) {
        ResearchChemicalFragment fragment = new ResearchChemicalFragment();
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
        fetchResearchChemicalTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_research_chemical, container, false);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (fetchResearchChemicalTask != null && fetchResearchChemicalTask.getStatus() == AsyncTask.Status.RUNNING) {
            fetchResearchChemicalTask.cancel(true);
        }
    }

    public class FetchResearchChemicalTask extends AsyncTask<Void, Void, Void> {
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
            JSONArray substanceArray = forecastJson.getJSONArray("researchChemicalIndex");

            // Get the JSON object representing the day
            JSONObject substance = substanceArray.getJSONObject(0);

            summaryText = substance.getString("summaryText");
            imageURL = substance.getString("imageURL");
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

            LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.research_chemical_linear_layout);

            if (!imageURL.equals("null")) {
                final ImageView imageView = (ImageView) rootView.findViewById(R.id.research_chemical_image);

                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
                imageLoader.loadImage(imageURL, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        // Transition drawable with a transparent drwabale and the final bitmap
                        final TransitionDrawable td =
                                new TransitionDrawable(new Drawable[]{
                                        new ColorDrawable(Color.TRANSPARENT),
                                        new BitmapDrawable(loadedImage)
                                });
                        imageView.setImageDrawable(td);
                        td.startTransition(250);
                        super.onLoadingComplete(imageUri, view, loadedImage);
                    }
                });
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setVisibility(View.VISIBLE);
            }

            if (!summaryText.equals("null")) {
                TextView titleTextView = (TextView) getLayoutInflater(null).inflate(R.layout.title_text_view, null);
                titleTextView.setText("Summary");
                titleTextView.setTextColor(Color.WHITE);
                titleTextView.setShadowLayer(6, 2, 2, Color.BLACK);

                TextView paragraphTextView = new TextView(getContext());
                paragraphTextView.setText(summaryText);
                paragraphTextView.setPadding(10, 0, 0, 10);
                paragraphTextView.setTextColor(Color.WHITE);
                paragraphTextView.setShadowLayer(6, 2, 2, Color.BLACK);

                linearLayout.addView(titleTextView);
                linearLayout.addView(paragraphTextView);
            }

            rootView.invalidate();

            super.onPostExecute(aVoid);
        }
    }
}

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
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    private static final String ARG_URL = "url";
    private static final String ARG_INDEX_TYPE = "indexType";
    private static String imageURL;
    private static String effectsClassification;
    private static String botanicalClassification;
    private static String chemicalName;
    private static String commonNames;
    private static String uses;
    private static String description;
    private String mURL;
    private String mIndexType;
    private FetchSubstanceTask fetchSubstanceTask = new FetchSubstanceTask();

    public MainFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param url       substanceURL.
     * @param indexType substance index type.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String url, String indexType) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putString(ARG_INDEX_TYPE, indexType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mURL = getArguments().getString("url");
            mIndexType = getArguments().getString("indexType");
        }
        fetchSubstanceTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
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

    public class FetchSubstanceTask extends AsyncTask<Void, Void, Void> {
        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * <p>
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
                imageURL = substance.getString("imageURL");
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
                chemicalName = substance.getString("chemicalName");
            } catch (JSONException e) {
                // Do nothing
            }
            try {
                commonNames = substance.getString("commonNames");
                commonNames = commonNames.replaceAll("; ", "\n");
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

                URL url = new URL(mURL);

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

            LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.main_linear_layout);

            if (imageURL != null && !imageURL.equals("null")) {
                final ImageView imageView = (ImageView) rootView.findViewById(R.id.substance_image);

                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                layoutParams.height = (rootView.getWidth() * 2) / 3;

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
                        // Set background to loading bitmap
                        // substanceImageView.setBackgroundDrawable(
                        //        new BitmapDrawable(mResources, mLoadingBitmap));

                        imageView.setImageDrawable(td);
                        td.startTransition(250);
                        super.onLoadingComplete(imageUri, view, loadedImage);
                    }
                });
                imageView.setLayoutParams(layoutParams);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setVisibility(View.VISIBLE);
            }

            if (effectsClassification != null && !effectsClassification.equals("null")) {
                TextView titleTextView = (TextView) getLayoutInflater(null).inflate(R.layout.title_text_view, null);
                titleTextView.setText("Effects Classification");

                TextView paragraphTextView = new TextView(getContext());
                paragraphTextView.setText(effectsClassification);
                paragraphTextView.setPadding(10, 0, 0, 10);

                linearLayout.addView(titleTextView);
                linearLayout.addView(paragraphTextView);
            }

            if (botanicalClassification != null && !botanicalClassification.equals("null")) {
                TextView titleTextView = (TextView) getLayoutInflater(null).inflate(R.layout.title_text_view, null);
                titleTextView.setText("Botanical Classification");

                TextView paragraphTextView = new TextView(getContext());
                paragraphTextView.setText(botanicalClassification.replaceAll(", ", "\n"));
                paragraphTextView.setPadding(10, 0, 0, 10);

                linearLayout.addView(titleTextView);
                linearLayout.addView(paragraphTextView);
            }

            if (chemicalName != null && !chemicalName.equals("null")) {
                TextView titleTextView = (TextView) getLayoutInflater(null).inflate(R.layout.title_text_view, null);
                titleTextView.setText("Chemical Name");

                TextView paragraphTextView = new TextView(getContext());
                paragraphTextView.setText(chemicalName);
                paragraphTextView.setPadding(10, 0, 0, 10);

                linearLayout.addView(titleTextView);
                linearLayout.addView(paragraphTextView);
            }

            if (commonNames != null && !commonNames.equals("null")) {
                TextView titleTextView = (TextView) getLayoutInflater(null).inflate(R.layout.title_text_view, null);
                titleTextView.setText("Common Names");

                TextView paragraphTextView = new TextView(getContext());
                paragraphTextView.setText(commonNames);
                paragraphTextView.setPadding(10, 0, 0, 10);

                linearLayout.addView(titleTextView);
                linearLayout.addView(paragraphTextView);
            }

            if (description != null && !description.equals("null")) {
                TextView titleTextView = (TextView) getLayoutInflater(null).inflate(R.layout.title_text_view, null);
                titleTextView.setText("Description");

                TextView paragraphTextView = new TextView(getContext());
                paragraphTextView.setText(description);
                paragraphTextView.setPadding(10, 0, 0, 10);

                linearLayout.addView(titleTextView);
                linearLayout.addView(paragraphTextView);
            }

            if (uses != null && !uses.equals("null")) {
                TextView titleTextView = (TextView) getLayoutInflater(null).inflate(R.layout.title_text_view, null);
                titleTextView.setText("Uses");

                TextView paragraphTextView = new TextView(getContext());
                paragraphTextView.setText(uses);
                paragraphTextView.setPadding(10, 0, 0, 10);

                linearLayout.addView(titleTextView);
                linearLayout.addView(paragraphTextView);
            }

            rootView.invalidate();

            super.onPostExecute(aVoid);
        }
    }
}

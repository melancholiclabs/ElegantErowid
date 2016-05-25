package com.melancholiclabs.eleganterowid.reports;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.melancholiclabs.eleganterowid.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Scanner;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExperienceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExperienceFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_AUTHOR = "author";
    private static final String ARG_SUBSTANCE = "substance";
    private static final String ARG_PUB_DATE = "date";
    private static final String ARG_URL = "url";

    private String mTitle;
    private String mAuthor;
    private String mSubstance;
    private String mPublishData;
    private String mUrl;

    private String doseTable;
    private String bodyWeight;
    private String text;
    private String year;
    private String gender;
    private String id;
    private String age;
    private String views;

    private FetchExperienceTask fetchExperienceTask = new FetchExperienceTask();

    public ExperienceFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Report title.
     * @param author Report author.
     * @param substance Report substances.
     * @param date Report date.
     * @param url Report url.
     * @return A new instance of fragment ExperienceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExperienceFragment newInstance(String title, String author, String substance, String date, String url) {
        ExperienceFragment fragment = new ExperienceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_AUTHOR, author);
        args.putString(ARG_SUBSTANCE, substance);
        args.putString(ARG_PUB_DATE, date);
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
            mAuthor = getArguments().getString(ARG_AUTHOR);
            mSubstance = getArguments().getString(ARG_SUBSTANCE);
            mPublishData = getArguments().getString(ARG_PUB_DATE);
            mUrl = getArguments().getString(ARG_URL);
        }
        fetchExperienceTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_experience, container, false);
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fetchExperienceTask != null && fetchExperienceTask.getStatus() == AsyncTask.Status.RUNNING) {
            fetchExperienceTask.cancel(true);
        }
    }

    public class FetchExperienceTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Document doc = Jsoup.connect(mUrl).get();

                Element dataTable = doc.select("table").get(2);
                StringBuilder builder = new StringBuilder();
                Elements tr = dataTable.select("tr");
                for (Element trElement : tr) {
                    StringBuilder lineBuilder = new StringBuilder();
                    Elements td = trElement.select("td");
                    for (Element tdElement : td) {
                        lineBuilder.append(tdElement.text());
                        lineBuilder.append("\t");
                    }
                    builder.append(lineBuilder.toString().trim());
                    builder.append("\n");
                }
                doseTable = builder.toString().trim();

                Element weightTable = doc.select("table").get(3);
                bodyWeight = weightTable.select("tr td").get(1).text();

                Element footerTable = doc.select("table").get(4);
                System.out.println(footerTable.toString());
                year = footerTable.select("tr td").first().text();
                id = footerTable.select("tr td").get(1).text();
                gender = footerTable.select("tr td").get(2).text();
                age = footerTable.select("tr td").get(4).text();
                views = footerTable.select("tr td").get(7).text();

                String raw = doc.select("div[class=report-text-surround]").first().toString();

                Scanner scanner = new Scanner(raw);
                boolean isBody = false;
                StringBuilder textBuilder = new StringBuilder();
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.contains("<!-- Start Body -->")) isBody = true;
                    if (line.contains("<!-- End Body -->")) isBody = false;
                    if (isBody) {
                        textBuilder.append(line.trim());
                        textBuilder.append("\n");
                    }
                }
                text = textBuilder.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            View rootView = getView();

            MaterialProgressBar materialProgressBar = (MaterialProgressBar) rootView.findViewById(R.id.experience_progress_bar);
            materialProgressBar.setVisibility(View.GONE);

            LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.experience_linear_layout);

            if (mTitle != null && !mTitle.equals("null")) {
                TextView paragraphTextView = (TextView) getLayoutInflater(null).inflate(R.layout.title_text_view, null);
                paragraphTextView.setText(mTitle);
                paragraphTextView.setPadding(10, 0, 10, 0);

                linearLayout.addView(paragraphTextView);
            }

            if (doseTable != null && !doseTable.equals("null")) {
                TextView paragraphTextView = (TextView) getLayoutInflater(null).inflate(R.layout.bold_text_view, null);
                paragraphTextView.setText(doseTable);
                paragraphTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                paragraphTextView.setPadding(10, 0, 10, 10);

                linearLayout.addView(paragraphTextView);
            }

            if (bodyWeight != null && !bodyWeight.equals("null")) {
                TextView paragraphTextView = (TextView) getLayoutInflater(null).inflate(R.layout.bold_text_view, null);
                paragraphTextView.setText("Body Weight: " + bodyWeight);
                paragraphTextView.setPadding(10, 0, 10, 0);

                linearLayout.addView(paragraphTextView);
            }

            if (year != null && !year.equals("null")) {
                TextView paragraphTextView = (TextView) getLayoutInflater(null).inflate(R.layout.bold_text_view, null);
                paragraphTextView.setText(year);
                paragraphTextView.setPadding(10, 0, 10, 0);

                linearLayout.addView(paragraphTextView);
            }

            if (mSubstance != null && !mSubstance.equals("null")) {
                TextView paragraphTextView = (TextView) getLayoutInflater(null).inflate(R.layout.bold_text_view, null);
                paragraphTextView.setText("Substances: " + mSubstance);
                paragraphTextView.setPadding(10, 0, 10, 0);

                linearLayout.addView(paragraphTextView);
            }

            if (age != null && !age.equals("null")) {
                TextView paragraphTextView = (TextView) getLayoutInflater(null).inflate(R.layout.bold_text_view, null);
                paragraphTextView.setText(age);
                paragraphTextView.setPadding(10, 0, 10, 0);

                linearLayout.addView(paragraphTextView);
            }

            if (gender != null && !gender.equals("null")) {
                TextView paragraphTextView = (TextView) getLayoutInflater(null).inflate(R.layout.bold_text_view, null);
                paragraphTextView.setText(gender);
                paragraphTextView.setPadding(10, 0, 10, 0);

                linearLayout.addView(paragraphTextView);
            }

            if (text != null && !text.equals("null")) {
                TextView paragraphTextView = new TextView(getContext());
                paragraphTextView.setText(Html.fromHtml(text.replaceAll("\u2019", "&#39;")));
                paragraphTextView.setPadding(10, 0, 10, 0);

                linearLayout.addView(paragraphTextView);
            }

            if (id != null && !id.equals("null")) {
                TextView paragraphTextView = (TextView) getLayoutInflater(null).inflate(R.layout.bold_text_view, null);
                paragraphTextView.setText(id);
                paragraphTextView.setPadding(10, 0, 10, 0);

                linearLayout.addView(paragraphTextView);
            }

            rootView.invalidate();

            super.onPostExecute(aVoid);
        }
    }
}

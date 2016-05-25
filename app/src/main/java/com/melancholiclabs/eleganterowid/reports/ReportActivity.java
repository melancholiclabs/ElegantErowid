package com.melancholiclabs.eleganterowid.reports;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.melancholiclabs.eleganterowid.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class ReportActivity extends AppCompatActivity implements ReportFragment.OnListFragmentInteractionListener {

    private static final String SHOW_ALL_SUFFIX = "&ShowViews=0&Start=0&Max=2000";

    private static final String ARG_URL = "url";
    private static final String ARG_NAME = "name";
    protected static ArrayList<Report> reports = new ArrayList<>();
    private static String mURL;
    private static String mName;
    private FetchReportsTask fetchReportsTask = new FetchReportsTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        mURL = b.getString(ARG_URL);
        mName = b.getString(ARG_NAME);

        getSupportActionBar().setTitle(mName);

        fetchReportsTask.execute();

        Fragment fragment = new ReportFragment().newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.report_container, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        System.out.println(getFragmentManager().getBackStackEntryCount());
        super.onBackPressed();
    }

    @Override
    public void onListFragmentInteraction(Report item) {
        Fragment fragment = new ExperienceFragment().newInstance(item.title, item.author, item.substance, item.date, item.url);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.report_container, fragment);
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.addToBackStack("experience");
        ft.commit();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fetchReportsTask != null && fetchReportsTask.getStatus() == AsyncTask.Status.RUNNING) {
            fetchReportsTask.cancel(true);
        }
    }

    public class Report {
        public String title;
        public String author;
        public String substance;
        public String date;
        public String url;

        public Report(String title, String author, String substance, String date, String url) {
            this.title = title;
            this.author = author;
            this.substance = substance;
            this.date = date;
            this.url = url;
        }

        @Override
        public String toString() {
            return title + ", " + author + ", " + substance + ", " + date + ", " + url;
        }
    }

    public class FetchReportsTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            reports.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(mURL).get();
                Element link = doc.select("td a").get(1);
                String fullDocUrl = link.attr("abs:href");

                Document fullDoc = Jsoup.connect(fullDocUrl + SHOW_ALL_SUFFIX).get();

                Element table = fullDoc.select("table table").get(0);
                Elements rows = table.select("tr[class]");
                for (Element element : rows) {
                    String title = element.select("td").get(1).text();
                    String author = element.select("td").get(2).text();
                    String substance = element.select("td").get(3).text();
                    String date = element.select("td").get(4).text();
                    String url = element.select("td").get(1).select("a[href]").attr("abs:href").toString();
                    reports.add(new Report(title, author, substance, date, url));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            MaterialProgressBar materialProgressBar = (MaterialProgressBar) findViewById(R.id.report_progress_bar);
            materialProgressBar.setVisibility(View.GONE);

            ReportFragment.myReportRecyclerViewAdapter.notifyDataSetChanged();
        }
    }
}

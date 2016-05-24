package com.melancholiclabs.eleganterowid.reports;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melancholiclabs.eleganterowid.NavigationActivity.Report;
import com.melancholiclabs.eleganterowid.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ReportFragment extends Fragment {

    private static final String SHOW_ALL_SUFFIX = "&ShowViews=0&Start=0&Max=1000";

    private static final String ARG_URL = "url";
    public static MyReportRecyclerViewAdapter myReportRecyclerViewAdapter;
    private static String mURL;
    private static ArrayList<Report> reports = new ArrayList<>();
    private OnListFragmentInteractionListener mListener;
    private FetchReportsTask fetchReportsTask = new FetchReportsTask();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ReportFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ReportFragment newInstance(String url) {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mURL = getArguments().getString(ARG_URL);
        }
        fetchReportsTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_report_list, container, false);

        Context context = myView.getContext();
        RecyclerView recyclerView = (RecyclerView) myView.findViewById(R.id.report_list);
        VerticalRecyclerViewFastScroller fastScroller = (VerticalRecyclerViewFastScroller) myView.findViewById(R.id.report_scroller);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        recyclerView.setOnScrollListener(fastScroller.getOnScrollListener());

        myReportRecyclerViewAdapter = new MyReportRecyclerViewAdapter(reports, mListener);
        recyclerView.setAdapter(myReportRecyclerViewAdapter);

        return myView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Report item);
    }

    public class FetchReportsTask extends AsyncTask<Void, Integer, Void> {

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
                    // reports.add(new Report(title, author, substance, date, url));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            myReportRecyclerViewAdapter.notifyDataSetChanged();
        }
    }
}

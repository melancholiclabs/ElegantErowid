package com.melancholiclabs.eleganterowid.index;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melancholiclabs.eleganterowid.NavigationActivity;
import com.melancholiclabs.eleganterowid.NavigationActivity.IndexItem;
import com.melancholiclabs.eleganterowid.R;

import java.util.ArrayList;

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IndexFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IndexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndexFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CATEGORY = "category";
    public static ArrayList<IndexItem> index = new ArrayList<>();
    public static IndexRecyclerViewAdapter indexRecyclerViewAdapter;
    // TODO: Rename and change types of parameters
    private String mCategory;
    private OnFragmentInteractionListener mListener;

    private FetchIndexTypeTask fetchIndexTypeTask = new FetchIndexTypeTask();

    public IndexFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param category Parameter 1.
     * @return A new instance of fragment IndexFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IndexFragment newInstance(String category) {
        IndexFragment fragment = new IndexFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategory = getArguments().getString(ARG_CATEGORY);
        }
        fetchIndexTypeTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_index_list, container, false);

        Context context = myView.getContext();
        RecyclerView recyclerView = (RecyclerView) myView.findViewById(R.id.list);
        VerticalRecyclerViewFastScroller fastScroller = (VerticalRecyclerViewFastScroller) myView.findViewById(R.id.fast_scroller);
        fastScroller.setRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        indexRecyclerViewAdapter = new IndexRecyclerViewAdapter(index, mListener);
        recyclerView.setAdapter(indexRecyclerViewAdapter);

        indexRecyclerViewAdapter.notifyDataSetChanged();

        return myView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStop() {
        super.onStop();

        if (fetchIndexTypeTask != null && fetchIndexTypeTask.getStatus() == AsyncTask.Status.RUNNING) {
            fetchIndexTypeTask.cancel(true);
        }
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
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(IndexItem item);
    }

    public class FetchIndexTypeTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            index.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (IndexItem item : NavigationActivity.mainIndex) {
                if (item.category.equals(mCategory)) index.add(item);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            indexRecyclerViewAdapter.notifyDataSetChanged();
        }
    }
}

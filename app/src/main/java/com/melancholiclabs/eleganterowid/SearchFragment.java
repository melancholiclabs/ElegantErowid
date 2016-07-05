package com.melancholiclabs.eleganterowid;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import com.melancholiclabs.eleganterowid.model.IndexItem;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

/**
 * Created by Melancoholic on 7/5/2016.
 */
public class SearchFragment extends Fragment {

    private static final String TAG = SearchFragment.class.getName();

    private static final ErowidDB EROWID_DB = ErowidDB.getInstance();

    private static final String ARG_QUERY = "query";
    private static final String ARG_OPTIONS = "options";

    private String mQuery;
    private boolean[] mOptions;

    private MaterialProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private VerticalRecyclerViewFastScroller mFastScroller;

    private ArrayList<IndexItem> mIndexItems;

    private OnFragmentInteractionListener mListener;

    private FetchFilteredIndexTask mFetchFilteredIndexTask;

    public SearchFragment() {
    }

    public static SearchFragment newInstance(String query, boolean[] options) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, query);
        args.putBooleanArray(ARG_OPTIONS, options);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mQuery = getArguments().getString(ARG_QUERY);
            mOptions = getArguments().getBooleanArray(ARG_OPTIONS);
        }

        mFetchFilteredIndexTask = new FetchFilteredIndexTask();
        mFetchFilteredIndexTask.execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_index_list, container, false);

        Context context = v.getContext();

        mProgressBar = (MaterialProgressBar) v.findViewById(R.id.index_progress_bar);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.index_recycler_view);
        mFastScroller = (VerticalRecyclerViewFastScroller) v.findViewById(R.id.index_fast_scroller);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mFastScroller.setRecyclerView(mRecyclerView);
        mRecyclerView.setOnScrollListener(mFastScroller.getOnScrollListener());

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
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
        Utils.gracefullyStop(mFetchFilteredIndexTask);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(IndexItem item);
    }

    public class SearchIndexRecyclerViewAdapter extends RecyclerView.Adapter<SearchIndexRecyclerViewAdapter.ViewHolder> {

        private final ArrayList<IndexItem> mValues;
        private final OnFragmentInteractionListener mListener;

        public SearchIndexRecyclerViewAdapter(ArrayList<IndexItem> items, OnFragmentInteractionListener listener) {
            mValues = items;
            mListener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_index, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mNameTextView.setText(mValues.get(position).getName());
            holder.mCaptionView.setText(mValues.get(position).getCaption());
            if (mValues.get(position).getCaption().equals(""))
                holder.mCaptionView.setVisibility(View.GONE);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onFragmentInteraction(holder.mItem);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mNameTextView;
            public final TextView mCaptionView;
            public IndexItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mNameTextView = (TextView) view.findViewById(R.id.index_name_view);
                mCaptionView = (TextView) view.findViewById(R.id.index_caption_view);
            }
        }
    }

    public class FetchFilteredIndexTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            mIndexItems = EROWID_DB.filterCustom(mQuery, mOptions);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mRecyclerView.setAdapter(new SearchIndexRecyclerViewAdapter(mIndexItems, mListener));
            mProgressBar.setVisibility(View.GONE);
        }
    }
}

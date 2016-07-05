package com.melancholiclabs.eleganterowid;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import com.melancholiclabs.eleganterowid.model.IndexItem;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

/**
 * Created by Melancoholic on 7/3/2016.
 */
public class IndexFragment extends Fragment {

    private static final String TAG = IndexFragment.class.getName();

    private static final ErowidDB EROWID_DB = ErowidDB.getInstance();

    private static final String ARG_INDEX_TYPE = "indexType";

    private String mIndexType;

    private OnFragmentInteractionListener mListener;

    private MaterialProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private VerticalRecyclerViewFastScroller mFastScroller;

    private ArrayList<IndexItem> mIndexItems;

    private LoadIndexTask mLoadIndexTask = new LoadIndexTask();

    public IndexFragment() {
    }

    public static IndexFragment newInstance(String indexType) {
        IndexFragment fragment = new IndexFragment();
        Bundle args = new Bundle();
        args.putString(ARG_INDEX_TYPE, indexType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) mIndexType = getArguments().getString(ARG_INDEX_TYPE);
        setHasOptionsMenu(true);
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

        if (mIndexType.equals(ErowidDB.Index.CHEMICALS.getIndexName())) {
            mIndexItems = EROWID_DB.getChemIndex();
            if (EROWID_DB.isChemIndexLoaded()) {
                hideProgressBar();
                mRecyclerView.setAdapter(new IndexRecyclerViewAdapter(mIndexItems, mListener));
            } else {
                mLoadIndexTask.execute();
            }
        } else if (mIndexType.equals(ErowidDB.Index.PLANTS.getIndexName())) {
            mIndexItems = EROWID_DB.getPlantIndex();
            if (EROWID_DB.isPlantIndexLoaded()) {
                hideProgressBar();
                mRecyclerView.setAdapter(new IndexRecyclerViewAdapter(mIndexItems, mListener));
            } else {
                mLoadIndexTask.execute();
            }
        } else if (mIndexType.equals(ErowidDB.Index.HERBS.getIndexName())) {
            mIndexItems = EROWID_DB.getHerbIndex();
            if (EROWID_DB.isHerbIndexLoaded()) {
                hideProgressBar();
                mRecyclerView.setAdapter(new IndexRecyclerViewAdapter(mIndexItems, mListener));
            } else {
                mLoadIndexTask.execute();
            }
        } else if (mIndexType.equals(ErowidDB.Index.PHARMS.getIndexName())) {
            mIndexItems = EROWID_DB.getPharmIndex();
            if (EROWID_DB.isPharmIndexLoaded()) {
                hideProgressBar();
                mRecyclerView.setAdapter(new IndexRecyclerViewAdapter(mIndexItems, mListener));
            } else {
                mLoadIndexTask.execute();
            }
        } else if (mIndexType.equals(ErowidDB.Index.SMARTS.getIndexName())) {
            mIndexItems = EROWID_DB.getSmartIndex();
            if (EROWID_DB.isSmartIndexLoaded()) {
                hideProgressBar();
                mRecyclerView.setAdapter(new IndexRecyclerViewAdapter(mIndexItems, mListener));
            } else {
                mLoadIndexTask.execute();
            }
        } else if (mIndexType.equals(ErowidDB.Index.ANIMALS.getIndexName())) {
            mIndexItems = EROWID_DB.getAnimalIndex();
            if (EROWID_DB.isAnimalIndexLoaded()) {
                hideProgressBar();
                mRecyclerView.setAdapter(new IndexRecyclerViewAdapter(mIndexItems, mListener));
            } else {
                mLoadIndexTask.execute();
            }
        } else if (mIndexType.equals(ErowidDB.Index.VAULT.getIndexName())) {
            mIndexItems = EROWID_DB.getVaultIndex();
            if (EROWID_DB.isVaultIndexLoaded()) {
                hideProgressBar();
                mRecyclerView.setAdapter(new IndexRecyclerViewAdapter(mIndexItems, mListener));
            } else {
                mLoadIndexTask.execute();
            }
        } else {
            Log.d(TAG, "IndexType could not be determined.");
        }

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.index_options_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.index_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("LSD; MDMA; etc...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mRecyclerView.setAdapter(new IndexRecyclerViewAdapter(EROWID_DB.filterIndex(newText, mIndexItems), mListener));
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mRecyclerView.setAdapter(new IndexRecyclerViewAdapter(mIndexItems, mListener));
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
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
        Utils.gracefullyStop(mLoadIndexTask);
    }

    /**
     * Hides the progressBar.
     */
    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(IndexItem item);
    }

    public class IndexRecyclerViewAdapter extends RecyclerView.Adapter<IndexRecyclerViewAdapter.ViewHolder> {

        private final ArrayList<IndexItem> mValues;
        private final OnFragmentInteractionListener mListener;

        public IndexRecyclerViewAdapter(ArrayList<IndexItem> items, OnFragmentInteractionListener listener) {
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

    /**
     * Waits for the index to load.
     */
    public class LoadIndexTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            if (mIndexType.equals(ErowidDB.Index.CHEMICALS.getIndexName())) {
                while (!EROWID_DB.isChemIndexLoaded());
            } else if (mIndexType.equals(ErowidDB.Index.PLANTS.getIndexName())) {
                while (!EROWID_DB.isPlantIndexLoaded());
            } else if (mIndexType.equals(ErowidDB.Index.HERBS.getIndexName())) {
                while (!EROWID_DB.isHerbIndexLoaded());
            } else if (mIndexType.equals(ErowidDB.Index.PHARMS.getIndexName())) {
                while (!EROWID_DB.isPharmIndexLoaded());
            } else if (mIndexType.equals(ErowidDB.Index.SMARTS.getIndexName())) {
                while (!EROWID_DB.isSmartIndexLoaded());
            } else if (mIndexType.equals(ErowidDB.Index.ANIMALS.getIndexName())) {
                while (!EROWID_DB.isAnimalIndexLoaded());
            } else if (mIndexType.equals(ErowidDB.Index.VAULT.getIndexName())) {
                while (!EROWID_DB.isVaultIndexLoaded());
            } else {
                Log.d(TAG, "IndexType could not be determined.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mRecyclerView.setAdapter(new IndexRecyclerViewAdapter(mIndexItems, mListener));
            hideProgressBar();
        }
    }
}

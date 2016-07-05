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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import com.melancholiclabs.eleganterowid.model.VaultIndexItem;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

/**
 * Created by Melancoholic on 7/4/2016.
 */
public class VaultIndexFragment extends Fragment {

    private static final String TAG = VaultIndexFragment.class.getName();

    private static final ErowidDB EROWID_DB = ErowidDB.getInstance();

    private OnFragmentInteractionListener mListener;

    private MaterialProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private VerticalRecyclerViewFastScroller mFastScroller;

    private ArrayList<VaultIndexItem> mVaultIndexItems;

    private GetReportIndexTask mGetReportIndexTask;

    public VaultIndexFragment() {
    }

    public static VaultIndexFragment newInstance() {
        VaultIndexFragment fragment = new VaultIndexFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        mVaultIndexItems = EROWID_DB.getReportIndex();

        mGetReportIndexTask = new GetReportIndexTask();
        mGetReportIndexTask.execute();

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
                mRecyclerView.setAdapter(new VaultIndexRecyclerViewAdapter(EROWID_DB.filterReportIndex(newText, mVaultIndexItems), mListener));
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mRecyclerView.setAdapter(new VaultIndexRecyclerViewAdapter(mVaultIndexItems, mListener));
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
        Utils.gracefullyStop(mGetReportIndexTask);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(VaultIndexItem item);
    }

    public class VaultIndexRecyclerViewAdapter extends RecyclerView.Adapter<VaultIndexRecyclerViewAdapter.ViewHolder> {

        private final ArrayList<VaultIndexItem> mValues;
        private final OnFragmentInteractionListener mListener;

        public VaultIndexRecyclerViewAdapter(ArrayList<VaultIndexItem> items, OnFragmentInteractionListener listener) {
            mValues = items;
            mListener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_vault_index, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mTitleView.setText(mValues.get(position).getTitle());
            holder.mAuthorView.setText(mValues.get(position).getAuthor());
            holder.mSubstanceView.setText(mValues.get(position).getSubstance());
            holder.mDateView.setText(mValues.get(position).getDate());
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
            public final TextView mTitleView;
            public final TextView mAuthorView;
            public final TextView mSubstanceView;
            public final TextView mDateView;
            public VaultIndexItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTitleView = (TextView) view.findViewById(R.id.vault_index_title_view);
                mAuthorView = (TextView) view.findViewById(R.id.vault_index_author_view);
                mSubstanceView = (TextView) view.findViewById(R.id.vault_index_substance_view);
                mDateView = (TextView) view.findViewById(R.id.vault_index_date_view);
            }
        }
    }

    /**
     * Hides the progressBar.
     */
    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    public class GetReportIndexTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            while (!EROWID_DB.isReportIndexLoaded()) ;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mRecyclerView.setAdapter(new VaultIndexRecyclerViewAdapter(mVaultIndexItems, mListener));
            hideProgressBar();
        }
    }
}

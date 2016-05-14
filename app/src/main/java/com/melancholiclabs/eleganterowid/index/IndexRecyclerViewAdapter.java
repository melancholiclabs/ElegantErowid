package com.melancholiclabs.eleganterowid.index;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.melancholiclabs.eleganterowid.NavigationActivity.IndexItem;
import com.melancholiclabs.eleganterowid.R;

import java.util.List;

/**
 * Created by Melancoholic on 5/13/2016.
 */
public class IndexRecyclerViewAdapter extends RecyclerView.Adapter<IndexRecyclerViewAdapter.ViewHolder> {

    private final List<IndexItem> mValues;
    private final IndexFragment.OnFragmentInteractionListener mListener;

    public IndexRecyclerViewAdapter(List<IndexItem> items, IndexFragment.OnFragmentInteractionListener listener) {
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
        holder.mSubstanceNameView.setText(mValues.get(position).name);
        holder.mSubstanceCaptionView.setText(mValues.get(position).caption);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
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
        public final TextView mSubstanceNameView;
        public final TextView mSubstanceCaptionView;
        public IndexItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mSubstanceNameView = (TextView) view.findViewById(R.id.index_name_view);
            mSubstanceCaptionView = (TextView) view.findViewById(R.id.index_caption_view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mSubstanceNameView.getText() + "'";
        }
    }
}

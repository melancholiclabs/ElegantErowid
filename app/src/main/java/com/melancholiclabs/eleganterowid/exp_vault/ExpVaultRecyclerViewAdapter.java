package com.melancholiclabs.eleganterowid.exp_vault;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.melancholiclabs.eleganterowid.NavigationActivity.Substance;
import com.melancholiclabs.eleganterowid.R;

import java.util.List;

/**
 * Created by Melancoholic on 5/22/2016.
 */
public class ExpVaultRecyclerViewAdapter extends RecyclerView.Adapter<ExpVaultRecyclerViewAdapter.ViewHolder> {

    private final List<Substance> mValues;
    private final ExpVaultFragment.OnFragmentInteractionListener mListener;

    public ExpVaultRecyclerViewAdapter(List<Substance> items, ExpVaultFragment.OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_exp_vault, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mSubstanceNameView.setText(mValues.get(position).name);
        if (mValues.get(position).caption.equals(""))
            holder.mSubstanceCaptionView.setVisibility(View.GONE);
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
        public Substance mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mSubstanceNameView = (TextView) view.findViewById(R.id.vault_name_view);
            mSubstanceCaptionView = (TextView) view.findViewById(R.id.vault_caption_view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mSubstanceNameView.getText() + "'";
        }
    }
}

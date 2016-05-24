package com.melancholiclabs.eleganterowid.reports;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.melancholiclabs.eleganterowid.NavigationActivity.Report;
import com.melancholiclabs.eleganterowid.R;
import com.melancholiclabs.eleganterowid.reports.ReportFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Report} and makes a call to the
 * specified {@link ReportFragment.OnListFragmentInteractionListener).
 * TODO: Replace the implementation with code for your data type.
 */
public class MyReportRecyclerViewAdapter extends RecyclerView.Adapter<MyReportRecyclerViewAdapter.ViewHolder> {

    private final List<Report> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyReportRecyclerViewAdapter(List<Report> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).title);
        holder.mAuthorView.setText(mValues.get(position).author);
        holder.mSubstanceView.setText(mValues.get(position).substance);
        holder.mDateView.setText(mValues.get(position).date);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
        public Report mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.report_title_view);
            mAuthorView = (TextView) view.findViewById(R.id.report_author_view);
            mSubstanceView = (TextView) view.findViewById(R.id.report_substance_view);
            mDateView = (TextView) view.findViewById(R.id.report_date_view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}

package com.melancholiclabs.eleganterowid.exp_vault;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melancholiclabs.eleganterowid.NavigationActivity;
import com.melancholiclabs.eleganterowid.NavigationActivity.Substance;
import com.melancholiclabs.eleganterowid.R;

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

/**
 * A placeholder fragment containing a simple view.
 */
public class ExpVaultFragment extends Fragment {

    public static ExpVaultRecyclerViewAdapter expVaultRecyclerViewAdapter;
    private OnFragmentInteractionListener mListener;

    public ExpVaultFragment() {
    }

    public static ExpVaultFragment newInstance() {
        ExpVaultFragment fragment = new ExpVaultFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_vault_list, container, false);

        Context context = myView.getContext();
        RecyclerView recyclerView = (RecyclerView) myView.findViewById(R.id.vault_list);
        VerticalRecyclerViewFastScroller fastScroller = (VerticalRecyclerViewFastScroller) myView.findViewById(R.id.vault_scroller);
        fastScroller.setRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        expVaultRecyclerViewAdapter = new ExpVaultRecyclerViewAdapter(NavigationActivity.vaultList, mListener);
        recyclerView.setAdapter(expVaultRecyclerViewAdapter);

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
        void onFragmentInteraction(Substance item);
    }
}

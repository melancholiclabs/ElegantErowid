package com.melancholiclabs.eleganterowid.substance;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melancholiclabs.eleganterowid.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubstanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubstanceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "id";
    private static final String ARG_NAME = "name";
    private static final String ARG_CATEGORY = "category";

    // TODO: Rename and change types of parameters
    private String mId;
    private String mName;
    private String mCategory;

    public SubstanceFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id       Parameter 1.
     * @param name     Parameter 2.
     * @param category Parameter 3.
     * @return A new instance of fragment SubstanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubstanceFragment newInstance(String id, String name, String category) {
        SubstanceFragment fragment = new SubstanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        args.putString(ARG_NAME, name);
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getString(ARG_ID);
            mName = getArguments().getString(ARG_NAME);
            mCategory = getArguments().getString(ARG_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_substance, container, false);
    }

}

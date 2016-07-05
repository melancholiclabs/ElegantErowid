package com.melancholiclabs.eleganterowid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.melancholiclabs.eleganterowid.model.IndexItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getName();

    private static final ErowidDB EROWID_DB = ErowidDB.getInstance();

    private SearchView mSearchView;

    private CheckBox mSubstancesCheckBox;
    private CheckBox mChemicalsCheckBox;
    private CheckBox mPlantsCheckBox;
    private CheckBox mHerbsCheckBox;
    private CheckBox mPharmsCheckBox;
    private CheckBox mSmartsCheckBox;
    private CheckBox mAnimalsCheckBox;
    private CheckBox mVaultCheckBox;

    private Button mAlcoholButton;
    private Button mCannabisButton;
    private Button mCocaineButton;
    private Button mDmtButton;
    private Button mHeroinButton;
    private Button mKetamineButton;
    private Button mLsdButton;
    private Button mMdmaButton;
    private Button mMethButton;
    private Button mMushroomsButton;

    private OnFragmentInteractionListener mListener;

    /**
     * Empty Constructor
     */
    public HomeFragment() {
    }

    /**
     * Returns a new instance of the HomeFragment
     *
     * @return HomeFragment instance
     */
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mSearchView = (SearchView) v.findViewById(R.id.home_search_view);
        mSearchView.setQueryHint("LSD; MDMA; etc...");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (mListener != null) mListener.onFragmentInteraction(query, getOptions());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mSubstancesCheckBox = (CheckBox) v.findViewById(R.id.checkbox_substances);
        mChemicalsCheckBox = (CheckBox) v.findViewById(R.id.checkbox_chems);
        mPlantsCheckBox = (CheckBox) v.findViewById(R.id.checkbox_plants);
        mHerbsCheckBox = (CheckBox) v.findViewById(R.id.checkbox_herbs);
        mPharmsCheckBox = (CheckBox) v.findViewById(R.id.checkbox_pharms);
        mSmartsCheckBox = (CheckBox) v.findViewById(R.id.checkbox_smarts);
        mAnimalsCheckBox = (CheckBox) v.findViewById(R.id.checkbox_animals);
        mVaultCheckBox = (CheckBox) v.findViewById(R.id.checkbox_vault);

        mAlcoholButton = (Button) v.findViewById(R.id.alcohol_button);
        mCannabisButton = (Button) v.findViewById(R.id.cannabis_button);
        mCocaineButton = (Button) v.findViewById(R.id.cocaine_button);
        mDmtButton = (Button) v.findViewById(R.id.dmt_button);
        mHeroinButton = (Button) v.findViewById(R.id.heroin_button);
        mKetamineButton = (Button) v.findViewById(R.id.ketamine_button);
        mLsdButton = (Button) v.findViewById(R.id.lsd_button);
        mMdmaButton = (Button) v.findViewById(R.id.mdma_button);
        mMethButton = (Button) v.findViewById(R.id.meth_button);
        mMushroomsButton = (Button) v.findViewById(R.id.mushrooms_button);

        mSubstancesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mChemicalsCheckBox.setChecked(true);
                    mPlantsCheckBox.setChecked(true);
                    mHerbsCheckBox.setChecked(true);
                    mPharmsCheckBox.setChecked(true);
                    mSmartsCheckBox.setChecked(true);
                    mAnimalsCheckBox.setChecked(true);
                    mVaultCheckBox.setChecked(false);
                }
            }
        });
        mChemicalsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    mSubstancesCheckBox.setChecked(false);
                }
            }
        });
        mPlantsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    mSubstancesCheckBox.setChecked(false);
                }
            }
        });
        mHerbsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    mSubstancesCheckBox.setChecked(false);
                }
            }
        });
        mPharmsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    mSubstancesCheckBox.setChecked(false);
                }
            }
        });
        mSmartsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    mSubstancesCheckBox.setChecked(false);
                }
            }
        });
        mAnimalsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    mSubstancesCheckBox.setChecked(false);
                }
            }
        });
        mVaultCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSubstancesCheckBox.setChecked(false);
                    mChemicalsCheckBox.setChecked(false);
                    mPlantsCheckBox.setChecked(false);
                    mHerbsCheckBox.setChecked(false);
                    mPharmsCheckBox.setChecked(false);
                    mSmartsCheckBox.setChecked(false);
                    mAnimalsCheckBox.setChecked(false);
                }
            }
        });

        mSubstancesCheckBox.setChecked(true);

        mAlcoholButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndexItem item = EROWID_DB.findSubstance("Alcohol");
                if (item != null) {
                    Intent intent = SubstanceActivity.newIntent(getContext(), item);
                    startActivity(intent);
                }
            }
        });
        mCannabisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndexItem item = EROWID_DB.findSubstance("Cannabis");
                if (item != null) {
                    Intent intent = SubstanceActivity.newIntent(getContext(), item);
                    startActivity(intent);
                }
            }
        });
        mCocaineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndexItem item = EROWID_DB.findSubstance("Cocaine & Crack");
                if (item != null) {
                    Intent intent = SubstanceActivity.newIntent(getContext(), item);
                    startActivity(intent);
                }
            }
        });
        mDmtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndexItem item = EROWID_DB.findSubstance("N,N-DMT");
                if (item != null) {
                    Intent intent = SubstanceActivity.newIntent(getContext(), item);
                    startActivity(intent);
                }
            }
        });
        mHeroinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndexItem item = EROWID_DB.findSubstance("Heroin");
                if (item != null) {
                    Intent intent = SubstanceActivity.newIntent(getContext(), item);
                    startActivity(intent);
                }
            }
        });
        mKetamineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndexItem item = EROWID_DB.findSubstance("Ketamine");
                if (item != null) {
                    Intent intent = SubstanceActivity.newIntent(getContext(), item);
                    startActivity(intent);
                }
            }
        });
        mLsdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndexItem item = EROWID_DB.findSubstance("LSD-25");
                if (item != null) {
                    Intent intent = SubstanceActivity.newIntent(getContext(), item);
                    startActivity(intent);
                }
            }
        });
        mMdmaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndexItem item = EROWID_DB.findSubstance("MDMA");
                if (item != null) {
                    Intent intent = SubstanceActivity.newIntent(getContext(), item);
                    startActivity(intent);
                }
            }
        });
        mMethButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndexItem item = EROWID_DB.findSubstance("Methamphetamine");
                if (item != null) {
                    Intent intent = SubstanceActivity.newIntent(getContext(), item);
                    startActivity(intent);
                }
            }
        });
        mMushroomsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndexItem item = EROWID_DB.findSubstance("Psilocybin Mushrooms");
                if (item != null) {
                    Intent intent = SubstanceActivity.newIntent(getContext(), item);
                    startActivity(intent);
                }
            }
        });

        return v;
    }

    private boolean[] getOptions() {
        boolean[] options = new boolean[7];

        if (mChemicalsCheckBox.isChecked()) options[0] = true;
        if (mPlantsCheckBox.isChecked()) options[1] = true;
        if (mHerbsCheckBox.isChecked()) options[2] = true;
        if (mPharmsCheckBox.isChecked()) options[3] = true;
        if (mSmartsCheckBox.isChecked()) options[4] = true;
        if (mAnimalsCheckBox.isChecked()) options[5] = true;
        if (mVaultCheckBox.isChecked()) options[6] = true;

        return options;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home_theme) {
            if (Utils.sTheme == Utils.THEME_DEFAULT) {
                Utils.changeToTheme(getActivity(), Utils.THEME_EXTRA);
            } else {
                Utils.changeToTheme(getActivity(), Utils.THEME_DEFAULT);
            }
        }

        return super.onOptionsItemSelected(item);
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String query, boolean[] options);
    }
}

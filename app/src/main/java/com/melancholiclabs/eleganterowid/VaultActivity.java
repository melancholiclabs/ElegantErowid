package com.melancholiclabs.eleganterowid;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.melancholiclabs.eleganterowid.model.IndexItem;
import com.melancholiclabs.eleganterowid.model.VaultIndexItem;

public class VaultActivity extends AppCompatActivity implements VaultIndexFragment.OnFragmentInteractionListener {

    private static final String TAG = VaultActivity.class.getName();

    private static final ErowidDB EROWID_DB = ErowidDB.getInstance();

    private static final String ARG_NAME = "name";
    private static final String ARG_URL = "url";
    private static final String ARG_INDEX_TYPE = "indexType";

    private String mName;
    private String mUrl;
    private String mIndexType;

    private LoadReportIndexTask mLoadReportIndexTask;

    public static Intent newIntent(Context packageContext, IndexItem item) {
        Intent intent = new Intent(packageContext, VaultActivity.class);
        intent.putExtra(ARG_NAME, item.getName());
        intent.putExtra(ARG_URL, item.getUrl());
        intent.putExtra(ARG_INDEX_TYPE, "reportIndex");
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault);

        mName = getIntent().getStringExtra(ARG_NAME);
        mUrl = getIntent().getStringExtra(ARG_URL);
        mIndexType = getIntent().getStringExtra(ARG_INDEX_TYPE);

        mLoadReportIndexTask = new LoadReportIndexTask();
        mLoadReportIndexTask.execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mName);

        Fragment fragment = VaultIndexFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(VaultIndexItem item) {
        Fragment fragment = ReportFragment.newInstance(item, mName, mUrl, mIndexType);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.addToBackStack("report");
        ft.commit();
    }

    public class LoadReportIndexTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            EROWID_DB.loadReportIndex(new IndexItem(mName, null, mUrl, mIndexType));
            return null;
        }
    }
}

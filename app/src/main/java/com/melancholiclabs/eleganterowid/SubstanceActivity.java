package com.melancholiclabs.eleganterowid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.melancholiclabs.eleganterowid.model.IndexItem;

import java.util.ArrayList;

public class SubstanceActivity extends AppCompatActivity {

    public static final ErowidDB EROWID_DB = ErowidDB.getInstance();
    public static final int MAIN = 0;
    public static final int BASICS = 1;
    public static final int EFFECTS = 2;
    public static final int IMAGES = 3;
    public static final int HEALTH = 4;
    public static final int LAW = 5;
    public static final int DOSE = 6;
    public static final int CHEMISTRY = 7;
    public static final int RESEARCH_CHEMICAL = 8;
    private static final String TAG = SubstanceActivity.class.getName();
    private static final String ARG_ID = "id";
    private static final String ARG_NAME = "name";
    private static final String ARG_CAPTION = "caption";
    private static final String ARG_CATEGORY = "category";
    private static final String ARG_PAGES = "pages";
    private static final String ARG_URL = "url";
    private static final String ARG_INDEX_TYPE = "indexType";

    private String mId;
    private String mName;
    private String mCaption;
    private String mCategory;
    private String[] mPages;
    private String mUrl;
    private String mIndexType;

    private ArrayList<String> mPageTitles = new ArrayList<>();
    
    private IndexItem mVaultMatch;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private Menu mMenu;

    private FetchMatchingVaultItemTask mFetchMatchingVaultItemTask;

    public static Intent newIntent(Context packageContext, IndexItem item) {
        Intent intent = new Intent(packageContext, SubstanceActivity.class);
        intent.putExtra(ARG_ID, item.getId());
        intent.putExtra(ARG_NAME, item.getName());
        intent.putExtra(ARG_CAPTION, item.getCaption());
        intent.putExtra(ARG_CATEGORY, item.getCategory());
        intent.putExtra(ARG_PAGES, item.getPages());
        intent.putExtra(ARG_URL, item.getUrl());
        intent.putExtra(ARG_INDEX_TYPE, item.getIndexType());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_substance);

        mId = getIntent().getStringExtra(ARG_ID);
        mName = getIntent().getStringExtra(ARG_NAME);
        mCaption = getIntent().getStringExtra(ARG_CAPTION);
        mCategory = getIntent().getStringExtra(ARG_CATEGORY);
        mPages = getIntent().getStringArrayExtra(ARG_PAGES);
        mUrl = getIntent().getStringExtra(ARG_URL);
        mIndexType = getIntent().getStringExtra(ARG_INDEX_TYPE);

        mPageTitles.add("MAIN");
        for (String url : mPages) {
            if (url.contains("basics")) mPageTitles.add("BASICS");
            if (url.contains("effects")) mPageTitles.add("EFFECTS");
            if (url.contains("images")) mPageTitles.add("IMAGES");
            if (url.contains("health")) mPageTitles.add("HEALTH");
            if (url.contains("law")) mPageTitles.add("LAW");
            if (url.contains("dose")) mPageTitles.add("DOSE");
            if (url.contains("chemistry")) mPageTitles.add("CHEMISTRY");
            if (url.contains("research_chems")) mPageTitles.add("RESEARCH CHEMICAL");
            Log.d(TAG, url);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mName);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(8);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_substance, menu);

        MenuItem item = mMenu.findItem(R.id.action_substance_vault);
        item.getIcon().setAlpha(130);
        item.setCheckable(false);

        mFetchMatchingVaultItemTask = new FetchMatchingVaultItemTask();
        mFetchMatchingVaultItemTask.execute();

        return true;
    }

    private void showVaultOption() {
        Log.d(TAG, "showVaultOption is called");
        MenuItem item = mMenu.findItem(R.id.action_substance_vault);
        item.getIcon().setAlpha(255);
        item.setCheckable(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_substance_vault) {
            Log.d(TAG, "Action \"substance_vault\" was selected");
            if (item.isCheckable()) {
                Intent intent = VaultActivity.newIntent(this, mVaultMatch);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Elegant Erowid could not match this substance to an experience vault item.", Toast.LENGTH_SHORT).show();
            }
            // TODO overridePendingAnimation
            return true;
        }

        if (id == R.id.action_substance_open_in_browser) {
            Log.d(TAG, "Action \"open_in_browser\" was selected");
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
            startActivity(browserIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Utils.gracefullyStop(mFetchMatchingVaultItemTask);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (mPageTitles.get(position).equals("MAIN"))
                return SubstanceFragment.newInstance(MAIN, mId, mCategory, mPages);
            if (mPageTitles.get(position).equals("BASICS"))
                return SubstanceFragment.newInstance(BASICS, mId, mCategory, mPages);
            if (mPageTitles.get(position).equals("EFFECTS"))
                return SubstanceFragment.newInstance(EFFECTS, mId, mCategory, mPages);
            if (mPageTitles.get(position).equals("IMAGES"))
                return SubstanceFragment.newInstance(IMAGES, mId, mCategory, mPages);
            if (mPageTitles.get(position).equals("HEALTH"))
                return SubstanceFragment.newInstance(HEALTH, mId, mCategory, mPages);
            if (mPageTitles.get(position).equals("LAW"))
                return SubstanceFragment.newInstance(LAW, mId, mCategory, mPages);
            if (mPageTitles.get(position).equals("DOSE"))
                return SubstanceFragment.newInstance(DOSE, mId, mCategory, mPages);
            if (mPageTitles.get(position).equals("CHEMISTRY"))
                return SubstanceFragment.newInstance(CHEMISTRY, mId, mCategory, mPages);
            if (mPageTitles.get(position).equals("RESEARCH CHEMICAL"))
                return SubstanceFragment.newInstance(RESEARCH_CHEMICAL, mId, mCategory, mPages);
            return null;
        }

        @Override
        public int getCount() {
            return mPageTitles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPageTitles.get(position);
        }
    }

    /**
     * Attempts to find a matching vault IndexItem and shows the menu item if available.
     */
    public class FetchMatchingVaultItemTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "FetchMatchingVaultItemTask is launched");
            mVaultMatch = EROWID_DB.findVaultMatch(new IndexItem(mId, mName, mCaption, mCategory, mPages, mUrl, mIndexType));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (mVaultMatch != null) showVaultOption();
        }
    }
}

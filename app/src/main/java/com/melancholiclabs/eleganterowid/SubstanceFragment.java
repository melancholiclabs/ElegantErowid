package com.melancholiclabs.eleganterowid;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.melancholiclabs.eleganterowid.model.IndexItem;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by Melancoholic on 7/3/2016.
 */
public class SubstanceFragment extends Fragment {

    private static final String TAG = SubstanceFragment.class.getName();

    private static final ErowidDB EROWID_DB = SubstanceActivity.EROWID_DB;

    private static final String ARG_PAGE = "page";
    private static final String ARG_ID = "id";
    private static final String ARG_CATEGORY = "category";
    private static final String ARG_PAGES = "pages";

    private int mPage;
    private String mId;
    private String mCategory;
    private String[] mPages;

    private LinearLayout mLinearLayout;
    private MaterialProgressBar mProgressBar;

    private LoadMainPageTask mLoadMainPageTask;
    private LoadBasicsPageTask mLoadBasicsPageTask;
    private LoadEffectsPageTask mLoadEffectsPageTask;
    private LoadImagesPageTask mLoadImagesPageTask;
    private LoadHealthPageTask mLoadHealthPageTask;
    private LoadLawPageTask mLoadLawPageTask;
    private LoadDosePageTask mLoadDosePageTask;
    private LoadChemistryPageTask mLoadChemistryPageTask;
    private LoadResearchChemicalPageTask mLoadResearchChemicalPageTask;

    public SubstanceFragment() {
    }

    public static SubstanceFragment newInstance(int page, String id, String category, String[] pages) {
        SubstanceFragment fragment = new SubstanceFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString(ARG_ID, id);
        args.putString(ARG_CATEGORY, category);
        args.putStringArray(ARG_PAGES, pages);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPage = getArguments().getInt(ARG_PAGE);
        mId = getArguments().getString(ARG_ID);
        mCategory = getArguments().getString(ARG_CATEGORY);
        mPages = getArguments().getStringArray(ARG_PAGES);

        switch (mPage) {
            case 0:
                mLoadMainPageTask = new LoadMainPageTask();
                mLoadMainPageTask.execute();
                break;
            case 1:
                mLoadBasicsPageTask = new LoadBasicsPageTask();
                mLoadBasicsPageTask.execute();
                break;
            case 2:
                mLoadEffectsPageTask = new LoadEffectsPageTask();
                mLoadEffectsPageTask.execute();
                break;
            case 3:
                mLoadImagesPageTask = new LoadImagesPageTask();
                mLoadImagesPageTask.execute();
                break;
            case 4:
                mLoadHealthPageTask = new LoadHealthPageTask();
                mLoadHealthPageTask.execute();
                break;
            case 5:
                mLoadLawPageTask = new LoadLawPageTask();
                mLoadLawPageTask.execute();
                break;
            case 6:
                mLoadDosePageTask = new LoadDosePageTask();
                mLoadDosePageTask.execute();
                break;
            case 7:
                mLoadChemistryPageTask = new LoadChemistryPageTask();
                mLoadChemistryPageTask.execute();
                break;
            case 8:
                mLoadResearchChemicalPageTask = new LoadResearchChemicalPageTask();
                mLoadResearchChemicalPageTask.execute();
                break;
            default:
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_substance, container, false);

        mLinearLayout = (LinearLayout) v.findViewById(R.id.substance_linear_layout);
        mProgressBar = (MaterialProgressBar) v.findViewById(R.id.substance_progress_bar);

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        Utils.gracefullyStop(mLoadMainPageTask);
        Utils.gracefullyStop(mLoadBasicsPageTask);
        Utils.gracefullyStop(mLoadEffectsPageTask);
        Utils.gracefullyStop(mLoadImagesPageTask);
        Utils.gracefullyStop(mLoadHealthPageTask);
        Utils.gracefullyStop(mLoadLawPageTask);
        Utils.gracefullyStop(mLoadDosePageTask);
        Utils.gracefullyStop(mLoadChemistryPageTask);
        Utils.gracefullyStop(mLoadResearchChemicalPageTask);
    }

    /**
     * Creates a Title TextView and Paragraph TextView then adds them to the LinearLayout.
     *
     * @param label title text and log tag
     * @param text  paragraph text
     */
    private void addSection(final String label, final String text) {
        TextView titleTextView = (TextView) getLayoutInflater(null).inflate(R.layout.title_text_view, null);
        titleTextView.setText(label);

        TextView paragraphTextView = (TextView) getLayoutInflater(null).inflate(R.layout.paragraph_text_view, null);
        paragraphTextView.setText(text);
        paragraphTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyTextToClipboard(label, text);
                return true;
            }
        });

        mLinearLayout.addView(titleTextView);
        mLinearLayout.addView(paragraphTextView);
    }

    /**
     * Creates a Title TextView and Paragraph TextView then adds them to the LinearLayout.
     *
     * @param label title text and log tag
     * @param text  paragraph text
     */
    private void addSection(final String label, final Spanned text) {
        TextView titleTextView = (TextView) getLayoutInflater(null).inflate(R.layout.title_text_view, null);
        titleTextView.setText(label);

        TextView paragraphTextView = (TextView) getLayoutInflater(null).inflate(R.layout.paragraph_text_view, null);
        paragraphTextView.setText(text);
        paragraphTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyTextToClipboard(label, text.toString());
                return true;
            }
        });

        mLinearLayout.addView(titleTextView);
        mLinearLayout.addView(paragraphTextView);
    }

    /**
     * Creates a TableLayout from a given string and adds it to the LinearLayout.
     *
     * @param table string to parse into a TableLayout
     */
    private void addTable(String label, String table) {
        TextView titleTextView = (TextView) getLayoutInflater(null).inflate(R.layout.title_text_view, null);
        titleTextView.setText(label);

        Context context = getContext();
        if (context == null) return;

        TableLayout tableLayout = new TableLayout(context);
        tableLayout.setShrinkAllColumns(true);

        Scanner scanner = new Scanner(table);
        ArrayList<TableRow> rows = new ArrayList<>();
        while (scanner.hasNextLine()) {
            TableRow tableRow = new TableRow(context);
            tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.setBackgroundColor(Color.LTGRAY);

            Scanner lineScanner = new Scanner(scanner.nextLine());
            lineScanner.useDelimiter("\t");
            ArrayList<TextView> columns = new ArrayList<>();
            while (lineScanner.hasNext()) {
                TextView textView = new TextView(context);
                textView.setText(lineScanner.next());
                textView.setTextColor(Color.BLACK);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setBackgroundColor(Color.WHITE);
                columns.add(textView);
            }

            tableRow.setWeightSum((float) columns.size());
            for (TextView textView : columns) {
                TableRow.LayoutParams params = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                params.setMargins(2, 2, 2, 2);
                textView.setLayoutParams(params);
                tableRow.addView(textView);
            }

            rows.add(tableRow);
        }

        tableLayout.setWeightSum(rows.size());
        for (TableRow tableRow : rows) {
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1f);
            params.setMargins(2, 2, 2, 2);
            tableRow.setLayoutParams(params);
            tableLayout.addView(tableRow);
        }

        mLinearLayout.addView(titleTextView);
        mLinearLayout.addView(tableLayout);
    }

    /**
     * Creates an ImageView from a url and adds it to the LinearLayout.
     *
     * @param url url of the image
     */
    private void addImage(String url) {
        if (url != null) {
            final ImageView imageView = new ImageView(getContext());

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.height = (mLinearLayout.getWidth() * 2) / 3;

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
            imageLoader.loadImage(url, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    // Transition drawable with a transparent drawable and the final bitmap
                    final TransitionDrawable td =
                            new TransitionDrawable(new Drawable[]{
                                    new ColorDrawable(Color.TRANSPARENT),
                                    new BitmapDrawable(loadedImage)
                            });
                    // Set background to loading bitmap
                    // substanceImageView.setBackgroundDrawable(
                    //        new BitmapDrawable(mResources, mLoadingBitmap));

                    imageView.setImageDrawable(td);
                    td.startTransition(100);
                    super.onLoadingComplete(imageUri, view, loadedImage);
                }
            });

            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            mLinearLayout.addView(imageView);
        }
    }

    /**
     * Creates a Bulleted list from a given string with tab delimitation.
     *
     * @param text string from which to make a bulleted list
     * @return Spanned bulleted list
     */
    private Spanned toBulletedList(String text) {
        Scanner scanner = new Scanner(text);
        scanner.useDelimiter("\t");

        StringBuilder builder = new StringBuilder();
        while (scanner.hasNext()) builder.append("&#8226; " + scanner.next() + "<br/>");
        builder.setLength(builder.length() - 1);

        return Html.fromHtml(builder.toString());
    }

    /**
     * Formats a string by replacing new line characters with two html line breaks
     *
     * @param text string to be formatted
     * @return Spanned formatted string
     */
    private Spanned toParagraph(String text) {
        return Html.fromHtml(text.replace("\n", "<br/><br/>"));
    }

    /**
     * Copies text to the clipboard and displays a toast describing the aciton.
     *
     * @param label description of the text for the toast
     * @param text  text to be copied to the clipboard
     */
    private void copyTextToClipboard(String label, String text) {
        ClipboardManager clipBoard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(label, text);
        clipBoard.setPrimaryClip(clipData);
        Toast.makeText(getActivity(), "Copied " + label + " to the clipboard.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Loads the Main page of the current Substance object and creates basic views for each field.
     */
    public class LoadMainPageTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            EROWID_DB.loadSubstance(new IndexItem(mId, mCategory, mPages));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressBar.setVisibility(View.GONE);

            final String imageUrl = EROWID_DB.getSubstance().getImageUrl();
            final String effectsClassification = EROWID_DB.getSubstance().getEffectsClassification();
            final String botanicalClassification = EROWID_DB.getSubstance().getBotanicalClassification();
            final String chemicalName = EROWID_DB.getSubstance().getChemicalName();
            final String commonNames = EROWID_DB.getSubstance().getCommonNames();
            final String uses = EROWID_DB.getSubstance().getUses();
            final String description = EROWID_DB.getSubstance().getDescription();


            if (imageUrl != null) addImage(imageUrl);
            if (effectsClassification != null)
                addSection("Effects Classification", effectsClassification);
            if (botanicalClassification != null)
                addSection("Botanical Classification", botanicalClassification);
            if (chemicalName != null) addSection("Chemical Name", chemicalName);
            if (commonNames != null) addSection("Common Names", commonNames);
            if (uses != null) addSection("Uses", uses);
            if (description != null) addSection("Description", description);

            View v = getView();
            if (v != null) v.invalidate();
        }
    }

    /**
     * Loads the Basics page of the current Substance object and creates basic views for each field.
     */
    public class LoadBasicsPageTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            EROWID_DB.loadSubstanceBasics();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (EROWID_DB.getSubstance().getBasics() == null) return;

            mProgressBar.setVisibility(View.GONE);

            final String description = EROWID_DB.getSubstance().getBasics().getDescription();
            final String effects = EROWID_DB.getSubstance().getBasics().getEffects();
            final String problems = EROWID_DB.getSubstance().getBasics().getProblems();
            final String disclaimer = EROWID_DB.getSubstance().getBasics().getDisclaimer();

            if (description != null) addSection("Description", toParagraph(description));
            if (effects != null) addSection("Effects", toParagraph(effects));
            if (problems != null) addSection("Problems", toParagraph(problems));
            if (disclaimer != null) addSection("Disclaimer", toParagraph(disclaimer));

            View v = getView();
            if (v != null) v.invalidate();
        }
    }

    /**
     * Loads the Effectss page of the current Substance object and creates basic views for each field.
     */
    public class LoadEffectsPageTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            EROWID_DB.loadSubstanceEffects();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (EROWID_DB.getSubstance().getEffects() == null) return;

            mProgressBar.setVisibility(View.GONE);

            final String positiveEffects = EROWID_DB.getSubstance().getEffects().getPositiveEffects();
            final String neutralEffects = EROWID_DB.getSubstance().getEffects().getNeutralEffects();
            final String negativeEffects = EROWID_DB.getSubstance().getEffects().getNegativeEffects();
            final String description = EROWID_DB.getSubstance().getEffects().getDescription();
            final String disclaimer = EROWID_DB.getSubstance().getEffects().getDisclaimer();

            if (positiveEffects != null)
                addSection("Positive Effects", toBulletedList(positiveEffects));
            if (neutralEffects != null)
                addSection("Neutral Effects", toBulletedList(neutralEffects));
            if (negativeEffects != null)
                addSection("Negative Effects", toBulletedList(negativeEffects));
            if (description != null) addSection("Description", toParagraph(description));
            if (disclaimer != null) addSection("Disclaimer", disclaimer);

            View v = getView();
            if (v != null) v.invalidate();
        }
    }

    /**
     * Loads the Images page of the current Substance object and creates basic views for each field.
     */
    public class LoadImagesPageTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            EROWID_DB.loadSubstanceImages();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (EROWID_DB.getSubstance().getImages() == null) return;

            mProgressBar.setVisibility(View.GONE);

            final String imageEntryList = EROWID_DB.getSubstance().getImages().getImageEntryList();

            if (imageEntryList != null) {
                TextView titleTextView = (TextView) getLayoutInflater(null).inflate(R.layout.title_text_view, null);
                titleTextView.setText("Image Entry List");
                mLinearLayout.addView(titleTextView);

                ArrayList<String> matches = new ArrayList<>();
                Pattern pattern = Pattern.compile("https(\\S*?)\\s");
                Matcher matcher = pattern.matcher(imageEntryList);
                while (matcher.find()) {
                    matches.add(matcher.group());
                }
                String linkText = imageEntryList;
                for (String match : matches) {
                    linkText = linkText.replace(match, "<br/><a href='" + match + "'>" + match + "</a><br/>");
                }

                Scanner scanner = new Scanner(linkText);
                while (scanner.hasNextLine()) {
                    TextView textView = (TextView) getLayoutInflater(null).inflate(R.layout.paragraph_text_view, null);
                    textView.setText(Html.fromHtml(scanner.nextLine()));
                    Linkify.addLinks(textView, Linkify.ALL);
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                    mLinearLayout.addView(textView);
                }
            }

            View v = getView();
            if (v != null) v.invalidate();
        }
    }

    /**
     * Loads the Health page of the current Substance object and creates basic views for each field.
     */
    public class LoadHealthPageTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            EROWID_DB.loadSubstanceHealth();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (EROWID_DB.getSubstance().getHealth() == null) return;

            mProgressBar.setVisibility(View.GONE);

            final String notes = EROWID_DB.getSubstance().getHealth().getNotes();
            final String deaths = EROWID_DB.getSubstance().getHealth().getDeaths();
            final String warnings = EROWID_DB.getSubstance().getHealth().getWarnings();
            final String cautions = EROWID_DB.getSubstance().getHealth().getCautions();
            final String benefits = EROWID_DB.getSubstance().getHealth().getBenefits();

            if (notes != null) addSection("Notes", toParagraph(notes));
            if (deaths != null) addSection("Deaths", toParagraph(deaths));
            if (warnings != null) addSection("Warnings", toParagraph(warnings));
            if (cautions != null) addSection("Cautions", toParagraph(cautions));
            if (benefits != null) addSection("Benefits", toParagraph(benefits));

            View v = getView();
            if (v != null) v.invalidate();
        }
    }

    /**
     * Loads the Law page of the current Substance object and creates basic views for each field.
     */
    public class LoadLawPageTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            EROWID_DB.loadSubstanceLaw();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (EROWID_DB.getSubstance().getLaw() == null) return;

            mProgressBar.setVisibility(View.GONE);

            final String legalTable = EROWID_DB.getSubstance().getLaw().getLegalTable();
            final String federalLaw = EROWID_DB.getSubstance().getLaw().getFederalLaw();
            final String stateLaw = EROWID_DB.getSubstance().getLaw().getStateLaw();
            final String internationalLaw = EROWID_DB.getSubstance().getLaw().getInternationalLaw();
            final String disclaimer = EROWID_DB.getSubstance().getLaw().getDisclaimer();

            if (legalTable != null) Log.d(TAG, legalTable);
            if (federalLaw != null) Log.d(TAG, legalTable);
            if (stateLaw != null) Log.d(TAG, legalTable);
            if (internationalLaw != null) Log.d(TAG, legalTable);
            if (disclaimer != null) Log.d(TAG, legalTable);

            if (legalTable != null) addTable("Legal Table", legalTable);
            if (federalLaw != null) addSection("Federal Law", toParagraph(federalLaw));
            // TODO fix this so that a string null check isn't required
            if (stateLaw != null && !stateLaw.equals("null"))
                addSection("State Law", toParagraph(stateLaw));
            if (internationalLaw != null)
                addSection("International Law", toParagraph(internationalLaw));
            if (disclaimer != null) addSection("Disclaimer", disclaimer);

            View v = getView();
            if (v != null) v.invalidate();
        }
    }

    /**
     * Loads the Dose page of the current Substance object and creates basic views for each field.
     */
    public class LoadDosePageTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            EROWID_DB.loadSubstanceDose();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (EROWID_DB.getSubstance().getDose() == null) return;

            mProgressBar.setVisibility(View.GONE);

            final String doseTable = EROWID_DB.getSubstance().getDose().getDoseTable();
            final String doseText = EROWID_DB.getSubstance().getDose().getDoseText();
            final String notes = EROWID_DB.getSubstance().getDose().getNotes();
            final String disclaimer = EROWID_DB.getSubstance().getDose().getDisclaimer();

            if (doseTable != null) addTable("Dose Table", doseTable);
            if (doseText != null) addSection("Dosing", doseText);
            // TODO fix this so that a string null check isn't required
            if (notes != null && !notes.equals("null") && !notes.equals("None"))
                addSection("Notes", toParagraph(notes));
            if (disclaimer != null) addSection("Disclaimer", disclaimer);

            View v = getView();
            if (v != null) v.invalidate();
        }
    }

    /**
     * Loads the Chemistry page of the current Substance object and creates basic views for each field.
     */
    public class LoadChemistryPageTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            EROWID_DB.loadSubstanceChemistry();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (EROWID_DB.getSubstance().getChemistry() == null) return;

            mProgressBar.setVisibility(View.GONE);

            final String chemTable = EROWID_DB.getSubstance().getChemistry().getChemTable();
            final String moleculeURL = EROWID_DB.getSubstance().getChemistry().getMoleculeUrl();

            if (chemTable != null) addTable("Chemistry Table", chemTable);
            if (moleculeURL != null) addImage(moleculeURL);

            View v = getView();
            if (v != null) v.invalidate();
        }
    }

    /**
     * Loads the ResearchChemical page of the current Substance object and creates basic views for each field.
     */
    public class LoadResearchChemicalPageTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            EROWID_DB.loadSubstanceResearchChemical();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (EROWID_DB.getSubstance().getResearchChemical() == null) return;

            mProgressBar.setVisibility(View.GONE);

            final String imageUrl = EROWID_DB.getSubstance().getResearchChemical().getImageUrl();
            final String summary = EROWID_DB.getSubstance().getResearchChemical().getSummary();

            if (imageUrl != null) addImage(imageUrl);
            if (summary != null) addSection("Summary", toParagraph(summary));

            View v = getView();
            if (v != null) v.invalidate();
        }
    }
}

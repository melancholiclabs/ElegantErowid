package com.melancholiclabs.eleganterowid;

import android.util.Log;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.melancholiclabs.eleganterowid.model.IndexItem;
import com.melancholiclabs.eleganterowid.model.Report;
import com.melancholiclabs.eleganterowid.model.Substance;
import com.melancholiclabs.eleganterowid.model.VaultIndexItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by Melancoholic on 7/1/2016.
 */
public class ErowidDB {

    /**
     * Tag for logging.
     */
    private static final String TAG = ErowidDB.class.getName();

    /**
     * Singleton ErowidDB instance.
     */
    private static ErowidDB instance;

    /**
     * Index of Chemical substances.
     */
    private ArrayList<IndexItem> chemIndex = new ArrayList<>();
    /**
     * Index of Plant substances.
     */
    private ArrayList<IndexItem> plantIndex = new ArrayList<>();
    /**
     * Index of Herb substances.
     */
    private ArrayList<IndexItem> herbIndex = new ArrayList<>();
    /**
     * Index of Pharm substances.
     */
    private ArrayList<IndexItem> pharmIndex = new ArrayList<>();
    /**
     * Index of Smart substances.
     */
    private ArrayList<IndexItem> smartIndex = new ArrayList<>();
    /**
     * Index of Animal substances.
     */
    private ArrayList<IndexItem> animalIndex = new ArrayList<>();

    /**
     * Index of the Experience Vault.
     */
    private ArrayList<IndexItem> vaultIndex = new ArrayList<>();

    /**
     * Index of reports of a item in the vaultIndex.
     */
    private ArrayList<VaultIndexItem> reportIndex = new ArrayList<>();

    /**
     * Substance reference to hold the currently selected substance.
     */
    private Substance substance = null;

    /**
     * Report reference to hold the currently selected report.
     */
    private Report report = null;

    /**
     * True if the chemIndex has finished loading.
     */
    private boolean chemIndexLoaded = false;
    /**
     * True if the plantIndex has finished loading.
     */
    private boolean plantIndexLoaded = false;
    /**
     * True if the herbIndex has finished loading.
     */
    private boolean herbIndexLoaded = false;
    /**
     * True if the pharmIndex has finished loading.
     */
    private boolean pharmIndexLoaded = false;
    /**
     * True if the smartIndex has finished loading.
     */
    private boolean smartIndexLoaded = false;
    /**
     * True if the animalIndex has finished loading.
     */
    private boolean animalIndexLoaded = false;
    /**
     * True if the vaultIndex has finished loading.
     */
    private boolean vaultIndexLoaded = false;
    /**
     * True if the reportIndex has finished loading.
     */
    private boolean reportIndexLoaded = false;

    /**
     * Enum representing substance indexes that hold the value of their respective url.
     */
    public enum Index {
        CHEMICALS("http://104.131.56.118/erowid/api.php/chemIndex?columns=id,name,url,effectsClassification,category,basicsURL,effectsURL,imagesURL,healthURL,lawURL,doseURL,chemistryURL,researchChemicalsURL&transform=1", "chemIndex"),
        PLANTS("http://104.131.56.118/erowid/api.php/plantIndex?columns=id,name,url,effectsClassification,category,basicsURL,effectsURL,imagesURL,healthURL,lawURL,doseURL,chemistryURL&transform=1", "plantIndex"),
        HERBS("http://104.131.56.118/erowid/api.php/herbIndex?columns=id,name,url,commonNames,category,basicsURL,effectsURL,imagesURL,healthURL,lawURL,doseURL,chemistryURL&transform=1", "herbIndex"),
        PHARMS("http://104.131.56.118/erowid/api.php/pharmIndex?columns=id,name,url,effectsClassification,category,basicsURL,effectsURL,imagesURL,healthURL,lawURL,doseURL,chemistryURL&transform=1", "pharmIndex"),
        SMARTS("http://104.131.56.118/erowid/api.php/smartIndex?columns=id,name,url,effectsClassification,category,basicsURL,effectsURL,imagesURL,healthURL,lawURL,doseURL,chemistryURL&transform=1", "smartIndex"),
        ANIMALS("http://104.131.56.118/erowid/api.php/animalIndex?columns=id,name,url,effectsClassification,category,basicsURL,effectsURL,imagesURL,healthURL,lawURL,doseURL,chemistryURL&transform=1", "animalIndex"),
        VAULT("http://www.erowid.org/experiences/exp_list.shtml", "vaultIndex");

        private String url;
        private String indexName;

        Index(String url, String indexName) {
            this.url = url;
            this.indexName = indexName;
        }

        public String getUrl() {
            return this.url;
        }

        public String getIndexName() {
            return this.indexName;
        }
    }

    /**
     * Enum representing tokens of an api call.
     */
    public enum ApiURL {
        SUBSTANCE_URL_PREFIX("http://104.131.56.118/erowid/api.php/"),
        SUBSTANCE_URL_MIDDLE("?filter=id,eq,"),
        SUBSTANCE_URL_SUFFIX("&columns=effectsClassification,botanicalClassification,commonNames,chemicalName,uses,description,imageURL,basicsURL,effectsURL,imagesURL,healthURL,lawURL,doseURL,chemistryURL,researchChemicalsURL&transform=1"),

        BASICS_URL_PREFIX("http://104.131.56.118/erowid/api.php/basicsIndex?filter=id,eq,"),
        BASICS_URL_SUFFIX("&columns=description,descriptionSections,effects,effectsSections,problems,problemsSections,cautionDisclaimer&transform=1"),
        BASICS_URL_INDEX("basicsIndex"),

        EFFECTS_URL_PREFIX("http://104.131.56.118/erowid/api.php/effectsIndex?filter=id,eq,"),
        EFFECTS_URL_SUFFIX("&columns=duration,positiveEffects,neutralEffects,negativeEffects,description,experienceReports,cautionDisclaimer&transform=1"),
        EFFECTS_URL_INDEX("effectsIndex"),

        IMAGES_URL_PREFIX("http://104.131.56.118/erowid/api.php/imagesIndex?filter=id,eq,"),
        IMAGES_URL_SUFFIX("&columns=imageEntryList&transform=1"),
        IMAGES_URL_INDEX("imagesIndex"),

        HEALTH_URL_PREFIX("http://104.131.56.118/erowid/api.php/healthIndex?filter=id,eq,"),
        HEALTH_URL_SUFFIX("&columns=notes,deaths,warnings,cautions,benefits&transform=1"),
        HEALTH_URL_INDEX("healthIndex"),

        LAW_URL_PREFIX("http://104.131.56.118/erowid/api.php/lawIndex?filter=id,eq,"),
        LAW_URL_SUFFIX("&columns=legalTable,federalLawText,stateLaw,internationalLaw,cautionDisclaimer&transform=1"),
        LAW_URL_INDEX("lawIndex"),

        DOSE_URL_PREFIX("http://104.131.56.118/erowid/api.php/doseIndex?filter=id,eq,"),
        DOSE_URL_SUFFIX("&columns=doseTable,doseText,notes,cautionDisclaimer&transform=1"),
        DOSE_URL_INDEX("doseIndex"),

        CHEMISTRY_URL_PREFIX("http://104.131.56.118/erowid/api.php/chemistryIndex?filter=id,eq,"),
        CHEMISTRY_URL_SUFFIX("&columns=chemTable,moleculeURL&transform=1"),
        CHEMISTRY_URL_INDEX("chemistryIndex"),

        RESEARCH_CHEMICAL_URL_PREFIX("http://104.131.56.118/erowid/api.php/researchChemicalIndex?filter=id,eq,"),
        RESEARCH_CHEMICAL_URL_SUFFIX("&columns=summaryText,imageURL&transform=1"),
        RESEARCH_CHEMICAL_URL_INDEX("researchChemicalIndex"),

        VAULT_URL_SHOW_MORE("&ShowViews=0&Start=0&Max=2000");

        private String token;

        ApiURL(String token) {
            this.token = token;
        }

        public String getToken() {
            return this.token;
        }
    }

    /**
     * Empty constructor to avoid object creation.
     */
    public ErowidDB() {
    }

    /**
     * Returns an instance of the ErowidDB class.
     *
     * @return ErowidDB instance
     */
    public static ErowidDB getInstance() {
        if (instance == null) instance = new ErowidDB();
        return instance;
    }

    /**
     * Get chemIndex.
     *
     * @return chemIndex
     */
    public ArrayList<IndexItem> getChemIndex() {
        return chemIndex;
    }

    /**
     * Get plantIndex.
     *
     * @return plantIndex
     */
    public ArrayList<IndexItem> getPlantIndex() {
        return plantIndex;
    }

    /**
     * Get herbIndex.
     *
     * @return herbIndex
     */
    public ArrayList<IndexItem> getHerbIndex() {
        return herbIndex;
    }

    /**
     * Get pharmIndex.
     *
     * @return pharmIndex
     */
    public ArrayList<IndexItem> getPharmIndex() {
        return pharmIndex;
    }

    /**
     * Get smartIndex.
     *
     * @return smartIndex
     */
    public ArrayList<IndexItem> getSmartIndex() {
        return smartIndex;
    }

    /**
     * Get animalIndex.
     *
     * @return animalIndex
     */
    public ArrayList<IndexItem> getAnimalIndex() {
        return animalIndex;
    }

    /**
     * Get vaultIndex.
     *
     * @return vaultIndex
     */
    public ArrayList<IndexItem> getVaultIndex() {
        return vaultIndex;
    }

    /**
     * Get reportIndex.
     *
     * @return reportIndex
     */
    public ArrayList<VaultIndexItem> getReportIndex() {
        return reportIndex;
    }

    /**
     * Get substance.
     *
     * @return
     */
    public Substance getSubstance() {
        return substance;
    }

    /**
     * Get report.
     *
     * @return report
     */
    public Report getReport() {
        return report;
    }

    /**
     * Gets the chemIndexLoaded.
     *
     * @return chemIndexLoaded
     */
    public boolean isChemIndexLoaded() {
        return chemIndexLoaded;
    }

    /**
     * Gets the plantIndexLoaded.
     *
     * @return plantIndexLoaded
     */
    public boolean isPlantIndexLoaded() {
        return plantIndexLoaded;
    }

    /**
     * Gets the herbIndexLoaded.
     *
     * @return herbIndexLoaded
     */
    public boolean isHerbIndexLoaded() {
        return herbIndexLoaded;
    }

    /**
     * Gets the pharmIndexLoaded.
     *
     * @return pharmIndexLoaded
     */
    public boolean isPharmIndexLoaded() {
        return pharmIndexLoaded;
    }

    /**
     * Gets the smartIndexLoaded.
     *
     * @return smartIndexLoaded
     */
    public boolean isSmartIndexLoaded() {
        return smartIndexLoaded;
    }

    /**
     * Gets the animalIndexLoaded.
     *
     * @return animalIndexLoaded
     */
    public boolean isAnimalIndexLoaded() {
        return animalIndexLoaded;
    }

    /**
     * Gets the vaultIndexLoaded.
     *
     * @return vaultIndexLoaded
     */
    public boolean isVaultIndexLoaded() {
        return vaultIndexLoaded;
    }

    /**
     * Gets the reportIndexLoaded.
     *
     * @return reportIndexLoaded
     */
    public boolean isReportIndexLoaded() {
        return reportIndexLoaded;
    }

    /**
     * Get JSON of the specified index from the server.
     *
     * @param urlString index query url
     * @return json string of the index
     */
    private String getApiCallJson(String urlString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String json = null;

        try {
            URL url = new URL(urlString);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder builder = new StringBuilder();
            if (inputStream == null) return null;
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            // Stream was empty, skip parsing
            if (builder.length() == 0) return null;
            json = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return json;
    }

    /**
     * Loads chemIndex.
     */
    public void loadChemIndex() {
        chemIndexLoaded = false;
        String json = getApiCallJson(Index.CHEMICALS.getUrl());
        loadSubstanceIndexFromJson(json, Index.CHEMICALS.toString(), chemIndex);
        chemIndexLoaded = true;
    }

    /**
     * Loads plantIndex.
     */
    public void loadPlantIndex() {
        plantIndexLoaded = false;
        String json = getApiCallJson(Index.PLANTS.getUrl());
        loadSubstanceIndexFromJson(json, Index.PLANTS.toString(), plantIndex);
        plantIndexLoaded = true;
    }

    /**
     * Loads herbIndex.
     */
    public void loadHerbIndex() {
        herbIndexLoaded = false;
        String json = getApiCallJson(Index.HERBS.getUrl());
        loadSubstanceIndexFromJson(json, Index.HERBS.toString(), herbIndex);
        herbIndexLoaded = true;
    }

    /**
     * Loads pharmIndex.
     */
    public void loadPharmIndex() {
        pharmIndexLoaded = false;
        String json = getApiCallJson(Index.PHARMS.getUrl());
        loadSubstanceIndexFromJson(json, Index.PHARMS.toString(), pharmIndex);
        pharmIndexLoaded = true;
    }

    /**
     * Loads smartIndex.
     */
    public void loadSmartIndex() {
        smartIndexLoaded = false;
        String json = getApiCallJson(Index.SMARTS.getUrl());
        loadSubstanceIndexFromJson(json, Index.SMARTS.toString(), smartIndex);
        smartIndexLoaded = true;
    }

    /**
     * Loads animalIndex.
     */
    public void loadAnimalIndex() {
        animalIndexLoaded = false;
        String json = getApiCallJson(Index.ANIMALS.getUrl());
        loadSubstanceIndexFromJson(json, Index.ANIMALS.toString(), animalIndex);
        animalIndexLoaded = true;
    }

    /**
     * Loads vaultIndex.
     */
    public void loadVaultIndex() {
        vaultIndexLoaded = false;
        try {
            Document doc = Jsoup.connect(Index.VAULT.getUrl()).get();
            Element table = doc.select("table").get(2);
            Elements rows = table.select("td");
            for (Element element : rows) {
                try {
                    String name = element.select("u").get(0).text();
                    String url = element.select("a[href]").first().attr("abs:href").toString();
                    Scanner scanner = new Scanner(element.text());
                    scanner.useDelimiter(" - ");
                    scanner.next();
                    StringBuilder builder = new StringBuilder();
                    while (scanner.hasNext()) {
                        builder.append(scanner.next());
                    }
                    vaultIndex.add(new IndexItem(name, builder.toString(), url, Index.VAULT.getIndexName()));
                } catch (IndexOutOfBoundsException e) {
                    // Do nothing
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(vaultIndex);
        vaultIndexLoaded = true;
    }

    /**
     * Loads an ArrayList index of IndexItems that represent substances from a json String.
     *
     * @param json      index json
     * @param indexType type of index to load
     */
    private void loadSubstanceIndexFromJson(String json, String indexType, ArrayList<IndexItem> index) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = null;

            if (indexType.equals(Index.CHEMICALS.toString())) {
                jsonArray = jsonObject.getJSONArray(Index.CHEMICALS.getIndexName());
            } else if (indexType.equals(Index.PLANTS.toString())) {
                jsonArray = jsonObject.getJSONArray(Index.PLANTS.getIndexName());
            } else if (indexType.equals(Index.HERBS.toString())) {
                jsonArray = jsonObject.getJSONArray(Index.HERBS.getIndexName());
            } else if (indexType.equals(Index.PHARMS.toString())) {
                jsonArray = jsonObject.getJSONArray(Index.PHARMS.getIndexName());
            } else if (indexType.equals(Index.SMARTS.toString())) {
                jsonArray = jsonObject.getJSONArray(Index.SMARTS.getIndexName());
            } else if (indexType.equals(Index.ANIMALS.toString())) {
                jsonArray = jsonObject.getJSONArray(Index.ANIMALS.getIndexName());
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                String id;
                String name;
                String caption;
                String category;
                String[] pages;
                String url;

                JSONObject item = jsonArray.getJSONObject(i);

                id = item.getString("id");
                name = item.getString("name");
                if (!indexType.equals(Index.HERBS.toString())) {
                    caption = item.getString("effectsClassification");
                } else {
                    caption = item.getString("commonNames");
                }
                category = item.getString("category");

                pages = new String[8];
                pages[0] = item.getString("basicsURL");
                pages[1] = item.getString("effectsURL");
                pages[2] = item.getString("imagesURL");
                pages[3] = item.getString("healthURL");
                pages[4] = item.getString("lawURL");
                pages[5] = item.getString("doseURL");
                pages[6] = item.getString("chemistryURL");
                if (indexType.equals(Index.CHEMICALS.toString())) {
                    pages[7] = item.getString("researchChemicalsURL");
                } else {
                    pages[7] = "null";
                }
                url = item.getString("url");

                index.add(new IndexItem(id, name, caption, category, pages, url, indexType));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * Filters a given index according to a specific query.
     *
     * @param query query to be filtered
     * @param index index to be filtered
     * @return filtered ArrayList
     */
    public ArrayList<IndexItem> filterIndex(final String query, ArrayList<IndexItem> index) {
        Collection<String> temp = new ArrayList<>();
        for (IndexItem item : index) {
            temp.add(item.getName());
        }

        temp = Collections2.filter(temp, new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                String temp = input.toLowerCase().replaceAll("-", "");
                if (temp.contains(query.toLowerCase())) return true;
                if (query.toLowerCase().contains(temp)) return true;
                return false;
            }
        });

        ArrayList<IndexItem> result = new ArrayList<>();
        for (String name : temp) {
            for (IndexItem item : index) {
                if (item.getName().equals(name)) {
                    result.add(item);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Filters the chemIndex according to the specified query.
     *
     * @param query query to be filtered
     * @return filtered ArrayList of IndexItems
     */
    public ArrayList<IndexItem> filterChemIndex(String query) {
        return filterIndex(query, chemIndex);
    }

    /**
     * Filters the plantIndex according to the specified query.
     *
     * @param query query to be filtered
     * @return filtered ArrayList of IndexItems
     */
    public ArrayList<IndexItem> filterPlantIndex(String query) {
        return filterIndex(query, plantIndex);
    }

    /**
     * Filters the herbIndex according to the specified query.
     *
     * @param query query to be filtered
     * @return filtered ArrayList of IndexItems
     */
    public ArrayList<IndexItem> filterHerbIndex(String query) {
        return filterIndex(query, herbIndex);
    }

    /**
     * Filters the pharmIndex according to the specified query.
     *
     * @param query query to be filtered
     * @return filtered ArrayList of IndexItems
     */
    public ArrayList<IndexItem> filterPharmIndex(String query) {
        return filterIndex(query, pharmIndex);
    }

    /**
     * Filters the smartIndex according to the specified query.
     *
     * @param query query to be filtered
     * @return filtered ArrayList of IndexItems
     */
    public ArrayList<IndexItem> filterSmartIndex(String query) {
        return filterIndex(query, smartIndex);
    }

    /**
     * Filters the animalIndex according to the specified query.
     *
     * @param query query to be filtered
     * @return filtered ArrayList of IndexItems
     */
    public ArrayList<IndexItem> filterAnimalIndex(String query) {
        return filterIndex(query, animalIndex);
    }

    /**
     * Filters the vaultIndex according to the specified query.
     *
     * @param query query to be filtered
     * @return filtered ArrayList of IndexItems
     */
    public ArrayList<IndexItem> filterVaultIndex(String query) {
        return filterIndex(query, vaultIndex);
    }

    /**
     * Filters indexes for a query according to the booleans in the boolean[].
     *
     * @param query   query to be filtered
     * @param options 0:chemIndex, 1:plantIndex, 2:herbIndex, 3:pharmIndex, 4:smartIndex, 5:animalIndex, 6:vaultIndex
     * @return filtered ArrayList of IndexItems
     */
    public ArrayList<IndexItem> filterCustom(String query, boolean[] options) {
        ArrayList<IndexItem> temp = new ArrayList<>();
        if (options[0]) {
            for (IndexItem item : filterChemIndex(query)) {
                temp.add(item);
            }
        }
        if (options[1]) {
            for (IndexItem item : filterPlantIndex(query)) {
                temp.add(item);
            }
        }
        if (options[2]) {
            for (IndexItem item : filterHerbIndex(query)) {
                temp.add(item);
            }
        }
        if (options[3]) {
            for (IndexItem item : filterPharmIndex(query)) {
                temp.add(item);
            }
        }
        if (options[4]) {
            for (IndexItem item : filterSmartIndex(query)) {
                temp.add(item);
            }
        }
        if (options[5]) {
            for (IndexItem item : filterAnimalIndex(query)) {
                temp.add(item);
            }
        }
        if (options[6]) {
            for (IndexItem item : filterVaultIndex(query)) {
                temp.add(item);
            }
        }
        return temp;
    }

    /**
     * Filters the reportIndex according to a specific query.
     *
     * @param query query to be filtered
     * @param index index to be filtered
     * @return filtered ArrayList
     */
    public ArrayList<VaultIndexItem> filterReportIndex(final String query, ArrayList<VaultIndexItem> index) {
        Collection<String> temp = new ArrayList<>();
        for (VaultIndexItem item : index) {
            temp.add(item.getTitle());
        }

        temp = Collections2.filter(temp, new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.toLowerCase().contains(query.toLowerCase());
            }
        });

        ArrayList<VaultIndexItem> result = new ArrayList<>();
        for (String name : temp) {
            for (VaultIndexItem item : index) {
                if (item.getTitle().equals(name)) {
                    result.add(item);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Loads a substance into memory, given an IndexItem.
     *
     * @param item IndexItem from which to load a substance from
     */
    public void loadSubstance(IndexItem item) {
        StringBuilder builder = new StringBuilder();
        builder.append(ApiURL.SUBSTANCE_URL_PREFIX.getToken());
        builder.append(getIndexType(item));
        builder.append(ApiURL.SUBSTANCE_URL_MIDDLE.getToken());
        builder.append(item.getId());
        builder.append(ApiURL.SUBSTANCE_URL_SUFFIX.getToken());

        Log.d(TAG, builder.toString());

        String json = getApiCallJson(builder.toString());

        String imageURL = null;
        String effectsClassification = null;
        String botanicalClassification = null;
        String chemicalName = null;
        String commonNames = null;
        String uses = null;
        String description = null;

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(getIndexType(item));
            JSONObject jsonSubstance = jsonArray.getJSONObject(0);

            try {
                imageURL = jsonSubstance.getString("imageURL");
            } catch (JSONException e) {
                Log.d(TAG, "No value for imageURL");
            }
            try {
                if (getIndexType(item).equals(Index.HERBS.getIndexName())) {
                    botanicalClassification = jsonSubstance.getString("botanicalClassification");
                }
                effectsClassification = jsonSubstance.getString("effectsClassification");
            } catch (JSONException e) {
                Log.d(TAG, "No value for effectsClassification or botanicalClassification");
            }
            try {
                chemicalName = jsonSubstance.getString("chemicalName");
            } catch (JSONException e) {
                Log.d(TAG, "No value for chemicalName");
            }
            try {
                commonNames = jsonSubstance.getString("commonNames");
            } catch (JSONException e) {
                Log.d(TAG, "No value for commonNames");
            }
            try {
                uses = jsonSubstance.getString("uses");
            } catch (JSONException e) {
                Log.d(TAG, "No value for uses");
            }
            try {
                description = jsonSubstance.getString("description");
            } catch (JSONException e) {
                Log.d(TAG, "No value for description");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        substance = new Substance(item, imageURL, effectsClassification, botanicalClassification, chemicalName, commonNames, uses, description);
    }

    /**
     * Loads a Basics page for the current substance instance.
     */
    public void loadSubstanceBasics() {
        if (substance.getPages()[1] == null) return;

        while (substance == null) ;

        StringBuilder builder = new StringBuilder();
        builder.append(ApiURL.BASICS_URL_PREFIX.getToken());
        builder.append(substance.getId());
        builder.append(ApiURL.BASICS_URL_SUFFIX.getToken());

        String json = getApiCallJson(builder.toString());

        String description = null;
        String effects = null;
        String problems = null;
        String disclaimer = null;

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(ApiURL.BASICS_URL_INDEX.getToken());
            JSONObject basics = jsonArray.getJSONObject(0);

            try {
                description = basics.getString("description");
            } catch (JSONException e) {
                Log.d(TAG, "No value for description");
            }
            try {
                effects = basics.getString("effects");
            } catch (JSONException e) {
                Log.d(TAG, "No value for effects");
            }
            try {
                problems = basics.getString("problems");
            } catch (JSONException e) {
                Log.d(TAG, "No value for problems");
            }
            try {
                disclaimer = basics.getString("cautionDisclaimer");
            } catch (JSONException e) {
                Log.d(TAG, "No value for disclaimer");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        substance.loadBasics(description, effects, problems, disclaimer);
    }

    /**
     * Loads an Effects page for the current substance instance.
     */
    public void loadSubstanceEffects() {
        if (substance.getPages()[2] == null) return;

        while (substance == null) ;

        StringBuilder builder = new StringBuilder();
        builder.append(ApiURL.EFFECTS_URL_PREFIX.getToken());
        builder.append(substance.getId());
        builder.append(ApiURL.EFFECTS_URL_SUFFIX.getToken());

        String json = getApiCallJson(builder.toString());

        String positiveEffects = null;
        String neutralEffects = null;
        String negativeEffects = null;
        String description = null;
        String disclaimer = null;

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(ApiURL.EFFECTS_URL_INDEX.getToken());
            JSONObject basics = jsonArray.getJSONObject(0);

            try {
                positiveEffects = basics.getString("positiveEffects");
            } catch (JSONException e) {
                Log.d(TAG, "No value for positiveEffects");
            }
            try {
                neutralEffects = basics.getString("neutralEffects");
            } catch (JSONException e) {
                Log.d(TAG, "No value for neutralEffects");
            }
            try {
                negativeEffects = basics.getString("negativeEffects");
            } catch (JSONException e) {
                Log.d(TAG, "No value for negativeEffects");
            }
            try {
                description = basics.getString("description");
            } catch (JSONException e) {
                Log.d(TAG, "No value for description");
            }
            try {
                disclaimer = basics.getString("cautionDisclaimer");
            } catch (JSONException e) {
                Log.d(TAG, "No value for disclaimer");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        substance.loadEffects(positiveEffects, neutralEffects, negativeEffects, description, disclaimer);
    }

    /**
     * Loads an Images page for the current substance instance.
     */
    public void loadSubstanceImages() {
        if (substance.getPages()[2] == null) return;

        while (substance == null) ;

        StringBuilder builder = new StringBuilder();
        builder.append(ApiURL.IMAGES_URL_PREFIX.getToken());
        builder.append(substance.getId());
        builder.append(ApiURL.IMAGES_URL_SUFFIX.getToken());

        String json = getApiCallJson(builder.toString());

        String imageEntryList = null;

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(ApiURL.IMAGES_URL_INDEX.getToken());
            JSONObject basics = jsonArray.getJSONObject(0);

            try {
                imageEntryList = basics.getString("imageEntryList");
            } catch (JSONException e) {
                Log.d(TAG, "No value for imageEntryList");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        substance.loadImages(imageEntryList);
    }

    /**
     * Loads a Health page for the current substance instance.
     */
    public void loadSubstanceHealth() {
        if (substance.getPages()[3] == null) return;

        while (substance == null) ;

        StringBuilder builder = new StringBuilder();
        builder.append(ApiURL.HEALTH_URL_PREFIX.getToken());
        builder.append(substance.getId());
        builder.append(ApiURL.HEALTH_URL_SUFFIX.getToken());

        String json = getApiCallJson(builder.toString());

        String notes = null;
        String deaths = null;
        String warnings = null;
        String cautions = null;
        String benefits = null;

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(ApiURL.HEALTH_URL_INDEX.getToken());
            JSONObject basics = jsonArray.getJSONObject(0);

            try {
                notes = basics.getString("notes");
            } catch (JSONException e) {
                Log.d(TAG, "No value for notes");
            }
            try {
                deaths = basics.getString("deaths");
            } catch (JSONException e) {
                Log.d(TAG, "No value for deaths");
            }
            try {
                warnings = basics.getString("warnings");
            } catch (JSONException e) {
                Log.d(TAG, "No value for warnings");
            }
            try {
                cautions = basics.getString("cautions");
            } catch (JSONException e) {
                Log.d(TAG, "No value for cautions");
            }
            try {
                benefits = basics.getString("benefits");
            } catch (JSONException e) {
                Log.d(TAG, "No value for benefits");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        substance.loadHealth(notes, deaths, warnings, cautions, benefits);
    }

    /**
     * Loads a Law page for the current substance instance.
     */
    public void loadSubstanceLaw() {
        if (substance.getPages()[4] == null) return;

        while (substance == null) ;

        StringBuilder builder = new StringBuilder();
        builder.append(ApiURL.LAW_URL_PREFIX.getToken());
        builder.append(substance.getId());
        builder.append(ApiURL.LAW_URL_SUFFIX.getToken());

        String json = getApiCallJson(builder.toString());

        String legalTable = null;
        String federalLaw = null;
        String stateLaw = null;
        String internationalLaw = null;
        String disclaimer = null;

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(ApiURL.LAW_URL_INDEX.getToken());
            JSONObject basics = jsonArray.getJSONObject(0);

            try {
                legalTable = basics.getString("legalTable");
            } catch (JSONException e) {
                Log.d(TAG, "No value for legalTable");
            }
            try {
                federalLaw = basics.getString("federalLawText");
            } catch (JSONException e) {
                Log.d(TAG, "No value for federalLaw");
            }
            try {
                stateLaw = basics.getString("stateLaw");
            } catch (JSONException e) {
                Log.d(TAG, "No value for stateLaw");
            }
            try {
                internationalLaw = basics.getString("internationalLaw");
            } catch (JSONException e) {
                Log.d(TAG, "No value for internationalLaw");
            }
            try {
                disclaimer = basics.getString("cautionDisclaimer");
            } catch (JSONException e) {
                Log.d(TAG, "No value for disclaimer");
            }
        } catch (JSONException e) {
            Log.d(TAG, json);
            e.printStackTrace();
        }
        substance.loadLaw(legalTable, federalLaw, stateLaw, internationalLaw, disclaimer);
    }

    /**
     * Loads a Dose page for the current substance instance.
     */
    public void loadSubstanceDose() {
        if (substance.getPages()[5] == null) return;

        while (substance == null) ;

        StringBuilder builder = new StringBuilder();
        builder.append(ApiURL.DOSE_URL_PREFIX.getToken());
        builder.append(substance.getId());
        builder.append(ApiURL.DOSE_URL_SUFFIX.getToken());

        String json = getApiCallJson(builder.toString());

        String doseTable = null;
        String doseText = null;
        String notes = null;
        String disclaimer = null;

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(ApiURL.DOSE_URL_INDEX.getToken());
            JSONObject basics = jsonArray.getJSONObject(0);

            try {
                doseTable = basics.getString("doseTable");
            } catch (JSONException e) {
                Log.d(TAG, "No value for doseTable");
            }
            try {
                doseText = basics.getString("doseText");
            } catch (JSONException e) {
                Log.d(TAG, "No value for doseText");
            }
            try {
                notes = basics.getString("notes");
            } catch (JSONException e) {
                Log.d(TAG, "No value for notes");
            }
            try {
                disclaimer = basics.getString("cautionDisclaimer");
            } catch (JSONException e) {
                Log.d(TAG, "No value for disclaimer");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        substance.loadDose(doseTable, doseText, notes, disclaimer);
    }

    /**
     * Loads a Chemistry page for the current substance instance.
     */
    public void loadSubstanceChemistry() {
        if (substance.getPages()[6] == null) return;

        while (substance == null) ;

        StringBuilder builder = new StringBuilder();
        builder.append(ApiURL.CHEMISTRY_URL_PREFIX.getToken());
        builder.append(substance.getId());
        builder.append(ApiURL.CHEMISTRY_URL_SUFFIX.getToken());

        String json = getApiCallJson(builder.toString());

        String chemTable = null;
        String moleculeUrl = null;

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(ApiURL.CHEMISTRY_URL_INDEX.getToken());
            JSONObject basics = jsonArray.getJSONObject(0);

            try {
                chemTable = basics.getString("chemTable");
            } catch (JSONException e) {
                Log.d(TAG, "No value for chemTable");
            }
            try {
                moleculeUrl = basics.getString("moleculeURL");
            } catch (JSONException e) {
                Log.d(TAG, "No value for moleculeUrl");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        substance.loadChemistry(chemTable, moleculeUrl);
    }

    /**
     * Loads a ResearchChemical page for the current substance instance.
     */
    public void loadSubstanceResearchChemical() {
        if (substance.getPages()[7] == null) return;

        while (substance == null) ;

        StringBuilder builder = new StringBuilder();
        builder.append(ApiURL.RESEARCH_CHEMICAL_URL_PREFIX.getToken());
        builder.append(substance.getId());
        builder.append(ApiURL.RESEARCH_CHEMICAL_URL_SUFFIX.getToken());

        String json = getApiCallJson(builder.toString());

        String summary = null;
        String imageUrl = null;

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(ApiURL.RESEARCH_CHEMICAL_URL_INDEX.getToken());
            JSONObject basics = jsonArray.getJSONObject(0);

            try {
                summary = basics.getString("summaryText");
            } catch (JSONException e) {
                Log.d(TAG, "No value for summary");
            }
            try {
                imageUrl = basics.getString("imageURL");
            } catch (JSONException e) {
                Log.d(TAG, "No value for imageUrl");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        substance.loadResearchChemical(summary, imageUrl);
    }

    /**
     * Loads VaultIndexItems into the reportIndex, given a vault IndexItem.
     *
     * @param item IndexItem to load reports for
     */
    public void loadReportIndex(IndexItem item) {
        reportIndexLoaded = false;
        reportIndex.clear();
        try {
            Document doc = Jsoup.connect(item.getUrl()).get();
            Element link = doc.select("td a").get(1);
            String fullDocUrl = link.attr("abs:href");

            Document fullDoc = Jsoup.connect(fullDocUrl + ApiURL.VAULT_URL_SHOW_MORE.getToken()).get();

            Element table = fullDoc.select("table table").get(0);
            Elements rows = table.select("tr[class]");
            for (Element element : rows) {
                String title = element.select("td").get(1).text();
                String author = element.select("td").get(2).text();
                String substance = element.select("td").get(3).text();
                String date = element.select("td").get(4).text();
                String url = element.select("td").get(1).select("a[href]").attr("abs:href").toString();
                reportIndex.add(new VaultIndexItem(title, author, substance, date, url));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        reportIndexLoaded = true;
    }

    /**
     * Loads a Report, given a VaultIndexItem.
     *
     * @param item VaultIndexItem to load a report for
     */
    public void loadReport(VaultIndexItem item) {
        String doseTable = null;
        String bodyWeight = null;
        String year = null;
        String id = null;
        String gender = null;
        String age = null;
        String views = null;
        String text = null;

        try {
            Document doc = Jsoup.connect(item.getUrl()).get();
            Element dataTable = doc.select("table").get(2);
            StringBuilder builder = new StringBuilder();
            Elements tr = dataTable.select("tr");
            for (Element trElement : tr) {
                StringBuilder lineBuilder = new StringBuilder();
                Elements td = trElement.select("td");
                for (Element tdElement : td) {
                    String line = tdElement.text();
                    lineBuilder.append(line.replace("DOSE:", ""));
                    lineBuilder.append("\t");
                }
                builder.append(lineBuilder.toString().trim());
                builder.append("\n");
            }

            doseTable = builder.toString().trim();

            Element weightTable = doc.select("table").get(3);
            bodyWeight = weightTable.select("tr td").get(1).text();

            Element footerTable = doc.select("table").get(4);
            System.out.println(footerTable.toString());
            year = footerTable.select("tr td").first().text();
            id = footerTable.select("tr td").get(1).text();
            gender = footerTable.select("tr td").get(2).text();
            age = footerTable.select("tr td").get(4).text();
            views = footerTable.select("tr td").get(7).text();

            String raw = doc.select("div[class=report-text-surround]").first().toString();

            Scanner scanner = new Scanner(raw);
            boolean isBody = false;
            StringBuilder textBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("<!-- Start Body -->")) isBody = true;
                if (line.contains("<!-- End Body -->")) isBody = false;
                if (isBody) {
                    textBuilder.append(line.trim());
                    textBuilder.append("\n");
                }
            }
            text = textBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        report = new Report(item, doseTable, bodyWeight, text, year, gender, id, age, views);
    }

    /**
     * Dereferences substance for garbage collection.
     */
    public void unloadSubstance() {
        substance = null;
    }

    /**
     * Removes all items from the reportIndex to free up memory.
     */
    public void unloadReportIndex() {
        reportIndex.clear();
    }

    /**
     * Dereferences report for garbage collection.
     */
    public void unloadReport() {
        report = null;
    }

    /**
     * Get the indexType of the indexItem.
     *
     * @param item IndexItem from which to determine the indexType
     * @return indexType
     */
    private String getIndexType(IndexItem item) {
        String indexType = null;
        if (item.getCategory().toUpperCase().equals(Index.CHEMICALS.toString())) {
            indexType = Index.CHEMICALS.getIndexName();
        } else if (item.getCategory().toUpperCase().equals(Index.PLANTS.toString())) {
            indexType = Index.PLANTS.getIndexName();
        } else if (item.getCategory().toUpperCase().equals(Index.HERBS.toString())) {
            indexType = Index.HERBS.getIndexName();
        } else if (item.getCategory().toUpperCase().equals(Index.PHARMS.toString())) {
            indexType = Index.PHARMS.getIndexName();
        } else if (item.getCategory().toUpperCase().equals(Index.SMARTS.toString())) {
            indexType = Index.SMARTS.getIndexName();
        } else if (item.getCategory().toUpperCase().equals(Index.ANIMALS.toString())) {
            indexType = Index.ANIMALS.getIndexName();
        } else {
            Log.d(TAG, "IndexType could not be determined from " + item.getName());
        }
        return indexType;
    }

    /**
     * Attempts to find a Vault IndexItem that matches the given Substance IndexItem.
     *
     * @param substance IndexItem to be matched
     * @return mathed vault IndexItem or null if none is found
     */
    public IndexItem findVaultMatch(IndexItem substance) {
        return findMatch(substance, vaultIndex);
    }

    /**
     * Attempts to find a Substance IndexItem that matches the given Vault IndexItem.
     *
     * @param vaultItem IndexItem to be matched
     * @return mathed substance IndexItem or null if none is found
     */
    public IndexItem findSubstanceMatch(IndexItem vaultItem) {
        IndexItem match = null;

        match = findMatch(vaultItem, chemIndex);
        if (match != null) return match;
        match = findMatch(vaultItem, plantIndex);
        if (match != null) return match;
        match = findMatch(vaultItem, herbIndex);
        if (match != null) return match;
        match = findMatch(vaultItem, pharmIndex);
        if (match != null) return match;
        match = findMatch(vaultItem, smartIndex);
        if (match != null) return match;
        match = findMatch(vaultItem, animalIndex);

        return match;
    }

    /**
     * Attempts to find an IndexItem that matches the query from within the given index.
     *
     * @param query query to be filtered
     * @param index index to filter
     * @return matching IndexItem or null if none is matched
     */
    private IndexItem findMatch(IndexItem query, ArrayList<IndexItem> index) {
        ArrayList<IndexItem> temp = filterVaultIndex(query.getName());
        Log.d(TAG, "Results: " + temp.toString());
        for (IndexItem item : index) {
            if (item.getName().toLowerCase().equals(query.getName().toLowerCase())) {
                Log.d(TAG, "Matching item " + item.getName());
                return item;
            }
        }
        // If there is only one result, consider it a match
        if (temp.size() == 1) {
            Log.d(TAG, "Matching item " + temp.get(0));
            return temp.get(0);
        }
        // Return null if no match was found
        return null;
    }

    public IndexItem findSubstance(String name) {
        for (IndexItem item : chemIndex) {
            if (item.getName().equals(name)) return item;
        }
        for (IndexItem item : plantIndex) {
            if (item.getName().equals(name)) return item;
        }
        for (IndexItem item : herbIndex) {
            if (item.getName().equals(name)) return item;
        }
        for (IndexItem item : pharmIndex) {
            if (item.getName().equals(name)) return item;
        }
        for (IndexItem item : smartIndex) {
            if (item.getName().equals(name)) return item;
        }
        for (IndexItem item : animalIndex) {
            if (item.getName().equals(name)) return item;
        }
        return null;
    }
}

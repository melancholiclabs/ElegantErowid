package com.melancholiclabs.eleganterowid.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Melancoholic on 7/1/2016.
 */
public class IndexItem implements Comparable<IndexItem>, Serializable {

    /** Id of the IndexItem. */
    private String id;
    /** Name of the IndexItem. */
    private String name;
    /** Caption of the IndexItem. */
    private String caption;
    /** Category of the IndexItem. */
    private String category;
    /** Links to the pages of the IndexItem. */
    private String[] pages;
    /** Url of the IndexItem. */
    private String url;
    /** Index type of the IndexItem. */
    private String indexType;

    /**
     * Constructor for an IndexItem of a substance index.
     *
     * @param id substance id
     * @param name substance name
     * @param caption substance caption
     * @param category substance category
     * @param pages array of strings containing urls to substance external pages
     * @param url substance url
     */
    public IndexItem(String id, String name, String caption, String category, String[] pages, String url, String indexType) {
        this(name, caption, url, indexType);
        this.id = id;
        this.category = category;
        this.pages = pages;
    }

    /**
     * Constructor for an IndexItem of the vault index.
     *
     * @param name vault report name
     * @param caption vault report caption
     * @param url vault report url
     */
    public IndexItem(String name, String caption, String url, String indexType) {
        this.name = name;
        this.caption = caption;
        this.url = url;
        this.indexType = indexType;
    }

    /**
     * Constructor with just enough information to make an api call to load a substance.
     *
     * @param id
     * @param category
     * @param pages
     */
    public IndexItem(String id, String category, String[] pages) {
        this.id = id;
        this.category = category;
        this.pages = pages;
    }

    /**
     * Gets the Id.
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the caption.
     *
     * @return caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Gets the category.
     *
     * @return category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gets the pages.
     *
     * @return pages
     */
    public String[] getPages() {
        return pages;
    }

    /**
     * Gets the url.
     *
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets the indexType.
     *
     * @return indexType
     */
    public String getIndexType() {
        return indexType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("IndexItem{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", caption='").append(caption).append('\'');
        sb.append(", category='").append(category).append('\'');
        sb.append(", pages=").append(Arrays.toString(pages));
        sb.append(", url='").append(url).append('\'');
        sb.append(", indexType='").append(indexType).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(IndexItem o) {
        return this.getName().compareTo(o.getName());
    }
}

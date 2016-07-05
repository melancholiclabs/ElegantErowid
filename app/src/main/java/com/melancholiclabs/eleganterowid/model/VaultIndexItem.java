package com.melancholiclabs.eleganterowid.model;

/**
 * Created by Melancoholic on 7/2/2016.
 */
public class VaultIndexItem implements Comparable<VaultIndexItem> {

    /**
     * Title of the vault entry.
     */
    private String title;
    /**
     * Author of the vault entry.
     */
    private String author;
    /**
     * Substances of the vault entry.
     */
    private String substance;
    /**
     * Date of the vault entry.
     */
    private String date;
    /**
     * URL of the vault entry.
     */
    private String url;

    /**
     * Constructs a VaultIndexItem.
     *
     * @param title     vault entry title
     * @param author    vault entry author
     * @param substance vault entry substance(s)
     * @param date      vault entry date
     * @param url       vault entry url
     */
    public VaultIndexItem(String title, String author, String substance, String date, String url) {
        this.title = title;
        this.author = author;
        this.substance = substance;
        this.date = date;
        this.url = url;
    }

    /**
     * Get the title.
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the author.
     *
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Get the substance(s).
     *
     * @return substance(s)
     */
    public String getSubstance() {
        return substance;
    }

    /**
     * Get the date.
     *
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * Get the url.
     *
     * @return url
     */
    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VaultIndexItem{");
        sb.append("title='").append(title).append('\'');
        sb.append(", author='").append(author).append('\'');
        sb.append(", substance='").append(substance).append('\'');
        sb.append(", date='").append(date).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(VaultIndexItem o) {
        return this.getTitle().compareTo(o.getTitle());
    }
}

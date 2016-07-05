package com.melancholiclabs.eleganterowid.model;

import java.io.Serializable;

/**
 * Created by Melancoholic on 7/2/2016.
 */
public class Report extends VaultIndexItem implements Serializable {

    /**
     * Table detailing the amounts taken and intervals.
     */
    private String doseTable;
    /**
     * Body weight of the user.
     */
    private String bodyWeight;
    /**
     * Text of the experience report.
     */
    private String text;
    /**
     * Year of the experience.
     */
    private String year;
    /**
     * Gender of the user.
     */
    private String gender;
    /**
     * Id of the experience.
     */
    private String id;
    /**
     * Age of the user at the time of the experience.
     */
    private String age;
    /**
     * Amount of views of the experience page.
     */
    private String views;

    /**
     * Constructs a VaultIndexItem.
     *
     * @param item       vault entry item
     * @param doseTable  report dose table
     * @param bodyWeight report body weight
     * @param text       report text
     * @param year       report year
     * @param gender     report gender
     * @param id         report id
     * @param age        report age
     * @param views      report views
     */
    public Report(VaultIndexItem item, String doseTable, String bodyWeight, String text, String year, String gender, String id, String age, String views) {
        super(item.getTitle(), item.getAuthor(), item.getSubstance(), item.getDate(), item.getUrl());
        this.doseTable = doseTable;
        this.bodyWeight = bodyWeight;
        this.text = text;
        this.year = year;
        this.gender = gender;
        this.id = id;
        this.age = age;
        this.views = views;
    }

    /**
     * Gets the doseTable.
     *
     * @return doseTable
     */
    public String getDoseTable() {
        return doseTable;
    }

    /**
     * Gets the bodyWeight.
     *
     * @return bodyWeight
     */
    public String getBodyWeight() {
        return bodyWeight;
    }

    /**
     * Gets the text.
     *
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the year.
     *
     * @return year
     */
    public String getYear() {
        return year;
    }

    /**
     * Gets the gender.
     *
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Gets the id.
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the age.
     *
     * @return age
     */
    public String getAge() {
        return age;
    }

    /**
     * Gets the views.
     *
     * @return views
     */
    public String getViews() {
        return views;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Report{");
        sb.append("doseTable='").append(doseTable).append('\'');
        sb.append(", bodyWeight='").append(bodyWeight).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append(", year='").append(year).append('\'');
        sb.append(", gender='").append(gender).append('\'');
        sb.append(", id='").append(id).append('\'');
        sb.append(", age='").append(age).append('\'');
        sb.append(", views='").append(views).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

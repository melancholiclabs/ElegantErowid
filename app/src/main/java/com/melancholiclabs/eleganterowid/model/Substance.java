package com.melancholiclabs.eleganterowid.model;

import java.io.Serializable;

/**
 * Created by Melancoholic on 7/2/2016.
 */
public class Substance extends IndexItem implements Serializable {

    /**
     * ImageUrl of the substance's image.
     */
    private String imageUrl;
    /**
     * Effects Classification of the susbtance.
     */
    private String effectsClassification;
    /**
     * Botanical Classification of the substance.
     */
    private String botanicalClassification;
    /**
     * Chemical name of the substance.
     */
    private String chemicalName;
    /**
     * Common names of the substance.
     */
    private String commonNames;
    /**
     * Uses of the substance.
     */
    private String uses;
    /**
     * Description of the substance.
     */
    private String description;

    /**
     * Basics page of the substance.
     */
    private Basics basics;
    /**
     * Effects page of the substance.
     */
    private Effects effects;
    /**
     * Images page of the substance.
     */
    private Images images;
    /**
     * Health page of the substance.
     */
    private Health health;
    /**
     * Law page of the substance.
     */
    private Law law;
    /**
     * Dose page of the substance.
     */
    private Dose dose;
    /**
     * Chemistry page of the substance.
     */
    private Chemistry chemistry;
    /**
     * Research Chemical page of the substance.
     */
    private ResearchChemical researchChemical;

    /**
     * Constructs a Substance object.
     *
     * @param item                    IndexItem from which to construct the Substance
     * @param imageUrl                url to the substance's image
     * @param effectsClassification   effects classification of the substance
     * @param botanicalClassification botanical classification of the substance
     * @param chemicalName            chemical name of the substance
     * @param commonNames             common names of the substance
     * @param uses                    uses of the substance
     * @param description             description of the substance
     */
    public Substance(IndexItem item, String imageUrl, String effectsClassification, String botanicalClassification, String chemicalName, String commonNames, String uses, String description) {
        super(item.getId(), item.getName(), item.getCaption(), item.getCategory(), item.getPages(), item.getUrl(), item.getIndexType());
        this.imageUrl = imageUrl;
        this.effectsClassification = effectsClassification;
        this.botanicalClassification = botanicalClassification;
        this.chemicalName = chemicalName;
        this.commonNames = commonNames;
        this.uses = uses;
        this.description = description;
    }

    /**
     * Gets the imageUrl.
     *
     * @return imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Gets the effectsClassification.
     *
     * @return effectsClassification
     */
    public String getEffectsClassification() {
        return effectsClassification;
    }

    /**
     * Gets the botanicalClassification.
     *
     * @return botanicalClassification
     */
    public String getBotanicalClassification() {
        return botanicalClassification;
    }

    /**
     * Gets the chemicalName.
     *
     * @return chemicalName
     */
    public String getChemicalName() {
        return chemicalName;
    }

    /**
     * Gets the commonNames.
     *
     * @return commonNames
     */
    public String getCommonNames() {
        return commonNames;
    }

    /**
     * Gets the uses.
     *
     * @return uses
     */
    public String getUses() {
        return uses;
    }

    /**
     * Gets the description.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the basics.
     *
     * @return basics
     */
    public Basics getBasics() {
        return basics;
    }

    /**
     * Loads basics with a newly constructed Basics object from the arguments passed in.
     *
     * @param description basics description
     * @param effects     basics effects
     * @param problems    basics problems
     * @param disclaimer  basics disclaimer
     */
    public void loadBasics(String description, String effects, String problems, String disclaimer) {
        this.basics = new Basics(description, effects, problems, disclaimer);
    }

    /**
     * Gets the effects.
     *
     * @return effects
     */
    public Effects getEffects() {
        return effects;
    }

    /**
     * Loads basics with a newly constructed Basics object from the arguments passed in.
     *
     * @param positiveEffects effects positive effects
     * @param neutralEffects  effects neutral effects
     * @param negativeEffects effects negative effects
     * @param description     effects description
     * @param disclaimer      effects disclaimer
     */
    public void loadEffects(String positiveEffects, String neutralEffects, String negativeEffects, String description, String disclaimer) {
        this.effects = new Effects(positiveEffects, neutralEffects, negativeEffects, description, disclaimer);
    }

    /**
     * Gets the images.
     *
     * @return images
     */
    public Images getImages() {
        return images;
    }

    /**
     * Loads images with a newly constructed Images object from the arguments passed in.
     *
     * @param imageEntryList images image entry list
     */
    public void loadImages(String imageEntryList) {
        this.images = new Images(imageEntryList);
    }

    /**
     * Gets the health.
     *
     * @return health
     */
    public Health getHealth() {
        return health;
    }

    /**
     * Loads health with a newly constructed Health object from the arguments passed in.
     *
     * @param notes    health notes
     * @param deaths   health deaths
     * @param warnings health warnings
     * @param cautions health cautions
     * @param benefits health benefits
     */
    public void loadHealth(String notes, String deaths, String warnings, String cautions, String benefits) {
        this.health = new Health(notes, deaths, warnings, cautions, benefits);
    }

    /**
     * Gets the law.
     *
     * @return law
     */
    public Law getLaw() {
        return law;
    }

    /**
     * Loads law with a newly constructed Law object from the arguments passed in.
     *
     * @param legalTable       law legal table
     * @param federalLaw       law federal law
     * @param stateLaw         law state law
     * @param internationalLaw law international law
     * @param disclaimer       law disclaimer
     */
    public void loadLaw(String legalTable, String federalLaw, String stateLaw, String internationalLaw, String disclaimer) {
        this.law = new Law(legalTable, federalLaw, stateLaw, internationalLaw, disclaimer);
    }

    /**
     * Gets the dose.
     *
     * @return dose
     */
    public Dose getDose() {
        return dose;
    }

    /**
     * Loads dose with a newly constructed Dose object from the arguments passed in.
     *
     * @param doseTable  dose dose table
     * @param doseText   dose dose text
     * @param notes      dose notes
     * @param disclaimer dose disclaimer
     */
    public void loadDose(String doseTable, String doseText, String notes, String disclaimer) {
        this.dose = new Dose(doseTable, doseText, notes, disclaimer);
    }

    /**
     * Gets the chemistry.
     *
     * @return chemistry
     */
    public Chemistry getChemistry() {
        return chemistry;
    }

    /**
     * Loads chemistry with a newly constructed Chemistry object from the arguments passed in.
     *
     * @param chemTable   chemistry chem table
     * @param moleculeUrl chemistry molecule url
     */
    public void loadChemistry(String chemTable, String moleculeUrl) {
        this.chemistry = new Chemistry(chemTable, moleculeUrl);
    }

    /**
     * Gets the researchChemical.
     *
     * @return researchChemical
     */
    public ResearchChemical getResearchChemical() {
        return researchChemical;
    }

    /**
     * Loads researchChemical with a newly constructed ResearchChemical object from the arguments passed in.
     *
     * @param summary  research chemical summary
     * @param imageUrl research chemical image url
     */
    public void loadResearchChemical(String summary, String imageUrl) {
        this.researchChemical = new ResearchChemical(summary, imageUrl);
    }

    /**
     * Class representing a substance's Basics page.
     */
    public class Basics {
        /**
         * Basics page description.
         */
        public String description;
        /**
         * Basics page effects.
         */
        public String effects;
        /**
         * Basics page problems.
         */
        public String problems;
        /**
         * Basics page disclaimer.
         */
        public String disclaimer;

        /**
         * Constructs a Basics object.
         *
         * @param description basics description
         * @param effects     basics effects
         * @param problems    basics problems
         * @param disclaimer  basics disclaimer
         */
        public Basics(String description, String effects, String problems, String disclaimer) {
            this.description = description;
            this.effects = effects;
            this.problems = problems;
            this.disclaimer = disclaimer;
        }

        /**
         * Gets the description.
         *
         * @return description
         */
        public String getDescription() {
            return this.description;
        }

        /**
         * Gets the effects.
         *
         * @return effects
         */
        public String getEffects() {
            return this.effects;
        }

        /**
         * Gets the problems.
         *
         * @return problems
         */
        public String getProblems() {
            return this.problems;
        }

        /**
         * Gets the disclaimer.
         *
         * @return disclaimer
         */
        public String getDisclaimer() {
            return this.disclaimer;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Basics{");
            sb.append("description='").append(description).append('\'');
            sb.append(", effects='").append(effects).append('\'');
            sb.append(", problems='").append(problems).append('\'');
            sb.append(", disclaimer='").append(disclaimer).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     * Class representing a substance's Effects page.
     */
    public class Effects {

        /**
         * Effects page positive effects.
         */
        public String positiveEffects;
        /**
         * Effects page neutral effects.
         */
        public String neutralEffects;
        /**
         * Effects page negative effects.
         */
        public String negativeEffects;
        /**
         * Effects page description.
         */
        public String description;
        /**
         * Effects page disclaimer.
         */
        public String disclaimer;

        /**
         * Constructs an Effects object.
         *
         * @param positiveEffects effects positive effects
         * @param neutralEffects  effects neutral effects
         * @param negativeEffects effects negative effects
         * @param description     effects description
         * @param disclaimer      effects disclaimer
         */
        public Effects(String positiveEffects, String neutralEffects, String negativeEffects, String description, String disclaimer) {
            this.positiveEffects = positiveEffects;
            this.neutralEffects = neutralEffects;
            this.negativeEffects = negativeEffects;
            this.description = description;
            this.disclaimer = disclaimer;
        }

        /**
         * Gets the positiveEffects.
         *
         * @return positiveEffects
         */
        public String getPositiveEffects() {
            return this.positiveEffects;
        }

        /**
         * Gets the neutralEffects.
         *
         * @return neutralEffects
         */
        public String getNeutralEffects() {
            return this.neutralEffects;
        }

        /**
         * Gets the negativeEffects.
         *
         * @return negativeEffects
         */
        public String getNegativeEffects() {
            return this.negativeEffects;
        }

        /**
         * Gets the description.
         *
         * @return description
         */
        public String getDescription() {
            return this.description;
        }

        /**
         * Gets the disclaimer.
         *
         * @return disclaimer
         */
        public String getDisclaimer() {
            return this.disclaimer;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Effects{");
            sb.append("positiveEffects='").append(positiveEffects).append('\'');
            sb.append(", neutralEffects='").append(neutralEffects).append('\'');
            sb.append(", negativeEffects='").append(negativeEffects).append('\'');
            sb.append(", description='").append(description).append('\'');
            sb.append(", disclaimer='").append(disclaimer).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     * Class representing a substance's Images page.
     */
    public class Images {
        /**
         * Images page image entry list.
         */
        public String imageEntryList;

        /**
         * Constructs an Images object.
         *
         * @param imageEntryList image image entry list
         */
        public Images(String imageEntryList) {
            this.imageEntryList = imageEntryList;
        }

        /**
         * Gets the imageEntryList.
         *
         * @return imageEntryList
         */
        public String getImageEntryList() {
            return this.imageEntryList;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Images{");
            sb.append("imageEntryList='").append(imageEntryList).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     * Class representing a substance's Health page.
     */
    public class Health {
        /**
         * Health page notes.
         */
        public String notes;
        /**
         * Health page deaths.
         */
        public String deaths;
        /**
         * Health page warnings.
         */
        public String warnings;
        /**
         * Health page cautions.
         */
        public String cautions;
        /**
         * Health page benefits.
         */
        public String benefits;

        /**
         * Constructs a Health object.
         *
         * @param notes    health notes
         * @param deaths   health deaths
         * @param warnings health warnings
         * @param cautions health cautions
         * @param benefits health benefits
         */
        public Health(String notes, String deaths, String warnings, String cautions, String benefits) {
            this.notes = notes;
            this.deaths = deaths;
            this.warnings = warnings;
            this.cautions = cautions;
            this.benefits = benefits;
        }

        /**
         * Gets the notes.
         *
         * @return notes
         */
        public String getNotes() {
            return notes;
        }

        /**
         * Gets the deaths.
         *
         * @return deaths
         */
        public String getDeaths() {
            return deaths;
        }

        /**
         * Gets the warnings.
         *
         * @return warnings
         */
        public String getWarnings() {
            return warnings;
        }

        /**
         * Gets the cautions.
         *
         * @return cautions
         */
        public String getCautions() {
            return cautions;
        }

        /**
         * Gets the benefits.
         *
         * @return benefits
         */
        public String getBenefits() {
            return benefits;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Health{");
            sb.append("notes='").append(notes).append('\'');
            sb.append(", deaths='").append(deaths).append('\'');
            sb.append(", warnings='").append(warnings).append('\'');
            sb.append(", cautions='").append(cautions).append('\'');
            sb.append(", benefits='").append(benefits).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     * Class representing a substance's Law page.
     */
    public class Law {
        /**
         * Law page legal table.
         */
        public String legalTable;
        /**
         * Law page federal law.
         */
        public String federalLaw;
        /**
         * Law page state law.
         */
        public String stateLaw;
        /**
         * Law page international law.
         */
        public String internationalLaw;
        /**
         * Law page disclaimer.
         */
        public String disclaimer;

        /**
         * Constructs a Law object.
         *
         * @param legalTable       law legal table
         * @param federalLaw       law federal law
         * @param stateLaw         law state law
         * @param internationalLaw law international law
         * @param disclaimer       law disclaimer
         */
        public Law(String legalTable, String federalLaw, String stateLaw, String internationalLaw, String disclaimer) {
            this.legalTable = legalTable;
            this.federalLaw = federalLaw;
            this.stateLaw = stateLaw;
            this.internationalLaw = internationalLaw;
            this.disclaimer = disclaimer;
        }

        /**
         * Gets the legalTable.
         *
         * @return legalTable
         */
        public String getLegalTable() {
            return legalTable;
        }

        /**
         * Gets the federalLaw.
         *
         * @return federalLaw
         */
        public String getFederalLaw() {
            return federalLaw;
        }

        /**
         * Gets the stateLaw.
         *
         * @return stateLaw
         */
        public String getStateLaw() {
            return stateLaw;
        }

        /**
         * Gets the internationLaw.
         *
         * @return internationLaw
         */
        public String getInternationalLaw() {
            return internationalLaw;
        }

        /**
         * Gets the disclaimer.
         *
         * @return disclaimer
         */
        public String getDisclaimer() {
            return disclaimer;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Law{");
            sb.append("legalTable='").append(legalTable).append('\'');
            sb.append(", federalLaw='").append(federalLaw).append('\'');
            sb.append(", stateLaw='").append(stateLaw).append('\'');
            sb.append(", internationalLaw='").append(internationalLaw).append('\'');
            sb.append(", disclaimer='").append(disclaimer).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     * Class representing a substance's Dose page.
     */
    public class Dose {
        /**
         * Dose page dose table.
         */
        public String doseTable;
        /**
         * Dose page dose text.
         */
        public String doseText;
        /**
         * Dose page notes.
         */
        public String notes;
        /**
         * Dose page disclaimer.
         */
        public String disclaimer;

        /**
         * Constructs a Dose object.
         *
         * @param doseTable  dose dose table
         * @param doseText   dose dose text
         * @param notes      dose notes
         * @param disclaimer dose disclaimer
         */
        public Dose(String doseTable, String doseText, String notes, String disclaimer) {
            this.doseTable = doseTable;
            this.doseText = doseText;
            this.notes = notes;
            this.disclaimer = disclaimer;
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
         * Gets the doseText.
         *
         * @return doseText
         */
        public String getDoseText() {
            return doseText;
        }

        /**
         * Gets the notes.
         *
         * @return notes
         */
        public String getNotes() {
            return notes;
        }

        /**
         * Gets the disclaimer.
         *
         * @return disclaimer
         */
        public String getDisclaimer() {
            return disclaimer;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Dose{");
            sb.append("doseTable='").append(doseTable).append('\'');
            sb.append(", doseText='").append(doseText).append('\'');
            sb.append(", notes='").append(notes).append('\'');
            sb.append(", disclaimer='").append(disclaimer).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     * Class representing a substance's Chemistry page.
     */
    public class Chemistry {
        /**
         * Chemistry page chem table.
         */
        public String chemTable;
        /**
         * Chemistry page molecule url.
         */
        public String moleculeUrl;

        /**
         * Constructs a Chemistry object.
         *
         * @param chemTable   chemistry chem table
         * @param moleculeUrl chemistry molecule url
         */
        public Chemistry(String chemTable, String moleculeUrl) {
            this.chemTable = chemTable;
            this.moleculeUrl = moleculeUrl;
        }

        /**
         * Gets the chemTable.
         *
         * @return chemTable
         */
        public String getChemTable() {
            return chemTable;
        }

        /**
         * Gets the moleculeUrl.
         *
         * @return moleculeUrl
         */
        public String getMoleculeUrl() {
            return moleculeUrl;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Chemistry{");
            sb.append("chemTable='").append(chemTable).append('\'');
            sb.append(", moleculeUrl='").append(moleculeUrl).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     * Class representing a substance's Research Chemical page.
     */
    public class ResearchChemical {
        /**
         * Research Chemical page summary.
         */
        public String summary;
        /**
         * Research Chemical page image url.
         */
        public String imageUrl;

        /**
         * Constructs a ResearchChemical object.
         *
         * @param summary  research chemical summary
         * @param imageUrl research chemical image url
         */
        public ResearchChemical(String summary, String imageUrl) {
            this.summary = summary;
            this.imageUrl = imageUrl;
        }

        /**
         * Gets the summary.
         *
         * @return summary
         */
        public String getSummary() {
            return summary;
        }

        /**
         * Gets the imageUrl.
         *
         * @return imageUrl
         */
        public String getImageUrl() {
            return imageUrl;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("ResearchChemical{");
            sb.append("summary='").append(summary).append('\'');
            sb.append(", imageUrl='").append(imageUrl).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Substance{");
        sb.append("imageUrl='").append(imageUrl).append('\'');
        sb.append(", effectsClassification='").append(effectsClassification).append('\'');
        sb.append(", botanicalClassification='").append(botanicalClassification).append('\'');
        sb.append(", chemicalName='").append(chemicalName).append('\'');
        sb.append(", commonNames='").append(commonNames).append('\'');
        sb.append(", uses='").append(uses).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

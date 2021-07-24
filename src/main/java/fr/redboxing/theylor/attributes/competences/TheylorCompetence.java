package fr.redboxing.theylor.attributes.competences;

public interface TheylorCompetence {
    String getNameTranslationKey();
    String getDescriptionTranslationKey();
    CompetenceType getCompetenceType();
    boolean getDefaultState();
}

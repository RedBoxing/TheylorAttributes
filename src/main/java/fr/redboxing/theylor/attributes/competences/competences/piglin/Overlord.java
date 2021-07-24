package fr.redboxing.theylor.attributes.competences.competences.piglin;

import fr.redboxing.theylor.attributes.competences.CompetenceType;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;

public class Overlord implements TheylorCompetence {
    @Override
    public String getNameTranslationKey() {
        return "theylor.attributes.competences.overlord";
    }

    @Override
    public String getDescriptionTranslationKey() {
        return "theylor.attributes.competences.descriptions.overlord";
    }

    @Override
    public CompetenceType getCompetenceType() {
        return CompetenceType.TERTIARY;
    }

    @Override
    public boolean getDefaultState() {
        return true;
    }
}

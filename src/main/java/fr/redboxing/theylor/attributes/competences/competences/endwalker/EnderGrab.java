package fr.redboxing.theylor.attributes.competences.competences.endwalker;

import fr.redboxing.theylor.attributes.competences.CompetenceType;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;

public class EnderGrab implements TheylorCompetence {
    @Override
    public String getNameTranslationKey() {
        return "theylor.attributes.competences.endergrab";
    }

    @Override
    public String getDescriptionTranslationKey() {
        return "theylor.attributes.competences.descriptions.endergrab";
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

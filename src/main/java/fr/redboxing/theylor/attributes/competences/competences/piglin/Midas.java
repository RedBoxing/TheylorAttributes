package fr.redboxing.theylor.attributes.competences.competences.piglin;

import fr.redboxing.theylor.attributes.competences.CompetenceType;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;

public class Midas implements TheylorCompetence {
    @Override
    public String getNameTranslationKey() {
        return "theylor.attributes.competences.midas";
    }

    @Override
    public String getDescriptionTranslationKey() {
        return "theylor.attributes.competences.descriptions.midas";
    }

    @Override
    public CompetenceType getCompetenceType() {
        return CompetenceType.PRIMARY;
    }

    @Override
    public boolean getDefaultState() {
        return true;
    }
}

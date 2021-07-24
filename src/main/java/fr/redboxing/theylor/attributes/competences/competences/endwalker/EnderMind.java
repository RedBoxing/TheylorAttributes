package fr.redboxing.theylor.attributes.competences.competences.endwalker;

import fr.redboxing.theylor.attributes.competences.CompetenceType;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;

public class EnderMind implements TheylorCompetence {
    @Override
    public String getNameTranslationKey() {
        return "theylor.attributes.competences.endermind";
    }

    @Override
    public String getDescriptionTranslationKey() {
        return "theylor.attributes.competences.descriptions.endermind";
    }

    @Override
    public CompetenceType getCompetenceType() {
        return CompetenceType.SECONDARY;
    }

    @Override
    public boolean getDefaultState() {
        return true;
    }
}

package fr.redboxing.theylor.attributes.classes.classes;

import fr.redboxing.theylor.attributes.classes.TheylorClass;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;
import fr.redboxing.theylor.attributes.competences.TheylorCompetences;

import java.util.Arrays;
import java.util.List;

public class Human implements TheylorClass {
    @Override
    public String getNameTranslationKey() {
        return "theylor.attributes.classes.human";
    }

    @Override
    public List<TheylorCompetence> getCompetences() {
        return Arrays.asList(TheylorCompetences.FRIENDSHIP_POWER);
    }
}

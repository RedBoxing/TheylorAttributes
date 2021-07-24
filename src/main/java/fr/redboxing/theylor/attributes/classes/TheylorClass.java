package fr.redboxing.theylor.attributes.classes;

import fr.redboxing.theylor.attributes.competences.TheylorCompetence;

import java.util.List;

public interface TheylorClass {
    String getNameTranslationKey();
    List<TheylorCompetence> getCompetences();
}

package fr.redboxing.theylor.attributes.competences;

import fr.redboxing.theylor.attributes.utils.Translations;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CompetenceType {
    PRIMARY("theylor.attributes.competences.primary"),
    SECONDARY("theylor.attributes.competences.secondary"),
    TERTIARY("theylor.attributes.competences.tertiary"),
    ULTIMATE("theylor.attributes.competences.ultimate");

    @Getter
    private final String translationKey;

    public String getTranslatedName() {
        return Translations.tr(this.translationKey);
    }
}

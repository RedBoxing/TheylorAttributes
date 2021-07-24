package fr.redboxing.theylor.attributes.commands.suggestions;

public class CompletionProviders {
    private static final ClassSuggestionProvider CLASS_SUGGESTION_PROVIDER = new ClassSuggestionProvider();
    private static final CompetenceSuggestionProvider COMPETENCE_SUGGESTION_PROVIDER = new CompetenceSuggestionProvider();

    public static ClassSuggestionProvider suggestedClasses() {
        return CLASS_SUGGESTION_PROVIDER;
    }

    public static CompetenceSuggestionProvider suggestedCompetences() {
        return COMPETENCE_SUGGESTION_PROVIDER;
    }
}

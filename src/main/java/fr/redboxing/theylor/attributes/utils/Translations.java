package fr.redboxing.theylor.attributes.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import fr.redboxing.theylor.attributes.TheylorSettings;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class Translations {
    private static Map<String, String> translationMap;

    public static String tr(String key)
    {
        return translationMap == null ? key : translationMap.getOrDefault(key, key);
    }

    public static String tr(String key, String str)
    {
        return translationMap == null ? str : translationMap.getOrDefault(key, str);
    }

    public static boolean hasTranslations()
    {
        return translationMap != null;
    }

    public static boolean hasTranslation(String key)
    {
        return translationMap != null && translationMap.containsKey(key);
    }

    public static Map<String, String> getTranslation()
    {
        String dataJSON;
        try
        {
            dataJSON = IOUtils.toString(
                    Objects.requireNonNull(Translations.class.getClassLoader().getResourceAsStream(String.format("assets/theylor/lang/%s.json", TheylorSettings.LANGUAGE))),
                    StandardCharsets.UTF_8);
        } catch (NullPointerException | IOException e) {
            return null;
        }
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        return gson.fromJson(dataJSON, new TypeToken<Map<String, String>>() {}.getType());
    }

    public static void updateTranslations() {
        translationMap = getTranslation();
    }
}

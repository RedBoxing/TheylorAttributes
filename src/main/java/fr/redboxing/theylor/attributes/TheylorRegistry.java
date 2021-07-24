package fr.redboxing.theylor.attributes;

import fr.redboxing.theylor.attributes.classes.TheylorClass;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TheylorRegistry {
    public static final Registry<TheylorClass> CLASSES = FabricRegistryBuilder.createSimple(TheylorClass.class, new Identifier(TheylorAttributes.MOD_ID, "classes")).buildAndRegister();
    public static final Registry<TheylorCompetence> COMPETENCES = FabricRegistryBuilder.createSimple(TheylorCompetence.class, new Identifier(TheylorAttributes.MOD_ID, "competences")).buildAndRegister();;
}

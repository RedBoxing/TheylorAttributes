package fr.redboxing.theylor.attributes.classes;

import fr.redboxing.theylor.attributes.TheylorRegistry;
import fr.redboxing.theylor.attributes.classes.classes.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static fr.redboxing.theylor.attributes.TheylorAttributes.MOD_ID;

public class TheylorClasses {
    public static TheylorClass HUMAN = register("human", new Human());
    public static TheylorClass END_WALKER = register("end_walker", new EndWalker());
    public static TheylorClass DRAGON = register("dragon", new Dragon());
    public static TheylorClass MAGMA_CUBE = register("magma_cube", new MagmaCube());
    public static TheylorClass PIGLIN = register("piglin", new Piglin());

    private static TheylorClass register(String name, TheylorClass theylorClass) {
        return Registry.register(TheylorRegistry.CLASSES, new Identifier(MOD_ID, name), theylorClass);
    }
}

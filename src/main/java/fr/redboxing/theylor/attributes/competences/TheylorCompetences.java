package fr.redboxing.theylor.attributes.competences;

import fr.redboxing.theylor.attributes.TheylorRegistry;
import fr.redboxing.theylor.attributes.competences.competences.*;
import fr.redboxing.theylor.attributes.competences.competences.dragon.DragonDash;
import fr.redboxing.theylor.attributes.competences.competences.dragon.DragonRush;
import fr.redboxing.theylor.attributes.competences.competences.dragon.Forcefield;
import fr.redboxing.theylor.attributes.competences.competences.dragon.Sniper;
import fr.redboxing.theylor.attributes.competences.competences.endwalker.EnderGrab;
import fr.redboxing.theylor.attributes.competences.competences.endwalker.EnderMind;
import fr.redboxing.theylor.attributes.competences.competences.endwalker.EnderWave;
import fr.redboxing.theylor.attributes.competences.competences.endwalker.Teleportation;
import fr.redboxing.theylor.attributes.competences.competences.human.FriendshipPower;
import fr.redboxing.theylor.attributes.competences.competences.magmacube.MagmaBuff;
import fr.redboxing.theylor.attributes.competences.competences.magmacube.Stridder;
import fr.redboxing.theylor.attributes.competences.competences.piglin.Bruteforce;
import fr.redboxing.theylor.attributes.competences.competences.piglin.GoldDigger;
import fr.redboxing.theylor.attributes.competences.competences.piglin.Midas;
import fr.redboxing.theylor.attributes.competences.competences.piglin.Overlord;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static fr.redboxing.theylor.attributes.TheylorAttributes.MOD_ID;

public class TheylorCompetences {
    public static TheylorCompetence TELEPORTATION = register("teleportation", new Teleportation());
    public static TheylorCompetence ENDERMIND = register("endermind", new EnderMind());
    public static TheylorCompetence ENDERGRAB = register("endergrab", new EnderGrab());
    public static TheylorCompetence ENDERWAVE = register("enderwave", new EnderWave());
    public static TheylorCompetence LAST_RESSOURCE = register("last_ressource", new LastResource());
    public static TheylorCompetence FORCEFIELD = register("forcefield", new Forcefield());
    public static TheylorCompetence SNIPER = register("sniper", new Sniper());
    public static TheylorCompetence DRAGON_DASH = register("dragon_dash", new DragonDash());
    public static TheylorCompetence DRAGON_RUSH = register("dragon_rush", new DragonRush());
    public static TheylorCompetence MAGMA_BUFF = register("magma_buff", new MagmaBuff());
    public static TheylorCompetence STRIDDER = register("stridder", new Stridder());
    public static TheylorCompetence FRIENDSHIP_POWER = register("friendship_power", new FriendshipPower());
    public static TheylorCompetence GOLD_DIGGER = register("gold_digger", new GoldDigger());
    public static TheylorCompetence MIDAS = register("midas", new Midas());
    public static TheylorCompetence OVERLORD = register("overlord", new Overlord());
    public static TheylorCompetence BRUTEFORCE = register("bruteforce", new Bruteforce());

    private static TheylorCompetence register(String name, TheylorCompetence competence) {
        return Registry.register(TheylorRegistry.COMPETENCES, new Identifier(MOD_ID, name), competence);
    }
}

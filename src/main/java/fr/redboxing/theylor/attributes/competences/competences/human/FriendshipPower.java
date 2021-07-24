package fr.redboxing.theylor.attributes.competences.competences.human;

import fr.redboxing.theylor.attributes.classes.TheylorClasses;
import fr.redboxing.theylor.attributes.competences.CompetenceType;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;
import fr.redboxing.theylor.attributes.events.ServerPlayerEntityTickCallback;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;

public class FriendshipPower implements TheylorCompetence {
    public FriendshipPower() {
        ServerPlayerEntityTickCallback.EVENT.register(this::tick);
    }

    private void tick(ServerPlayerEntity player) {
        TheylorPlayer theylorPlayer = TheylorPlayer.get(player);
        int humans = player.getServerWorld().getPlayers(s -> s.isInRange(player, 30) && TheylorPlayer.get(s).isClass(TheylorClasses.HUMAN)).size();
        if(humans > 3) humans = 3;

        if(theylorPlayer.hasCompetenceEnabled(this) && player.age % 10 == 0 && humans > 1) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 10 * 20, humans - 1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 10 * 20, humans - 1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 10 * 20, humans - 1));
        }
    }

    @Override
    public String getNameTranslationKey() {
        return "theylor.attributes.competences.friendship_power";
    }

    @Override
    public String getDescriptionTranslationKey() {
        return "theylor.attributes.competences.descriptions.friendship_power";
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

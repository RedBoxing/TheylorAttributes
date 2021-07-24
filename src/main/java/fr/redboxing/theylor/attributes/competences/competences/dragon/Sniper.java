package fr.redboxing.theylor.attributes.competences.competences.dragon;

import fr.redboxing.theylor.attributes.competences.CompetenceType;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;
import fr.redboxing.theylor.attributes.competences.TheylorCompetences;
import fr.redboxing.theylor.attributes.events.LivingEntityDamageCallback;
import fr.redboxing.theylor.attributes.events.ServerPlayerEntityDamageCallback;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class Sniper implements TheylorCompetence {
    public Sniper() {
        ServerPlayerEntityDamageCallback.EVENT.register(this::onDamage);
    }

    public float onDamage(ServerPlayerEntity player, DamageSource source, float amount) {
        if(source instanceof ProjectileDamageSource) {
            ProjectileDamageSource projectileDamageSource = (ProjectileDamageSource) source;
            if(projectileDamageSource.getAttacker() != null && (projectileDamageSource.getAttacker() instanceof PlayerEntity)) {
                TheylorPlayer theylorPlayer = TheylorPlayer.get((PlayerEntity) projectileDamageSource.getAttacker());
                if(theylorPlayer.hasCompetenceEnabled(this)) {
                    return amount * 2.5F;
                } else if(theylorPlayer.hasCompetenceEnabled(TheylorCompetences.DRAGON_RUSH) && theylorPlayer.getCooldownManager().isCoolingDown(TheylorCompetences.DRAGON_RUSH)) {
                    return amount * 4.0F;
                }
            }
        }

        return -1;
    }

    @Override
    public String getNameTranslationKey() {
        return "theylor.attributes.competences.sniper";
    }

    @Override
    public String getDescriptionTranslationKey() {
        return "theylor.attributes.competences.descriptions.sniper";
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

package fr.redboxing.theylor.attributes.competences.competences.magmacube;

import fr.redboxing.theylor.attributes.classes.classes.MagmaCube;
import fr.redboxing.theylor.attributes.competences.CompetenceType;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;
import fr.redboxing.theylor.attributes.competences.TheylorCompetences;
import fr.redboxing.theylor.attributes.events.LivingEntityDamageCallback;
import fr.redboxing.theylor.attributes.events.ServerPlayerEntityDamageCallback;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public class MagmaBuff implements TheylorCompetence {
    public MagmaBuff() {
        LivingEntityDamageCallback.EVENT.register(this::onDamage);
    }

    private ActionResult onDamage(LivingEntity entity, DamageSource source, float amount) {
        if((entity instanceof ServerPlayerEntity) && (source == DamageSource.IN_FIRE || source == DamageSource.ON_FIRE || source == DamageSource.HOT_FLOOR || source == DamageSource.LAVA || source == DamageSource.CRAMMING)) {
            TheylorPlayer theylorPlayer = TheylorPlayer.get((PlayerEntity) entity);
            if(theylorPlayer.hasCompetenceEnabled(this)) {
                return ActionResult.FAIL;
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public String getNameTranslationKey() {
        return "theylor.attributes.competences.magma_buff";
    }

    @Override
    public String getDescriptionTranslationKey() {
        return "theylor.attributes.competences.descriptions.magma_buff";
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

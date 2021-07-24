package fr.redboxing.theylor.attributes.classes.classes;

import fr.redboxing.theylor.attributes.classes.TheylorClass;
import fr.redboxing.theylor.attributes.classes.TheylorClasses;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;
import fr.redboxing.theylor.attributes.competences.TheylorCompetences;
import fr.redboxing.theylor.attributes.events.LivingEntityDamageCallback;
import fr.redboxing.theylor.attributes.events.ServerPlayerEntityDamageCallback;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

import java.util.Arrays;
import java.util.List;

public class Dragon implements TheylorClass {
    /*public Dragon() {
        LivingEntityDamageCallback.EVENT.register(this::onDamage);
    }

   /* public ActionResult onDamage(LivingEntity entity, DamageSource source, float amount) {
        if((entity instanceof PlayerEntity) && source == DamageSource.GENERIC) {
            if(entity.getAttacker() != null && (entity.getAttacker() instanceof PlayerEntity)) {
                TheylorPlayer theylorPlayer = TheylorPlayer.get((PlayerEntity) entity);
                if(theylorPlayer != null && theylorPlayer.isClass(TheylorClasses.DRAGON)) {
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 3 * 20, 1));
                }
            }
        }

        return ActionResult.PASS;
    }*/

    @Override
    public String getNameTranslationKey() {
        return "theylor.attributes.classes.dragon";
    }

    @Override
    public List<TheylorCompetence> getCompetences() {
        return Arrays.asList(TheylorCompetences.FORCEFIELD, TheylorCompetences.SNIPER, TheylorCompetences.DRAGON_DASH, TheylorCompetences.DRAGON_RUSH, TheylorCompetences.LAST_RESSOURCE);
    }
}

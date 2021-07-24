package fr.redboxing.theylor.attributes.competences.competences.piglin;

import fr.redboxing.theylor.attributes.competences.CompetenceType;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;
import fr.redboxing.theylor.attributes.events.PlayerEntityDamageCallback;
import fr.redboxing.theylor.attributes.events.ServerPlayerEntityDamageCallback;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;

public class GoldDigger implements TheylorCompetence {
    public GoldDigger() {
        PlayerEntityDamageCallback.EVENT.register(this::modifyDamage);
        //ServerPlayerEntityDamageCallback.EVENT.register(this::modifyDamageTaken);
    }

 /*   private float modifyDamageTaken(ServerPlayerEntity playerEntity, DamageSource source, float amount) {
        if(source == DamageSource.GENERIC || source instanceof EntityDamageSource || source instanceof ProjectileDamageSource) {
            if (TheylorPlayer.get(playerEntity).hasCompetenceEnabled(this)) {
                return amount / 2;
            }
        }

        return amount;
    }*/

    public float modifyDamage(PlayerEntity entity, Entity target, DamageSource source, float f) {
        if(entity.getMainHandStack().getItem() == Items.GOLDEN_SWORD) {
            if(TheylorPlayer.get(entity).hasCompetenceEnabled(this)) {
                return f * 2;
            }
        }

        return f;
    }

    @Override
    public String getNameTranslationKey() {
        return "theylor.attributes.competences.gold_digger";
    }

    @Override
    public String getDescriptionTranslationKey() {
        return "theylor.attributes.competences.descriptions.gold_digger";
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

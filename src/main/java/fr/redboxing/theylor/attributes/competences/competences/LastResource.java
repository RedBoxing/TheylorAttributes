package fr.redboxing.theylor.attributes.competences.competences;

import fr.redboxing.theylor.attributes.competences.CompetenceType;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;
import fr.redboxing.theylor.attributes.competences.TheylorCompetences;
import fr.redboxing.theylor.attributes.entity.RidableEnderDragon;
import fr.redboxing.theylor.attributes.events.ServerPlayerEntityTickCallback;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;

public class LastResource implements TheylorCompetence {
    private static int ticks;
    private static TheylorPlayer first;
    private static TheylorPlayer second;
    private static boolean active = false;

    public LastResource() {
        ServerPlayerEntityTickCallback.EVENT.register(this::tick);
    }

    private void tick(ServerPlayerEntity playerEntity) {
        ++ticks;

        TheylorPlayer theylorPlayer = TheylorPlayer.get(playerEntity);
        if(ticks % 20 == 0 && theylorPlayer.hasCompetenceEnabled(TheylorCompetences.LAST_RESSOURCE)) {
            System.out.println("first " + first);
            System.out.println("second " + second);

            if(first == null) {
                first = theylorPlayer;
            }

            if(second == null) {
                TargetPredicate targetPredicate = TargetPredicate.createAttackable();
                targetPredicate.setPredicate(entity -> TheylorPlayer.get((PlayerEntity) entity).hasCompetenceEnabled(this) && entity.isInRange(playerEntity, 100));

                PlayerEntity otherPlayer = playerEntity.getServerWorld().getClosestPlayer(targetPredicate, playerEntity);

                if (otherPlayer == null) {
                    playerEntity.getServerWorld().spawnParticles(ParticleTypes.WITCH, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), 100, 1, 1, 1, 1);
                } else {
                    second = TheylorPlayer.get(otherPlayer);
                }
            } else {
                playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 3 * 20, 1));
                playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 3 * 20, 2));
                playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 3 * 20, 1));

                if (first == theylorPlayer && !active) {
                    active = true;
                    RidableEnderDragon ridableEnderDragon = new RidableEnderDragon(playerEntity.getServerWorld());
                    ridableEnderDragon.setPosition(playerEntity.getPos());
                    playerEntity.getServerWorld().spawnEntity(ridableEnderDragon);
                    playerEntity.startRiding(ridableEnderDragon, true);
                }
            }
        }
    }

    @Override
    public String getNameTranslationKey() {
        return "theylor.attributes.competences.last_resource";
    }

    @Override
    public String getDescriptionTranslationKey() {
        return "theylor.attributes.competences.descriptions.last_resource";
    }

    @Override
    public CompetenceType getCompetenceType() {
        return CompetenceType.ULTIMATE;
    }

    @Override
    public boolean getDefaultState() {
        return false;
    }

    public static TheylorPlayer getFirst() {
        return first;
    }

    public static void setFirst(TheylorPlayer first) {
        LastResource.first = first;
    }

    public static TheylorPlayer getSecond() {
        return second;
    }

    public static void setSecond(TheylorPlayer second) {
        LastResource.second = second;
    }

    public static boolean isActive() {
        return active;
    }

    public static void setActive(boolean active) {
        LastResource.active = active;
    }
}

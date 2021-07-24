package fr.redboxing.theylor.attributes.classes.classes;

import fr.redboxing.theylor.attributes.classes.TheylorClass;
import fr.redboxing.theylor.attributes.classes.TheylorClasses;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;
import fr.redboxing.theylor.attributes.competences.TheylorCompetences;
import fr.redboxing.theylor.attributes.events.LivingEntityDamageCallback;
import fr.redboxing.theylor.attributes.events.ServerPlayerEntityTickCallback;
import fr.redboxing.theylor.attributes.interfaces.IServerPlayerEntity;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.MathHelper;

import java.util.*;

public class EndWalker implements TheylorClass {
 /*   private static Map<UUID, Integer> cooldowns = new HashMap<>();
    private static int ticks;

    public EndWalker() {
        LivingEntityDamageCallback.EVENT.register(this::onDamage);
        ServerPlayerEntityTickCallback.EVENT.register(this::tick);
    }

    private void tick(ServerPlayerEntity playerEntity) {
        ++ticks;

        if(ticks % 5 == 0 && cooldowns.containsKey(playerEntity.getUuid())) {
            int cooldown = cooldowns.get(playerEntity.getUuid());
            if(cooldown > 0) {
                cooldowns.replace(playerEntity.getUuid(), cooldown - 1);
            }
        }
    }*/

   /* private ActionResult onDamage(LivingEntity entity, DamageSource source, float amount) {
        if(!(source instanceof ProjectileDamageSource) || !source.getName().equals("arrow")) return ActionResult.PASS;
        if(entity instanceof ServerPlayerEntity) {
            TheylorPlayer theylorPlayer = ((IServerPlayerEntity) entity).getTheylorPlayer();
            if(theylorPlayer.isClass(TheylorClasses.END_WALKER)) {
                ServerWorld world = ((ServerPlayerEntity) entity).getServerWorld();

                if(!cooldowns.containsKey(entity.getUuid())) cooldowns.put(entity.getUuid(), 0);
                if(cooldowns.get(entity.getUuid()) == 0) {
                    for (int i = 0; i < 16; ++i) {
                        double x = entity.getX() + (entity.getRandom().nextDouble() - 0.5D) * 16.0D;
                        double y = MathHelper.clamp(entity.getY() + (double) (entity.getRandom().nextInt(16) - 8), (double) world.getBottomY(), (double) (world.getBottomY() + ((ServerWorld) world).getLogicalHeight() - 1));
                        double z = entity.getZ() + (entity.getRandom().nextDouble() - 0.5D) * 16.0D;
                        if (entity.hasVehicle()) {
                            entity.stopRiding();
                        }

                        if(cooldowns.containsKey(entity.getUuid())) {
                            cooldowns.replace(entity.getUuid(), 3);
                        } else {
                            cooldowns.put(entity.getUuid(), 3);
                        }

                        if (entity.teleport(x, y, z, true)) {
                            world.playSound(null, entity.prevX, entity.prevY, entity.prevZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                            entity.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                            return ActionResult.FAIL;
                        }
                    }
                }
            }
        }

        return ActionResult.PASS;
    }*/

    @Override
    public String getNameTranslationKey() {
        return "theylor.attributes.classes.endwalker";
    }

    @Override
    public List<TheylorCompetence> getCompetences() {
        return Arrays.asList(TheylorCompetences.TELEPORTATION, TheylorCompetences.ENDERMIND, TheylorCompetences.ENDERGRAB, TheylorCompetences.ENDERWAVE, TheylorCompetences.LAST_RESSOURCE);
    }
}

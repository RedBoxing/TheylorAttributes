package fr.redboxing.theylor.attributes.classes.classes;

import fr.redboxing.theylor.attributes.classes.TheylorClass;
import fr.redboxing.theylor.attributes.classes.TheylorClasses;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;
import fr.redboxing.theylor.attributes.competences.TheylorCompetences;
import fr.redboxing.theylor.attributes.events.ServerPlayerEntityTickCallback;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MagmaCube implements TheylorClass {
    private static int ticks;

    public MagmaCube() {
        ServerPlayerEntityTickCallback.EVENT.register(this::tick);
    }

    private void tick(ServerPlayerEntity playerEntity) {
        ++ticks;
        if(ticks % 20 == 0 && TheylorPlayer.get(playerEntity).isClass(this) && playerEntity.isTouchingWater()) {
            playerEntity.damage(DamageSource.GENERIC, 1.0F);
        }
    }

    @Override
    public String getNameTranslationKey() {
        return "theylor.attributes.classes.magma_cube";
    }

    @Override
    public List<TheylorCompetence> getCompetences() {
        return Arrays.asList(TheylorCompetences.MAGMA_BUFF, TheylorCompetences.STRIDDER);
    }
}

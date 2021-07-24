package fr.redboxing.theylor.attributes.player;

import fr.redboxing.theylor.attributes.TheylorAttributes;
import fr.redboxing.theylor.attributes.TheylorRegistry;
import fr.redboxing.theylor.attributes.classes.TheylorClass;
import fr.redboxing.theylor.attributes.classes.TheylorClasses;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;
import fr.redboxing.theylor.attributes.events.ServerPlayerEntityTickCallback;
import fr.redboxing.theylor.attributes.interfaces.IServerPlayerEntity;
import fr.redboxing.theylor.attributes.sql.Table;
import fr.redboxing.theylor.attributes.utils.CooldownManager;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TheylorPlayer {
    @Getter
    @NotNull
    private TheylorClass theylorClass;

    @Getter
    private final UUID uuid;

    @Getter
    @Setter
    private List<TheylorCompetence> competences = new ArrayList<>();

    @Getter
    @Setter
    private Map<TheylorCompetence, Boolean> competenceStates = new HashMap<>();

    @Getter
    private final CooldownManager cooldownManager;

    public TheylorPlayer(UUID uuid) {
        this.uuid = uuid;
        this.cooldownManager = new CooldownManager();

        setTheylorClass(TheylorAttributes.getClassFromDatabase(uuid));

        ServerPlayerEntityTickCallback.EVENT.register(this::tick);
    }

    private void tick(ServerPlayerEntity playerEntity) {
        this.cooldownManager.update();
    }

    public boolean isClass(TheylorClass theylorClass) {
        return this.theylorClass == theylorClass;
    }

    public List<TheylorCompetence> getClassCompetences() {
        return this.theylorClass.getCompetences() != null ? this.theylorClass.getCompetences() : new ArrayList<>();
    }

    public boolean hasCompetenceEnabled(TheylorCompetence competence) {
        return getClassCompetences().contains(competence) && this.competences.contains(competence) && this.competenceStates.get(competence);
    }

    public boolean hasCompetence(TheylorCompetence competence) {
        return getClassCompetences().contains(competence);
    }

    public boolean getCompetenceState(TheylorCompetence competence) {
        return this.competenceStates.get(competence);
    }

    public void setCompetenceState(TheylorCompetence competence, boolean state) {
        if(hasCompetence(competence)) {
            this.competenceStates.replace(competence, state);
        }
    }

    public void setTheylorClass(Identifier identifier) {
        this.theylorClass =  TheylorRegistry.CLASSES.get(identifier);
        this.competences.clear();
        for(TheylorCompetence competence : getClassCompetences()) {
            this.competences.add(competence);
            this.competenceStates.put(competence, competence.getDefaultState());
        }
    }

    public void setTheylorClass(TheylorClass theylorClass) {
        this.theylorClass =  theylorClass;
        this.competences.clear();
        for(TheylorCompetence competence : getClassCompetences()) {
            this.competences.add(competence);
            this.competenceStates.put(competence, competence.getDefaultState());
        }
    }

    public ServerPlayerEntity getPlayer(MinecraftServer server) {
        return server.getPlayerManager().getPlayer(this.uuid);
    }

    public static TheylorPlayer get(PlayerEntity player) {
        if(player instanceof ServerPlayerEntity) {
            return ((IServerPlayerEntity) player).getTheylorPlayer();
        }

        return null;
    }
}

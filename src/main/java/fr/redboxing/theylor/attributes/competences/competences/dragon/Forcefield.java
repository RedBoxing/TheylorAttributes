package fr.redboxing.theylor.attributes.competences.competences.dragon;

import fr.redboxing.theylor.attributes.competences.CompetenceType;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;
import fr.redboxing.theylor.attributes.competences.TheylorCompetences;
import fr.redboxing.theylor.attributes.events.ServerPlayerEntityTickCallback;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class Forcefield implements TheylorCompetence {
    public Forcefield() {
        ServerPlayerEntityTickCallback.EVENT.register(this::tick);
    }

    private void tick(ServerPlayerEntity player) {
        TheylorPlayer theylorPlayer = TheylorPlayer.get(player);
        if(theylorPlayer.hasCompetenceEnabled(this) && player.isSneaking() && player.getPitch() == -90 && player.getBlockStateAtPos().getBlock() != Blocks.LADDER) {
            for(Entity entity : player.getServerWorld().getOtherEntities(player, Box.of(player.getPos(), 10, 10, 10))) {
                Entity entity1 = entity;

                if(entity1.getVehicle() != null) {
                    entity1 = entity1.getVehicle();
                }

                velocity(entity1, getTrajectory2d(player, entity1), 1.6, true, 0.8, 0, 10);
            }
        }
    }

    public Vec3d setY(Vec3d vec3d, double y) {
        return new Vec3d(vec3d.getX(), y, vec3d.getZ());
    }

    public Vec3d getTrajectory2d(Entity from, Entity to){
        Vec3d vec3d = to.getPos().subtract(from.getPos());
        return setY(vec3d, 0).normalize();
    }

    public void velocity(Entity ent, Vec3d vec, double str, boolean ySet, double yBase, double yAdd, double yMax) {
        if (Double.isNaN(vec.getX()) || Double.isNaN(vec.getY()) || Double.isNaN(vec.getZ()) || vec.length() == 0)
            return;
        if (ySet)
            vec = setY(vec, yBase);
        vec.normalize();
        vec.multiply(str);
        vec = setY(vec, vec.getY() + yAdd);
        if (vec.getY() > yMax)
            vec = setY(vec, yMax);
        ent.fallDistance = 0;
        ent.setVelocity(vec);
    }

    @Override
    public String getNameTranslationKey() {
        return "theylor.attributes.competences.forcefield";
    }

    @Override
    public String getDescriptionTranslationKey() {
        return "theylor.attributes.competences.descriptions.forcefield";
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

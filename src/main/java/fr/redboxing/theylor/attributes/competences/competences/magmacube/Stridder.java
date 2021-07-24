package fr.redboxing.theylor.attributes.competences.competences.magmacube;

import fr.redboxing.theylor.attributes.competences.CompetenceType;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;
import fr.redboxing.theylor.attributes.events.PlayerMoveCallback;
import fr.redboxing.theylor.attributes.events.ServerWorldsTickCallback;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Stridder implements TheylorCompetence, Runnable {
    private final HashMap<Location, Integer> blocks = new HashMap<>();

    public Stridder() {
        PlayerMoveCallback.EVENT.register(this::onPlayerMove);
        ServerWorldsTickCallback.EVENT.register(this::tick);
    }

    private void tick(int ticks) {
        if(ticks % 20 == 0) {
            this.run();
        }
    }

    private void onPlayerMove(ServerPlayerEntity player, BlockPos blockPos, World world) {
        TheylorPlayer theylorPlayer = TheylorPlayer.get(player);
        if(theylorPlayer.hasCompetenceEnabled(this)) {
            if(player.isFallFlying() || !player.isOnGround() || player.hasVehicle()) return;

            BlockState blockState = Blocks.MAGMA_BLOCK.getDefaultState();
            float f = (float)Math.min(16, 5);
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            Iterator var7 = BlockPos.iterate(blockPos.add((-f), -1.0D, (-f)), blockPos.add(f, -1.0D, f)).iterator();

            while(var7.hasNext()) {
                BlockPos blockPos2 = (BlockPos)var7.next();
                if (blockPos2.isWithinDistance(player.getPos(), f)) {
                    mutable.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
                    BlockState blockState2 = world.getBlockState(mutable);
                    if (blockState2.isAir()) {
                        BlockState blockState3 = world.getBlockState(blockPos2);
                        if (blockState3.getMaterial() == Material.LAVA && blockState3.get(FluidBlock.LEVEL) == 0 && blockState.canPlaceAt(world, blockPos2) && world.canPlace(blockState, blockPos2, ShapeContext.absent())) {
                            world.setBlockState(blockPos2, blockState);
                            //world.getBlockTickScheduler().schedule(blockPos2, Blocks.MAGMA_BLOCK, MathHelper.nextInt(player.getRandom(), 60, 120));
                            //world.getBlockTickScheduler().schedule(blockPos2, Blocks.MAGMA_BLOCK, 1);
                            blocks.put(new Location(blockPos2, world), 5);
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getNameTranslationKey() {
        return "theylor.attributes.competences.stridder";
    }

    @Override
    public String getDescriptionTranslationKey() {
        return "theylor.attributes.competences.descriptions.stridder";
    }

    @Override
    public CompetenceType getCompetenceType() {
        return CompetenceType.SECONDARY;
    }

    @Override
    public boolean getDefaultState() {
        return true;
    }

    @Override
    public void run() {
        if(this.blocks.size() == 0) return;
        
        HashMap<Location, Integer> temp = (HashMap<Location, Integer>) this.blocks.clone();
        for(Map.Entry<Location, Integer> entry : temp.entrySet()) {
            Location loc = entry.getKey();
            Integer age = entry.getValue();

            BlockPos blockPos = loc.pos;
            World world = loc.world;

            if(age > 0) {
                this.blocks.replace(loc, age - 1);
            } else {
                world.setBlockState(blockPos, Blocks.LAVA.getDefaultState());
                this.blocks.remove(loc);
            }
        }
    }

    @RequiredArgsConstructor
    static class Location {
        public final BlockPos pos;
        public final World world;
    }
}

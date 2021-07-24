package fr.redboxing.theylor.attributes.mixin.net.minecraft.block;

import fr.redboxing.theylor.attributes.TheylorAttributes;
import fr.redboxing.theylor.attributes.competences.TheylorCompetences;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AbstractBlock.class)
public class MixinAbstractBlocks {
    /**
     * @author RedBoxing
     */
    @Deprecated
    @Overwrite
    public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
        float f = state.getHardness(world, pos);

        if(TheylorAttributes.getBreakables().contains(state.getBlock()) && player.getMainHandStack().isEmpty()) {
            TheylorPlayer theylorPlayer = TheylorPlayer.get(player);
            if(theylorPlayer != null && theylorPlayer.hasCompetenceEnabled(TheylorCompetences.ENDERGRAB)) {
                f = 0.8F;
            }
        }

        if (f == -1.0F) {
            return 0.0F;
        } else {
            int i = player.canHarvest(state) ? 30 : 100;
            return player.getBlockBreakingSpeed(state) / f / (float)i;
        }
    }
}

package fr.redboxing.theylor.attributes.mixin.net.minecraft.server.network;

import fr.redboxing.theylor.attributes.TheylorAttributes;
import fr.redboxing.theylor.attributes.competences.TheylorCompetences;
import fr.redboxing.theylor.attributes.interfaces.IServerPlayerEntity;
import fr.redboxing.theylor.attributes.mixin.net.minecraft.entity.IMixinEntity;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerActionResponseS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldEventS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class MixinServerPlayerInteractionManager {
    @Shadow
    @Final
    protected ServerPlayerEntity player;

    @Shadow protected ServerWorld world;

    @Shadow private int blockBreakingProgress;

    @Shadow private boolean mining;

    @Shadow private BlockPos miningPos;

    @Shadow public abstract boolean tryBreakBlock(BlockPos pos);

    private int id;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(ServerPlayerEntity player, CallbackInfo ci) {
        this.id = ((IMixinEntity) this.player).getCURRENT_ID().incrementAndGet();
    }

    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockBreakingInfo(ILnet/minecraft/util/math/BlockPos;I)V"))
    private void redirectUpdateSetBlockBreakingInfo(ServerWorld world, int entityId, BlockPos pos, int progress) {
        world.setBlockBreakingInfo(id, pos, progress);
    }

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;continueMining(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;I)F", ordinal = 1, shift = At.Shift.AFTER))
    private void update(CallbackInfo ci) {
        if(this.blockBreakingProgress >= 10 && TheylorAttributes.getBreakables().contains(this.world.getBlockState(this.miningPos).getBlock())) {
            this.mining = false;
            this.world.setBlockBreakingInfo(id, this.miningPos, -1);

            BlockState blockState = this.world.getBlockState(this.miningPos);
            if (this.tryBreakBlock(this.miningPos)) {
                this.player.networkHandler.sendPacket(new WorldEventS2CPacket(2001, this.miningPos, Block.getRawIdFromState(blockState), false));
                this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(this.miningPos, blockState, PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, true, "destroyed"));
            } else {
                this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(this.miningPos, blockState, PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, false, "destroyed"));
            }
        }
    }

    @Redirect(method = "continueMining", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockBreakingInfo(ILnet/minecraft/util/math/BlockPos;I)V"))
    private void redirectContinueMiningSetBlockBreakingInfo(ServerWorld world, int entityId, BlockPos pos, int progress) {
        world.setBlockBreakingInfo(id, pos, progress);
    }

    @Redirect(method = "processBlockBreakingAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockBreakingInfo(ILnet/minecraft/util/math/BlockPos;I)V"))
    private void redirectBlockBreakingActionSetBlockBreakingInfo(ServerWorld world, int entityId, BlockPos pos, int progress) {
        world.setBlockBreakingInfo(id, pos, progress);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBroken(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"), method = "tryBreakBlock", locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onBlockBroken(BlockPos pos, CallbackInfoReturnable<Boolean> cir, BlockState state, BlockEntity entity, Block block, boolean b1) {
        if(player.isCreative()) return;

        TheylorPlayer theylorPlayer = ((IServerPlayerEntity) player).getTheylorPlayer();
        if(theylorPlayer.hasCompetenceEnabled(TheylorCompetences.ENDERGRAB)) {
            ItemStack stack = player.getMainHandStack();
            if(!stack.isEmpty()) return;

            ItemStack itemStack = new ItemStack(state.getBlock());
            if(block instanceof BlockWithEntity) {
                if(entity != null) {
                    NbtCompound nbt = new NbtCompound();
                    entity.writeNbt(nbt);
                    itemStack.getOrCreateSubNbt("BlockEntityTag").copyFrom(nbt);
                }
            }

            Block.dropStack(world, pos, itemStack);

            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @ModifyArgs(method = "tryBreakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;afterBreak(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/item/ItemStack;)V"))
    private void tryBreakBlock(Args args) {
        ItemStack stack = args.get(5);
        if(TheylorPlayer.get(player).hasCompetenceEnabled(TheylorCompetences.GOLD_DIGGER) && stack.getItem() == Items.GOLDEN_PICKAXE) {
            ItemStack itemStack = new ItemStack(Items.DIAMOND_PICKAXE);
            itemStack.setNbt(stack.getNbt());
            args.set(5, itemStack);
        }
    }
}

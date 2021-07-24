package fr.redboxing.theylor.attributes.competences.competences.endwalker;

import fr.redboxing.theylor.attributes.competences.CompetenceType;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class Teleportation implements TheylorCompetence {
    public Teleportation() {
        UseItemCallback.EVENT.register(this::onInteract);
    }

    private TypedActionResult<ItemStack> onInteract(PlayerEntity player, World world, Hand hand) {
        TheylorPlayer theylorPlayer = TheylorPlayer.get(player);
        if(theylorPlayer.hasCompetenceEnabled(this) && hand == Hand.MAIN_HAND && !player.isSneaking()) {
            ItemStack itemStack = player.getMainHandStack();
            if(!(itemStack.getItem() instanceof SwordItem)) return TypedActionResult.pass(ItemStack.EMPTY);
            if(!tryTeleport(world, player, hand)) {
                player.sendMessage(new LiteralText("Failed to teleport you").formatted(Formatting.RED), true);
            }
            return TypedActionResult.success(itemStack);
        }

        return TypedActionResult.pass(ItemStack.EMPTY);
    }

    private boolean tryTeleport(World world, PlayerEntity player, Hand hand) {
        Vec3d targetVec = player.getPos().add(0, player.getEyeHeight(player.getPose()), 0);
        Vec3d lookVec = player.getRotationVector();
        BlockPos target = null;
        for (double i = 8; i >= 1; i -= 0.5) {
            Vec3d v3d = targetVec.add(lookVec.multiply(i, i, i));
            target = new BlockPos(Math.round(v3d.x), Math.round(v3d.y), Math.round(v3d.z));
            if (canTeleportTo(world, target.down())) {
                break;
            } else {
                target = null;
            }
        }
        if (target != null) {
            if (!player.getEntityWorld().isClient()) {
                BlockPos teleportVec = target.add(0.5, 0.0, 0.5);
                if (teleportVec == null) {
                    return false;
                }
                player.teleport(teleportVec.getX(), teleportVec.getY(), teleportVec.getZ());
                world.sendEntityStatus(player, (byte)46);
            }
            player.fallDistance = 0;
            //player.swingHand(hand, true);
            world.playSound(null, player.prevX, player.prevY, player.prevZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
            player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1F, 1F);
            return true;
        } else {
            return false;
        }
    }

    private static boolean canTeleportTo(World world, BlockPos target) {
        return !world.getBlockState(target.toImmutable().up(1)).isSolidBlock(world, target.toImmutable().up(1))
                && !world.getBlockState(target.toImmutable().up(2)).isSolidBlock(world, target.toImmutable().up(2))
                && target.getY() > 0;
    }

    @Override
    public String getNameTranslationKey() {
        return "theylor.attributes.competences.teleportation";
    }

    @Override
    public String getDescriptionTranslationKey() {
        return "theylor.attributes.competences.descriptions.teleportation";
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

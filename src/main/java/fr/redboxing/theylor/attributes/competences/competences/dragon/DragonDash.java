package fr.redboxing.theylor.attributes.competences.competences.dragon;

import fr.redboxing.theylor.attributes.competences.CompetenceType;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;
import fr.redboxing.theylor.attributes.competences.TheylorCompetences;
import fr.redboxing.theylor.attributes.events.LivingEntityDamageCallback;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import fr.redboxing.theylor.attributes.utils.Translations;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class DragonDash implements TheylorCompetence {
    public DragonDash() {
        UseItemCallback.EVENT.register(this::onInteract);
        LivingEntityDamageCallback.EVENT.register(this::onDamage);
    }

    private TypedActionResult<ItemStack> onInteract(PlayerEntity player, World world, Hand hand) {
        TheylorPlayer theylorPlayer = TheylorPlayer.get(player);
        if(theylorPlayer.hasCompetenceEnabled(this) && hand == Hand.MAIN_HAND && !player.isSneaking()) {
            ItemStack itemStack = player.getMainHandStack();
            if(!(itemStack.getItem() instanceof SwordItem)) return TypedActionResult.pass(ItemStack.EMPTY);
            player.sendMessage(new LiteralText("Vous avez activer " + Translations.tr(getNameTranslationKey() + " !")).formatted(Formatting.GREEN), false);

            player.setVelocity(player.getRotationVector().multiply(4));
            ((ServerPlayerEntity)player).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
            world.playSound(null, player.prevX, player.prevY, player.prevZ, SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 1.0F, 1.0F);
            player.playSound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, 1.0F, 1.0F);

            return TypedActionResult.success(itemStack);
        }

        return TypedActionResult.pass(ItemStack.EMPTY);
    }

    private ActionResult onDamage(LivingEntity entity, DamageSource source, float amount) {
        if(entity instanceof PlayerEntity && source == DamageSource.FALL) {
            TheylorPlayer theylorPlayer = TheylorPlayer.get((PlayerEntity) entity);
            if(theylorPlayer.hasCompetenceEnabled(this)) {
                if(entity.isSneaking())
                    entity.getEntityWorld().createExplosion(entity, entity.getX(), entity.getY(), entity.getZ(), 3.0F, Explosion.DestructionType.NONE);

                return ActionResult.FAIL;
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public String getNameTranslationKey() {
        return "theylor.attributes.competences.dragon_dash";
    }

    @Override
    public String getDescriptionTranslationKey() {
        return "theylor.attributes.competences.descriptions.dragon_dash";
    }

    @Override
    public CompetenceType getCompetenceType() {
        return CompetenceType.TERTIARY;
    }

    @Override
    public boolean getDefaultState() {
        return true;
    }
}

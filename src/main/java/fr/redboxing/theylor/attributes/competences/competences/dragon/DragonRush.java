package fr.redboxing.theylor.attributes.competences.competences.dragon;

import fr.redboxing.theylor.attributes.competences.CompetenceType;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;
import fr.redboxing.theylor.attributes.competences.TheylorCompetences;
import fr.redboxing.theylor.attributes.events.LivingEntityDamageCallback;
import fr.redboxing.theylor.attributes.events.ServerPlayerEntityDamageCallback;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import fr.redboxing.theylor.attributes.utils.Translations;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class DragonRush implements TheylorCompetence {
    public DragonRush() {
        UseItemCallback.EVENT.register(this::onInteract);
        //ServerPlayerEntityDamageCallback.EVENT.register(this::onArrowDamage);
    }

    private TypedActionResult<ItemStack> onInteract(PlayerEntity player, World world, Hand hand) {
        TheylorPlayer theylorPlayer = TheylorPlayer.get(player);
        if(theylorPlayer.hasCompetenceEnabled(this) && hand == Hand.MAIN_HAND && player.isSneaking()) {
            ItemStack itemStack = player.getMainHandStack();
            if(!(itemStack.getItem() instanceof SwordItem)) return TypedActionResult.pass(ItemStack.EMPTY);
            if(theylorPlayer.getCooldownManager().isCoolingDown(this)) return TypedActionResult.pass(ItemStack.EMPTY);
            player.sendMessage(new LiteralText("Vous avez activer " + Translations.tr(getNameTranslationKey()) + " !").formatted(Formatting.GREEN), false);

            player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 40 * 20, 7));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 40 * 20, 2));

            world.playSound(null, player.prevX, player.prevY, player.prevZ, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, 1.0F, 1.0F);
            player.playSound(SoundEvents.ENTITY_WITHER_SPAWN, 1.0F, 1.0F);

            theylorPlayer.getCooldownManager().set(this, 60 * 20);
            return TypedActionResult.success(itemStack);
        }

        return TypedActionResult.pass(ItemStack.EMPTY);
    }

   /* public float onArrowDamage(ServerPlayerEntity player, DamageSource source, float amount) {
        if(source instanceof ProjectileDamageSource) {
            ProjectileDamageSource projectileDamageSource = (ProjectileDamageSource) source;
            if(projectileDamageSource.getAttacker() != null && (projectileDamageSource.getAttacker() instanceof PlayerEntity)) {
                TheylorPlayer theylorPlayer = TheylorPlayer.get((PlayerEntity) projectileDamageSource.getAttacker());
                if(theylorPlayer.hasCompetenceEnabled(this) && theylorPlayer.getCooldownManager().isCoolingDown(this)) {
                    return amount * 4;
                }
            }
        }

        return -1;
    }*/

    @Override
    public String getNameTranslationKey() {
        return "theylor.attributes.competences.dragon_rush";
    }

    @Override
    public String getDescriptionTranslationKey() {
        return "theylor.attributes.competences.descriptions.dragon_rush";
    }

    @Override
    public CompetenceType getCompetenceType() {
        return CompetenceType.ULTIMATE;
    }

    @Override
    public boolean getDefaultState() {
        return true;
    }
}

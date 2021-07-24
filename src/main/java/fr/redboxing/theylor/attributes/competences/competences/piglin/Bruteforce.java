package fr.redboxing.theylor.attributes.competences.competences.piglin;

import fr.redboxing.theylor.attributes.competences.CompetenceType;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;
import fr.redboxing.theylor.attributes.competences.TheylorCompetences;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import fr.redboxing.theylor.attributes.utils.Translations;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Bruteforce implements TheylorCompetence {
    public Bruteforce() {
        UseItemCallback.EVENT.register(this::onInteract);
    }

    private TypedActionResult<ItemStack> onInteract(PlayerEntity player, World world, Hand hand) {
        TheylorPlayer theylorPlayer = TheylorPlayer.get(player);
        if(theylorPlayer.hasCompetenceEnabled(this) && hand == Hand.MAIN_HAND && player.isSneaking()) {
            if(theylorPlayer.getCooldownManager().isCoolingDown(this)) return TypedActionResult.pass(ItemStack.EMPTY);

            ItemStack itemStack = player.getMainHandStack();
            if(!(itemStack.getItem() instanceof SwordItem)) return TypedActionResult.pass(ItemStack.EMPTY);

            player.sendMessage(new LiteralText("Vous avez activer " + Translations.tr(getNameTranslationKey() + " !")).formatted(Formatting.GREEN), false);

            player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 20 * 20, 2));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20 * 20, 1));

            theylorPlayer.getCooldownManager().set(this, 600 * 20);
        }

        return TypedActionResult.pass(ItemStack.EMPTY);
    }

    @Override
    public String getNameTranslationKey() {
        return "theylor.attributes.competences.bruteforce";
    }

    @Override
    public String getDescriptionTranslationKey() {
        return "theylor.attributes.competences.descriptions.bruteforce";
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

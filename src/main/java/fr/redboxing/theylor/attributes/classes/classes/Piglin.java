package fr.redboxing.theylor.attributes.classes.classes;

import fr.redboxing.theylor.attributes.classes.TheylorClass;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;
import fr.redboxing.theylor.attributes.competences.TheylorCompetences;
import fr.redboxing.theylor.attributes.events.ServerPlayerEntityTickCallback;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Piglin implements TheylorClass {
    private static final List<Item> GOLD = Arrays.asList(Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_SWORD, Items.GOLDEN_SHOVEL, Items.GOLDEN_HOE, Items.STONE_PICKAXE, Items.WOODEN_PICKAXE);
    private static final List<Item> GOLDEN_ARMOR = Arrays.asList(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS, Items.ELYTRA, Items.CARVED_PUMPKIN, Items.PLAYER_HEAD, Items.SKELETON_SKULL, Items.WITHER_SKELETON_SKULL, Items.ZOMBIE_HEAD, Items.DRAGON_HEAD);
    private static int ticks;

    public Piglin() {
        ServerPlayerEntityTickCallback.EVENT.register(this::tick);
    }

    private void tick(ServerPlayerEntity playerEntity) {
        ++ticks;

        if(ticks % 20 == 0 && TheylorPlayer.get(playerEntity).isClass(this)) {
            Item item = playerEntity.getMainHandStack().getItem();
            if(!GOLD.contains(item)) {
                if(item instanceof PickaxeItem || item instanceof ShovelItem) {
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 3 * 20, 10));
                } else if(item instanceof AxeItem) {
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 3 * 20, 5));
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 3 * 20, 10));
                } else if (item instanceof SwordItem) {
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 3 * 20, 5));
                }
            }

            List<ItemStack> armor = (List<ItemStack>) playerEntity.getArmorItems();
            boolean wearOtherThanGold = false;

            for(ItemStack itemStack : armor) {
                if(!itemStack.isEmpty() && !GOLDEN_ARMOR.contains(itemStack.getItem())) {
                    wearOtherThanGold = true;
                    break;
                }
            }

            if(wearOtherThanGold) {
                playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 3 * 20, 10));
                playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 3 * 20, 5));
            }
        }
    }


    @Override
    public String getNameTranslationKey() {
        return "theylor.attributes.classes.piglin";
    }

    @Override
    public List<TheylorCompetence> getCompetences() {
        return Arrays.asList(TheylorCompetences.MIDAS, TheylorCompetences.GOLD_DIGGER, TheylorCompetences.OVERLORD, TheylorCompetences.BRUTEFORCE);
    }
}

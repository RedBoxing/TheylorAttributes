package fr.redboxing.theylor.attributes.mixin.net.minecraft.entity.boss.dragon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(EnderDragonEntity.class)
public interface IMixinEnderDragonEntity {
    @Invoker("tickWithEndCrystals")
    void invokeTickWithEndCrystals();

    @Invoker("movePart")
    void invokeMovePart(EnderDragonPart enderDragonPart, double dx, double dy, double dz);

    @Invoker("launchLivingEntities")
    void invokeLaunchLivingEntities(List<Entity> entities);

    @Invoker("damageLivingEntities")
    void invokeDamageLivingEntities(List<Entity> entities);

    @Invoker("getHeadVerticalMovement")
    float invokeGetHeadVerticalMovement();

    @Invoker("wrapYawChange")
    float invokeWrapYawChange(double yawDegrees);

    @Invoker("destroyBlocks")
    boolean invokeDestroyBlocks(Box box);

    @Accessor("body")
    EnderDragonPart getBody();

    @Accessor("leftWing")
    EnderDragonPart getLeftWing();

    @Accessor("rightWing")
    EnderDragonPart getRightWing();

    @Accessor("neck")
    EnderDragonPart getNeck();

    @Accessor("tail1")
    EnderDragonPart getTail1();

    @Accessor("tail2")
    EnderDragonPart getTail2();

    @Accessor("tail3")
    EnderDragonPart getTail3();
}

package fr.redboxing.theylor.attributes.mixin.net.minecraft.server.network;

import com.mojang.authlib.GameProfile;
import fr.redboxing.theylor.attributes.events.ServerPlayerEntityDamageCallback;
import fr.redboxing.theylor.attributes.events.ServerPlayerEntityTickCallback;
import fr.redboxing.theylor.attributes.interfaces.IServerPlayerEntity;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity implements IServerPlayerEntity {
    @Getter
    @Setter
    private TheylorPlayer theylorPlayer;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void onInit(MinecraftServer server, ServerWorld world, GameProfile profile, CallbackInfo ci) {
        this.theylorPlayer = new TheylorPlayer(profile.getId());
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo ci) {
        ServerPlayerEntityTickCallback.EVENT.invoker().interact(((ServerPlayerEntity) (Object) this));
    }

    @ModifyArg(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), index = 1)
    private float damage(DamageSource source, float amount) {
        float result = ServerPlayerEntityDamageCallback.EVENT.invoker().interact(((ServerPlayerEntity) (Object) this), source, amount);

        if(result != -1) {
            return result;
        }

        return amount;
    }
}

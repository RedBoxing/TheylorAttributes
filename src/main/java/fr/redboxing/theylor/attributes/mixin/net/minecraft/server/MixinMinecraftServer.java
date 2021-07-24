package fr.redboxing.theylor.attributes.mixin.net.minecraft.server;

import fr.redboxing.theylor.attributes.events.ServerWorldsTickCallback;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {
    @Shadow private int ticks;

    @Inject(method = "tickWorlds", at = @At("HEAD"))
    public void tickWorlds(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        ServerWorldsTickCallback.EVENT.invoker().interact(this.ticks);
    }
}

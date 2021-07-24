package fr.redboxing.theylor.attributes.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CMDCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(literal("custommodeldata")
                    .then(argument("modelData", IntegerArgumentType.integer()).executes(CMDCommand::execute))
            );
        });
    }

    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int modelData = IntegerArgumentType.getInteger(context, "modelData");
        NbtCompound nbt = context.getSource().getPlayer().getMainHandStack().getNbt();
        if(nbt == null) nbt = new NbtCompound();

        nbt.putInt("CustomModelData", modelData);

        context.getSource().getPlayer().getMainHandStack().setNbt(nbt);

        return 0;
    }
}

package fr.redboxing.theylor.attributes.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import fr.redboxing.theylor.attributes.TheylorAttributes;
import fr.redboxing.theylor.attributes.TheylorRegistry;
import fr.redboxing.theylor.attributes.classes.TheylorClass;
import fr.redboxing.theylor.attributes.commands.suggestions.CompletionProviders;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;
import fr.redboxing.theylor.attributes.entity.RidableEnderDragon;
import fr.redboxing.theylor.attributes.interfaces.IServerPlayerEntity;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import fr.redboxing.theylor.attributes.sql.Table;
import fr.redboxing.theylor.attributes.utils.Translations;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ClassCommand {
    private static final DynamicCommandExceptionType UNKNOWN_CLASS_EXCEPTION = new DynamicCommandExceptionType((object) -> new LiteralText("Classe " + object + " Inconnue !").formatted(Formatting.RED));
    private static final DynamicCommandExceptionType UNKNOWN_COMPETENCE_EXCEPTION = new DynamicCommandExceptionType((object) -> new LiteralText("Competence " + object + " Inconnue !").formatted(Formatting.RED));

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(literal("class")
                    .then(literal("set")
                            .then(argument("target", EntityArgumentType.player())
                                    .then(argument("class", IdentifierArgumentType.identifier())
                                            .suggests(CompletionProviders.suggestedClasses())
                                            .executes(ClassCommand::executeSet))))

                    .then(literal("info").executes(ClassCommand::executeInfo))

                    .then(literal("enable")
                        .then(argument("competence", IdentifierArgumentType.identifier())
                            .suggests(CompletionProviders.suggestedCompetences())
                                .executes(context -> executeToggle(context, true))))

                    .then(literal("disable")
                            .then(argument("competence", IdentifierArgumentType.identifier())
                                    .suggests(CompletionProviders.suggestedCompetences())
                                    .executes(context -> executeToggle(context, false))))

                    .then(literal("dragon").requires(source -> source.hasPermissionLevel(2)).executes(ClassCommand::executeDragon))
            );
        });
    }

    private static int executeDragon(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        RidableEnderDragon ridableEnderDragon = new RidableEnderDragon(context.getSource().getWorld());
        ridableEnderDragon.setPosition(context.getSource().getPosition());
        context.getSource().getWorld().spawnEntity(ridableEnderDragon);
        context.getSource().getPlayer().startRiding(ridableEnderDragon, true);

        return 0;
    }

    private static int executeSet(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
        TheylorClass theylorClass = getClassArgument(context, "class");

        TheylorPlayer theylorPlayer = ((IServerPlayerEntity) target).getTheylorPlayer();
        theylorPlayer.setTheylorClass(theylorClass);

        target.sendMessage(new LiteralText("Vous êtes maintenent un ").append(Translations.tr(theylorPlayer.getTheylorClass().getNameTranslationKey())).formatted(Formatting.GREEN), false);

        if(target != context.getSource().getPlayer())
            context.getSource().getPlayer().sendMessage(((LiteralText)target.getName()).append(" est maintenent un ").append(Translations.tr(theylorPlayer.getTheylorClass().getNameTranslationKey())).formatted(Formatting.GREEN), false);

        TheylorAttributes.saveClassToDatabase(target, theylorPlayer.getTheylorClass());
        return 0;
    }

    private static int executeInfo(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        TheylorPlayer theylorPlayer = ((IServerPlayerEntity) context.getSource().getPlayer()).getTheylorPlayer();
        TheylorClass theylorClass = theylorPlayer.getTheylorClass();

        MutableText text = new LiteralText("Classe: ").formatted(Formatting.GOLD).append(new LiteralText(Translations.tr(theylorClass.getNameTranslationKey())).formatted(Formatting.AQUA));
        text.append("\n\n");
        text.append(new LiteralText("Compétences: ").formatted(Formatting.GOLD));
        text.append("\n");

        for(TheylorCompetence competence : theylorPlayer.getClassCompetences()) {
            text.append("    ");

            MutableText name = new LiteralText(Translations.tr(competence.getNameTranslationKey())).formatted(Formatting.GOLD);
            name.setStyle(name.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText(Translations.tr(competence.getDescriptionTranslationKey())).formatted(Formatting.AQUA))));
            name.append(": ");

            text.append(name);

            MutableText enable = new LiteralText(theylorPlayer.hasCompetenceEnabled(competence) ? Formatting.LIGHT_PURPLE + "[" + Formatting.GREEN + "ENABLE" + Formatting.LIGHT_PURPLE + "]" : Formatting.GREEN + "ENABLE");
            enable.setStyle(enable.getStyle()
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("Click here to enable " + Translations.tr(competence.getNameTranslationKey())).formatted(Formatting.GREEN)))
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/class enable " + TheylorRegistry.COMPETENCES.getId(competence))));
            text.append(enable);

            text.append(" ");

            MutableText disable = new LiteralText(!theylorPlayer.hasCompetenceEnabled(competence) ? Formatting.LIGHT_PURPLE + "[" + Formatting.RED + "DISABLE" + Formatting.LIGHT_PURPLE + "]" : Formatting.RED + "DISABLE");
            disable.setStyle(disable.getStyle()
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("Click here to disable " + Translations.tr(competence.getNameTranslationKey())).formatted(Formatting.RED)))
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/class disable " + TheylorRegistry.COMPETENCES.getId(competence))));
            text.append(disable);

            text.append("\n");
        }

        context.getSource().getPlayer().sendMessage(text, false);
        return 0;
    }

    private static int executeToggle(CommandContext<ServerCommandSource> context, boolean state) throws CommandSyntaxException {
        TheylorPlayer theylorPlayer = ((IServerPlayerEntity) context.getSource().getPlayer()).getTheylorPlayer();
        TheylorCompetence competence = getCompetenceArgument(context, "competence");

        if(!theylorPlayer.hasCompetence(competence)) {
            context.getSource().getPlayer().sendMessage(new LiteralText("You're current class does not have this competence !").formatted(Formatting.RED), false);
            return 0;
        }

        if(theylorPlayer.hasCompetenceEnabled(competence)) {
            if(state) {
                context.getSource().getPlayer().sendMessage(new LiteralText(Translations.tr(competence.getNameTranslationKey()) + " est déja activé !").formatted(Formatting.RED), false);
                return 0;
            }

            theylorPlayer.setCompetenceState(competence, false);
            context.getSource().getPlayer().sendMessage(new LiteralText(Translations.tr(competence.getNameTranslationKey()) + " A été désactivé !").formatted(Formatting.GREEN), false);
        } else {
            if(!state) {
                context.getSource().getPlayer().sendMessage(new LiteralText(Translations.tr(competence.getNameTranslationKey()) + " est déja désactivé !").formatted(Formatting.RED), false);
                return 0;
            }

            theylorPlayer.setCompetenceState(competence, true);
            context.getSource().getPlayer().sendMessage(new LiteralText(Translations.tr(competence.getNameTranslationKey()) + " A été activé !").formatted(Formatting.GREEN), false);
        }

        return 0;
    }

    private static TheylorClass getClassArgument(CommandContext<ServerCommandSource> context, String argumentName) throws CommandSyntaxException {
        Identifier identifier = context.getArgument(argumentName, Identifier.class);
        return TheylorRegistry.CLASSES.getOrEmpty(identifier).orElseThrow(() -> {
            return UNKNOWN_CLASS_EXCEPTION.create(identifier);
        });
    }

    private static TheylorCompetence getCompetenceArgument(CommandContext<ServerCommandSource> context, String argumentName) throws CommandSyntaxException {
        Identifier identifier = context.getArgument(argumentName, Identifier.class);
        return TheylorRegistry.COMPETENCES.getOrEmpty(identifier).orElseThrow(() -> {
            return UNKNOWN_COMPETENCE_EXCEPTION.create(identifier);
        });
    }
}

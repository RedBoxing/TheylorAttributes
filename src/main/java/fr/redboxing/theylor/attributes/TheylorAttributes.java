package fr.redboxing.theylor.attributes;

import fr.redboxing.theylor.attributes.classes.TheylorClass;
import fr.redboxing.theylor.attributes.classes.TheylorClasses;
import fr.redboxing.theylor.attributes.commands.CMDCommand;
import fr.redboxing.theylor.attributes.commands.ClassCommand;
import fr.redboxing.theylor.attributes.sql.SQLHandler;
import fr.redboxing.theylor.attributes.sql.Table;
import fr.redboxing.theylor.attributes.sql.column.PlayerDataColumns;
import fr.redboxing.theylor.attributes.utils.Translations;
import lombok.Getter;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TheylorAttributes implements DedicatedServerModInitializer {
    public static String MOD_ID = "theylor-attributes";

    @Getter
    private static final Logger logger = LogManager.getLogger("TheylorAttributes");

    @Getter
    private static Table playerDataTable;

    @Getter
    private static final List<Block> breakables = Arrays.asList(Blocks.END_PORTAL_FRAME, Blocks.END_PORTAL, Blocks.SPAWNER);

    @Override
    public void onInitializeServer() {
        Translations.updateTranslations();
        SQLHandler.connect();

        if (SQLHandler.connection != null) {
            logger.info("Successfully connected to database!");
            SQLHandler.disconnect();
        }

        playerDataTable = new Table("players", PlayerDataColumns.getColumns());
        registerCommands();
    }

    private void registerCommands() {
        ClassCommand.register();
        CMDCommand.register();
    }

    public static void saveClassToDatabase(PlayerEntity player, TheylorClass theylorClass) {
        Table table = getPlayerDataTable();
        table.startTransaction();
        table.createRow("id", player.getUuidAsString());
        table.set(player.getUuidAsString(), "class", Objects.requireNonNull(TheylorRegistry.CLASSES.getId(theylorClass)));
        table.endTransaction();
    }

    public static TheylorClass getClassFromDatabase(PlayerEntity player) {
        return getClassFromDatabase(player.getUuid());
    }

    public static TheylorClass getClassFromDatabase(UUID uuid) {
        Table table = getPlayerDataTable();
        table.startTransaction();
        Identifier id = table.get(uuid.toString(), "class", Objects.requireNonNull(TheylorRegistry.CLASSES.getId(TheylorClasses.HUMAN)));
        table.endTransaction();

        return TheylorRegistry.CLASSES.get(id);
    }
}

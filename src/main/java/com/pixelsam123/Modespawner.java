package com.pixelsam123;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.quiltmc.config.api.Config;
import org.quiltmc.config.api.ConfigEnvironment;
import org.quiltmc.config.api.Constraint;
import org.quiltmc.config.api.values.TrackedValue;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.impl.config.Json5Serializer;
import org.quiltmc.loader.impl.config.NightConfigSerializer;
import org.quiltmc.loader.impl.lib.electronwill.nightconfig.toml.TomlParser;
import org.quiltmc.loader.impl.lib.electronwill.nightconfig.toml.TomlWriter;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

import static net.minecraft.server.command.CommandManager.*;

public class Modespawner implements ModInitializer {
	public static final String MOD_ID = "modespawner";
	public static final Logger LOGGER = LoggerFactory.getLogger(Modespawner.class);

	public static final TrackedValue<Integer> GLOBAL_DESPAWN_TIME = TrackedValue.create(
		6000,
		"despawnTime",
		creator -> {
			creator.constraint(Constraint.range(1, Integer.MAX_VALUE));
		}
	);

	private static final Config globalConfig = Config.create(
		new ConfigEnvironment(
			Path.of("config"),
			new NightConfigSerializer<>("toml", new TomlParser(), new TomlWriter()),
			Json5Serializer.INSTANCE
		),
		MOD_ID,
		"globalConfig",
		builder -> {
			builder.field(GLOBAL_DESPAWN_TIME);
		}
	);

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("Initializing");

		CommandRegistrationCallback.EVENT.register((dispatcher, context, environment) -> {
			dispatcher.register(literal("despawntime").executes(Modespawner::handleDespawnTimeCommand));
			dispatcher.register(literal("despawntime").then(literal("set")
				.requires(source -> source.hasPermissionLevel(OWNER_PERMISSION_LEVEL))
				.then(argument(
					"ticks",
					IntegerArgumentType.integer()
				).executes(Modespawner::handleDespawnTimeSetCommand))));
		});
	}

	private static int handleDespawnTimeCommand(CommandContext<ServerCommandSource> context) {
		context
			.getSource()
			.sendFeedback(
				Text.literal("Item despawn time is " + GLOBAL_DESPAWN_TIME.getRealValue() + " ticks"),
				true
			);
		return 1;
	}

	private static int handleDespawnTimeSetCommand(CommandContext<ServerCommandSource> context) {
		final int ticksArg = context.getArgument("ticks", int.class);
		if (ticksArg < 1) {
			context
				.getSource()
				.sendError(Text.literal(
					"Don't set this to under 1 tick (because I don't know what will happen)"));
			return -1;
		}

		GLOBAL_DESPAWN_TIME.setValue(ticksArg, true);
		context
			.getSource()
			.sendFeedback(
				Text.literal("Set item despawn time to " + GLOBAL_DESPAWN_TIME.getRealValue() + " ticks"),
				true
			);
		return 1;
	}
}

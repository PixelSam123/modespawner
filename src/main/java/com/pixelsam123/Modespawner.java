package com.pixelsam123;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.server.command.CommandManager.literal;

public class Modespawner implements ModInitializer {
	public static final String MOD_ID = "modespawner";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static int despawnTime = 6000;

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("Hello Quilt world from {}!", mod.metadata().name());

		CommandRegistrationCallback.EVENT.register((dispatcher, context, environment) -> {
			dispatcher.register(literal("despawntime").executes(Modespawner::handleDespawnTimeCommand));
		});
	}

	private static int handleDespawnTimeCommand(CommandContext<ServerCommandSource> context) {
		context.getSource().sendFeedback(
			Text.literal("Item despawn time is " + despawnTime + " ticks"),
			true
		);
		return 1;
	}
}

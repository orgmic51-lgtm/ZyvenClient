package me.zyven.zyvenclient;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

public final class ZyvenCommands {
    private ZyvenCommands() {}

    private static int say(FabricClientCommandSource source, String name, boolean on) {
        source.sendFeedback(Component.literal("ZyvenClient " + name + ": " + (on ? "ON" : "OFF")));
        return 1;
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommandManager.literal("zyvenclient")
                        .executes(context -> {
                            context.getSource().getClient().setScreen(new ZyvenMenuScreen());
                            return 1;
                        })
                        .then(ClientCommandManager.literal("hud").executes(context -> {
                            ZyvenClient.hudEnabled = !ZyvenClient.hudEnabled;
                            return say(context.getSource(), "HUD", ZyvenClient.hudEnabled);
                        }))
                        .then(ClientCommandManager.literal("fps").executes(context -> {
                            ZyvenClient.fpsEnabled = !ZyvenClient.fpsEnabled;
                            return say(context.getSource(), "FPS counter", ZyvenClient.fpsEnabled);
                        }))
                        .then(ClientCommandManager.literal("armor").executes(context -> {
                            ZyvenClient.armorEnabled = !ZyvenClient.armorEnabled;
                            return say(context.getSource(), "Armor HUD", ZyvenClient.armorEnabled);
                        }))
                        .then(ClientCommandManager.literal("fullbright").executes(context -> {
                            ZyvenClient.toggleFullbright();
                            return say(context.getSource(), "Fullbright", ZyvenClient.fullbrightEnabled);
                        }))
                        .then(ClientCommandManager.literal("cape").executes(context -> {
                            ZyvenClient.capeEnabled = !ZyvenClient.capeEnabled;
                            return say(context.getSource(), "Cape", ZyvenClient.capeEnabled);
                        }))
                        .then(ClientCommandManager.literal("reloadcape").executes(context -> {
                            ZyvenCapes.load();
                            context.getSource().sendFeedback(Component.literal(ZyvenCapes.hasCape()
                                    ? "ZyvenClient: cape loaded"
                                    : "ZyvenClient: no zyvenclient/cape.png found in your game folder"));
                            return 1;
                        }))
                )
        );
    }
}

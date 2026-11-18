package me.zyven.zyvenclient;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class ZyvenClient implements ClientModInitializer {
    public static final String MOD_ID = "zyvenclient";
    public static boolean hudEnabled = true;
    public static boolean fullbrightEnabled = false;
    public static boolean sprintEnabled = false;
    public static boolean zoomEnabled = false;
    public static boolean fpsEnabled = true;
    public static boolean armorEnabled = true;
    public static boolean capeEnabled = true;

    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(MOD_ID, "general")
    );
    private static KeyMapping openMenuKey;
    private static KeyMapping zoomKey;
    private static double previousGamma = 1.0;
    private static int previousFov = 70;

    @Override
    public void onInitializeClient() {
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.zyvenclient.open_menu",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                CATEGORY
        ));

        zoomKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.zyvenclient.zoom",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                CATEGORY
        ));

        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                Identifier.fromNamespaceAndPath(MOD_ID, "client_hud"),
                (graphics, deltaTracker) -> ZyvenHud.render(graphics)
        );

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> ZyvenCapes.load());

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.consumeClick()) {
                client.setScreen(new ZyvenMenuScreen());
            }

            boolean zoomNow = zoomKey.isDown();
            if (zoomNow && !zoomEnabled) {
                previousFov = client.options.fov().get();
                client.options.fov().set(30);
                zoomEnabled = true;
            } else if (!zoomNow && zoomEnabled) {
                client.options.fov().set(previousFov);
                zoomEnabled = false;
            }

            if (client.player != null && sprintEnabled && client.player.isAlive()) {
                client.player.setSprinting(true);
            }
        });

        ZyvenCommands.register();
    }

    public static void toggleFullbright() {
        Minecraft client = Minecraft.getInstance();
        if (client.options == null) return;

        if (!fullbrightEnabled) {
            previousGamma = client.options.gamma().get();
            client.options.gamma().set(16.0);
            fullbrightEnabled = true;
        } else {
            client.options.gamma().set(previousGamma);
            fullbrightEnabled = false;
        }
        client.options.save();
    }

    public static void setHudEnabled(boolean enabled) {
        hudEnabled = enabled;
    }

    public static void setSprintEnabled(boolean enabled) {
        sprintEnabled = enabled;
    }
}

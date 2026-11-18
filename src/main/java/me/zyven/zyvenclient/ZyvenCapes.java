package me.zyven.zyvenclient;

import com.mojang.blaze3d.platform.NativeImage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ZyvenCapes {
    private static final Logger LOGGER = LoggerFactory.getLogger("ZyvenClient");
    private static final Identifier ID = Identifier.fromNamespaceAndPath(ZyvenClient.MOD_ID, "custom_cape");
    private static ClientAsset.ResourceTexture cape;

    private ZyvenCapes() {}

    public static ClientAsset.ResourceTexture getCape() {
        return cape;
    }

    public static boolean hasCape() {
        return cape != null;
    }

    public static void load() {
        Path dir = FabricLoader.getInstance().getGameDir().resolve("zyvenclient");
        Path file = dir.resolve("cape.png");
        try {
            Files.createDirectories(dir);
            if (!Files.exists(file)) {
                cape = null;
                return;
            }
            try (InputStream in = Files.newInputStream(file)) {
                NativeImage image = NativeImage.read(in);
                Minecraft.getInstance().getTextureManager()
                        .register(ID, new DynamicTexture(() -> "zyvenclient_cape", image));
                cape = new ClientAsset.ResourceTexture(ID, ID);
            }
        } catch (IOException | RuntimeException e) {
            cape = null;
            LOGGER.error("Could not load cape.png", e);
        }
    }
}

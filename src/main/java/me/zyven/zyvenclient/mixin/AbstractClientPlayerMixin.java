package me.zyven.zyvenclient.mixin;

import me.zyven.zyvenclient.ZyvenCapes;
import me.zyven.zyvenclient.ZyvenClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {
    @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
    private void zyven$customCape(CallbackInfoReturnable<PlayerSkin> cir) {
        if (!ZyvenClient.capeEnabled) return;
        ClientAsset.ResourceTexture cape = ZyvenCapes.getCape();
        if (cape == null) return;
        if ((Object) this != Minecraft.getInstance().player) return;

        PlayerSkin skin = cir.getReturnValue();
        cir.setReturnValue(new PlayerSkin(skin.body(), cape, skin.elytra(), skin.model(), skin.secure()));
    }
}

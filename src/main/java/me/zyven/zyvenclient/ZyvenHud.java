package me.zyven.zyvenclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public final class ZyvenHud {
    private static final EquipmentSlot[] ARMOR = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    private ZyvenHud() {}

    public static void render(GuiGraphics g) {
        if (!ZyvenClient.hudEnabled) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;
        Font font = mc.font;

        String xyz = "XYZ: " + (int) mc.player.getX() + " / " + (int) mc.player.getY() + " / " + (int) mc.player.getZ();
        int lines = 2 + (ZyvenClient.fpsEnabled ? 1 : 0);
        int width = Math.max(106, font.width(xyz) + 19);
        int bottom = 8 + 12 + 10 * (lines - 1) + 8;

        g.fill(6, 5, 6 + width, bottom, 0x99071320);
        g.fill(6, 5, 8, bottom, 0xFF22B8F0);

        int y = 8;
        g.drawString(font, "ZYVENCLIENT", 13, y, 0xFF38BDF8, false);
        y += 12;
        g.drawString(font, xyz, 13, y, 0xFFE5F7FF, false);
        y += 10;

        if (ZyvenClient.fpsEnabled) {
            int fps = mc.getFps();
            int color = fps >= 60 ? 0xFF55FF55 : fps >= 30 ? 0xFFFFFF55 : 0xFFFF5555;
            g.drawString(font, "FPS: " + fps, 13, y, color, false);
        }

        if (ZyvenClient.armorEnabled) {
            int ay = bottom + 4;
            for (EquipmentSlot slot : ARMOR) {
                ItemStack stack = mc.player.getItemBySlot(slot);
                if (stack.isEmpty()) continue;
                g.renderItem(stack, 6, ay);
                g.renderItemDecorations(font, stack, 6, ay);
                if (stack.isDamageableItem()) {
                    int max = stack.getMaxDamage();
                    int left = max - stack.getDamageValue();
                    float frac = max == 0 ? 1f : (float) left / max;
                    int color = frac > 0.5f ? 0xFF55FF55 : frac > 0.2f ? 0xFFFFFF55 : 0xFFFF5555;
                    g.drawString(font, String.valueOf(left), 26, ay + 4, color, true);
                }
                ay += 18;
            }
        }
    }
}

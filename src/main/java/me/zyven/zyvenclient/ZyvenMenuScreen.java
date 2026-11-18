package me.zyven.zyvenclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.BooleanSupplier;

public final class ZyvenMenuScreen extends Screen {
    private static final int CYAN = 0xFF38BDF8;

    public ZyvenMenuScreen() {
        super(Component.literal("ZyvenClient"));
    }

    private void toggleButton(int x, int y, int w, String name, BooleanSupplier state, Runnable toggle) {
        addRenderableWidget(Button.builder(label(name, state.getAsBoolean()), button -> {
            toggle.run();
            button.setMessage(label(name, state.getAsBoolean()));
        }).bounds(x, y, w, 20).build());
    }

    private static Component label(String name, boolean on) {
        return Component.literal(name + ": " + (on ? "ON" : "OFF"));
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        int top = Math.max(40, this.height / 2 - 60);
        int colWidth = Math.min(100, (this.width - 30) / 2);
        int leftX = center - colWidth - 3;
        int rightX = center + 3;

        toggleButton(leftX, top, colWidth, "HUD", () -> ZyvenClient.hudEnabled,
                () -> ZyvenClient.hudEnabled = !ZyvenClient.hudEnabled);
        toggleButton(rightX, top, colWidth, "FPS", () -> ZyvenClient.fpsEnabled,
                () -> ZyvenClient.fpsEnabled = !ZyvenClient.fpsEnabled);

        toggleButton(leftX, top + 25, colWidth, "Armor", () -> ZyvenClient.armorEnabled,
                () -> ZyvenClient.armorEnabled = !ZyvenClient.armorEnabled);
        toggleButton(rightX, top + 25, colWidth, "Cape", () -> ZyvenClient.capeEnabled,
                () -> ZyvenClient.capeEnabled = !ZyvenClient.capeEnabled);

        toggleButton(leftX, top + 50, colWidth, "Fullbright", () -> ZyvenClient.fullbrightEnabled,
                ZyvenClient::toggleFullbright);
        toggleButton(rightX, top + 50, colWidth, "Sprint", () -> ZyvenClient.sprintEnabled,
                () -> ZyvenClient.sprintEnabled = !ZyvenClient.sprintEnabled);

        addRenderableWidget(Button.builder(Component.literal("Done"), button ->
                Minecraft.getInstance().setScreen(null)
        ).bounds(leftX, top + 78, colWidth * 2 + 6, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics, mouseX, mouseY, delta);
        int center = this.width / 2;
        int panelWidth = Math.min(250, this.width - 12);
        int left = center - panelWidth / 2;
        int panelTop = Math.max(12, Math.max(40, this.height / 2 - 60) - 42);

        graphics.fill(left, panelTop, left + panelWidth, panelTop + 150, 0xEE071321);
        graphics.fill(left, panelTop, left + panelWidth, panelTop + 3, CYAN);
        graphics.drawCenteredString(this.font, "ZYVENCLIENT", center, panelTop + 12, CYAN);
        graphics.drawCenteredString(this.font, "LIGHTWEIGHT MOBILE CLIENT", center, panelTop + 25, 0xFFB7E9FF);
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

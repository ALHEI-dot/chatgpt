package com.codex.realseasons;

import com.codex.realseasons.config.RealSeasonsCadence;
import com.codex.realseasons.config.RealSeasonsCommonConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.time.DayOfWeek;

/**
 * Native Minecraft config screen. Keeping this independent of Cloth Config
 * avoids relying on an optional Fabric UI library through Connector.
 */
public final class RealSeasonsClientConfigScreenFactory extends Screen {
    private final Screen parent;
    private boolean southernHemisphere;
    private RealSeasonsCadence cadence;
    private boolean interpolate;
    private String timezone;
    private DayOfWeek weekStart;
    private int weekMinDays;

    private Button hemisphereButton;
    private Button cadenceButton;
    private Button interpolationButton;
    private Button weekStartButton;
    private EditBox timezoneField;
    private EditBox weekMinDaysField;

    private RealSeasonsClientConfigScreenFactory(Screen parent, RealSeasonsCommonConfig config) {
        super(Component.literal("Real Serene Seasons Configuration"));
        this.parent = parent;
        this.southernHemisphere = config.useSouthernHemisphere();
        this.cadence = config.cadence();
        this.interpolate = config.interpolateTimeOfDay();
        this.timezone = config.timezoneId();
        this.weekStart = config.weekStartDay();
        this.weekMinDays = config.weekMinDays();
    }

    public static Screen create(Screen parent) {
        return new RealSeasonsClientConfigScreenFactory(parent, RealSeasonsCommonConfig.loadOrCreate());
    }

    @Override
    protected void init() {
        int left = this.width / 2 - 150;
        int width = 300;
        int y = 48;

        hemisphereButton = addRenderableWidget(Button.builder(hemisphereMessage(), button -> {
            southernHemisphere = !southernHemisphere;
            button.setMessage(hemisphereMessage());
        }).bounds(left, y, width, 24).build());

        cadenceButton = addRenderableWidget(Button.builder(cadenceMessage(), button -> {
            RealSeasonsCadence[] values = RealSeasonsCadence.values();
            cadence = values[(cadence.ordinal() + 1) % values.length];
            button.setMessage(cadenceMessage());
        }).bounds(left, y + 36, width, 24).build());

        interpolationButton = addRenderableWidget(Button.builder(interpolationMessage(), button -> {
            interpolate = !interpolate;
            button.setMessage(interpolationMessage());
        }).bounds(left, y + 72, width, 24).build());

        timezoneField = addRenderableWidget(new EditBox(this.font, left, y + 116, width, 20,
                Component.literal("Timezone ID")));
        timezoneField.setMaxLength(128);
        timezoneField.setValue(timezone == null ? "" : timezone);

        weekStartButton = addRenderableWidget(Button.builder(weekStartMessage(), button -> {
            DayOfWeek[] values = DayOfWeek.values();
            weekStart = values[(weekStart.ordinal() + 1) % values.length];
            button.setMessage(weekStartMessage());
        }).bounds(left, y + 166, width, 24).build());

        weekMinDaysField = addRenderableWidget(new EditBox(this.font, left, y + 210, width, 20,
                Component.literal("Week minimum days")));
        weekMinDaysField.setMaxLength(1);
        weekMinDaysField.setValue(Integer.toString(weekMinDays));

        addRenderableWidget(Button.builder(Component.literal("Save"), button -> saveAndClose())
                .bounds(this.width / 2 - 154, this.height - 32, 150, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Cancel"), button -> onClose())
                .bounds(this.width / 2 + 4, this.height - 32, 150, 20).build());
    }

    private Component hemisphereMessage() {
        return Component.literal("Southern Hemisphere: " + (southernHemisphere ? "ON" : "OFF"));
    }

    private Component cadenceMessage() {
        return Component.literal("Cadence: " + cadence.name() + " (change)");
    }

    private Component interpolationMessage() {
        return Component.literal("Interpolate Time Of Day: " + (interpolate ? "ON" : "OFF"));
    }

    private Component weekStartMessage() {
        return Component.literal("Week Start Day: " + weekStart.name() + " (change)");
    }

    private void saveAndClose() {
        timezone = timezoneField.getValue().trim();
        int parsedWeekMinDays = weekMinDays;
        try {
            parsedWeekMinDays = Integer.parseInt(weekMinDaysField.getValue().trim());
        } catch (NumberFormatException ignored) {
        }
        weekMinDays = Math.max(1, Math.min(7, parsedWeekMinDays));

        RealSeasonsCommonConfig updated = RealSeasonsCommonConfig.overwrite(
                southernHemisphere, cadence, interpolate, timezone, weekStart, weekMinDays);
        RealSeasonsModInitializer.reloadConfig(updated);
        if (this.minecraft != null) {
            this.minecraft.setScreen(parent);
        }
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(parent);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 18, 0xFFFFFF);
        int left = this.width / 2 - 150;
        int y = 48;
        graphics.drawString(this.font, "Calendar hemisphere", left, y - 11, 0xA0A0A0);
        graphics.drawString(this.font, "Length of each sub-season", left, y + 25, 0xA0A0A0);
        graphics.drawString(this.font, "Smooth transitions during the day", left, y + 61, 0xA0A0A0);
        graphics.drawString(this.font, "Timezone ID (blank uses system default)", left, y + 103, 0xA0A0A0);
        graphics.drawString(this.font, "First day used for weekly cadence", left, y + 153, 0xA0A0A0);
        graphics.drawString(this.font, "Minimum days in the first week (1-7)", left, y + 197, 0xA0A0A0);
        super.render(graphics, mouseX, mouseY, partialTick);
    }
}

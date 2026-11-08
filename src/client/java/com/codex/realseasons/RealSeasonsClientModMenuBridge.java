package com.codex.realseasons;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;

/** Registers the native Real Serene Seasons settings screen with Mod Menu. */
public final class RealSeasonsClientModMenuBridge implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return RealSeasonsClientConfigScreenFactory::create;
    }
}

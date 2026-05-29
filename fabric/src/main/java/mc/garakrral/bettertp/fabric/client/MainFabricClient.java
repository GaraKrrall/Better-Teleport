package mc.garakrral.bettertp.fabric.client;

import mc.garakrral.bettertp.client.MainClient;
import mc.garakrral.bettertp.fabric.client.handler.FabricClientNetworkHandler;


import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;


public final class MainFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MainClient.registerKeybinds();
        KeyBindingHelper.registerKeyBinding(MainClient.OPEN_MENU_KEY);
        FabricClientNetworkHandler.registerClientReceivers();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (MainClient.OPEN_MENU_KEY.consumeClick()) {
                MainClient.openTeleportRequestScreen();
            }
        });
    }
}
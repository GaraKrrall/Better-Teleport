package mc.garakrral.bettertp.neoforge.client;

import mc.garakrral.bettertp.Main;
import mc.garakrral.bettertp.client.MainClient;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(modid = Main.MOD_ID, value = Dist.CLIENT)
public final class MainNeoForgeClient {
    private MainNeoForgeClient() {}

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        System.out.println("REGISTER KEYS EVENT");
        MainClient.registerKeybinds();
        event.register(MainClient.OPEN_MENU_KEY);
    }

    @EventBusSubscriber(modid = Main.MOD_ID, value = Dist.CLIENT)
    public static final class GameEvents {
        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;

            while (MainClient.OPEN_MENU_KEY.consumeClick()) {
                MainClient.openTeleportRequestScreen();
            }
        }
    }
}
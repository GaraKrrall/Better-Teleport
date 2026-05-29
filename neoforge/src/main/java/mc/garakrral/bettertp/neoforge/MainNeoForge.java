package mc.garakrral.bettertp.neoforge;

import mc.garakrral.bettertp.Main;
import mc.garakrral.bettertp.neoforge.network.NeoForgeBetterTpPlatform;
import mc.garakrral.bettertp.neoforge.network.NeoForgeNetworkHandler;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Main.MOD_ID)
public final class MainNeoForge {

    public MainNeoForge(
            IEventBus modBus
    ) {

        Main.init();

        modBus.addListener(NeoForgeNetworkHandler::register);
        mc.garakrral.bettertp.network.BetterTpNetwork.init(new NeoForgeBetterTpPlatform());
    }
}
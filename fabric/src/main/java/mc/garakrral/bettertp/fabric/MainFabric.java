package mc.garakrral.bettertp.fabric;

import mc.garakrral.bettertp.Main;
import mc.garakrral.bettertp.fabric.network.BetterTpNetwork;
import mc.garakrral.bettertp.fabric.network.FabricBetterTpPlatform;
import net.fabricmc.api.ModInitializer;

public final class MainFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        Main.init();

        BetterTpNetwork.registerPayloads();
        BetterTpNetwork.registerServerReceivers();
        mc.garakrral.bettertp.network.BetterTpNetwork.init(new FabricBetterTpPlatform());
    }
}
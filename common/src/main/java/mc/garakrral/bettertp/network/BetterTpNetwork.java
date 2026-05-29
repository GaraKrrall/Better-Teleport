package mc.garakrral.bettertp.network;

import java.util.List;
import java.util.UUID;

import mc.garakrral.bettertp.teleport.TpRequestEntry;
import net.minecraft.server.level.ServerPlayer;

public final class BetterTpNetwork {
    private static BetterTpPlatform PLATFORM;

    private BetterTpNetwork() {}

    public static void init(BetterTpPlatform platform) {
        System.out.println("INIT NETWORK " + platform);
        PLATFORM = platform;
    }

    private static BetterTpPlatform platform() {
        System.out.println("PLATFORM = " + PLATFORM);
        if (PLATFORM == null) {
            throw new IllegalStateException("BetterTpNetwork platform not initialized");
        }
        return PLATFORM;
    }

    public static void requestSync() {
        platform().requestSync();
    }

    public static void sendRequestToServer(UUID targetUuid) {
        platform().sendRequestToServer(targetUuid);
    }

    public static void sendReplyToServer(UUID requesterUuid, boolean accept) {
        platform().sendReplyToServer(requesterUuid, accept);
    }

    public static void pushIncomingToClient(ServerPlayer player, List<TpRequestEntry> entries) {
        platform().pushIncomingToClient(player, entries);
    }

    public static void pushToastToClient(ServerPlayer player, String title, String body) {
        platform().pushToastToClient(player, title, body);
    }
}
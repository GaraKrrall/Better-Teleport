package mc.garakrral.bettertp.network;

import java.util.List;
import java.util.UUID;

import mc.garakrral.bettertp.teleport.TpRequestEntry;

import net.minecraft.server.level.ServerPlayer;

public interface BetterTpPlatform {
    void requestSync();
    void sendRequestToServer(UUID targetUuid);
    void sendReplyToServer(UUID requesterUuid, boolean accept);
    void pushIncomingToClient(ServerPlayer player, List<TpRequestEntry> entries);
    void pushToastToClient(ServerPlayer player, String title, String body);
}
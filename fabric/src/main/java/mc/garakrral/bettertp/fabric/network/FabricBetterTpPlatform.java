package mc.garakrral.bettertp.fabric.network;

import java.util.List;
import java.util.UUID;

import mc.garakrral.bettertp.network.BetterTpPlatform;
import mc.garakrral.bettertp.network.packet.*;
import mc.garakrral.bettertp.teleport.TpRequestEntry;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public final class FabricBetterTpPlatform
        implements BetterTpPlatform {

    @Override
    public void requestSync() {
        ClientPlayNetworking.send(
                new RequestSyncPacket()
        );
    }

    @Override
    public void sendRequestToServer(UUID targetUuid) {
        ClientPlayNetworking.send(
                new RequestTpPacket(targetUuid)
        );
    }

    @Override
    public void sendReplyToServer(
            UUID requesterUuid,
            boolean accept
    ) {
        ClientPlayNetworking.send(
                new ReplyTpPacket(
                        requesterUuid,
                        accept
                )
        );
    }

    @Override
    public void pushIncomingToClient(
            ServerPlayer player,
            List<TpRequestEntry> entries
    ) {
        ServerPlayNetworking.send(
                player,
                new SyncIncomingPacket(entries)
        );
    }

    @Override
    public void pushToastToClient(
            ServerPlayer player,
            String title,
            String body
    ) {
        ServerPlayNetworking.send(
                player,
                new ToastPacket(
                        title,
                        body
                )
        );
    }
}

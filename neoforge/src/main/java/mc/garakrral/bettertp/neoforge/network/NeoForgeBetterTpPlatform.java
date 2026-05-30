package mc.garakrral.bettertp.neoforge.network;

import java.util.List;
import java.util.UUID;

import mc.garakrral.bettertp.network.BetterTpPlatform;
import mc.garakrral.bettertp.network.packet.*;
import mc.garakrral.bettertp.teleport.TpRequestEntry;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;

public final class NeoForgeBetterTpPlatform implements BetterTpPlatform {

    @Override
    public void requestSync() {
        ClientPacketDistributor.sendToServer(
                new RequestSyncPacket()
        );
    }

    @Override
    public void sendRequestToServer(UUID targetUuid) {
        ClientPacketDistributor.sendToServer(
                new RequestTpPacket(targetUuid)
        );
    }

    @Override
    public void sendReplyToServer(UUID requesterUuid, boolean accept) {
        ClientPacketDistributor.sendToServer(
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
        PacketDistributor.sendToPlayer(
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
        PacketDistributor.sendToPlayer(
                player,
                new ToastPacket(title, body)
        );
    }
}
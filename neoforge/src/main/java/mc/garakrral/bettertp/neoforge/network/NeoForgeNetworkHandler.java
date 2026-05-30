package mc.garakrral.bettertp.neoforge.network;

import mc.garakrral.bettertp.client.state.TpRequestClientState;
import mc.garakrral.bettertp.neoforge.client.handler.ClientPacketHandlers;
import mc.garakrral.bettertp.network.manager.TpRequestManager;
import mc.garakrral.bettertp.network.packet.*;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public final class NeoForgeNetworkHandler {

    private NeoForgeNetworkHandler() {}

    public static void register(
            RegisterPayloadHandlersEvent event
    ) {

        var registrar =
                event.registrar("1");

        registrar.playToClient(
                SyncIncomingPacket.TYPE,
                SyncIncomingPacket.STREAM_CODEC,
                (packet, context) -> {

                    context.enqueueWork(() ->
                            TpRequestClientState.setIncoming(
                                    packet.entries()
                            )
                    );
                }
        );
        registrar.playToClient(
                ToastPacket.TYPE,
                ToastPacket.STREAM_CODEC,
                (packet, context) ->
                        ClientPacketHandlers.handleToast(packet)
        );

        registrar.playToServer(
                RequestSyncPacket.TYPE,
                RequestSyncPacket.STREAM_CODEC,
                (packet, context) -> {

                    context.enqueueWork(() -> {

                        ServerPlayer player =
                                (ServerPlayer) context.player();

                        TpRequestManager.syncIncomingTo(
                                player
                        );
                    });
                }
        );

        registrar.playToServer(
                RequestTpPacket.TYPE,
                RequestTpPacket.STREAM_CODEC,
                (packet, context) -> {

                    context.enqueueWork(() -> {

                        ServerPlayer requester =
                                (ServerPlayer) context.player();

                        ServerPlayer target =
                                requester.getServer()
                                        .getPlayerList()
                                        .getPlayer(
                                                packet.targetUuid()
                                        );

                        if (target == null) {
                            return;
                        }

                        TpRequestManager.sendRequest(
                                requester,
                                target
                        );
                    });
                }
        );
        registrar.playToServer(
                ReplyTpPacket.TYPE,
                ReplyTpPacket.STREAM_CODEC,
                (packet, context) -> {

                    context.enqueueWork(() -> {

                        ServerPlayer target =
                                (ServerPlayer) context.player();

                        if (packet.accept()) {

                            TpRequestManager.accept(
                                    target,
                                    packet.requesterUuid()
                            );

                        } else {

                            TpRequestManager.reject(
                                    target,
                                    packet.requesterUuid()
                            );
                        }
                    });
                }
        );

    }
}
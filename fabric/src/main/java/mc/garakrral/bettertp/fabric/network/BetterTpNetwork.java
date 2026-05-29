package mc.garakrral.bettertp.fabric.network;

import mc.garakrral.bettertp.client.state.TpRequestClientState;
import mc.garakrral.bettertp.fabric.client.SimpleModTextToast;
import mc.garakrral.bettertp.network.manager.TpRequestManager;
import mc.garakrral.bettertp.network.packet.*;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public final class BetterTpNetwork {

    private BetterTpNetwork() {}

    public static void registerPayloads() {

        PayloadTypeRegistry.playC2S().register(
                RequestSyncPacket.TYPE,
                RequestSyncPacket.STREAM_CODEC
        );

        PayloadTypeRegistry.playC2S().register(
                RequestTpPacket.TYPE,
                RequestTpPacket.STREAM_CODEC
        );

        PayloadTypeRegistry.playC2S().register(
                ReplyTpPacket.TYPE,
                ReplyTpPacket.STREAM_CODEC
        );

        PayloadTypeRegistry.playS2C().register(
                SyncIncomingPacket.TYPE,
                SyncIncomingPacket.STREAM_CODEC
        );

        PayloadTypeRegistry.playS2C().register(
                ToastPacket.TYPE,
                ToastPacket.STREAM_CODEC
        );
    }

    public static void registerServerReceivers() {

        ServerPlayNetworking.registerGlobalReceiver(
                RequestSyncPacket.TYPE,
                (packet, context) -> {

                    context.server().execute(() ->
                            TpRequestManager.syncIncomingTo(
                                    context.player()
                            )
                    );
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(
                RequestTpPacket.TYPE,
                (packet, context) -> {

                    context.server().execute(() -> {

                        ServerPlayer requester =
                                context.player();

                        ServerPlayer target =
                                requester.server
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

        ServerPlayNetworking.registerGlobalReceiver(
                ReplyTpPacket.TYPE,
                (packet, context) -> {

                    context.server().execute(() -> {

                        ServerPlayer target =
                                context.player();

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
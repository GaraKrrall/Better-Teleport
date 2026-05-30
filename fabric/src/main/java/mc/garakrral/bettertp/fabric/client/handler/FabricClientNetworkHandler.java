package mc.garakrral.bettertp.fabric.client.handler;

import mc.garakrral.bettertp.client.state.TpRequestClientState;
import mc.garakrral.bettertp.fabric.client.SimpleModTextToast;
import mc.garakrral.bettertp.network.packet.SyncIncomingPacket;
import mc.garakrral.bettertp.network.packet.ToastPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class FabricClientNetworkHandler {
    public static void registerClientReceivers() {

        ClientPlayNetworking.registerGlobalReceiver(
                SyncIncomingPacket.TYPE,
                (packet, context) ->
                        context.client().execute(() ->
                                TpRequestClientState.setIncoming(
                                        packet.entries()
                                )
                        )
        );

        ClientPlayNetworking.registerGlobalReceiver(
                ToastPacket.TYPE,
                (packet, context) ->
                        context.client().execute(() ->

                                Minecraft.getInstance()
                                        .getToastManager()
                                        .addToast(
                                                new SimpleModTextToast(
                                                        Component.literal(packet.title()),
                                                        Component.literal(packet.body())
                                                )
                                        )
                        )
        );
    }
}

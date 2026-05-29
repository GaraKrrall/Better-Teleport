package mc.garakrral.bettertp.network.packet;

import mc.garakrral.bettertp.Main;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record RequestSyncPacket() implements CustomPacketPayload {

    public static final Type<RequestSyncPacket> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath(
                            Main.MOD_ID,
                            "request_sync"
                    )
            );

    public static final StreamCodec<
            RegistryFriendlyByteBuf,
            RequestSyncPacket
            > STREAM_CODEC =
            StreamCodec.unit(new RequestSyncPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
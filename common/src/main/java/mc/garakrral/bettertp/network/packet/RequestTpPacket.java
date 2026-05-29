package mc.garakrral.bettertp.network.packet;

import java.util.UUID;

import mc.garakrral.bettertp.Main;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record RequestTpPacket(
        UUID targetUuid
) implements CustomPacketPayload {

    public static final Type<RequestTpPacket> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath(
                            Main.MOD_ID,
                            "request_tp"
                    )
            );

    public static final StreamCodec<
            RegistryFriendlyByteBuf,
            RequestTpPacket
            > STREAM_CODEC = new StreamCodec<>() {

        @Override
        public RequestTpPacket decode(
                RegistryFriendlyByteBuf buf
        ) {
            return new RequestTpPacket(
                    buf.readUUID()
            );
        }

        @Override
        public void encode(
                RegistryFriendlyByteBuf buf,
                RequestTpPacket packet
        ) {
            buf.writeUUID(
                    packet.targetUuid()
            );
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
package mc.garakrral.bettertp.network.packet;

import java.util.UUID;

import mc.garakrral.bettertp.Main;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ReplyTpPacket(
        UUID requesterUuid,
        boolean accept
) implements CustomPacketPayload {

    public static final Type<ReplyTpPacket> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath(
                            Main.MOD_ID,
                            "reply_tp"
                    )
            );

    public static final StreamCodec<
            RegistryFriendlyByteBuf,
            ReplyTpPacket
            > STREAM_CODEC = new StreamCodec<>() {

        @Override
        public ReplyTpPacket decode(
                RegistryFriendlyByteBuf buf
        ) {
            return new ReplyTpPacket(
                    buf.readUUID(),
                    buf.readBoolean()
            );
        }

        @Override
        public void encode(
                RegistryFriendlyByteBuf buf,
                ReplyTpPacket packet
        ) {
            buf.writeUUID(
                    packet.requesterUuid()
            );

            buf.writeBoolean(
                    packet.accept()
            );
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
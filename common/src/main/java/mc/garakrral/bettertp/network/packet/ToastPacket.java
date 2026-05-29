package mc.garakrral.bettertp.network.packet;

import mc.garakrral.bettertp.Main;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ToastPacket(
        String title,
        String body
) implements CustomPacketPayload {

    public static final Type<ToastPacket> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath(
                            Main.MOD_ID,
                            "toast"
                    )
            );

    public static final StreamCodec<
            RegistryFriendlyByteBuf,
            ToastPacket
            > STREAM_CODEC = new StreamCodec<>() {

        @Override
        public ToastPacket decode(
                RegistryFriendlyByteBuf buf
        ) {
            return new ToastPacket(
                    buf.readUtf(),
                    buf.readUtf()
            );
        }

        @Override
        public void encode(
                RegistryFriendlyByteBuf buf,
                ToastPacket packet
        ) {
            buf.writeUtf(
                    packet.title()
            );

            buf.writeUtf(
                    packet.body()
            );
        }
    };
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
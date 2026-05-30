package mc.garakrral.bettertp.network.packet;

import java.util.ArrayList;
import java.util.List;

import mc.garakrral.bettertp.Main;
import mc.garakrral.bettertp.teleport.TpRequestEntry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record SyncIncomingPacket(
        List<TpRequestEntry> entries
) implements CustomPacketPayload {

    public static final Type<SyncIncomingPacket> TYPE =
            new Type<>(
                    Identifier.fromNamespaceAndPath(
                            Main.MOD_ID,
                            "sync_incoming"
                    )
            );

    public static final StreamCodec<
            RegistryFriendlyByteBuf,
            SyncIncomingPacket
            > STREAM_CODEC =
            new StreamCodec<>() {

                @Override
                public SyncIncomingPacket decode(
                        RegistryFriendlyByteBuf buf
                ) {
                    int size = buf.readVarInt();

                    List<TpRequestEntry> list =
                            new ArrayList<>();

                    for (int i = 0; i < size; i++) {

                        list.add(
                                new TpRequestEntry(
                                        buf.readUUID(),
                                        buf.readUtf()
                                )
                        );
                    }

                    return new SyncIncomingPacket(list);
                }

                @Override
                public void encode(
                        RegistryFriendlyByteBuf buf,
                        SyncIncomingPacket packet
                ) {

                    buf.writeVarInt(
                            packet.entries.size()
                    );

                    for (
                            TpRequestEntry entry
                            : packet.entries
                    ) {

                        buf.writeUUID(
                                entry.requesterUuid()
                        );

                        buf.writeUtf(
                                entry.requesterName()
                        );
                    }
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
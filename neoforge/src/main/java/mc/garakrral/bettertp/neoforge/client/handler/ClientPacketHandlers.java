package mc.garakrral.bettertp.neoforge.client.handler;

import mc.garakrral.bettertp.neoforge.client.SimpleModTextToast;
import mc.garakrral.bettertp.network.packet.ToastPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ClientPacketHandlers {

    public static void handleToast(
            ToastPacket packet
    ) {

        Minecraft.getInstance()
                .getToastManager()
                .addToast(
                        new SimpleModTextToast(
                                Component.literal(packet.title()),
                                Component.literal(packet.body())
                        )
                );
    }
}

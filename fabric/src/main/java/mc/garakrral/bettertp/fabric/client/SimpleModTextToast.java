package mc.garakrral.bettertp.fabric.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;

public final class SimpleModTextToast implements Toast {
    private final Component title;
    private final Component body;
    private long started = -1L;

    public SimpleModTextToast(Component title, Component body) {
        this.title = title;
        this.body = body;
    }

    @Override
    public Visibility render(GuiGraphics graphics, ToastComponent toastComponent, long time) {
        if (started < 0L) started = time;

        graphics.fill(0, 0, 160, 32, 0xFF202020);
        graphics.drawString(Minecraft.getInstance().font, title, 8, 7, 0xFFFFFF, false);
        graphics.drawString(Minecraft.getInstance().font, body, 8, 18, 0xB0B0B0, false);

        return time - started >= 3000L ? Visibility.HIDE : Visibility.SHOW;
    }
}

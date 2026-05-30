package mc.garakrral.bettertp.neoforge.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.network.chat.Component;

public final class SimpleModTextToast implements Toast {
    private final Component title;
    private final Component subtitle;
    private long started;

    private Toast.Visibility visibility = Visibility.SHOW;

    public SimpleModTextToast(Component title, Component subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    @Override
    public Visibility getWantedVisibility() {
        return visibility;
    }

    @Override
    public void update(ToastManager toastManager, long time) {
        if (started == 0L) {
            started = time;
        }

        if (time - started >= 3000L) {
            visibility = Visibility.HIDE;
        }
    }

    @Override
    public void render(GuiGraphics graphics, Font font, long time) {
        graphics.fill(0, 0, 160, 32, 0xFF202020);

        graphics.drawString(font, title, 8, 7, 0xFFFFFF, false);
        graphics.drawString(font, subtitle, 8, 18, 0xB0B0B0, false);
    }
}

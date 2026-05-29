package mc.garakrral.bettertp.client;

import com.mojang.blaze3d.platform.InputConstants;
import mc.garakrral.bettertp.client.screen.SendTeleportRequestScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class MainClient {
    public static KeyMapping OPEN_MENU_KEY;

    private MainClient() {}

    public static void registerKeybinds() {
        System.out.println("REGISTER KEYBINDS");
        if (OPEN_MENU_KEY != null) return;

        OPEN_MENU_KEY = new KeyMapping(
                "key.better_teleport.open_menu",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "key.categories.bettertp"
        );
    }

    public static void openTeleportRequestScreen() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        mc.setScreen(new SendTeleportRequestScreen(mc.screen));
    }
}

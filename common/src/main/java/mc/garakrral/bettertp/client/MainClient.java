package mc.garakrral.bettertp.client;

import com.mojang.blaze3d.platform.InputConstants;
import mc.garakrral.bettertp.client.screen.SendTeleportRequestScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class MainClient {
    public static KeyMapping OPEN_MENU_KEY;

    public static final KeyMapping.Category BETTERTP_CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath("bettertp", "keybinds"));

    private MainClient() {}

    public static void registerKeybinds() {
        System.out.println("REGISTER KEYBINDS");
        if (OPEN_MENU_KEY != null) return;

        OPEN_MENU_KEY = new KeyMapping(
                "key.better_teleport.open_menu",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                BETTERTP_CATEGORY
        );
    }

    public static void openTeleportRequestScreen() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        mc.setScreen(new SendTeleportRequestScreen(mc.screen));
    }
}

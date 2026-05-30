package mc.garakrral.bettertp.client.screen;

import com.mojang.authlib.GameProfile;
import mc.garakrral.bettertp.client.state.TpRequestClientState;
import mc.garakrral.bettertp.network.BetterTpNetwork;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SendTeleportRequestScreen extends Screen {
    private static final Component TITLE = Component.literal("Teleport");
    private static final Component TAB_PLAYERS = Component.literal("Players");
    private static final Component TAB_PENDING = Component.literal("Pending Requests");
    private static final Component REQUEST_TEXT = Component.literal(" Request");
    private static final Component EMPTY_TEXT = Component.literal("No players online...");
    private static final Component EMPTY_REQUEST = Component.literal("No incoming requests...");
    private static final int PANEL_WIDTH = 220;
    private static final int TAB_HEIGHT = 20;
    private static final int ROW_HEIGHT = 28;
    private static final int PANEL_PADDING = 8;

    private final Screen parent;

    private final List<PlayerRow> players = new ArrayList<>();
    private final List<PlayerRow> pendingRequests = new ArrayList<>();

    private Page currentPage = Page.PLAYERS;
    private int scroll;
    private int maxScroll;

    private int panelX;
    private int panelY;
    private int panelH;

    private final List<RectAction> clickTargets = new ArrayList<>();

    public SendTeleportRequestScreen(Screen parent) {
        super(TITLE);
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        rebuildPlayers();
        layoutPanel();
        BetterTpNetwork.requestSync();
    }

    private void layoutPanel() {
        this.panelX = (this.width - PANEL_WIDTH) / 2;
        this.panelY = 40;
        this.panelH = this.height - 80;
        recomputeScroll();
    }

    private void rebuildPlayers() {
        this.players.clear();

        Minecraft mc = this.minecraft;
        if (mc == null || mc.getConnection() == null || mc.player == null) {
            return;
        }

        for (PlayerInfo info : mc.getConnection().getOnlinePlayers()) {
            GameProfile profile = info.getProfile();

            UUID uuid = profile.id();

            if (uuid.equals(mc.player.getUUID()))
                continue;

            this.players.add(
                    new PlayerRow(
                            uuid,
                            profile.name()
                    )
            );
        }


        this.players.sort((a, b) -> a.name.compareToIgnoreCase(b.name));
        recomputeScroll();
    }

    private void recomputeScroll() {
        int listHeight = getListHeight();
        int rows = currentPage == Page.PLAYERS ? players.size() : pendingRequests.size();
        this.maxScroll = Math.max(0, rows * ROW_HEIGHT - listHeight);
        this.scroll = Mth.clamp(this.scroll, 0, this.maxScroll);
    }

    private int getListTop() {
        return this.panelY + 32;
    }

    private int getListBottom() {
        return this.panelY + this.panelH - 8;
    }

    private int getListHeight() {
        return getListBottom() - getListTop();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.minecraft != null && this.minecraft.level != null) {
            rebuildPlayers();
        }
    }
    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics,
                                   int mouseX,
                                   int mouseY,
                                   float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        this.clickTargets.clear();

        graphics.fill(0, 0, this.width, this.height, 0xAA000000);

        drawPanel(graphics);
        drawTabs(graphics, mouseX, mouseY);
        drawPage(graphics, mouseX, mouseY);

        graphics.text(this.font, TITLE, this.panelX + 8, this.panelY + 8, 0xFFFFFFFF, false);
    }

    private void drawPanel(@UnknownNullability GuiGraphicsExtractor graphics) {
        graphics.fill(this.panelX, this.panelY, this.panelX + PANEL_WIDTH, this.panelY + this.panelH, 0xFF101010);
        graphics.fill(this.panelX, this.panelY, this.panelX + PANEL_WIDTH, this.panelY + 1, 0xFF2A2A2A);
        graphics.fill(this.panelX, this.panelY + 1, this.panelX + PANEL_WIDTH, this.panelY + 25, 0xFF1A1A1A);
        graphics.fill(this.panelX, this.panelY + 25, this.panelX + PANEL_WIDTH, this.panelY + 26, 0xFF2A2A2A);
    }

    private void drawTabs(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        int tabY = this.panelY - 28;
        int tabW = PANEL_WIDTH / 2;

        drawTab(graphics, this.panelX, tabY, tabW, TAB_HEIGHT, TAB_PLAYERS, this.currentPage == Page.PLAYERS,
                mouseX, mouseY, Page.PLAYERS);
        drawTab(graphics, this.panelX + tabW, tabY, tabW, TAB_HEIGHT, TAB_PENDING, this.currentPage == Page.PENDING,
                mouseX, mouseY, Page.PENDING);
    }

    private void drawTab(GuiGraphicsExtractor graphics, int x, int y, int w, int h, Component label, boolean selected,
                         int mouseX, int mouseY, Page page) {
        boolean hover = mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h;
        int bg = selected ? 0xFF2B2B2B : (hover ? 0xFF202020 : 0xFF171717);
        int border = selected ? 0xFFFFFFFF : 0xFF444444;

        graphics.fill(x, y, x + w, y + h, bg);
        graphics.fill(x, y, x + w, y + 1, border);
        graphics.fill(x, y + h - 1, x + w, y + h, border);
        graphics.fill(x, y, x + 1, y + h, border);
        graphics.fill(x + w - 1, y, x + w, y + h, border);

        int textX = x + (w - this.font.width(label)) / 2;
        int textY = y + 6;
        graphics.text(this.font, label, textX, textY, selected ? 0xFFFFFFFF : 0xFFB0B0B0, false);

        this.clickTargets.add(new RectAction(x, y, w, h, () -> {
            this.currentPage = page;
            this.scroll = 0;
            recomputeScroll();
        }));
    }

    private void drawPage(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (this.currentPage == Page.PLAYERS) {
            drawPlayersPage(graphics, mouseX, mouseY);
        } else {
            drawPendingPage(graphics);
        }
    }

    private void drawPlayersPage(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        int listX = this.panelX + PANEL_PADDING;
        int listY = getListTop();
        int listW = PANEL_WIDTH - PANEL_PADDING * 2;
        int listH = getListHeight();

        if (this.players.isEmpty()) {
            drawCenteredMessage(graphics, EMPTY_TEXT, listX, listY, listW, listH);
            return;
        }

        int startIndex = Math.max(0, this.scroll / ROW_HEIGHT);
        int yOffset = -(this.scroll % ROW_HEIGHT);

        for (int i = startIndex; i < this.players.size(); i++) {
            int rowY = listY + yOffset + (i - startIndex) * ROW_HEIGHT;
            if (rowY > listY + listH) break;
            if (rowY + ROW_HEIGHT < listY) continue;

            PlayerRow row = this.players.get(i);
            boolean hoverRow = mouseX >= listX && mouseX < listX + listW && mouseY >= rowY && mouseY < rowY + ROW_HEIGHT;

            int rowBg = hoverRow ? 0xFF202020 : 0xFF141414;
            graphics.fill(listX, rowY, listX + listW, rowY + ROW_HEIGHT - 1, rowBg);
            graphics.fill(listX, rowY + ROW_HEIGHT - 1, listX + listW, rowY + ROW_HEIGHT, 0xFF2A2A2A);

            int nameX = listX + 6;
            int nameY = rowY + 10;
            graphics.text(this.font, row.name, nameX, nameY, 0xFFFFFFFF, false);

            int btnW = 74;
            int btnH = 20;
            int btnX = listX + listW - btnW - 4;
            int btnY = rowY + 4;

            boolean sent = row.sending;
            int btnBg = sent ? 0xFF3A3A3A : (hoverRow ? 0xFF2E2E2E : 0xFF252525);
            int btnBorder = sent ? 0xFF777777 : 0xFF555555;

            graphics.fill(btnX, btnY, btnX + btnW, btnY + btnH, btnBg);
            graphics.fill(btnX, btnY, btnX + btnW, btnY + 1, btnBorder);
            graphics.fill(btnX, btnY + btnH - 1, btnX + btnW, btnY + btnH, btnBorder);
            graphics.fill(btnX, btnY, btnX + 1, btnY + btnH, btnBorder);
            graphics.fill(btnX + btnW - 1, btnY, btnX + btnW, btnY + btnH, btnBorder);

            Component text = sent ? Component.literal("Sent") : REQUEST_TEXT;
            int textX = btnX + (btnW - this.font.width(text)) / 2;
            int textY = btnY + 6;
            graphics.text(this.font, text, textX, textY, sent ? 0xFFC0C0C0 : 0xFFFFFFFF, false);

            if (!sent) {
                PlayerRow finalRow = row;
                this.clickTargets.add(new RectAction(btnX, btnY, btnW, btnH, () -> sendTeleportRequest(finalRow)));
            }
        }
    }

    private void drawPendingPage(GuiGraphicsExtractor graphics) {
        int listX = this.panelX + PANEL_PADDING;
        int listY = getListTop();
        int listW = PANEL_WIDTH - PANEL_PADDING * 2;
        int listH = getListHeight();

        var requests = TpRequestClientState.incoming();

        if (requests.isEmpty()) {
            drawCenteredMessage(
                    graphics,
                    EMPTY_REQUEST,
                    listX, listY, listW, listH
            );
            return;
        }

        int y = listY;
        for (int i = 0; i < requests.size(); i++) {
            var req = requests.get(i);
            int rowH = 28;

            if (y + rowH < listY) {
                y += rowH;
                continue;
            }
            if (y > listY + listH) break;

            graphics.fill(listX, y, listX + listW, y + rowH - 1, 0xFF141414);
            graphics.fill(listX, y + rowH - 1, listX + listW, y + rowH, 0xFF2A2A2A);

            graphics.text(this.font, req.requesterName(), listX + 6, y + 10, 0xFFFFFFFF, false);

            int btnW = 38;
            int btnH = 20;
            int gap = 4;
            int rejectX = listX + listW - btnW - 4;
            int acceptX = rejectX - btnW - gap;
            int btnY = y + 4;

            drawSmallButton(graphics, acceptX, btnY, btnW, btnH, "✓");
            drawSmallButton(graphics, rejectX, btnY, btnW, btnH, "✕");

            UUID requesterUuid = req.requesterUuid();

            this.clickTargets.add(new RectAction(
                    acceptX, btnY, btnW, btnH,
                    () -> BetterTpNetwork.sendReplyToServer(requesterUuid, true)
            ));
            this.clickTargets.add(new RectAction(
                    rejectX, btnY, btnW, btnH,
                    () -> BetterTpNetwork.sendReplyToServer(requesterUuid, false)
            ));

            y += rowH;
        }
    }

    private void drawSmallButton(GuiGraphicsExtractor graphics, int x, int y, int w, int h, String text) {
        graphics.fill(x, y, x + w, y + h, 0xFF252525);
        graphics.fill(x, y, x + w, y + 1, 0xFF555555);
        graphics.fill(x, y + h - 1, x + w, y + h, 0xFF555555);
        graphics.fill(x, y, x + 1, y + h, 0xFF555555);
        graphics.fill(x + w - 1, y, x + w, y + h, 0xFF555555);

        int textX = x + (w - this.font.width(text)) / 2;
        int textY = y + 6;
        graphics.text(this.font, text, textX, textY, 0xFFFFFFFF, false);
    }

    private void drawCenteredMessage(GuiGraphicsExtractor graphics, Component message, int x, int y, int w, int h) {
        int textX = x + (w - this.font.width(message)) / 2;
        int textY = y + h / 2 - 4;
        graphics.text(this.font, message, textX, textY, 0xFFA0A0A0, false);
    }

    private void sendTeleportRequest(PlayerRow row) {
        row.sending = true;
        BetterTpNetwork.sendRequestToServer(row.uuid);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (this.currentPage == Page.PLAYERS && isInsidePanel(mouseX, mouseY)) {
            this.scroll = Mth.clamp((int)(this.scroll - scrollY * 12), 0, this.maxScroll);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
        if (mouseButtonEvent.button() != 0) {
            return super.mouseClicked(mouseButtonEvent, bl);
        }

        double mouseX = mouseButtonEvent.x();
        double mouseY = mouseButtonEvent.y();

        for (int i = this.clickTargets.size() - 1; i >= 0; i--) {
            RectAction action = this.clickTargets.get(i);
            if (action.contains(mouseX, mouseY)) {
                action.run();
                return true;
            }
        }

        if (!isInsidePanel(mouseX, mouseY) && !isInsideTabs(mouseX, mouseY)) {
            onClose();
            return true;
        }

        return super.mouseClicked(mouseButtonEvent, bl);
    }

    private boolean isInsidePanel(double mouseX, double mouseY) {
        return mouseX >= this.panelX && mouseX < this.panelX + PANEL_WIDTH
                && mouseY >= this.panelY && mouseY < this.panelY + this.panelH;
    }

    private boolean isInsideTabs(double mouseX, double mouseY) {
        int tabY = this.panelY - 28;
        int tabH = TAB_HEIGHT;
        return mouseX >= this.panelX && mouseX < this.panelX + PANEL_WIDTH
                && mouseY >= tabY && mouseY < tabY + tabH;
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private enum Page {
        PLAYERS,
        PENDING
    }

    private static final class PlayerRow {
        private final UUID uuid;
        private final String name;
        private boolean sending;

        private PlayerRow(UUID uuid, String name) {
            this.uuid = uuid;
            this.name = name;
        }
    }

    private static final class RectAction {
        private final int x, y, w, h;
        private final Runnable action;

        private RectAction(int x, int y, int w, int h, Runnable action) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.action = action;
        }

        private boolean contains(double mx, double my) {
            return mx >= x && mx < x + w && my >= y && my < y + h;
        }

        private void run() {
            action.run();
        }
    }
}
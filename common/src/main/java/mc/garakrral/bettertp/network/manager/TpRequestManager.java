package mc.garakrral.bettertp.network.manager;

import java.util.*;

import mc.garakrral.bettertp.network.BetterTpNetwork;
import mc.garakrral.bettertp.teleport.TpRequestEntry;
import net.minecraft.server.level.ServerPlayer;

public final class TpRequestManager {
    private static final Map<UUID, LinkedHashMap<UUID, TpRequestEntry>> INCOMING = new LinkedHashMap<>();

    private TpRequestManager() {}

    public static void sendRequest(ServerPlayer requester, ServerPlayer target) {
        if (requester == null || target == null) return;
        if (requester.getUUID().equals(target.getUUID())) return;

        LinkedHashMap<UUID, TpRequestEntry> map =
                INCOMING.computeIfAbsent(target.getUUID(), k -> new LinkedHashMap<>());
        map.put(requester.getUUID(), new TpRequestEntry(requester.getUUID(), requester.getName().getString()));

        BetterTpNetwork.pushIncomingToClient(target, snapshot(target.getUUID()));
        BetterTpNetwork.pushToastToClient(requester, "Teleport request sent", target.getName().getString());
        BetterTpNetwork.pushToastToClient(target, "New teleport request!", requester.getName().getString());
    }

    public static void accept(ServerPlayer target, UUID requesterUuid) {
        LinkedHashMap<UUID, TpRequestEntry> map = INCOMING.get(target.getUUID());
        if (map == null) return;

        TpRequestEntry entry = map.remove(requesterUuid);
        if (entry == null) return;

        ServerPlayer requester = target.server.getPlayerList().getPlayer(requesterUuid);
        if (requester != null) {
            requester.teleportTo(
                    target.serverLevel(),
                    target.getX(),
                    target.getY(),
                    target.getZ(),
                    Set.of(),
                    requester.getYRot(),
                    requester.getXRot(),
                    true
            );

            BetterTpNetwork.pushToastToClient(requester, "Teleport request accepted!", target.getName().getString());
        }

        BetterTpNetwork.pushToastToClient(target, "Request accepted!", entry.requesterName());
        BetterTpNetwork.pushIncomingToClient(target, snapshot(target.getUUID()));
    }

    public static void reject(ServerPlayer target, UUID requesterUuid) {
        LinkedHashMap<UUID, TpRequestEntry> map = INCOMING.get(target.getUUID());
        if (map == null) return;

        TpRequestEntry entry = map.remove(requesterUuid);
        if (entry == null) return;

        ServerPlayer requester = target.server.getPlayerList().getPlayer(requesterUuid);
        if (requester != null) {
            BetterTpNetwork.pushToastToClient(requester, "Teleport request rejected!", target.getName().getString());
        }

        BetterTpNetwork.pushToastToClient(target, "Request rejected!", entry.requesterName());
        BetterTpNetwork.pushIncomingToClient(target, snapshot(target.getUUID()));
    }

    public static void syncIncomingTo(ServerPlayer target) {
        BetterTpNetwork.pushIncomingToClient(target, snapshot(target.getUUID()));
    }

    public static List<TpRequestEntry> snapshot(UUID targetUuid) {
        LinkedHashMap<UUID, TpRequestEntry> map = INCOMING.get(targetUuid);
        if (map == null || map.isEmpty()) return List.of();
        return new ArrayList<>(map.values());
    }
}
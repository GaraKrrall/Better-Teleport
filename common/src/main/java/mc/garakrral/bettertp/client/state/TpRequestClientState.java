package mc.garakrral.bettertp.client.state;

import mc.garakrral.bettertp.teleport.TpRequestEntry;

import java.util.ArrayList;
import java.util.List;

public final class TpRequestClientState {
    private static final List<TpRequestEntry> INCOMING = new ArrayList<>();

    private TpRequestClientState() {}

    public static synchronized void setIncoming(List<TpRequestEntry> entries) {
        INCOMING.clear();
        INCOMING.addAll(entries);
    }

    public static synchronized List<TpRequestEntry> incoming() {
        return List.copyOf(INCOMING);
    }

    public static synchronized void clear() {
        INCOMING.clear();
    }
}

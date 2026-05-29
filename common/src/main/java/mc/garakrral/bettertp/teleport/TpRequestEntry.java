package mc.garakrral.bettertp.teleport;

import java.util.UUID;

public record TpRequestEntry(
        UUID requesterUuid,
        String requesterName
) {
}
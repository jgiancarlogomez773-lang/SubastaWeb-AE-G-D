package com.subastaweb.application;

import com.subastaweb.domain.Auction;
import com.subastaweb.domain.DomainException;
import com.subastaweb.domain.ErrorCode;
import com.subastaweb.domain.Role;
import org.springframework.stereotype.Component;

/**
 * T1-14 / HU-03: reglas de propiedad y visibilidad aplicadas en el servidor.
 * Los mensajes son genéricos para no revelar datos del recurso ajeno.
 */
@Component
public class AuctionAccessPolicy {

    public boolean isOwner(Long actorId, Auction auction) {
        return actorId != null && auction.getSeller().getId().equals(actorId);
    }

    /** 403: operaciones de escritura sobre una solicitud solo para su vendedor. */
    public void requireOwner(Long actorId, Auction auction) {
        if (!isOwner(actorId, auction)) {
            throw new DomainException(ErrorCode.FORBIDDEN, "No tienes permiso para esta acción.");
        }
    }

    /** 404: una subasta no pública solo existe para su vendedor o para un ADMIN. */
    public void requireVisible(Long actorId, Role actorRole, Auction auction) {
        boolean visible = auction.getStatus().isPublicVisible()
                || isOwner(actorId, auction)
                || actorRole == Role.ADMIN;
        if (!visible) {
            throw new DomainException(ErrorCode.AUCTION_NOT_PUBLIC, "La subasta no está disponible.");
        }
    }
}

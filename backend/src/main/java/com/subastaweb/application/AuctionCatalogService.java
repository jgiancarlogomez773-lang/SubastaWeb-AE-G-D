package com.subastaweb.application;

import com.subastaweb.domain.Auction;
import com.subastaweb.domain.AuctionStatus;
import com.subastaweb.domain.DomainException;
import com.subastaweb.domain.ErrorCode;
import com.subastaweb.domain.FieldIssue;
import com.subastaweb.persistence.AuctionRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** T2-13 y T2-14: consultas de subastas protegidas por estado. */
@Service
public class AuctionCatalogService {

    public static final int MAX_PAGE_SIZE = 50;
    private static final int MAX_PAGE = 100_000;

    private final AuctionRepository auctions;
    private final AuctionAccessPolicy accessPolicy;

    public AuctionCatalogService(AuctionRepository auctions, AuctionAccessPolicy accessPolicy) {
        this.auctions = auctions;
        this.accessPolicy = accessPolicy;
    }

    /** T2-13: solo ACTIVA y FINALIZADA. Las pendientes y rechazadas nunca salen en el catálogo. */
    @Transactional(readOnly = true)
    public Page<Auction> listPublic(int page, int size) {
        List<FieldIssue> issues = new ArrayList<>();
        if (page < 0 || page > MAX_PAGE) {
            issues.add(new FieldIssue("page", "La página debe estar entre 0 y 100000."));
        }
        if (size < 1 || size > MAX_PAGE_SIZE) {
            issues.add(new FieldIssue("size", "El tamaño debe estar entre 1 y 50."));
        }
        if (!issues.isEmpty()) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR,
                    "Los parámetros de consulta no son válidos.", issues);
        }
        return auctions.findPage(AuctionStatus.publicStatuses(), PageRequest.of(page, size));
    }

    /**
     * T2-14: detalle. Si la subasta no existe o no es pública (y quien pregunta no es su dueño)
     * la respuesta es la misma: 404, sin revelar que existe.
     */
    @Transactional(readOnly = true)
    public Auction getVisible(Long id, Long actorId) {
        Auction auction = auctions.findWithCategoryById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.AUCTION_NOT_PUBLIC,
                        "La subasta no está disponible."));
        accessPolicy.requireVisible(actorId, auction);
        return auction;
    }
}

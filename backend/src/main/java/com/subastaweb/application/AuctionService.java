package com.subastaweb.application;
import com.subastaweb.application.UpdateAuctionCommand;

import com.subastaweb.domain.Auction;
import com.subastaweb.domain.AuctionImage;
import com.subastaweb.domain.AuctionRules;
import com.subastaweb.domain.AuctionStatus;
import com.subastaweb.domain.Category;
import com.subastaweb.domain.DomainException;
import com.subastaweb.domain.ErrorCode;
import com.subastaweb.domain.FieldIssue;
import com.subastaweb.domain.ImageType;
import com.subastaweb.domain.User;
import com.subastaweb.persistence.AuctionRepository;
import com.subastaweb.persistence.CategoryRepository;
import com.subastaweb.persistence.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class AuctionService {

    private static final int TITLE_MIN = 5;
    private static final int TITLE_MAX = 120;
    private static final int DESCRIPTION_MIN = 20;
    private static final int DESCRIPTION_MAX = 2000;

    private final AuctionRepository auctions;
    private final UserRepository users;
    private final CategoryRepository categories;
    private final ImageStorage storage;

    public AuctionService(AuctionRepository auctions, UserRepository users,
                          CategoryRepository categories, ImageStorage storage) {
        this.auctions = auctions;
        this.users = users;
        this.categories = categories;
        this.storage = storage;
    }

    @Transactional
    public Auction create(Long sellerId, CreateAuctionCommand cmd) {
        User seller = users.findById(sellerId)
                .orElseThrow(() -> new DomainException(ErrorCode.AUTH_REQUIRED, "Sesion no valida."));

        String title = cmd.title() == null ? "" : cmd.title().trim();
        String description = cmd.description() == null ? "" : cmd.description().trim();
        validateFields(title, description, cmd.durationHours());

        ImageType imageType = validateImage(cmd.image());

        Category category = categories.findByIdAndActiveTrue(cmd.categoryId())
                .orElseThrow(() -> new DomainException(ErrorCode.VALIDATION_ERROR,
                        "Los datos enviados no son validos.",
                        List.of(new FieldIssue("categoryId", "La categoria no existe o no esta disponible."))));

        String storedName = storage.store(cmd.image(), imageType.extension());
        deleteImageUnlessCommitted(storedName);

        Auction auction = new Auction(seller, category, title, description,
                cmd.startingPrice(), cmd.durationHours(), AuctionStatus.PENDIENTE_REVISION);
        auction.addImage(new AuctionImage(storedName, imageType.mimeType(), cmd.image().length));
        return auctions.save(auction);
    }

    @Transactional(readOnly = true)
    public List<Auction> findMine(Long sellerId) {
        if (sellerId == null || !users.existsById(sellerId)) {
            throw new DomainException(ErrorCode.AUTH_REQUIRED, "Sesion no valida.");
        }
        return auctions.findBySeller_IdOrderByCreatedAtDesc(sellerId);
    }

    private void validateFields(String title, String description, int durationHours) {
        List<FieldIssue> issues = new ArrayList<>();
        if (title.length() < TITLE_MIN || title.length() > TITLE_MAX) {
            issues.add(new FieldIssue("title", "El titulo debe tener entre 5 y 120 caracteres."));
        }
        if (description.length() < DESCRIPTION_MIN || description.length() > DESCRIPTION_MAX) {
            issues.add(new FieldIssue("description", "La descripcion debe tener entre 20 y 2000 caracteres."));
        }
        if (!AuctionRules.isAllowedDuration(durationHours)) {
            issues.add(new FieldIssue("durationHours", "La duracion debe ser de 24, 48 o 72 horas."));
        }
        if (!issues.isEmpty()) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, "Los datos enviados no son validos.", issues);
        }
    }

    private ImageType validateImage(byte[] image) {
        if (image == null || image.length == 0) {
            throw invalidImage("Debes adjuntar una imagen.");
        }
        if (image.length > AuctionRules.MAX_IMAGE_BYTES) {
            throw invalidImage("La imagen supera el maximo de 3 MB.");
        }
        return ImageType.detect(image)
                .orElseThrow(() -> invalidImage("La imagen debe ser JPG, PNG o WebP."));
    }

    private static DomainException invalidImage(String message) {
        return new DomainException(ErrorCode.INVALID_IMAGE, "La imagen no es valida.",
                List.of(new FieldIssue("image", message)));
    }

    private void deleteImageUnlessCommitted(String storedName) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status != STATUS_COMMITTED) {
                    storage.delete(storedName);
                }
            }
        });
    }

    @Transactional
    public Auction patchRejected(Long actorId, Long auctionId, UpdateAuctionCommand cmd) {
        if (actorId == null || !users.existsById(actorId)) {
            throw new DomainException(ErrorCode.AUTH_REQUIRED, "Sesion no valida.");
        }
        Auction auction = auctions.findById(auctionId)
                .orElseThrow(() -> new DomainException(ErrorCode.AUCTION_NOT_PUBLIC, "La subasta no existe."));

        if (!auction.getSeller().getId().equals(actorId)) {
            throw new DomainException(ErrorCode.FORBIDDEN, "No tienes permiso para editar esta subasta.");
        }
        if (auction.getStatus() != AuctionStatus.RECHAZADA) {
            throw new DomainException(ErrorCode.AUCTION_CONFLICT,
                    "Solo se pueden editar subastas en estado RECHAZADA.");
        }

        String nextTitle = cmd.title() != null ? cmd.title().trim() : auction.getTitle();
        String nextDescription = cmd.description() != null ? cmd.description().trim() : auction.getDescription();
        Long nextCategoryId = cmd.categoryId() != null ? cmd.categoryId() : auction.getCategory().getId();
        long nextStartingPrice = cmd.startingPrice() != null ? cmd.startingPrice() : auction.getStartingPrice();
        int nextDurationHours = cmd.durationHours() != null ? cmd.durationHours() : auction.getDurationHours();

        validateFields(nextTitle, nextDescription, nextDurationHours);

        Category nextCategory = categories.findByIdAndActiveTrue(nextCategoryId)
                .orElseThrow(() -> new DomainException(ErrorCode.VALIDATION_ERROR,
                        "Los datos enviados no son validos.",
                        List.of(new FieldIssue("categoryId", "La categoria no existe o no esta disponible."))));

        boolean changed = !nextTitle.equals(auction.getTitle())
                || !nextDescription.equals(auction.getDescription())
                || !nextCategory.getId().equals(auction.getCategory().getId())
                || nextStartingPrice != auction.getStartingPrice()
                || nextDurationHours != auction.getDurationHours();

        if (changed) {
            auction.applyRejectedEdit(nextTitle, nextDescription, nextCategory, nextStartingPrice, nextDurationHours);
        }
        return auctions.save(auction);
    }

    @Transactional
    public Auction resubmit(Long actorId, Long auctionId, Integer expectedContentVersion) {
        if (actorId == null || !users.existsById(actorId)) {
            throw new DomainException(ErrorCode.AUTH_REQUIRED, "Sesion no valida.");
        }
        Auction auction = auctions.findById(auctionId)
                .orElseThrow(() -> new DomainException(ErrorCode.AUCTION_NOT_PUBLIC, "La subasta no existe."));

        if (!auction.getSeller().getId().equals(actorId)) {
            throw new DomainException(ErrorCode.FORBIDDEN, "No tienes permiso para reenviar esta subasta.");
        }
        if (auction.getStatus() != AuctionStatus.RECHAZADA) {
            throw new DomainException(ErrorCode.AUCTION_CONFLICT,
                    "Solo se pueden reenviar subastas en estado RECHAZADA.");
        }
        if (expectedContentVersion != null && auction.getContentVersion() != expectedContentVersion) {
            throw new DomainException(ErrorCode.AUCTION_CONFLICT,
                    "La version de contenido esta desfasada.");
        }
        if (auction.getContentVersion() <= auction.getRejectedContentVersion()) {
            throw new DomainException(ErrorCode.AUCTION_CONFLICT,
                    "Debe realizar cambios antes de reenviar la subasta.");
        }

        auction.markResubmitted();
        return auctions.save(auction);
    }
}

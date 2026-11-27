package com.subastaweb.application;

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
import com.subastaweb.persistence.AuctionImageRepository;
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

    private final AuctionRepository auctions;
    private final AuctionImageRepository auctionImages;
    private final UserRepository users;
    private final CategoryRepository categories;
    private final ImageStorage storage;

    public AuctionService(AuctionRepository auctions, AuctionImageRepository auctionImages,
                          UserRepository users, CategoryRepository categories, ImageStorage storage) {
        this.auctions = auctions;
        this.auctionImages = auctionImages;
        this.users = users;
        this.categories = categories;
        this.storage = storage;
    }

    /**
     * HU-04: crea una solicitud privada (PENDIENTE_REVISION) con su imagen. Todo se valida antes de guardar
     * nada y, si la transacción no termina bien, se borra el archivo: no queda persistencia parcial.
     */
    @Transactional
    public Auction create(Long sellerId, CreateAuctionCommand cmd) {
        User seller = users.findById(sellerId)
                .orElseThrow(() -> new DomainException(ErrorCode.AUTH_REQUIRED, "Sesión no válida."));

        String title = cmd.title().trim();
        String description = cmd.description().trim();
        validateFields(title, description, cmd.startingPrice(), cmd.durationHours());

        ImageType imageType = validateImage(cmd.image());

        Category category = categories.findByIdAndActiveTrue(cmd.categoryId())
                .orElseThrow(() -> new DomainException(ErrorCode.VALIDATION_ERROR,
                        "Los datos enviados no son válidos.",
                        List.of(new FieldIssue("categoryId", "La categoría no existe o no está disponible."))));

        String storedName = storage.store(cmd.image(), imageType.extension());
        deleteFileUnlessCommitted(storedName);

        Auction auction = auctions.save(new Auction(seller, category, title, description,
                cmd.startingPrice(), cmd.durationHours(), AuctionStatus.PENDIENTE_REVISION));
        auctionImages.save(new AuctionImage(auction, storedName, imageType.mimeType(), cmd.image().length));
        return auction;
    }

    /** Las mismas reglas que la base (V2), para devolver 422 claro en vez de un error de restricción. */
    private void validateFields(String title, String description, long price, int durationHours) {
        List<FieldIssue> issues = new ArrayList<>();
        if (title.length() < AuctionRules.TITLE_MIN || title.length() > AuctionRules.TITLE_MAX) {
            issues.add(new FieldIssue("title", "El título debe tener entre 5 y 120 caracteres."));
        }
        if (description.length() < AuctionRules.DESCRIPTION_MIN
                || description.length() > AuctionRules.DESCRIPTION_MAX) {
            issues.add(new FieldIssue("description", "La descripción debe tener entre 20 y 2000 caracteres."));
        }
        if (price < AuctionRules.MIN_STARTING_PRICE || price > AuctionRules.MAX_STARTING_PRICE) {
            issues.add(new FieldIssue("startingPrice", "El precio inicial está fuera del rango permitido."));
        }
        if (!AuctionRules.isAllowedDuration(durationHours)) {
            issues.add(new FieldIssue("durationHours", "La duración debe ser 24, 48 o 72 horas."));
        }
        if (!issues.isEmpty()) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, "Los datos enviados no son válidos.", issues);
        }
    }

    private ImageType validateImage(byte[] image) {
        if (image == null || image.length == 0) {
            throw invalidImage("Debes adjuntar una imagen.");
        }
        if (image.length > AuctionRules.MAX_IMAGE_BYTES) {
            throw invalidImage("La imagen supera el máximo de 3 MB.");
        }
        return ImageType.detect(image)
                .orElseThrow(() -> invalidImage("La imagen debe ser JPG, PNG o WebP."));
    }

    private static DomainException invalidImage(String message) {
        return new DomainException(ErrorCode.INVALID_IMAGE, "La imagen no es válida.",
                List.of(new FieldIssue("image", message)));
    }

    private void deleteFileUnlessCommitted(String storedName) {
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
}

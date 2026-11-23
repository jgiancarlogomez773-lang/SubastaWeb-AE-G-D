
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

    /**
     * HU-04: crea una solicitud privada (PENDIENTE_REVISION).
     * Si la transacción falla, se elimina la imagen almacenada.
     */
    @Transactional
    public Auction create(Long sellerId, CreateAuctionCommand cmd) {
        User seller = users.findById(sellerId)
                .orElseThrow(() -> new DomainException(
                        ErrorCode.AUTH_REQUIRED, "Sesión no válida."));

        String title = cmd.title() == null ? "" : cmd.title().trim();
        String description = cmd.description() == null ? "" : cmd.description().trim();

        validateFields(title, description, cmd.durationHours());

        ImageType imageType = validateImage(cmd.image());

        Category category = categories.findByIdAndActiveTrue(cmd.categoryId())
                .orElseThrow(() -> new DomainException(
                        ErrorCode.VALIDATION_ERROR,
                        "Los datos enviados no son válidos.",
                        List.of(new FieldIssue(
                                "categoryId",
                                "La categoría no existe o no está disponible."))));

        String storedName = storage.store(cmd.image(), imageType.extension());
        deleteImageUnlessCommitted(storedName);

        Auction auction = new Auction(
                seller,
                category,
                title,
                description,
                cmd.startingPrice(),
                cmd.durationHours(),
                AuctionStatus.PENDIENTE_REVISION
        );

        auction.addImage(new AuctionImage(
                storedName,
                imageType.mimeType(),
                cmd.image().length
        ));

        return auctions.save(auction);
    }

    /** Valida los campos que también exige la base de datos. */
    private void validateFields(String title, String description, int durationHours) {
        List<FieldIssue> issues = new ArrayList<>();

        if (title.length() < TITLE_MIN || title.length() > TITLE_MAX) {
            issues.add(new FieldIssue(
                    "title", "El título debe tener entre 5 y 120 caracteres."));
        }

        if (description.length() < DESCRIPTION_MIN
                || description.length() > DESCRIPTION_MAX) {
            issues.add(new FieldIssue(
                    "description",
                    "La descripción debe tener entre 20 y 2000 caracteres."));
        }

        if (!AuctionRules.isAllowedDuration(durationHours)) {
            issues.add(new FieldIssue(
                    "durationHours",
                    "La duración debe ser de 24, 48 o 72 horas."));
        }

        if (!issues.isEmpty()) {
            throw new DomainException(
                    ErrorCode.VALIDATION_ERROR,
                    "Los datos enviados no son válidos.",
                    issues
            );
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
                .orElseThrow(() -> invalidImage(
                        "La imagen debe ser JPG, PNG o WebP."));
    }

    private static DomainException invalidImage(String message) {
        return new DomainException(
                ErrorCode.INVALID_IMAGE,
                "La imagen no es válida.",
                List.of(new FieldIssue("image", message))
        );
    }

    private void deleteImageUnlessCommitted(String storedName) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCompletion(int status) {
                        if (status != STATUS_COMMITTED) {
                            storage.delete(storedName);
                        }
                    }
                }
        );
    }
}

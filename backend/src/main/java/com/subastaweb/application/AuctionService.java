package com.subastaweb.application;

import com.subastaweb.domain.Auction;
import com.subastaweb.domain.AuctionRules;
import com.subastaweb.domain.AuctionImage;
import com.subastaweb.domain.AuctionStatus;
import com.subastaweb.domain.Category;
import com.subastaweb.domain.DomainException;
import com.subastaweb.domain.ErrorCode;
import com.subastaweb.domain.FieldIssue;
import com.subastaweb.domain.ImageType;
import com.subastaweb.domain.User;
import com.subastaweb.persistence.AuctionRepository;
import com.subastaweb.persistence.AuctionImageRepository;
import com.subastaweb.persistence.CategoryRepository;
import com.subastaweb.persistence.UserRepository;
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
     * HU-04: crea una solicitud privada (PENDIENTE_REVISION). Todo se valida antes de guardar nada y, si la
     * transacción no termina bien, se borra la imagen: no queda persistencia parcial.
     */
    @Transactional
    public Auction create(Long sellerId, CreateAuctionCommand cmd) {
        User seller = users.findById(sellerId)
                .orElseThrow(() -> new DomainException(ErrorCode.AUTH_REQUIRED, "Sesión no válida."));

        ImageType imageType = validateImage(cmd.image());

        Category category = categories.findByIdAndActiveTrue(cmd.categoryId())
                .orElseThrow(() -> new DomainException(ErrorCode.VALIDATION_ERROR,
                        "Los datos enviados no son válidos.",
                        List.of(new FieldIssue("categoryId", "La categoría no existe o no está disponible."))));

        String storedName = storage.store(cmd.image(), imageType.extension());
        deleteImageUnlessCommitted(storedName);

                Auction auction = auctions.save(new Auction(
                seller, category, cmd.title().trim(), cmd.description().trim(),
                cmd.startingPrice(), cmd.durationHours(), AuctionStatus.PENDIENTE_REVISION));
        auctionImages.save(new AuctionImage(
                auction, storedName, imageType.mimeType(), cmd.image().length));
        return auction;
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
}

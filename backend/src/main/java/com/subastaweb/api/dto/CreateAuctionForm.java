package com.subastaweb.api.dto;

import com.subastaweb.domain.AuctionRules;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

/** Campos multipart/form-data de POST /api/v1/auctions. */
public class CreateAuctionForm {

    @NotBlank(message = "El título es obligatorio.")
    @Size(min = 5, max = 120, message = "El título debe tener entre 5 y 120 caracteres.")
    private String title;

    @NotBlank(message = "La descripción es obligatoria.")
    @Size(min = 20, max = 2000, message = "La descripción debe tener entre 20 y 2000 caracteres.")
    private String description;

    @NotNull(message = "La categoría es obligatoria.")
    @Positive(message = "La categoría no es válida.")
    private Long categoryId;

    @NotNull(message = "El precio inicial es obligatorio.")
    @Min(value = AuctionRules.MIN_STARTING_PRICE, message = "El precio inicial mínimo es COP 1.000.")
    @Max(value = AuctionRules.MAX_STARTING_PRICE, message = "El precio inicial supera el máximo permitido.")
    private Long startingPrice;

    @NotNull(message = "La duración es obligatoria.")
    
    private Integer durationHours;
        @AssertTrue(message = "La duración debe ser 24, 48 o 72 horas.")
    public boolean isDurationAllowed() {
        return durationHours == null || AuctionRules.ALLOWED_DURATIONS_HOURS.contains(durationHours);
    }

    private MultipartFile image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(Long startingPrice) {
        this.startingPrice = startingPrice;
    }

    public Integer getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(Integer durationHours) {
        this.durationHours = durationHours;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}

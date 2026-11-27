package com.subastaweb.api;

import com.subastaweb.api.dto.AuctionResponse;
import com.subastaweb.api.dto.CreateAuctionForm;
import com.subastaweb.api.dto.PageResponse;
import com.subastaweb.api.dto.PublicAuctionDetail;
import com.subastaweb.api.dto.PublicAuctionSummary;
import com.subastaweb.application.AuctionCatalogService;
import com.subastaweb.application.AuctionService;
import com.subastaweb.application.CreateAuctionCommand;
import com.subastaweb.domain.Auction;
import com.subastaweb.security.SessionUser;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/auctions")
public class AuctionController {

    private final AuctionService auctionService;
    private final AuctionCatalogService catalogService;

    public AuctionController(AuctionService auctionService, AuctionCatalogService catalogService) {
        this.auctionService = auctionService;
        this.catalogService = catalogService;
    }

    /** T2-13: catálogo público paginado (solo ACTIVA y FINALIZADA). */
    @GetMapping
    public PageResponse<PublicAuctionSummary> list(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "12") int size) {
        Page<Auction> result = catalogService.listPublic(page, size);
        List<PublicAuctionSummary> items = result.getContent().stream().map(PublicAuctionSummary::from).toList();
        return PageResponse.of(result, items);
    }

    /** T2-14: detalle protegido por estado. Una subasta privada responde 404 a quien no sea su dueño. */
    @GetMapping("/{id}")
    public PublicAuctionDetail detail(@PathVariable Long id, @AuthenticationPrincipal SessionUser user) {
        Auction auction = catalogService.getVisible(id, user == null ? null : user.id());
        return PublicAuctionDetail.from(auction);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AuctionResponse> create(@Valid @ModelAttribute CreateAuctionForm form,
                                                  @AuthenticationPrincipal SessionUser user) throws IOException {
        MultipartFile file = form.getImage();
        byte[] image = (file == null || file.isEmpty()) ? null : file.getBytes();
        Auction auction = auctionService.create(user.id(), new CreateAuctionCommand(
                form.getTitle(), form.getDescription(), form.getCategoryId(),
                form.getStartingPrice(), form.getDurationHours(), image));
        return ResponseEntity.status(HttpStatus.CREATED).body(AuctionResponse.from(auction));
    }
}

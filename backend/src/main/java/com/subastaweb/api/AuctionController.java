package com.subastaweb.api;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import com.subastaweb.application.UpdateAuctionCommand;
import com.subastaweb.api.dto.UpdateAuctionRequest;

import com.subastaweb.api.dto.AuctionResponse;
import com.subastaweb.api.dto.CreateAuctionForm;
import com.subastaweb.application.AuctionService;
import com.subastaweb.application.CreateAuctionCommand;
import com.subastaweb.domain.Auction;
import com.subastaweb.security.SessionUser;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/auctions")
public class AuctionController {

    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
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

    @GetMapping("/mine")
    public ResponseEntity<List<AuctionResponse>> mine(@AuthenticationPrincipal SessionUser user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<AuctionResponse> mine = auctionService.findMine(user.id()).stream()
                .map(AuctionResponse::from)
                .toList();
        return ResponseEntity.ok(mine);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AuctionResponse> patch(@PathVariable Long id,
                                                 @RequestBody UpdateAuctionRequest body,
                                                 @AuthenticationPrincipal SessionUser user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Auction updated = auctionService.patchRejected(user.id(), id, new UpdateAuctionCommand(
                body.title(),
                body.description(),
                body.categoryId(),
                body.startingPrice(),
                body.durationHours()));
        return ResponseEntity.ok(AuctionResponse.from(updated));
    }

    @PostMapping("/{id}/resubmit")
    public ResponseEntity<AuctionResponse> resubmit(@PathVariable Long id,
                                                    @RequestParam(required = false) Integer contentVersion,
                                                    @AuthenticationPrincipal SessionUser user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Auction resubmitted = auctionService.resubmit(user.id(), id, contentVersion);
        return ResponseEntity.ok(AuctionResponse.from(resubmitted));
    }
}

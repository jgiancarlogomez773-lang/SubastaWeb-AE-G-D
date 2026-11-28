package com.subastaweb.persistence;

import com.subastaweb.domain.Auction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findBySeller_IdOrderByCreatedAtDesc(Long sellerId);
}
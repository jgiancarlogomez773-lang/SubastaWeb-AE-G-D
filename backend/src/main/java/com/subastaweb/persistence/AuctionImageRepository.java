package com.subastaweb.persistence;

import com.subastaweb.domain.AuctionImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionImageRepository extends JpaRepository<AuctionImage, Long> {
}

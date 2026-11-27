package com.subastaweb.persistence;

import com.subastaweb.domain.Auction;
import com.subastaweb.domain.AuctionStatus;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    /** T2-13: página del catálogo (más recientes primero, mismo orden del índice ix_auctions_public). */
    @Query(value = "select a from Auction a join fetch a.category "
            + "where a.status in :statuses order by a.createdAt desc, a.id desc",
            countQuery = "select count(a) from Auction a where a.status in :statuses")
    Page<Auction> findPage(@Param("statuses") Collection<AuctionStatus> statuses, Pageable pageable);

    /** T2-14: detalle con su categoría ya cargada. */
    @Query("select a from Auction a join fetch a.category where a.id = :id")
    Optional<Auction> findWithCategoryById(@Param("id") Long id);
}

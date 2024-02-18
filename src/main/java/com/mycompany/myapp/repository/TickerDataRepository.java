package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TickerData;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TickerData entity.
 */
@Repository
public interface TickerDataRepository extends JpaRepository<TickerData, Long> {
    default Optional<TickerData> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<TickerData> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<TickerData> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select tickerData from TickerData tickerData left join fetch tickerData.ticker",
        countQuery = "select count(tickerData) from TickerData tickerData"
    )
    Page<TickerData> findAllWithToOneRelationships(Pageable pageable);

    @Query("select tickerData from TickerData tickerData left join fetch tickerData.ticker")
    List<TickerData> findAllWithToOneRelationships();

    @Query("select tickerData from TickerData tickerData left join fetch tickerData.ticker where tickerData.id =:id")
    Optional<TickerData> findOneWithToOneRelationships(@Param("id") Long id);
}

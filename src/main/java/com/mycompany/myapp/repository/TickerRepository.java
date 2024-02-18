package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Ticker;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ticker entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TickerRepository extends JpaRepository<Ticker, Long> {
    @Query("select ticker from Ticker ticker where ticker.user.login = ?#{principal.username}")
    List<Ticker> findByUserIsCurrentUser();

    Optional<Ticker> findBySymbol(String symbol);
}

package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.TickerRepository;
import com.mycompany.myapp.service.TickerService;
import com.mycompany.myapp.service.dto.TickerDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Ticker}.
 */
@RestController
@RequestMapping("/api/tickers")
public class TickerResource {

    private final Logger log = LoggerFactory.getLogger(TickerResource.class);

    private static final String ENTITY_NAME = "ticker";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TickerService tickerService;

    private final TickerRepository tickerRepository;

    public TickerResource(TickerService tickerService, TickerRepository tickerRepository) {
        this.tickerService = tickerService;
        this.tickerRepository = tickerRepository;
    }

    /**
     * {@code POST  /tickers} : Create a new ticker.
     *
     * @param tickerDTO the tickerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tickerDTO, or with status {@code 400 (Bad Request)} if the ticker has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TickerDTO> createTicker(@Valid @RequestBody TickerDTO tickerDTO) throws URISyntaxException {
        log.debug("REST request to save Ticker : {}", tickerDTO);
        if (tickerDTO.getId() != null) {
            throw new BadRequestAlertException("A new ticker cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TickerDTO result = tickerService.save(tickerDTO);
        return ResponseEntity
            .created(new URI("/api/tickers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tickers/:id} : Updates an existing ticker.
     *
     * @param id the id of the tickerDTO to save.
     * @param tickerDTO the tickerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tickerDTO,
     * or with status {@code 400 (Bad Request)} if the tickerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tickerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TickerDTO> updateTicker(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TickerDTO tickerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Ticker : {}, {}", id, tickerDTO);
        if (tickerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tickerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tickerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TickerDTO result = tickerService.update(tickerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tickerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tickers/:id} : Partial updates given fields of an existing ticker, field will ignore if it is null
     *
     * @param id the id of the tickerDTO to save.
     * @param tickerDTO the tickerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tickerDTO,
     * or with status {@code 400 (Bad Request)} if the tickerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tickerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tickerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TickerDTO> partialUpdateTicker(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TickerDTO tickerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ticker partially : {}, {}", id, tickerDTO);
        if (tickerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tickerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tickerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TickerDTO> result = tickerService.partialUpdate(tickerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tickerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tickers} : get all the tickers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tickers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TickerDTO>> getAllTickers(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        System.out.println("COTNROLER************COTNROLER**************COTNROLER");
        log.debug("REST request to get a page of Tickers");
        Page<TickerDTO> page = tickerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tickers/:id} : get the "id" ticker.
     *
     * @param id the id of the tickerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tickerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{symbol}")
    public ResponseEntity<TickerDTO> getTicker(@PathVariable String symbol) {
        log.debug("REST request to get Ticker : {}", symbol);
        Optional<TickerDTO> tickerDTO = tickerService.findOne(symbol);
        return ResponseUtil.wrapOrNotFound(tickerDTO);
    }

    /**
     * {@code DELETE  /tickers/:id} : delete the "id" ticker.
     *
     * @param id the id of the tickerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicker(@PathVariable Long id) {
        log.debug("REST request to delete Ticker : {}", id);
        tickerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

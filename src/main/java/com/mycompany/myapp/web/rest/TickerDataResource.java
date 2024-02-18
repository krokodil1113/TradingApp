package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.TickerDataRepository;
import com.mycompany.myapp.service.TickerDataService;
import com.mycompany.myapp.service.dto.TickerDataDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.TickerData}.
 */
@RestController
@RequestMapping("/api/ticker-data")
public class TickerDataResource {

    private final Logger log = LoggerFactory.getLogger(TickerDataResource.class);

    private static final String ENTITY_NAME = "tickerData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TickerDataService tickerDataService;

    private final TickerDataRepository tickerDataRepository;

    public TickerDataResource(TickerDataService tickerDataService, TickerDataRepository tickerDataRepository) {
        this.tickerDataService = tickerDataService;
        this.tickerDataRepository = tickerDataRepository;
    }

    /**
     * {@code POST  /ticker-data} : Create a new tickerData.
     *
     * @param tickerDataDTO the tickerDataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tickerDataDTO, or with status {@code 400 (Bad Request)} if the tickerData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TickerDataDTO> createTickerData(@Valid @RequestBody TickerDataDTO tickerDataDTO) throws URISyntaxException {
        log.debug("REST request to save TickerData : {}", tickerDataDTO);
        if (tickerDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new tickerData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TickerDataDTO result = tickerDataService.save(tickerDataDTO);
        return ResponseEntity
            .created(new URI("/api/ticker-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ticker-data/:id} : Updates an existing tickerData.
     *
     * @param id the id of the tickerDataDTO to save.
     * @param tickerDataDTO the tickerDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tickerDataDTO,
     * or with status {@code 400 (Bad Request)} if the tickerDataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tickerDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TickerDataDTO> updateTickerData(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TickerDataDTO tickerDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TickerData : {}, {}", id, tickerDataDTO);
        if (tickerDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tickerDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tickerDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TickerDataDTO result = tickerDataService.update(tickerDataDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tickerDataDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ticker-data/:id} : Partial updates given fields of an existing tickerData, field will ignore if it is null
     *
     * @param id the id of the tickerDataDTO to save.
     * @param tickerDataDTO the tickerDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tickerDataDTO,
     * or with status {@code 400 (Bad Request)} if the tickerDataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tickerDataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tickerDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TickerDataDTO> partialUpdateTickerData(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TickerDataDTO tickerDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TickerData partially : {}, {}", id, tickerDataDTO);
        if (tickerDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tickerDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tickerDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TickerDataDTO> result = tickerDataService.partialUpdate(tickerDataDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tickerDataDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ticker-data} : get all the tickerData.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tickerData in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TickerDataDTO>> getAllTickerData(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of TickerData");
        Page<TickerDataDTO> page;
        if (eagerload) {
            page = tickerDataService.findAllWithEagerRelationships(pageable);
        } else {
            page = tickerDataService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ticker-data/:id} : get the "id" tickerData.
     *
     * @param id the id of the tickerDataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tickerDataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TickerDataDTO> getTickerData(@PathVariable Long id) {
        log.debug("REST request to get TickerData : {}", id);
        Optional<TickerDataDTO> tickerDataDTO = tickerDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tickerDataDTO);
    }

    /**
     * {@code DELETE  /ticker-data/:id} : delete the "id" tickerData.
     *
     * @param id the id of the tickerDataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTickerData(@PathVariable Long id) {
        log.debug("REST request to delete TickerData : {}", id);
        tickerDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TickerData;
import com.mycompany.myapp.repository.TickerDataRepository;
import com.mycompany.myapp.service.dto.TickerDataDTO;
import com.mycompany.myapp.service.mapper.TickerDataMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.TickerData}.
 */
@Service
@Transactional
public class TickerDataService {

    private final Logger log = LoggerFactory.getLogger(TickerDataService.class);

    private final TickerDataRepository tickerDataRepository;

    private final TickerDataMapper tickerDataMapper;

    public TickerDataService(TickerDataRepository tickerDataRepository, TickerDataMapper tickerDataMapper) {
        this.tickerDataRepository = tickerDataRepository;
        this.tickerDataMapper = tickerDataMapper;
    }

    /**
     * Save a tickerData.
     *
     * @param tickerDataDTO the entity to save.
     * @return the persisted entity.
     */
    public TickerDataDTO save(TickerDataDTO tickerDataDTO) {
        log.debug("Request to save TickerData : {}", tickerDataDTO);
        TickerData tickerData = tickerDataMapper.toEntity(tickerDataDTO);
        tickerData = tickerDataRepository.save(tickerData);
        return tickerDataMapper.toDto(tickerData);
    }

    /**
     * Update a tickerData.
     *
     * @param tickerDataDTO the entity to save.
     * @return the persisted entity.
     */
    public TickerDataDTO update(TickerDataDTO tickerDataDTO) {
        log.debug("Request to update TickerData : {}", tickerDataDTO);
        TickerData tickerData = tickerDataMapper.toEntity(tickerDataDTO);
        tickerData = tickerDataRepository.save(tickerData);
        return tickerDataMapper.toDto(tickerData);
    }

    /**
     * Partially update a tickerData.
     *
     * @param tickerDataDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TickerDataDTO> partialUpdate(TickerDataDTO tickerDataDTO) {
        log.debug("Request to partially update TickerData : {}", tickerDataDTO);

        return tickerDataRepository
            .findById(tickerDataDTO.getId())
            .map(existingTickerData -> {
                tickerDataMapper.partialUpdate(existingTickerData, tickerDataDTO);

                return existingTickerData;
            })
            .map(tickerDataRepository::save)
            .map(tickerDataMapper::toDto);
    }

    /**
     * Get all the tickerData.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TickerDataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TickerData");
        return tickerDataRepository.findAll(pageable).map(tickerDataMapper::toDto);
    }

    /**
     * Get all the tickerData with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TickerDataDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tickerDataRepository.findAllWithEagerRelationships(pageable).map(tickerDataMapper::toDto);
    }

    /**
     * Get one tickerData by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TickerDataDTO> findOne(Long id) {
        log.debug("Request to get TickerData : {}", id);
        return tickerDataRepository.findOneWithEagerRelationships(id).map(tickerDataMapper::toDto);
    }

    /**
     * Delete the tickerData by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TickerData : {}", id);
        tickerDataRepository.deleteById(id);
    }
}

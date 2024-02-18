package com.mycompany.myapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.domain.Ticker;
import com.mycompany.myapp.repository.TickerRepository;
import com.mycompany.myapp.service.dto.TickerDTO;
import com.mycompany.myapp.service.mapper.TickerMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Ticker}.
 */
@Service
@Transactional
public class TickerService {

    private final Logger log = LoggerFactory.getLogger(TickerService.class);

    private final TickerRepository tickerRepository;

    private final TickerMapper tickerMapper;

    public TickerService(TickerRepository tickerRepository, TickerMapper tickerMapper) {
        this.tickerRepository = tickerRepository;
        this.tickerMapper = tickerMapper;
    }

    /**
     * Save a ticker.
     *
     * @param tickerDTO the entity to save.
     * @return the persisted entity.
     */
    public TickerDTO save(TickerDTO tickerDTO) {
        log.debug("Request to save Ticker : {}", tickerDTO);
        Ticker ticker = tickerMapper.toEntity(tickerDTO);
        ticker = tickerRepository.save(ticker);
        return tickerMapper.toDto(ticker);
    }

    /**
     * Update a ticker.
     *
     * @param tickerDTO the entity to save.
     * @return the persisted entity.
     */
    public TickerDTO update(TickerDTO tickerDTO) {
        log.debug("Request to update Ticker : {}", tickerDTO);
        Ticker ticker = tickerMapper.toEntity(tickerDTO);
        ticker = tickerRepository.save(ticker);
        return tickerMapper.toDto(ticker);
    }

    /**
     * Partially update a ticker.
     *
     * @param tickerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TickerDTO> partialUpdate(TickerDTO tickerDTO) {
        log.debug("Request to partially update Ticker : {}", tickerDTO);

        return tickerRepository
            .findById(tickerDTO.getId())
            .map(existingTicker -> {
                tickerMapper.partialUpdate(existingTicker, tickerDTO);

                return existingTicker;
            })
            .map(tickerRepository::save)
            .map(tickerMapper::toDto);
    }

    /**
     * Get all the tickers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    /*  @Transactional(readOnly = true)
    public Page<TickerDTO> findAll(Pageable pageable) {
        System.out.println("SERVICE************SERVICE**************SERVICE");
        log.debug("Request to get all Tickers");
        return tickerRepository.findAll(pageable).map(tickerMapper::toDto);
    } */

    @Transactional(readOnly = true)
    public Page<TickerDTO> findAll(Pageable page) {
        log.debug("find by criteria : {}, page: {}", page);
        //      final Specification<Ticker> specification = createSpecification(criteria);

        // Set up WebClient

        final String pythonApiUrl = "http://127.0.0.1:5000/items?page=0&size=10";
        // WebClient webClient = WebClient.create();

        WebClient webClient = WebClient
            .builder()
            .exchangeStrategies(
                ExchangeStrategies
                    .builder()
                    .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(6 * 1024 * 1024)) // 10 MB buffer size
                    .build()
            )
            .build();

        // Make a GET request to the Python API and retrieve the data asynchronously

        String pythonApiData = webClient.get().uri(pythonApiUrl).retrieve().bodyToMono(String.class).block(); // block() is used here for simplicity, handle the response asynchronously in a real application

        // Process the data obtained from the Python API (replace this with your own logic)

        List<TickerDTO> pythonApiTickers = processPythonApiData(pythonApiData);
        int pageSize = 10;
        List<TickerDTO> list = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            System.out.println("EVO GA" + i);
            System.out.println(pythonApiTickers.get(i));
            list.add(pythonApiTickers.get(i));
        }

        for (int i = 0; i < list.size(); i++) {
            System.out.println("Ticker------------------ " + list.get(i));
        }

        // Create a Page based on the data from the Python API
        return new PageImpl<>(list, PageRequest.of(0, pageSize), pythonApiTickers.size());
    }

    private List<TickerDTO> processPythonApiData(String pythonApiData) {
        try {
            // Parse the JSON data and get the "items" array
            JsonNode root = new ObjectMapper().readTree(pythonApiData);
            JsonNode itemsNode = root.get("items");

            // Deserialize the "items" array into a list of TickerDTO
            return Arrays.asList(new ObjectMapper().treeToValue(itemsNode, TickerDTO[].class));
        } catch (JsonProcessingException e) {
            // Handle the exception or log the error
            e.printStackTrace();
            return Collections.emptyList(); // or return null, depending on your use case
        }
    }

    /**
     * Get one ticker by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TickerDTO> findOne(String symbol) {
        System.out.println("******************TickerService=======findOne");
        log.debug("Request to get Ticker : {}", symbol);
        return tickerRepository.findBySymbol(symbol).map(tickerMapper::toDto);
    }

    /**
     * Delete the ticker by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ticker : {}", id);
        tickerRepository.deleteById(id);
    }
}

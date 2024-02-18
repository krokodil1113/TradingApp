package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Ticker;
import com.mycompany.myapp.repository.TickerRepository;
import com.mycompany.myapp.service.dto.TickerDTO;
import com.mycompany.myapp.service.mapper.TickerMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TickerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TickerResourceIT {

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DISPLAY_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_SYMBOL = "BBBBBBBBBB";

    private static final String DEFAULT_FIGI = "AAAAAAAAAA";
    private static final String UPDATED_FIGI = "BBBBBBBBBB";

    private static final String DEFAULT_ISIN = "AAAAAAAAAA";
    private static final String UPDATED_ISIN = "BBBBBBBBBB";

    private static final String DEFAULT_MIC = "AAAAAAAAAA";
    private static final String UPDATED_MIC = "BBBBBBBBBB";

    private static final String DEFAULT_SHARE_CLASS_FIGI = "AAAAAAAAAA";
    private static final String UPDATED_SHARE_CLASS_FIGI = "BBBBBBBBBB";

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    private static final String DEFAULT_SYMBOL_2 = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL_2 = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tickers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TickerRepository tickerRepository;

    @Autowired
    private TickerMapper tickerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTickerMockMvc;

    private Ticker ticker;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticker createEntity(EntityManager em) {
        Ticker ticker = new Ticker()
            .currency(DEFAULT_CURRENCY)
            .description(DEFAULT_DESCRIPTION)
            .displaySymbol(DEFAULT_DISPLAY_SYMBOL)
            .figi(DEFAULT_FIGI)
            .isin(DEFAULT_ISIN)
            .mic(DEFAULT_MIC)
            .shareClassFIGI(DEFAULT_SHARE_CLASS_FIGI)
            .symbol(DEFAULT_SYMBOL)
            .symbol2(DEFAULT_SYMBOL_2)
            .type(DEFAULT_TYPE);
        return ticker;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticker createUpdatedEntity(EntityManager em) {
        Ticker ticker = new Ticker()
            .currency(UPDATED_CURRENCY)
            .description(UPDATED_DESCRIPTION)
            .displaySymbol(UPDATED_DISPLAY_SYMBOL)
            .figi(UPDATED_FIGI)
            .isin(UPDATED_ISIN)
            .mic(UPDATED_MIC)
            .shareClassFIGI(UPDATED_SHARE_CLASS_FIGI)
            .symbol(UPDATED_SYMBOL)
            .symbol2(UPDATED_SYMBOL_2)
            .type(UPDATED_TYPE);
        return ticker;
    }

    @BeforeEach
    public void initTest() {
        ticker = createEntity(em);
    }

    @Test
    @Transactional
    void createTicker() throws Exception {
        int databaseSizeBeforeCreate = tickerRepository.findAll().size();
        // Create the Ticker
        TickerDTO tickerDTO = tickerMapper.toDto(ticker);
        restTickerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tickerDTO)))
            .andExpect(status().isCreated());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeCreate + 1);
        Ticker testTicker = tickerList.get(tickerList.size() - 1);
        assertThat(testTicker.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testTicker.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTicker.getDisplaySymbol()).isEqualTo(DEFAULT_DISPLAY_SYMBOL);
        assertThat(testTicker.getFigi()).isEqualTo(DEFAULT_FIGI);
        assertThat(testTicker.getIsin()).isEqualTo(DEFAULT_ISIN);
        assertThat(testTicker.getMic()).isEqualTo(DEFAULT_MIC);
        assertThat(testTicker.getShareClassFIGI()).isEqualTo(DEFAULT_SHARE_CLASS_FIGI);
        assertThat(testTicker.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testTicker.getSymbol2()).isEqualTo(DEFAULT_SYMBOL_2);
        assertThat(testTicker.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createTickerWithExistingId() throws Exception {
        // Create the Ticker with an existing ID
        ticker.setId(1L);
        TickerDTO tickerDTO = tickerMapper.toDto(ticker);

        int databaseSizeBeforeCreate = tickerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTickerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tickerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSymbolIsRequired() throws Exception {
        int databaseSizeBeforeTest = tickerRepository.findAll().size();
        // set the field null
        ticker.setSymbol(null);

        // Create the Ticker, which fails.
        TickerDTO tickerDTO = tickerMapper.toDto(ticker);

        restTickerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tickerDTO)))
            .andExpect(status().isBadRequest());

        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTickers() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList
        restTickerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticker.getId().intValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].displaySymbol").value(hasItem(DEFAULT_DISPLAY_SYMBOL)))
            .andExpect(jsonPath("$.[*].figi").value(hasItem(DEFAULT_FIGI)))
            .andExpect(jsonPath("$.[*].isin").value(hasItem(DEFAULT_ISIN)))
            .andExpect(jsonPath("$.[*].mic").value(hasItem(DEFAULT_MIC)))
            .andExpect(jsonPath("$.[*].shareClassFIGI").value(hasItem(DEFAULT_SHARE_CLASS_FIGI)))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL)))
            .andExpect(jsonPath("$.[*].symbol2").value(hasItem(DEFAULT_SYMBOL_2)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    void getTicker() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get the ticker
        restTickerMockMvc
            .perform(get(ENTITY_API_URL_ID, ticker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ticker.getId().intValue()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.displaySymbol").value(DEFAULT_DISPLAY_SYMBOL))
            .andExpect(jsonPath("$.figi").value(DEFAULT_FIGI))
            .andExpect(jsonPath("$.isin").value(DEFAULT_ISIN))
            .andExpect(jsonPath("$.mic").value(DEFAULT_MIC))
            .andExpect(jsonPath("$.shareClassFIGI").value(DEFAULT_SHARE_CLASS_FIGI))
            .andExpect(jsonPath("$.symbol").value(DEFAULT_SYMBOL))
            .andExpect(jsonPath("$.symbol2").value(DEFAULT_SYMBOL_2))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingTicker() throws Exception {
        // Get the ticker
        restTickerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTicker() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();

        // Update the ticker
        Ticker updatedTicker = tickerRepository.findById(ticker.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTicker are not directly saved in db
        em.detach(updatedTicker);
        updatedTicker
            .currency(UPDATED_CURRENCY)
            .description(UPDATED_DESCRIPTION)
            .displaySymbol(UPDATED_DISPLAY_SYMBOL)
            .figi(UPDATED_FIGI)
            .isin(UPDATED_ISIN)
            .mic(UPDATED_MIC)
            .shareClassFIGI(UPDATED_SHARE_CLASS_FIGI)
            .symbol(UPDATED_SYMBOL)
            .symbol2(UPDATED_SYMBOL_2)
            .type(UPDATED_TYPE);
        TickerDTO tickerDTO = tickerMapper.toDto(updatedTicker);

        restTickerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tickerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tickerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
        Ticker testTicker = tickerList.get(tickerList.size() - 1);
        assertThat(testTicker.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testTicker.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTicker.getDisplaySymbol()).isEqualTo(UPDATED_DISPLAY_SYMBOL);
        assertThat(testTicker.getFigi()).isEqualTo(UPDATED_FIGI);
        assertThat(testTicker.getIsin()).isEqualTo(UPDATED_ISIN);
        assertThat(testTicker.getMic()).isEqualTo(UPDATED_MIC);
        assertThat(testTicker.getShareClassFIGI()).isEqualTo(UPDATED_SHARE_CLASS_FIGI);
        assertThat(testTicker.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testTicker.getSymbol2()).isEqualTo(UPDATED_SYMBOL_2);
        assertThat(testTicker.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingTicker() throws Exception {
        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();
        ticker.setId(longCount.incrementAndGet());

        // Create the Ticker
        TickerDTO tickerDTO = tickerMapper.toDto(ticker);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTickerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tickerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tickerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTicker() throws Exception {
        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();
        ticker.setId(longCount.incrementAndGet());

        // Create the Ticker
        TickerDTO tickerDTO = tickerMapper.toDto(ticker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tickerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTicker() throws Exception {
        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();
        ticker.setId(longCount.incrementAndGet());

        // Create the Ticker
        TickerDTO tickerDTO = tickerMapper.toDto(ticker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tickerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTickerWithPatch() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();

        // Update the ticker using partial update
        Ticker partialUpdatedTicker = new Ticker();
        partialUpdatedTicker.setId(ticker.getId());

        partialUpdatedTicker.currency(UPDATED_CURRENCY).displaySymbol(UPDATED_DISPLAY_SYMBOL).isin(UPDATED_ISIN).symbol2(UPDATED_SYMBOL_2);

        restTickerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTicker.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicker))
            )
            .andExpect(status().isOk());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
        Ticker testTicker = tickerList.get(tickerList.size() - 1);
        assertThat(testTicker.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testTicker.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTicker.getDisplaySymbol()).isEqualTo(UPDATED_DISPLAY_SYMBOL);
        assertThat(testTicker.getFigi()).isEqualTo(DEFAULT_FIGI);
        assertThat(testTicker.getIsin()).isEqualTo(UPDATED_ISIN);
        assertThat(testTicker.getMic()).isEqualTo(DEFAULT_MIC);
        assertThat(testTicker.getShareClassFIGI()).isEqualTo(DEFAULT_SHARE_CLASS_FIGI);
        assertThat(testTicker.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testTicker.getSymbol2()).isEqualTo(UPDATED_SYMBOL_2);
        assertThat(testTicker.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateTickerWithPatch() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();

        // Update the ticker using partial update
        Ticker partialUpdatedTicker = new Ticker();
        partialUpdatedTicker.setId(ticker.getId());

        partialUpdatedTicker
            .currency(UPDATED_CURRENCY)
            .description(UPDATED_DESCRIPTION)
            .displaySymbol(UPDATED_DISPLAY_SYMBOL)
            .figi(UPDATED_FIGI)
            .isin(UPDATED_ISIN)
            .mic(UPDATED_MIC)
            .shareClassFIGI(UPDATED_SHARE_CLASS_FIGI)
            .symbol(UPDATED_SYMBOL)
            .symbol2(UPDATED_SYMBOL_2)
            .type(UPDATED_TYPE);

        restTickerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTicker.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicker))
            )
            .andExpect(status().isOk());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
        Ticker testTicker = tickerList.get(tickerList.size() - 1);
        assertThat(testTicker.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testTicker.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTicker.getDisplaySymbol()).isEqualTo(UPDATED_DISPLAY_SYMBOL);
        assertThat(testTicker.getFigi()).isEqualTo(UPDATED_FIGI);
        assertThat(testTicker.getIsin()).isEqualTo(UPDATED_ISIN);
        assertThat(testTicker.getMic()).isEqualTo(UPDATED_MIC);
        assertThat(testTicker.getShareClassFIGI()).isEqualTo(UPDATED_SHARE_CLASS_FIGI);
        assertThat(testTicker.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testTicker.getSymbol2()).isEqualTo(UPDATED_SYMBOL_2);
        assertThat(testTicker.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingTicker() throws Exception {
        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();
        ticker.setId(longCount.incrementAndGet());

        // Create the Ticker
        TickerDTO tickerDTO = tickerMapper.toDto(ticker);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTickerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tickerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tickerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTicker() throws Exception {
        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();
        ticker.setId(longCount.incrementAndGet());

        // Create the Ticker
        TickerDTO tickerDTO = tickerMapper.toDto(ticker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tickerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTicker() throws Exception {
        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();
        ticker.setId(longCount.incrementAndGet());

        // Create the Ticker
        TickerDTO tickerDTO = tickerMapper.toDto(ticker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tickerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTicker() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        int databaseSizeBeforeDelete = tickerRepository.findAll().size();

        // Delete the ticker
        restTickerMockMvc
            .perform(delete(ENTITY_API_URL_ID, ticker.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

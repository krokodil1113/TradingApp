package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Ticker;
import com.mycompany.myapp.domain.TickerData;
import com.mycompany.myapp.repository.TickerDataRepository;
import com.mycompany.myapp.service.TickerDataService;
import com.mycompany.myapp.service.dto.TickerDataDTO;
import com.mycompany.myapp.service.mapper.TickerDataMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TickerDataResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TickerDataResourceIT {

    private static final String DEFAULT_DATE = "AAAAAAAAAA";
    private static final String UPDATED_DATE = "BBBBBBBBBB";

    private static final Double DEFAULT_ADJ_CLOSE = 1D;
    private static final Double UPDATED_ADJ_CLOSE = 2D;

    private static final Double DEFAULT_CLOSE = 1D;
    private static final Double UPDATED_CLOSE = 2D;

    private static final Double DEFAULT_HIGH = 1D;
    private static final Double UPDATED_HIGH = 2D;

    private static final Double DEFAULT_LOW = 1D;
    private static final Double UPDATED_LOW = 2D;

    private static final Double DEFAULT_OPEN = 1D;
    private static final Double UPDATED_OPEN = 2D;

    private static final Long DEFAULT_VOLUME = 1L;
    private static final Long UPDATED_VOLUME = 2L;

    private static final String ENTITY_API_URL = "/api/ticker-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TickerDataRepository tickerDataRepository;

    @Mock
    private TickerDataRepository tickerDataRepositoryMock;

    @Autowired
    private TickerDataMapper tickerDataMapper;

    @Mock
    private TickerDataService tickerDataServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTickerDataMockMvc;

    private TickerData tickerData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TickerData createEntity(EntityManager em) {
        TickerData tickerData = new TickerData()
            .date(DEFAULT_DATE)
            .adjClose(DEFAULT_ADJ_CLOSE)
            .close(DEFAULT_CLOSE)
            .high(DEFAULT_HIGH)
            .low(DEFAULT_LOW)
            .open(DEFAULT_OPEN)
            .volume(DEFAULT_VOLUME);
        // Add required entity
        Ticker ticker;
        if (TestUtil.findAll(em, Ticker.class).isEmpty()) {
            ticker = TickerResourceIT.createEntity(em);
            em.persist(ticker);
            em.flush();
        } else {
            ticker = TestUtil.findAll(em, Ticker.class).get(0);
        }
        tickerData.setTicker(ticker);
        return tickerData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TickerData createUpdatedEntity(EntityManager em) {
        TickerData tickerData = new TickerData()
            .date(UPDATED_DATE)
            .adjClose(UPDATED_ADJ_CLOSE)
            .close(UPDATED_CLOSE)
            .high(UPDATED_HIGH)
            .low(UPDATED_LOW)
            .open(UPDATED_OPEN)
            .volume(UPDATED_VOLUME);
        // Add required entity
        Ticker ticker;
        if (TestUtil.findAll(em, Ticker.class).isEmpty()) {
            ticker = TickerResourceIT.createUpdatedEntity(em);
            em.persist(ticker);
            em.flush();
        } else {
            ticker = TestUtil.findAll(em, Ticker.class).get(0);
        }
        tickerData.setTicker(ticker);
        return tickerData;
    }

    @BeforeEach
    public void initTest() {
        tickerData = createEntity(em);
    }

    @Test
    @Transactional
    void createTickerData() throws Exception {
        int databaseSizeBeforeCreate = tickerDataRepository.findAll().size();
        // Create the TickerData
        TickerDataDTO tickerDataDTO = tickerDataMapper.toDto(tickerData);
        restTickerDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tickerDataDTO)))
            .andExpect(status().isCreated());

        // Validate the TickerData in the database
        List<TickerData> tickerDataList = tickerDataRepository.findAll();
        assertThat(tickerDataList).hasSize(databaseSizeBeforeCreate + 1);
        TickerData testTickerData = tickerDataList.get(tickerDataList.size() - 1);
        assertThat(testTickerData.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testTickerData.getAdjClose()).isEqualTo(DEFAULT_ADJ_CLOSE);
        assertThat(testTickerData.getClose()).isEqualTo(DEFAULT_CLOSE);
        assertThat(testTickerData.getHigh()).isEqualTo(DEFAULT_HIGH);
        assertThat(testTickerData.getLow()).isEqualTo(DEFAULT_LOW);
        assertThat(testTickerData.getOpen()).isEqualTo(DEFAULT_OPEN);
        assertThat(testTickerData.getVolume()).isEqualTo(DEFAULT_VOLUME);
    }

    @Test
    @Transactional
    void createTickerDataWithExistingId() throws Exception {
        // Create the TickerData with an existing ID
        tickerData.setId(1L);
        TickerDataDTO tickerDataDTO = tickerDataMapper.toDto(tickerData);

        int databaseSizeBeforeCreate = tickerDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTickerDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tickerDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TickerData in the database
        List<TickerData> tickerDataList = tickerDataRepository.findAll();
        assertThat(tickerDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = tickerDataRepository.findAll().size();
        // set the field null
        tickerData.setDate(null);

        // Create the TickerData, which fails.
        TickerDataDTO tickerDataDTO = tickerDataMapper.toDto(tickerData);

        restTickerDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tickerDataDTO)))
            .andExpect(status().isBadRequest());

        List<TickerData> tickerDataList = tickerDataRepository.findAll();
        assertThat(tickerDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTickerData() throws Exception {
        // Initialize the database
        tickerDataRepository.saveAndFlush(tickerData);

        // Get all the tickerDataList
        restTickerDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tickerData.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE)))
            .andExpect(jsonPath("$.[*].adjClose").value(hasItem(DEFAULT_ADJ_CLOSE.doubleValue())))
            .andExpect(jsonPath("$.[*].close").value(hasItem(DEFAULT_CLOSE.doubleValue())))
            .andExpect(jsonPath("$.[*].high").value(hasItem(DEFAULT_HIGH.doubleValue())))
            .andExpect(jsonPath("$.[*].low").value(hasItem(DEFAULT_LOW.doubleValue())))
            .andExpect(jsonPath("$.[*].open").value(hasItem(DEFAULT_OPEN.doubleValue())))
            .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTickerDataWithEagerRelationshipsIsEnabled() throws Exception {
        when(tickerDataServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTickerDataMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tickerDataServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTickerDataWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tickerDataServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTickerDataMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(tickerDataRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTickerData() throws Exception {
        // Initialize the database
        tickerDataRepository.saveAndFlush(tickerData);

        // Get the tickerData
        restTickerDataMockMvc
            .perform(get(ENTITY_API_URL_ID, tickerData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tickerData.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE))
            .andExpect(jsonPath("$.adjClose").value(DEFAULT_ADJ_CLOSE.doubleValue()))
            .andExpect(jsonPath("$.close").value(DEFAULT_CLOSE.doubleValue()))
            .andExpect(jsonPath("$.high").value(DEFAULT_HIGH.doubleValue()))
            .andExpect(jsonPath("$.low").value(DEFAULT_LOW.doubleValue()))
            .andExpect(jsonPath("$.open").value(DEFAULT_OPEN.doubleValue()))
            .andExpect(jsonPath("$.volume").value(DEFAULT_VOLUME.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingTickerData() throws Exception {
        // Get the tickerData
        restTickerDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTickerData() throws Exception {
        // Initialize the database
        tickerDataRepository.saveAndFlush(tickerData);

        int databaseSizeBeforeUpdate = tickerDataRepository.findAll().size();

        // Update the tickerData
        TickerData updatedTickerData = tickerDataRepository.findById(tickerData.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTickerData are not directly saved in db
        em.detach(updatedTickerData);
        updatedTickerData
            .date(UPDATED_DATE)
            .adjClose(UPDATED_ADJ_CLOSE)
            .close(UPDATED_CLOSE)
            .high(UPDATED_HIGH)
            .low(UPDATED_LOW)
            .open(UPDATED_OPEN)
            .volume(UPDATED_VOLUME);
        TickerDataDTO tickerDataDTO = tickerDataMapper.toDto(updatedTickerData);

        restTickerDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tickerDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tickerDataDTO))
            )
            .andExpect(status().isOk());

        // Validate the TickerData in the database
        List<TickerData> tickerDataList = tickerDataRepository.findAll();
        assertThat(tickerDataList).hasSize(databaseSizeBeforeUpdate);
        TickerData testTickerData = tickerDataList.get(tickerDataList.size() - 1);
        assertThat(testTickerData.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTickerData.getAdjClose()).isEqualTo(UPDATED_ADJ_CLOSE);
        assertThat(testTickerData.getClose()).isEqualTo(UPDATED_CLOSE);
        assertThat(testTickerData.getHigh()).isEqualTo(UPDATED_HIGH);
        assertThat(testTickerData.getLow()).isEqualTo(UPDATED_LOW);
        assertThat(testTickerData.getOpen()).isEqualTo(UPDATED_OPEN);
        assertThat(testTickerData.getVolume()).isEqualTo(UPDATED_VOLUME);
    }

    @Test
    @Transactional
    void putNonExistingTickerData() throws Exception {
        int databaseSizeBeforeUpdate = tickerDataRepository.findAll().size();
        tickerData.setId(longCount.incrementAndGet());

        // Create the TickerData
        TickerDataDTO tickerDataDTO = tickerDataMapper.toDto(tickerData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTickerDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tickerDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tickerDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TickerData in the database
        List<TickerData> tickerDataList = tickerDataRepository.findAll();
        assertThat(tickerDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTickerData() throws Exception {
        int databaseSizeBeforeUpdate = tickerDataRepository.findAll().size();
        tickerData.setId(longCount.incrementAndGet());

        // Create the TickerData
        TickerDataDTO tickerDataDTO = tickerDataMapper.toDto(tickerData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickerDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tickerDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TickerData in the database
        List<TickerData> tickerDataList = tickerDataRepository.findAll();
        assertThat(tickerDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTickerData() throws Exception {
        int databaseSizeBeforeUpdate = tickerDataRepository.findAll().size();
        tickerData.setId(longCount.incrementAndGet());

        // Create the TickerData
        TickerDataDTO tickerDataDTO = tickerDataMapper.toDto(tickerData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickerDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tickerDataDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TickerData in the database
        List<TickerData> tickerDataList = tickerDataRepository.findAll();
        assertThat(tickerDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTickerDataWithPatch() throws Exception {
        // Initialize the database
        tickerDataRepository.saveAndFlush(tickerData);

        int databaseSizeBeforeUpdate = tickerDataRepository.findAll().size();

        // Update the tickerData using partial update
        TickerData partialUpdatedTickerData = new TickerData();
        partialUpdatedTickerData.setId(tickerData.getId());

        partialUpdatedTickerData.close(UPDATED_CLOSE).high(UPDATED_HIGH).low(UPDATED_LOW).open(UPDATED_OPEN);

        restTickerDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTickerData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTickerData))
            )
            .andExpect(status().isOk());

        // Validate the TickerData in the database
        List<TickerData> tickerDataList = tickerDataRepository.findAll();
        assertThat(tickerDataList).hasSize(databaseSizeBeforeUpdate);
        TickerData testTickerData = tickerDataList.get(tickerDataList.size() - 1);
        assertThat(testTickerData.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testTickerData.getAdjClose()).isEqualTo(DEFAULT_ADJ_CLOSE);
        assertThat(testTickerData.getClose()).isEqualTo(UPDATED_CLOSE);
        assertThat(testTickerData.getHigh()).isEqualTo(UPDATED_HIGH);
        assertThat(testTickerData.getLow()).isEqualTo(UPDATED_LOW);
        assertThat(testTickerData.getOpen()).isEqualTo(UPDATED_OPEN);
        assertThat(testTickerData.getVolume()).isEqualTo(DEFAULT_VOLUME);
    }

    @Test
    @Transactional
    void fullUpdateTickerDataWithPatch() throws Exception {
        // Initialize the database
        tickerDataRepository.saveAndFlush(tickerData);

        int databaseSizeBeforeUpdate = tickerDataRepository.findAll().size();

        // Update the tickerData using partial update
        TickerData partialUpdatedTickerData = new TickerData();
        partialUpdatedTickerData.setId(tickerData.getId());

        partialUpdatedTickerData
            .date(UPDATED_DATE)
            .adjClose(UPDATED_ADJ_CLOSE)
            .close(UPDATED_CLOSE)
            .high(UPDATED_HIGH)
            .low(UPDATED_LOW)
            .open(UPDATED_OPEN)
            .volume(UPDATED_VOLUME);

        restTickerDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTickerData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTickerData))
            )
            .andExpect(status().isOk());

        // Validate the TickerData in the database
        List<TickerData> tickerDataList = tickerDataRepository.findAll();
        assertThat(tickerDataList).hasSize(databaseSizeBeforeUpdate);
        TickerData testTickerData = tickerDataList.get(tickerDataList.size() - 1);
        assertThat(testTickerData.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTickerData.getAdjClose()).isEqualTo(UPDATED_ADJ_CLOSE);
        assertThat(testTickerData.getClose()).isEqualTo(UPDATED_CLOSE);
        assertThat(testTickerData.getHigh()).isEqualTo(UPDATED_HIGH);
        assertThat(testTickerData.getLow()).isEqualTo(UPDATED_LOW);
        assertThat(testTickerData.getOpen()).isEqualTo(UPDATED_OPEN);
        assertThat(testTickerData.getVolume()).isEqualTo(UPDATED_VOLUME);
    }

    @Test
    @Transactional
    void patchNonExistingTickerData() throws Exception {
        int databaseSizeBeforeUpdate = tickerDataRepository.findAll().size();
        tickerData.setId(longCount.incrementAndGet());

        // Create the TickerData
        TickerDataDTO tickerDataDTO = tickerDataMapper.toDto(tickerData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTickerDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tickerDataDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tickerDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TickerData in the database
        List<TickerData> tickerDataList = tickerDataRepository.findAll();
        assertThat(tickerDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTickerData() throws Exception {
        int databaseSizeBeforeUpdate = tickerDataRepository.findAll().size();
        tickerData.setId(longCount.incrementAndGet());

        // Create the TickerData
        TickerDataDTO tickerDataDTO = tickerDataMapper.toDto(tickerData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickerDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tickerDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TickerData in the database
        List<TickerData> tickerDataList = tickerDataRepository.findAll();
        assertThat(tickerDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTickerData() throws Exception {
        int databaseSizeBeforeUpdate = tickerDataRepository.findAll().size();
        tickerData.setId(longCount.incrementAndGet());

        // Create the TickerData
        TickerDataDTO tickerDataDTO = tickerDataMapper.toDto(tickerData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickerDataMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tickerDataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TickerData in the database
        List<TickerData> tickerDataList = tickerDataRepository.findAll();
        assertThat(tickerDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTickerData() throws Exception {
        // Initialize the database
        tickerDataRepository.saveAndFlush(tickerData);

        int databaseSizeBeforeDelete = tickerDataRepository.findAll().size();

        // Delete the tickerData
        restTickerDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, tickerData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TickerData> tickerDataList = tickerDataRepository.findAll();
        assertThat(tickerDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.TickerDataTestSamples.*;
import static com.mycompany.myapp.domain.TickerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TickerDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TickerData.class);
        TickerData tickerData1 = getTickerDataSample1();
        TickerData tickerData2 = new TickerData();
        assertThat(tickerData1).isNotEqualTo(tickerData2);

        tickerData2.setId(tickerData1.getId());
        assertThat(tickerData1).isEqualTo(tickerData2);

        tickerData2 = getTickerDataSample2();
        assertThat(tickerData1).isNotEqualTo(tickerData2);
    }

    @Test
    void tickerTest() throws Exception {
        TickerData tickerData = getTickerDataRandomSampleGenerator();
        Ticker tickerBack = getTickerRandomSampleGenerator();

        tickerData.setTicker(tickerBack);
        assertThat(tickerData.getTicker()).isEqualTo(tickerBack);

        tickerData.ticker(null);
        assertThat(tickerData.getTicker()).isNull();
    }
}

package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.TickerDataTestSamples.*;
import static com.mycompany.myapp.domain.TickerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TickerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ticker.class);
        Ticker ticker1 = getTickerSample1();
        Ticker ticker2 = new Ticker();
        assertThat(ticker1).isNotEqualTo(ticker2);

        ticker2.setId(ticker1.getId());
        assertThat(ticker1).isEqualTo(ticker2);

        ticker2 = getTickerSample2();
        assertThat(ticker1).isNotEqualTo(ticker2);
    }

    @Test
    void tickerDataTest() throws Exception {
        Ticker ticker = getTickerRandomSampleGenerator();
        TickerData tickerDataBack = getTickerDataRandomSampleGenerator();

        ticker.addTickerData(tickerDataBack);
        assertThat(ticker.getTickerData()).containsOnly(tickerDataBack);
        assertThat(tickerDataBack.getTicker()).isEqualTo(ticker);

        ticker.removeTickerData(tickerDataBack);
        assertThat(ticker.getTickerData()).doesNotContain(tickerDataBack);
        assertThat(tickerDataBack.getTicker()).isNull();

        ticker.tickerData(new HashSet<>(Set.of(tickerDataBack)));
        assertThat(ticker.getTickerData()).containsOnly(tickerDataBack);
        assertThat(tickerDataBack.getTicker()).isEqualTo(ticker);

        ticker.setTickerData(new HashSet<>());
        assertThat(ticker.getTickerData()).doesNotContain(tickerDataBack);
        assertThat(tickerDataBack.getTicker()).isNull();
    }
}

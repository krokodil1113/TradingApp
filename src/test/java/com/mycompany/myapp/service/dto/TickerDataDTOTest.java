package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TickerDataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TickerDataDTO.class);
        TickerDataDTO tickerDataDTO1 = new TickerDataDTO();
        tickerDataDTO1.setId(1L);
        TickerDataDTO tickerDataDTO2 = new TickerDataDTO();
        assertThat(tickerDataDTO1).isNotEqualTo(tickerDataDTO2);
        tickerDataDTO2.setId(tickerDataDTO1.getId());
        assertThat(tickerDataDTO1).isEqualTo(tickerDataDTO2);
        tickerDataDTO2.setId(2L);
        assertThat(tickerDataDTO1).isNotEqualTo(tickerDataDTO2);
        tickerDataDTO1.setId(null);
        assertThat(tickerDataDTO1).isNotEqualTo(tickerDataDTO2);
    }
}

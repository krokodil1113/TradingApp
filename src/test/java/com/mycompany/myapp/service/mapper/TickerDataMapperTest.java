package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class TickerDataMapperTest {

    private TickerDataMapper tickerDataMapper;

    @BeforeEach
    public void setUp() {
        tickerDataMapper = new TickerDataMapperImpl();
    }
}

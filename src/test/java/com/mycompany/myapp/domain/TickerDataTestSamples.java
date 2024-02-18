package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TickerDataTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TickerData getTickerDataSample1() {
        return new TickerData().id(1L).date("date1").volume(1L);
    }

    public static TickerData getTickerDataSample2() {
        return new TickerData().id(2L).date("date2").volume(2L);
    }

    public static TickerData getTickerDataRandomSampleGenerator() {
        return new TickerData().id(longCount.incrementAndGet()).date(UUID.randomUUID().toString()).volume(longCount.incrementAndGet());
    }
}

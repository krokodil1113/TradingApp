package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.TickerData} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TickerDataDTO implements Serializable {

    private Long id;

    @NotNull
    private String date;

    private Double adjClose;

    private Double close;

    private Double high;

    private Double low;

    private Double open;

    private Long volume;

    private TickerDTO ticker;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getAdjClose() {
        return adjClose;
    }

    public void setAdjClose(Double adjClose) {
        this.adjClose = adjClose;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public TickerDTO getTicker() {
        return ticker;
    }

    public void setTicker(TickerDTO ticker) {
        this.ticker = ticker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TickerDataDTO)) {
            return false;
        }

        TickerDataDTO tickerDataDTO = (TickerDataDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tickerDataDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TickerDataDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", adjClose=" + getAdjClose() +
            ", close=" + getClose() +
            ", high=" + getHigh() +
            ", low=" + getLow() +
            ", open=" + getOpen() +
            ", volume=" + getVolume() +
            ", ticker=" + getTicker() +
            "}";
    }
}

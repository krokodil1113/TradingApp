package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TickerData.
 */
@Entity
@Table(name = "ticker_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TickerData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "adj_close")
    private Double adjClose;

    @Column(name = "close")
    private Double close;

    @Column(name = "high")
    private Double high;

    @Column(name = "low")
    private Double low;

    @Column(name = "open")
    private Double open;

    @Column(name = "volume")
    private Long volume;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tickerData", "user" }, allowSetters = true)
    private Ticker ticker;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TickerData id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return this.date;
    }

    public TickerData date(String date) {
        this.setDate(date);
        return this;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getAdjClose() {
        return this.adjClose;
    }

    public TickerData adjClose(Double adjClose) {
        this.setAdjClose(adjClose);
        return this;
    }

    public void setAdjClose(Double adjClose) {
        this.adjClose = adjClose;
    }

    public Double getClose() {
        return this.close;
    }

    public TickerData close(Double close) {
        this.setClose(close);
        return this;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getHigh() {
        return this.high;
    }

    public TickerData high(Double high) {
        this.setHigh(high);
        return this;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return this.low;
    }

    public TickerData low(Double low) {
        this.setLow(low);
        return this;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getOpen() {
        return this.open;
    }

    public TickerData open(Double open) {
        this.setOpen(open);
        return this;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Long getVolume() {
        return this.volume;
    }

    public TickerData volume(Long volume) {
        this.setVolume(volume);
        return this;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public Ticker getTicker() {
        return this.ticker;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    public TickerData ticker(Ticker ticker) {
        this.setTicker(ticker);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TickerData)) {
            return false;
        }
        return getId() != null && getId().equals(((TickerData) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TickerData{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", adjClose=" + getAdjClose() +
            ", close=" + getClose() +
            ", high=" + getHigh() +
            ", low=" + getLow() +
            ", open=" + getOpen() +
            ", volume=" + getVolume() +
            "}";
    }
}

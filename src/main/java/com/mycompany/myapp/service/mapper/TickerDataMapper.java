package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Ticker;
import com.mycompany.myapp.domain.TickerData;
import com.mycompany.myapp.service.dto.TickerDTO;
import com.mycompany.myapp.service.dto.TickerDataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TickerData} and its DTO {@link TickerDataDTO}.
 */
@Mapper(componentModel = "spring")
public interface TickerDataMapper extends EntityMapper<TickerDataDTO, TickerData> {
    @Mapping(target = "ticker", source = "ticker", qualifiedByName = "tickerSymbol")
    TickerDataDTO toDto(TickerData s);

    @Named("tickerSymbol")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "symbol", source = "symbol")
    TickerDTO toDtoTickerSymbol(Ticker ticker);
}

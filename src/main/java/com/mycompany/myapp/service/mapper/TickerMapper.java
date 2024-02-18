package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Ticker;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.TickerDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ticker} and its DTO {@link TickerDTO}.
 */
@Mapper(componentModel = "spring")
public interface TickerMapper extends EntityMapper<TickerDTO, Ticker> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    TickerDTO toDto(Ticker s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}

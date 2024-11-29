package co.quest.xms.valuation.api.mapper;

import co.quest.xms.valuation.api.dto.UserDto;
import co.quest.xms.valuation.domain.model.User;

public class UserMapper {
    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }
}

package co.quest.xms.valuation.api.mapper;

import co.quest.xms.valuation.api.dto.ContactRequest;
import co.quest.xms.valuation.domain.model.ContactMessage;

public class ContactMapper {
    public static ContactMessage toModel(ContactRequest contactRequest) {
        return ContactMessage.builder()
                .name(contactRequest.name())
                .email(contactRequest.email())
                .message(contactRequest.message())
                .build();
    }
}

package co.quest.xms.valuation.application.repository;

import co.quest.xms.valuation.domain.model.ContactMessage;

/**
 * Defines the contract for persisting and retrieving contact messages.
 */
public interface ContactRepository {
    /**
     * Saves a contact message.
     *
     * @param contactMessage The contact message to save.
     * @return The saved contact message.
     */
    ContactMessage save(ContactMessage contactMessage);
}

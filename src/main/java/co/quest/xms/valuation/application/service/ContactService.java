package co.quest.xms.valuation.application.service;

import co.quest.xms.valuation.domain.model.ContactMessage;

/**
 * Port interface for managing contact form submissions.
 * This interface defines the contract for saving contact messages,
 * ensuring a clean separation between the domain layer and its implementation.
 */
public interface ContactService {

    /**
     * Saves a contact message.
     *
     * @param contactMessage the contact message containing user-submitted data such as name, email, and message content.
     * @return the saved contact message, including any additional metadata such as generated IDs or timestamps.
     */
    ContactMessage saveContactMessage(ContactMessage contactMessage);
}


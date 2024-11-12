package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountService accountService;

    public Message createMessage(Message newMessage) {
        // Validate that the message
        if (newMessage.getMessageText() == null || newMessage.getMessageText().isBlank()
                || newMessage.getMessageText().length() > 255) {
            throw new IllegalArgumentException("Invalid message text.");
        }
        // Ensure the account exists
        if (accountService.findById(newMessage.getPostedBy()).isEmpty()) {
            throw new IllegalArgumentException("User does not exist");
        }
        return messageRepository.save(newMessage);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageByID(Integer messageID) {
        return messageRepository.findById(messageID);
    }

    public int deleteMessageByID(Integer messageID) {
        // check message exists
        if (messageRepository.existsById(messageID)) {
            messageRepository.deleteById(messageID);
            return 1; // # rows deleted
        }
        return 0; // Else return 0 because no rows were deleted
    }

    public int updateMessage(Integer messageID, String updatedMessageText) {
        Optional<Message> existingMessage = messageRepository.findById(messageID);
        // Check Message Exists
        if (existingMessage.isEmpty()) {
            throw new IllegalArgumentException("Message not found");
        }

        // Validate message text
        if (updatedMessageText == null || updatedMessageText.isBlank() || updatedMessageText.length() > 255) {
            throw new IllegalArgumentException("Invalid message text");
        }
        // Update message
        Message message = existingMessage.get();
        message.setMessageText(updatedMessageText);
        messageRepository.save(message);
        return 1;
    }

    public List<Message> getMessagesByAccountID(Integer accountID) {
        return messageRepository.findByPostedBy(accountID);
    }
}

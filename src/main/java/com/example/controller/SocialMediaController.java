package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.List;

import com.example.service.*;
import com.example.entity.Account;
import com.example.entity.Message;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use
 * the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations.
 * You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerAccount(@RequestBody Account newAccount) {
        // Validate credentials
        if (newAccount.getUsername() == null || newAccount.getUsername().isBlank()) {
            return ResponseEntity.status(400).build(); // Username blank
        }
        if (newAccount.getPassword() == null || newAccount.getPassword().length() <= 4) {
            return ResponseEntity.status(400).build(); // Password less than 4 characters
        }

        // Username Duplication check
        Optional<Account> exisitingAccount = accountService.findByUsername(newAccount.getUsername());
        if (exisitingAccount.isPresent()) {
            return ResponseEntity.status(409).build(); // Conflict due to existing user
        }

        accountService.registerAccount(newAccount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Account> loginAccount(@RequestBody Account loginInfo) {
        // Validate credentials
        if (loginInfo.getUsername() == null || loginInfo.getUsername().isBlank() || loginInfo.getPassword() == null
                || loginInfo.getPassword().isBlank()) {
            return ResponseEntity.status(401).build(); // Return Unauthorized
        }

        // Check Username exists & password correct
        Optional<Account> exisitingAccount = accountService.findByUsername(loginInfo.getUsername());
        if (exisitingAccount.isPresent() && exisitingAccount.get().getPassword().equals(loginInfo.getPassword())) {
            return ResponseEntity.ok(exisitingAccount.get()); // Returns JSON with account details
        }

        return ResponseEntity.status(401).build(); // Return Unauthorized
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message newMessage) {
        try {
            Message createdMessage = messageService.createMessage(newMessage);
            return ResponseEntity.ok(createdMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{messageID}")
    public ResponseEntity<Message> getMessageByID(@PathVariable Integer messageID) {
        Optional<Message> message = messageService.getMessageByID(messageID);
        return message.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok().build());
    }

    @DeleteMapping("/messages/{messageID}")
    public ResponseEntity<Integer> deleteMessageByID(@PathVariable Integer messageID) {
        int rowCount = messageService.deleteMessageByID(messageID);
        if (rowCount > 0) {
            return ResponseEntity.ok(rowCount);
        }
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/messages/{messageID}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer messageID, @RequestBody Message updatedMessage) {
        try {
            int rowCount = messageService.updateMessage(messageID, updatedMessage.getMessageText());
            return ResponseEntity.ok(rowCount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("/accounts/{accountID}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountID(@PathVariable Integer accountID) {
        List<Message> messages = messageService.getMessagesByAccountID(accountID);

        return ResponseEntity.ok(messages);
    }
}

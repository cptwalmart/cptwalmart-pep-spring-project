package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.stereotype.Service;

import com.example.repository.AccountRepository;
import com.example.entity.*;

import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Optional<Account> findById(Integer accountID) {
        return accountRepository.findById(accountID);
    }

    public void registerAccount(Account account) {
        accountRepository.save(account);
    }
}

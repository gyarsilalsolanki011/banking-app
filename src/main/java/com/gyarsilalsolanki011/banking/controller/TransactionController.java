package com.gyarsilalsolanki011.banking.controller;

import com.gyarsilalsolanki011.banking.dto.AccountDto;
import com.gyarsilalsolanki011.banking.dto.TransactionDto;
import com.gyarsilalsolanki011.banking.entity.User;
import com.gyarsilalsolanki011.banking.enums.AccountType;
import com.gyarsilalsolanki011.banking.repository.UserRepository;
import com.gyarsilalsolanki011.banking.service.AccountService;
import com.gyarsilalsolanki011.banking.service.TransactionService;
import com.gyarsilalsolanki011.banking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionDto> deposit(@RequestParam String accountType, @RequestParam double amount, @RequestParam String email, Principal principal) {
        try {
            AccountType type;
            try {
                type = AccountType.valueOf(accountType.toUpperCase());
            } catch (IllegalArgumentException e){
                throw new IllegalArgumentException("Invalid account type! Choose: SAVINGS, CURRENT, or FIXED_DEPOSIT.");
            }

            TransactionDto transaction = null;
            String name = principal.getName();

            User userForId = userRepository.findByEmail(email).orElseThrow();
            Long userId = userForId.getId();

            userService.getUserById(userId, name);
            List<AccountDto> allAccounts = accountService.getAllAccountsByUserId(userId);
            for (AccountDto accountDto : allAccounts){
                if (accountDto.getAccountType() == type){
                     transaction = transactionService.deposit(accountDto.getAccountId(), amount);
                }
            }
            assert transaction != null;
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionDto> withdraw(@RequestParam String accountType, @RequestParam double amount, @RequestParam String email, Principal principal) {
        try {
            AccountType type;
            try {
                type = AccountType.valueOf(accountType.toUpperCase());
            } catch (IllegalArgumentException e){
                throw new IllegalArgumentException("Invalid account type! Choose: SAVINGS, CURRENT, or FIXED_DEPOSIT.");
            }

            TransactionDto transaction = null;
            String name = principal.getName();

            User userForId = userRepository.findByEmail(email).orElseThrow();
            Long userId = userForId.getId();

            userService.getUserById(userId, name);
            List<AccountDto> allAccounts = accountService.getAllAccountsByUserId(userId);
            for (AccountDto accountDto : allAccounts){
                if (accountDto.getAccountType() == type){
                    transaction = transactionService.withdraw(accountDto.getAccountId(), amount);
                }
            }
            assert transaction != null;
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionDto> transfer(@RequestParam String toAccountNumber, @RequestParam String accountType,
                                      double amount, @RequestParam String email, Principal principal){
        try {
            AccountType type;
            try {
                type = AccountType.valueOf(accountType.toUpperCase());
            } catch (IllegalArgumentException e){
                throw new IllegalArgumentException("Invalid account type! Choose: SAVINGS, CURRENT, or FIXED_DEPOSIT.");
            }

            TransactionDto transaction = null;
            String name = principal.getName();

            User userForId = userRepository.findByEmail(email).orElseThrow();
            Long userId = userForId.getId();

            userService.getUserById(userId, name);
            List<AccountDto> allAccounts = accountService.getAllAccountsByUserId(userId);
            for (AccountDto accountDto : allAccounts){
                if (accountDto.getAccountType() == type){
                    transaction = transactionService.transfer(accountDto.getAccountId(), toAccountNumber, amount);
                }
            }
            assert transaction != null;
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/account/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactions(@RequestParam String accountType, @RequestParam String email, Principal principal) {
        try {
            AccountType type;
            try {
                type = AccountType.valueOf(accountType.toUpperCase());
            } catch (IllegalArgumentException e){
                throw new IllegalArgumentException("Invalid account type! Choose: SAVINGS, CURRENT, or FIXED_DEPOSIT.");
            }

            List<TransactionDto> allTransaction = null;
            String name = principal.getName();

            User userForId = userRepository.findByEmail(email).orElseThrow();
            Long userId = userForId.getId();

            userService.getUserById(userId, name);
            List<AccountDto> allAccounts = accountService.getAllAccountsByUserId(userId);
            for (AccountDto accountDto : allAccounts){
                if (accountDto.getAccountType() == type){
                    allTransaction = transactionService.getTransactionsByAccountId(accountDto.getAccountId());
                }
            }
            assert allTransaction != null;
            return ResponseEntity.ok(allTransaction);
        } catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}

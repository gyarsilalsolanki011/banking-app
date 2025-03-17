package com.gyarsilalsolanki011.banking.seviceimpl;

import com.gyarsilalsolanki011.banking.dto.AdminDto;
import com.gyarsilalsolanki011.banking.entity.Account;
import com.gyarsilalsolanki011.banking.entity.Admin;
import com.gyarsilalsolanki011.banking.entity.Transaction;
import com.gyarsilalsolanki011.banking.enums.AdminRole;
import com.gyarsilalsolanki011.banking.enums.TransactionStatus;
import com.gyarsilalsolanki011.banking.enums.TransactionType;
import com.gyarsilalsolanki011.banking.mapper.AdminMapper;
import com.gyarsilalsolanki011.banking.repository.AccountRepository;
import com.gyarsilalsolanki011.banking.repository.AdminRepository;
import com.gyarsilalsolanki011.banking.repository.TransactionRepository;
import com.gyarsilalsolanki011.banking.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public AdminDto createAdmin(String username, String email, String password, AdminRole role) {
        Admin admin = new Admin(
                username,
                email,
                passwordEncoder.encode(password),
                role
        );
        adminRepository.save(admin);
        return AdminMapper.mapToAdminDto(admin);
    }

    @Override
    public AdminDto getAdminById(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        return AdminMapper.mapToAdminDto(admin);
    }

    @Override
    public List<AdminDto> getAllAdmins() {
        return adminRepository.findAll()
                .stream()
                .map(AdminMapper::mapToAdminDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAdmin(Long adminId) {
        if (!adminRepository.existsById(adminId)) {
            throw new RuntimeException("Admin not found");
        }
        adminRepository.deleteById(adminId);
    }

    @Override
    public void approveWithdrawal(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transaction.getTransactionType() != TransactionType.WITHDRAWAL || transaction.getStatus() != TransactionStatus.PENDING) {
            throw new RuntimeException("Invalid transaction for approval");
        }

        Account account = transaction.getAccount();
        account.setBalance(account.getBalance() - transaction.getAmount());
        accountRepository.save(account);

        transaction.setStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(transaction);
    }
}

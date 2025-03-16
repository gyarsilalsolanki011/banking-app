package com.gyarsilalsolanki011.banking.controller;

import com.gyarsilalsolanki011.banking.dto.AccountDto;
import com.gyarsilalsolanki011.banking.dto.AdminDto;
import com.gyarsilalsolanki011.banking.dto.UserDto;
import com.gyarsilalsolanki011.banking.enums.AdminRole;
import com.gyarsilalsolanki011.banking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<?> createAdmin(@RequestParam String username,
                                         @RequestParam String email,
                                         @RequestParam String password,
                                         @RequestParam String role) {
        AdminRole adminRole;
        try {
            adminRole = AdminRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid admin role! Choose: ADMIN, MANAGER, or SUPER_ADMIN.");
        }

        AdminDto adminDto = new AdminDto(null, username, email, adminRole);
        AdminDto newAdmin = adminService.createAdmin(adminDto, password);
        return ResponseEntity.ok(newAdmin);
    }

    @GetMapping("/{adminId}")
    public ResponseEntity<?> getAdminById(@PathVariable Long adminId) {
        try {
            AdminDto admin = adminService.getAdminById(adminId);
            return ResponseEntity.ok(admin);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{adminId}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long adminId) {
        try {
            adminService.deleteAdmin(adminId);
            return ResponseEntity.ok("Admin deleted successfully!");
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all-admins")
    public ResponseEntity<List<AdminDto>> getAllAdmins() {
        List<AdminDto> allAdmins = adminService.getAllAdmins();
        return ResponseEntity.ok(allAdmins);
    }

    // Get all users
    @GetMapping("/all-users")
    public  ResponseEntity<List<UserDto>> getAllUsers(){
        List<UserDto> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    // Get all accounts
    @GetMapping("/all-accounts")
    public ResponseEntity<List<AccountDto>> getAllAccounts(){
        List<AccountDto> allAccounts = accountService.getAllAccounts();
        return ResponseEntity.ok(allAccounts);
    }
}

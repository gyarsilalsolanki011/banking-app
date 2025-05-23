package com.gyarsilalsolanki011.banking.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserDetailService extends UserDetailsService {
    UserDetails loadUserByUsername(String username);
}

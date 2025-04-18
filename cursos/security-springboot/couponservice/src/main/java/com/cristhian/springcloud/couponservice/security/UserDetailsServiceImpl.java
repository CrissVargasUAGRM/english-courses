package com.cristhian.springcloud.couponservice.security;

import com.cristhian.springcloud.couponservice.model.User;
import com.cristhian.springcloud.couponservice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepo.findByEmail(username);
            if(user == null){
                throw new UsernameNotFoundException("User not found for email "+username);
            }
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getRoles());
        } catch (Exception ex) {
            throw new UsernameNotFoundException(ex.getMessage());
        }

    }
}

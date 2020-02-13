package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.authentication.Account;


public interface AccountRepo extends JpaRepository<Account, Long>{

	public Account findByUsername(String username);
}

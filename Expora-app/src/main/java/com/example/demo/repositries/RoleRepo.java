package com.example.demo.repositries;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;

public interface RoleRepo extends JpaRepository<Role,Integer> {

	Optional<User> findByName(String roleName);

	

}

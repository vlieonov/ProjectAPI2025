package com.vlieonov.projectapi.service;

import com.vlieonov.projectapi.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}

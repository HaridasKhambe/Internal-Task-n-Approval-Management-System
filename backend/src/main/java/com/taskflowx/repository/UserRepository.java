package com.taskflowx.repository;

import com.taskflowx.enums.Role;
import com.taskflowx.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  // For manager to get employees for assignment
  java.util.List<User> findByRole(Role role);

}

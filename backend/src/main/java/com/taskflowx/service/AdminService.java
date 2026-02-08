package com.taskflowx.service;

import com.taskflowx.dto.request.CreateUserRequest;
import com.taskflowx.dto.response.UserResponse;
import com.taskflowx.exception.ResourceNotFoundException;
import com.taskflowx.model.User;
import com.taskflowx.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserResponse createUser(CreateUserRequest request) {
    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new IllegalArgumentException("Email already exists");
    }

    User user = new User();
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setFullName(request.getFullName());
    user.setRole(request.getRole());
    user.setIsActive(true);

    User savedUser = userRepository.save(user);
    return mapToUserResponse(savedUser);
  }

  public List<UserResponse> getAllUsers() {
    return userRepository.findAll()
      .stream()
      .map(this::mapToUserResponse)
      .collect(Collectors.toList());
  }

  public UserResponse getUserById(Long userId) {
    User user = userRepository.findById(userId)
      .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    return mapToUserResponse(user);
  }

  private UserResponse mapToUserResponse(User user) {
    return new UserResponse(
      user.getId(),
      user.getEmail(),
      user.getFullName(),
      user.getRole(),
      user.getIsActive(),
      user.getCreatedAt()
    );
  }
}

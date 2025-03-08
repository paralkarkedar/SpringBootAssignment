package com.kedarr.www.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID userId;

	@Column(nullable = false)
	@NotEmpty(message = "Full name cannot be empty")
	private String fullName;

	@Column(nullable = false, unique = true)
	@NotEmpty(message = "Mobile number cannot be empty")
	@Pattern(regexp = "^(\\+91|0)?[6-9]\\d{9}$", message = "Invalid mobile number")
	private String mobNum;

	@Column(nullable = false, unique = true)
	@NotEmpty(message = "PAN number cannot be empty")
	@Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}", message = "Invalid PAN number format")
	private String panNum;

	@Column(nullable = true)
	private UUID managerId;

	@Column(nullable = false)
	private boolean isActive = true;

	@Column(updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column
	private LocalDateTime updatedAt;
}

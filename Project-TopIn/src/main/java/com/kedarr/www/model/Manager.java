package com.kedarr.www.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "managers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manager {

	@Id
	//@GeneratedValue(strategy = GenerationType.UUID)
	private UUID managerId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private boolean isActive = true;
}

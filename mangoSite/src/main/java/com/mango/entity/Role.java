package com.mango.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="TBL_ROLES")
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private ERole name;
	
	public Role() {

	}
	
	public Role(ERole name) {
	    this.name = name;
	}
}

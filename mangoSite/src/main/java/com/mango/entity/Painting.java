package com.mango.entity;

import java.util.List;

import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.UniqueElements;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="PAINTINGS_TBL")
public class Painting {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@NotBlank(message = "Il campo non può essere vuoto o contenere solo spazi.")
	private String title;
		
	private String img;
	
	private String size;
	
	@Range(min=1993, max=2199)
	private Integer year;
	
	private String description;
	
	//@UniqueElements
	private String slug;
	
	@OneToMany(mappedBy="painting")
	private List<Detail> details;
	
}

package com.mango.entity;

import java.util.List;

import org.hibernate.validator.constraints.Range;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "Il campo non può essere vuoto o contenere solo spazi.")
	private String title;
		
	private String img;
	
	private String size;
	
	@Range(min=1993, max=2199)
	private Integer year;
	
	private String tecnique;
	
	private String description;
	
	private String slug;
	
	@Column(columnDefinition = "boolean default false")
	private Boolean favorite;
	
	@OneToMany(mappedBy="painting", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Detail> details;
	
	@OneToOne(mappedBy = "painting", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
//    @JoinColumn(name = "image_painting_data_id", nullable = true)
    private ImagePaintingData imagePaintingData;
}

package com.mango.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="IMAGE_PAINTING_TBL")
public class ImagePaintingData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String name;
	
	private String type;
	
	@Lob
	@Column(name = "image_data", columnDefinition = "LONGBLOB")
	private byte[] imageData;
	
	@OneToOne
    @MapsId
    @JoinColumn(name = "painting_id")
    private Painting painting;
}

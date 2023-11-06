package com.mango.dto;

import java.io.Serializable;
import java.util.List;

import com.mango.entity.Painting;
import com.mango.service.PaintingService;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaintingDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;	
	private String img;	
	private String size;
	private Integer year;	
	private String description;
	private String slug;
//	private List<DetailDto> details;
	
	public static Painting dtoToEntity(PaintingDto dto) {
		Painting entity = new Painting();
		
		entity.setImg(dto.getImg());
		entity.setSize(dto.getSize());
		entity.setYear(dto.getYear());
		entity.setDescription(dto.getDescription());
		
		return entity;
	}
	
	public static PaintingDto entityToDto(Painting entity) {
		PaintingDto dto = new PaintingDto();
		
		dto.setTitle(entity.getTitle());
		dto.setImg(entity.getImg());
		dto.setSize(entity.getSize());
		dto.setYear(entity.getYear());
		dto.setDescription(entity.getDescription());		
		dto.setSlug(entity.getSlug());
		
		return dto;
	}
}

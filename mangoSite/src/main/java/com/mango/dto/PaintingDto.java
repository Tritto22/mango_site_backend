package com.mango.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.mango.entity.Painting;

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
	private Integer totPages;
//	private List<DetailDto> details;
	
	public static Painting dtoToEntity(PaintingDto dto, Painting entity) {
		
		entity.setImg(dto.getImg());
		entity.setSize(dto.getSize());
		entity.setYear(dto.getYear());
		entity.setDescription(dto.getDescription());
		
		return entity;
	}
	
	public static PaintingDto entityToDto(Painting entity, PaintingDto dto) {
		
//		dto.setTitle(entity.getTitle());
//		dto.setImg(entity.getImg());
//		dto.setSize(entity.getSize());
//		dto.setYear(entity.getYear());
//		dto.setDescription(entity.getDescription());		
//		dto.setSlug(entity.getSlug());
		
		BeanUtils.copyProperties(entity, dto);
		
		return dto;
	}
}

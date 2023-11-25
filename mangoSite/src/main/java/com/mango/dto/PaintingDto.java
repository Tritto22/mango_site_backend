package com.mango.dto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.mango.entity.Detail;
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
	private String tecnique;
	private String description;
	private String slug;
	private Integer totPages;
	private Boolean favorite;
	private List<DetailDto> details;
	
	public static Painting dtoToEntity(PaintingDto dto, Painting entity) {
		
//		var newPainting = new Painting();
		
		entity.setImg(dto.getImg());
		entity.setSize(dto.getSize());
		entity.setYear(dto.getYear());
		entity.setDescription(dto.getDescription());
		entity.setTecnique(dto.getTecnique());
		entity.setFavorite(dto.getFavorite());	
		
		return entity;
	}
	
	public static PaintingDto entityToDto(Painting entity) {
		
		var createdPainting = new PaintingDto();
		
		BeanUtils.copyProperties(entity, createdPainting);
		if(entity.getDetails()!=null) {
			List<DetailDto> detailDtos = entity.getDetails().stream()
	                .map(detail -> {
	                    var detailDto = new DetailDto();
	                    detailEntityToDto(detail, detailDto);
	                    return detailDto;
	                })
	                .collect(Collectors.toList());
			createdPainting.setDetails(detailDtos);
		}
		
		return createdPainting;
	}
	
	public static void detailDtoToEntity(DetailDto detailDto, Detail detail) {
        // Implementa la logica per la mappatura delle proprietà di DetailDto a Detail
        if(StringUtils.hasText(detailDto.getName()))
        	detail.setName(detailDto.getName());
        
		if(StringUtils.hasText(detailDto.getLinkImg()))
			detail.setLinkImg(detailDto.getLinkImg());
        
    }

    public static void detailEntityToDto(Detail detail, DetailDto detailDto) {
        // Implementa la logica per la mappatura delle proprietà di Detail a DetailDto
        detailDto.setName(detail.getName());
        detailDto.setLinkImg(detail.getLinkImg());
    }
}

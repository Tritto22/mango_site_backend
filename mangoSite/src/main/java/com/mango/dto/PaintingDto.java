package com.mango.dto;

import java.io.Serializable;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.mango.entity.Detail;
import com.mango.entity.ImagePaintingData;
import com.mango.entity.Painting;

import jakarta.persistence.Lob;
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
	
	@Lob
	private String imageDataBase64;
	
	public static Painting dtoToEntity(PaintingDto dto, Painting entity) {
		
//		var newPainting = new Painting();
		
		entity.setImg(dto.getImg());
		entity.setSize(dto.getSize());
		entity.setYear(dto.getYear());
		entity.setDescription(dto.getDescription());
		entity.setTecnique(dto.getTecnique());
		entity.setFavorite(dto.getFavorite());
		
		
		if(StringUtils.hasText(dto.getImageDataBase64())) {
			var entityImageData = new ImagePaintingData();
			
			entityImageData.setName(dto.getTitle()+"_image");
			entityImageData.setType(PaintingDto.getContentTypeFromBase64(dto.getImageDataBase64()));
			entityImageData.setImageData(PaintingDto.base64ToBytes(dto.getImageDataBase64()));
			
			entity.setImagePaintingData(entityImageData);
			entityImageData.setPainting(entity);
		}
		
		return entity;
	}
	
	public static PaintingDto entityToDto(Painting entity) {
		
		var createdPainting = new PaintingDto();
		
		BeanUtils.copyProperties(entity, createdPainting);
		if(entity.getDetails().size()>0 && entity.getDetails() != null) {
			List<DetailDto> detailDtos = entity.getDetails().stream()
	                .map(detail -> {
	                    var detailDto = new DetailDto();
	                    detailEntityToDto(detail, detailDto);
	                    return detailDto;
	                })
	                .collect(Collectors.toList());
			createdPainting.setDetails(detailDtos);
		}
		
		String imageBase64 = "data:"
							+ entity.getImagePaintingData().getType() 
							+ ";base64,"
							+ PaintingDto.bytesToBase64(entity.getImagePaintingData().getImageData());
		createdPainting.setImageDataBase64(imageBase64);
		
		return createdPainting;
	}
	
	// DETAILS METHODS
	
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
    
    // IMAGE CONVERSION METHODS
    
    public static byte[] base64ToBytes(String base64) {
    	
    	Pattern pattern = Pattern.compile("data:([a-zA-Z]+/[a-zA-Z]+);base64,");
    	Matcher matcher = pattern.matcher(base64);
    	if (matcher.find()) {
            base64 = base64.replaceAll(pattern.toString(), "");;
        }
    	
        return Base64.getDecoder().decode(base64);
    }

    public static String bytesToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    public static String getContentTypeFromBase64(String base64) {
        Pattern pattern = Pattern.compile("data:([a-zA-Z]+/[a-zA-Z]+);base64,");
        Matcher matcher = pattern.matcher(base64);
        
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
}

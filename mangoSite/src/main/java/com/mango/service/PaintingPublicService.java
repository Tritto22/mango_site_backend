package com.mango.service;

import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mango.dto.DetailDto;
import com.mango.dto.ErrorDto;
import com.mango.dto.PaintingDto;
import com.mango.dto.ResponseDto;
import com.mango.entity.Painting;
import com.mango.repository.PaintingRepository;

@Service
public class PaintingPublicService {

	@Autowired
	private PaintingRepository repository;
	
	// Slider
	public ResponseDto getSliderPaintings(){
		
		var response = new ResponseDto();
		
		try {
			Pageable pageable = PageRequest.of(0, 5);
			var favoritePaintings = repository.findByFavoriteIsTrue(pageable);

			var dtoPaintingList = favoritePaintings.stream()
                    .map(painting -> paintingToDto(painting, favoritePaintings.size()))
                    .collect(Collectors.toList());
			
			response.setPayload(dtoPaintingList);
		} catch(Exception e) {
			
			ErrorDto error = new ErrorDto();
			error.setMsg("Errore nel caricamento dei quadri!");
		}
				
		return response;
	}
	
	// Internal Methods
	private PaintingDto paintingToDto(Painting painting, Integer totPages) {
        
		var paintingDto = new PaintingDto();
		
        BeanUtils.copyProperties(painting, paintingDto);
        
        var detailDtos = painting.getDetails().stream()
                .map(detail -> {
                    var detailDto = new DetailDto();
                    PaintingDto.detailEntityToDto(detail, detailDto);
                    return detailDto;
                })
                .collect(Collectors.toList());
        
        paintingDto.setDetails(detailDtos);
        paintingDto.setTotPages(totPages);
        return paintingDto;
    }
}

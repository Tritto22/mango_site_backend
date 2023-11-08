package com.mango.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.mango.dto.ErrorDto;
import com.mango.dto.PaintingDto;
import com.mango.dto.ResponseDto;
import com.mango.entity.Painting;
import com.mango.exception.ValidationException;
import com.mango.repository.PaintingRepository;


@Service
public class PaintingService {

	@Autowired
	private PaintingRepository repository;
	
	// CREA SLUG
	public String createSlug(String input) {
		// Rimuovi spazi e caratteri speciali
	    String slug = input.replaceAll("[^a-zA-Z0-9\\s]", "");

	    // Sostituisci spazi con trattini
	    slug = slug.replaceAll("\\s+", "-");

	    // Trasforma tutto in minuscolo
	    slug = slug.toLowerCase();
		
		return slug;
	}
	
	// UNICITA SLUG
	public String createUniqueSlug(String input) {
        String slug = createSlug(input);
        String originalSlug = slug;
        int counter = 1;

        while (repository.existsBySlug(slug)) {
            // Lo slug non è unico, aggiungi un contatore e riprova
            slug = originalSlug + "-" + counter;
            counter++;
        }

        return slug;
    }
	
	// PUSH
	public ResponseDto savePainting(PaintingDto painting) {
		
		Painting newPainting = new Painting();
		PaintingDto createdPainting = new PaintingDto();
		ResponseDto response = new ResponseDto();
		
		try {
			
			paintingValidation(painting);
			PaintingDto.dtoToEntity(painting, newPainting);
			
			newPainting.setTitle(painting.getTitle());	
			newPainting.setSlug(createUniqueSlug(painting.getTitle()));
			
			repository.save(newPainting);
			
			
			
			PaintingDto.entityToDto(newPainting, createdPainting);

			
			response.setPayload(createdPainting);
			
		}catch(Exception e){
			
			ErrorDto error = new ErrorDto();
			error.setMsg(e.getMessage());
			response.setError(error);
		}
		

		return response;
	}
	
	// GET
	public ResponseDto getPaintings(int pageNumber, int pageSize){
		
		ResponseDto response = new ResponseDto();
		
		try {
			Pageable pageable = PageRequest.of(pageNumber, pageSize);
			Page<Painting> firstTen = repository.findAll(pageable);
			Integer totPages = (int) Math.ceil((double) repository.count() / pageSize);
//			Integer totPages = 1;
			List<PaintingDto> dtoPaintingList = convertToPaintingDtoList(firstTen, totPages);
			
			response.setPayload(dtoPaintingList);
		} catch(Exception e) {
			
			ErrorDto error = new ErrorDto();
			error.setMsg("Errore nel caricamento dei quadri!");
		}
				
		return response;
	}
	
	// PUT
	public ResponseDto updatePainting(PaintingDto painting) {
		
		Painting selectedPainting = new Painting();
		PaintingDto updatedDtoPainting = new PaintingDto();
		ResponseDto response = new ResponseDto();
		
		try {
			
			paintingValidation(painting);
			if(repository.existsBySlug(painting.getSlug())) {
				selectedPainting = repository.findBySlug(painting.getSlug());
				if(!selectedPainting.getTitle().equalsIgnoreCase(painting.getTitle())) {
					selectedPainting.setTitle(painting.getTitle());
					selectedPainting.setSlug(createUniqueSlug(painting.getTitle()));
				}
				PaintingDto.dtoToEntity(painting, selectedPainting);
	//			selectedPainting.setDetails(painting.getDetails());
				
				repository.save(selectedPainting);

				PaintingDto.entityToDto(selectedPainting, updatedDtoPainting);
				response.setPayload(updatedDtoPainting);
			} else {
	            ErrorDto error = new ErrorDto();
	            error.setMsg("Il quadro con lo slug specificato non esiste.");
	            response.setError(error);
	        }
		} catch(Exception e) {
			ErrorDto error = new ErrorDto();
			error.setMsg(e.getMessage());
			response.setError(error);
		}
		

		return response;
	}
	
	// DELETE
	public ResponseDto deletePainting(PaintingDto painting) {
		
		Painting selectedPainting = new Painting();
		PaintingDto delatedDtoPainting = new PaintingDto();
		ResponseDto response = new ResponseDto();
		try {
			if(repository.existsBySlug(painting.getSlug())) {
				selectedPainting = repository.findBySlug(painting.getSlug());
	
				repository.deleteById(selectedPainting.getId());
				
				PaintingDto.entityToDto(selectedPainting, delatedDtoPainting);
				response.setPayload(delatedDtoPainting);
			} else {
	            ErrorDto error = new ErrorDto();
	            error.setMsg("Il quadro con lo slug specificato non esiste.");
	            response.setError(error);
	        }
		} catch(Exception e) {
			ErrorDto error = new ErrorDto();
			error.setMsg(e.getMessage());
			response.setError(error);
		}

		return response;
	}
	
	// INTERNAL METHODS
	private void paintingValidation(PaintingDto painting) throws ValidationException {
		if(painting != null) {
			if(!StringUtils.hasText(painting.getTitle())) {
				
				throw new ValidationException("05", "Il titolo non può essere vuoto o contenere solo spazi!");
			}
			
			if(painting.getYear()!= null) {
				if(painting.getYear() < 1993 || painting.getYear() > 2199) {
					throw new ValidationException("06", "Attenzione! L'anno deve essere incluso tra il 1993 e il 2199.");
				}
			}					
		}
	}
	
	private List<PaintingDto> convertToPaintingDtoList(Page<Painting> paintingList, Integer totPages) {
        return paintingList.stream()
                .map(painting -> paintingToDto(painting, totPages))
                .collect(Collectors.toList());
    }

	private PaintingDto paintingToDto(Painting painting, Integer totPages) {
        PaintingDto paintingDto = new PaintingDto();
        BeanUtils.copyProperties(painting, paintingDto);
        
        paintingDto.setTotPages(totPages);
        return paintingDto;
    }
}

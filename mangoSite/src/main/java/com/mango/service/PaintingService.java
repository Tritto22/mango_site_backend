package com.mango.service;

import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mango.dto.DetailDto;
import com.mango.dto.ErrorDto;
import com.mango.dto.PaintingDto;
import com.mango.dto.ResponseDto;
import com.mango.entity.Detail;
import com.mango.entity.Painting;
import com.mango.exception.ValidationException;
import com.mango.repository.DetailRepository;
import com.mango.repository.PaintingRepository;


@Service
public class PaintingService {

	@Autowired
	private PaintingRepository repository;
	
	@Autowired
	private DetailRepository detailRepository;
	
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
		
		var response = new ResponseDto();
		
		try {
			
			paintingValidation(painting);
			
			// Step 1: Salva l'oggetto Painting nel database
			var newPainting = new Painting();
					PaintingDto.dtoToEntity(painting, newPainting);
	        newPainting.setTitle(painting.getTitle());
	        newPainting.setSlug(createUniqueSlug(painting.getTitle()));	        

	        repository.save(newPainting);

            var savedPainting = repository.findBySlug(newPainting.getSlug());
	        
	        // Step 2: Associa questo ID ai dettagli
	        var details = painting.getDetails().stream()
	                .map(detailDto -> {
	                    var detail = new Detail();
	                    PaintingDto.detailDtoToEntity(detailDto, detail);	                    
	                    // Imposta l'ID dell'oggetto Painting nei dettagli
	                    detail.setPainting(savedPainting);
	                    return detail;
	                })
	                .collect(Collectors.toList());
	        savedPainting.setDetails(details);
	        // Step 4: Salva i dettagli nel database
	        repository.save(savedPainting);
	        

	        // Aggiorna l'oggetto PaintingDto con le informazioni aggiornate
	        var createdPainting = PaintingDto.entityToDto(savedPainting);

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
		
		var response = new ResponseDto();
		
		try {
			Pageable pageable = PageRequest.of(pageNumber, pageSize);
			Page<Painting> firstTen = repository.findAll(pageable);
			Integer totPages = (int) Math.ceil((double) repository.count() / pageSize);
//			Integer totPages = 1;
			var dtoPaintingList = firstTen.stream()
	                .map(painting -> paintingToDto(painting, totPages))
	                .collect(Collectors.toList());
			
			response.setPayload(dtoPaintingList);
		} catch(Exception e) {
			
			ErrorDto error = new ErrorDto();
			error.setMsg("Errore nel caricamento dei quadri!");
		}
				
		return response;
	}
	
	// PUT
	public ResponseDto updatePainting(PaintingDto painting) {
		
		ResponseDto response = new ResponseDto();
		
		try {
			
			paintingValidation(painting);
			
			if(repository.existsBySlug(painting.getSlug())) {
				Painting selectedPainting = repository.findBySlug(painting.getSlug());
				
				selectedPainting = PaintingDto.dtoToEntity(painting, selectedPainting);
				
				if(!painting.getTitle().equalsIgnoreCase(selectedPainting.getTitle())) {
					selectedPainting.setTitle(painting.getTitle());
					selectedPainting.setSlug(createUniqueSlug(painting.getTitle()));
				}			
				
//				repository.save(selectedPainting);

				final Painting updatedPainting = selectedPainting;
				
				// Elimina i dettagli esistenti associati al quadro
	            detailRepository.deleteByPainting(updatedPainting);
				
				if(painting.getDetails() != null) {										
					var details = painting.getDetails().stream()
			                .map(detailDto -> {
			                    var detail = new Detail();
			                    PaintingDto.detailDtoToEntity(detailDto, detail);	
			                    
			                    // Imposta l'ID dell'oggetto Painting nei dettagli
			                    if(detail.getName() != null && detail.getLinkImg() != null) {
			                    	detail.setPainting(updatedPainting);
			                    }
			                    
			                    return detail;
			                })
			                .collect(Collectors.toList());
					updatedPainting.setDetails(details);
				} else {
					updatedPainting.setDetails(null);
				}
				BeanUtils.copyProperties(updatedPainting, selectedPainting);

				repository.save(updatedPainting);

				var updatedDtoPainting = PaintingDto.entityToDto(updatedPainting);
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
		
		var selectedPainting = new Painting();
		var response = new ResponseDto();
		
		try {
			if(repository.existsBySlug(painting.getSlug())) {
				selectedPainting = repository.findBySlug(painting.getSlug());
	
				repository.deleteById(selectedPainting.getId());
				
				var delatedDtoPainting = PaintingDto.entityToDto(selectedPainting);
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

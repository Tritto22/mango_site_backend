package com.mango.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mango.dto.ResponseDto;
import com.mango.service.PaintingPublicService;
import com.mango.service.PaintingService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/public")
public class PublicController {

	@Autowired
	private PaintingPublicService service;
	
	@Autowired
	private PaintingService adminService;
	
	@GetMapping("/home")
	public ResponseEntity<?> sliderPaintings() {

		ResponseDto response = service.getSliderPaintings();
	    
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/gallery")
	public ResponseEntity<?> galleryPaintings(
			@RequestParam(defaultValue = "0", required=false) int pageNumber,
		    @RequestParam(defaultValue = "10", required=false) int pageSize) {

		ResponseDto response = adminService.getPaintings(pageNumber, pageSize);
	    
		return ResponseEntity.ok(response);
	}
}

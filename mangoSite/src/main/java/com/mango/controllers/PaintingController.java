package com.mango.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mango.dto.PaintingDto;
import com.mango.dto.ResponseDto;
import com.mango.service.PaintingService;

@RestController
@RequestMapping("/api/admin")
public class PaintingController {
	
	@Autowired
	private PaintingService service;
	
//	@GetMapping("/dashboard")
//	public ResponseEntity<?> adminDashboard() {
//
//		ResponseDto response = service.getPaintings();
//	    
//		return ResponseEntity.ok(response);
//	}
	
	@GetMapping("/dashboard")
	public ResponseEntity<?> adminDashboard(
			@RequestParam(defaultValue = "0", required=false) int pageNumber,
		    @RequestParam(defaultValue = "10", required=false) int pageSize) {

		ResponseDto response = service.getPaintings(pageNumber, pageSize);
	    
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/addPainting")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> addPainting(@RequestBody PaintingDto painting) {
		
		ResponseDto response = service.savePainting(painting);
		
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/update")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> updatePainting(@RequestBody PaintingDto painting) {
		
		ResponseDto response = service.updatePainting(painting);
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/delete")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deletePainting(@RequestBody PaintingDto painting) {
		
		ResponseDto response = service.deletePainting(painting);
		
		return ResponseEntity.ok(response);
	}
}

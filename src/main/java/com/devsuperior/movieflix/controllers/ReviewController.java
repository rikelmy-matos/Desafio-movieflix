package com.devsuperior.movieflix.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.services.ReviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
	
	 @Autowired
	    private ReviewService service;

	    @PostMapping
	    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
	    public ResponseEntity<ReviewDTO> newReview(@Valid @RequestBody ReviewDTO reviewDTO) {
	        reviewDTO = service.saveReview(reviewDTO);
	        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
	                .buildAndExpand(reviewDTO.getId()).toUri();
	        return ResponseEntity.created(uri).body(reviewDTO);
	    }

}

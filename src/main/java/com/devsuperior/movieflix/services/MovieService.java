package com.devsuperior.movieflix.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieProjection;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.DatabaseException;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import com.devsuperior.movieflix.util.Utils;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MovieService {
	
	@Autowired
	private MovieRepository repository;
	
	
	/*
	@Transactional(readOnly = true)
	public Page<MovieCardDTO> findAllPaged(PageRequest pageRequest){
		Page<Movie> genreList = repository.findAll(pageRequest);
		return genreList.map(x -> new MovieCardDTO(x));
	}
	*/
	
	@Transactional(readOnly = true)
	public List<MovieCardDTO> findAll(){
		List<Movie> result = repository.findAll();
		return result.stream().map(x -> new MovieCardDTO(x)).toList();
	}
	
	@Transactional(readOnly = true)
	public MovieDetailsDTO findById(Long id){
		Optional<Movie> genre = repository.findById(id);
		return new MovieDetailsDTO(genre.orElseThrow(() -> new ResourceNotFoundException("Entity not found")));
	}
	
	@Transactional
	public MovieCardDTO insert(MovieCardDTO dto) {
		Movie genre = new Movie();
		genre.setTitle(dto.getTitle());
		genre = repository.save(genre);
		return new MovieCardDTO(genre);
	}
	
	@Transactional
	public MovieCardDTO update(Long id, MovieCardDTO dto) {
		try {
			Movie entity = repository.getReferenceById(id);
			entity.setTitle(dto.getTitle());
			entity = repository.save(entity);
			return new MovieCardDTO(entity);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("ID not found: " + id);	
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
		try {
			repository.deleteById(id);    		
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial");
		}
	}
}

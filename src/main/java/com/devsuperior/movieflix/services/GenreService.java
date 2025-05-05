package com.devsuperior.movieflix.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.GenreDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.services.exceptions.DatabaseException;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class GenreService {
	
	@Autowired
	private GenreRepository repository;
	
	
	/*
	@Transactional(readOnly = true)
	public Page<GenreDTO> findAllPaged(PageRequest pageRequest){
		Page<Genre> categoryList = repository.findAll(pageRequest);
		return categoryList.map(x -> new GenreDTO(x));
	}
	*/
	
	@Transactional(readOnly = true)
	public List<GenreDTO> findAll(){
		List<Genre> categoryList = repository.findAll();
		return categoryList.stream().map(x -> new GenreDTO(x)).toList();
	}
	
	@Transactional(readOnly = true)
	public GenreDTO findById(Long id){
		Optional<Genre> category = repository.findById(id);
		return new GenreDTO(category.orElseThrow(() -> new ResourceNotFoundException("Entity not found")));
	}
	
	@Transactional
	public GenreDTO insert(GenreDTO dto) {
		Genre category = new Genre();
		category.setName(dto.getName());
		category = repository.save(category);
		return new GenreDTO(category);
	}
	
	@Transactional
	public GenreDTO update(Long id, GenreDTO dto) {
		try {
			Genre entity = repository.getReferenceById(id);
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new GenreDTO(entity);
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

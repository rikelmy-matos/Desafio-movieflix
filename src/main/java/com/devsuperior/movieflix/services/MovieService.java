package com.devsuperior.movieflix.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.DatabaseException;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MovieService {
	
	@Autowired
	private MovieRepository repository;
	
	@Autowired
	private GenreRepository genreRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	
	/*
	@Transactional(readOnly = true)
	public Page<MovieCardDTO> findAllPaged(PageRequest pageRequest){
		Page<Movie> genreList = repository.findAll(pageRequest);
		return genreList.map(x -> new MovieCardDTO(x));
	}
	*/
	
	@Transactional(readOnly = true)
	public MovieDetailsDTO findById(Long id){
		Optional<Movie> genre = repository.findById(id);
		return new MovieDetailsDTO(genre.orElseThrow(() -> new ResourceNotFoundException("Entity not found")));
	}
	
	@Transactional(readOnly = true)
    public Page<MovieCardDTO> findAllPageMovieByGenre(Long genreId, Pageable pageable) {
        Genre genre = (genreId == 0) ? null : genreRepository.getReferenceById(genreId);
        Page<Movie> movies = repository.findMovieByGenre(genre, pageable);
        return movies.map(x -> new MovieCardDTO(x));
    }
	
    @Transactional(readOnly = true)
    public List<ReviewDTO> findByReviewMovieId(Long id) {
        List<Review> reviews = reviewRepository.findByReviewMovieId(id);
        return reviews.stream().map(x -> new ReviewDTO(x)).toList();
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

package com.devsuperior.movieflix.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
	
	
    @Query("SELECT obj FROM Movie obj " +
            "WHERE :genre IS NULL OR obj.genre = :genre " +
            "ORDER BY obj.title ASC ")
    Page<Movie> findMovieByGenre(@Param("genre") Genre genre, Pageable pageable);
}



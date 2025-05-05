package com.devsuperior.movieflix.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.UnauthorizedException;

import static com.devsuperior.movieflix.constants.Constants.UNATHORIZED_USER;

@Service
public class ReviewService {
	
	@Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private AuthService authService;

    @Transactional
    public ReviewDTO saveReview(ReviewDTO reviewDTO) {
        try {
            Review newReview = new Review();
            copyDtoToEntity(reviewDTO, newReview);
            return new ReviewDTO(newReview);
        } catch (RuntimeException e) {
            throw new UnauthorizedException(UNATHORIZED_USER);
        }
    }

    private void copyDtoToEntity(ReviewDTO reviewDTO, Review reviewEntity) {
        User user = authService.authenticated();
        reviewEntity.setId(reviewDTO.getId());
        reviewEntity.setText(reviewDTO.getText());
        reviewEntity.setMovie(movieRepository.getReferenceById(reviewDTO.getMovieId()));
        reviewEntity.setUser(user);
        reviewRepository.save(reviewEntity);
    }

}

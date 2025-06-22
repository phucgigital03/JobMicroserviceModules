package com.phuc.reviewms.review.impl;

import com.phuc.reviewms.review.Review;
import com.phuc.reviewms.review.ReviewRepository;
import com.phuc.reviewms.review.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;


    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Review> getAllReviews(Long companyId) {
        return reviewRepository.findByCompanyId(companyId);
    }

    @Override
    public boolean addReview(Long companyId, Review review) {
        if (companyId != null && review != null) {
            review.setCompanyId(companyId);
            reviewRepository.save(review);
            return true;
        }
        return false;
    }

    @Override
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    @Override
    public boolean deleteReview(Long reviewId) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            reviewRepository.delete(review); // delete from DB
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean updateReview(Long reviewId, Review updatedReview) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if( reviewOptional.isPresent()) {
            Review existingReview = reviewOptional.get();
            existingReview.setTitle(updatedReview.getTitle());
            existingReview.setDescription(updatedReview.getDescription());
            existingReview.setRating(updatedReview.getRating());
            existingReview.setCompanyId(updatedReview.getCompanyId());
            reviewRepository.save(existingReview); // save updated review
            return true;
        } else {
            return false; // review not found
        }
    }

    @Override
    public Double getAverageRating(Long companyId) {
        return reviewRepository.findByCompanyId(companyId)
                .stream()
                .mapToDouble(Review::getRating)
                .average().orElse(0);
    }
}

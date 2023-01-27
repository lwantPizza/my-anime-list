package com.myanimelist.dao;

import java.util.List;

import com.myanimelist.entity.Review;
import com.myanimelist.validation.entity.ValidReview;

public interface ReviewDao {

	public List<Review> findReviews(int animeId);

	public void save(ValidReview reviewForm);

	public Review remove(int reviewId);
}

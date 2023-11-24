package com.mango.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mango.entity.Painting;

public interface PaintingRepository extends JpaRepository<Painting, Integer> {
	List<Painting> findByTitle(String title);
	Painting findBySlug(String slug);
	boolean existsBySlug(String slug);
	Page<Painting> findAll(Pageable pageable);
	List<Painting> findByFavoriteIsTrue(Pageable pageable);
}

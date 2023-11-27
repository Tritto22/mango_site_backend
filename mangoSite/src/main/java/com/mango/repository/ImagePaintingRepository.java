package com.mango.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mango.entity.ImagePaintingData;

public interface ImagePaintingRepository extends JpaRepository<ImagePaintingData, Long> {
	Optional<ImagePaintingData> findByName(String name);
}

package com.mango.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.mango.entity.ImagePaintingData;
import com.mango.entity.Painting;

public interface ImagePaintingRepository extends JpaRepository<ImagePaintingData, Long> {
	Optional<ImagePaintingData> findByName(String name);
	
	@Transactional
	@Modifying
    @Query("DELETE FROM ImagePaintingData d WHERE d.painting = :painting")
    void deleteByPainting(@Param("painting") Painting painting);
}

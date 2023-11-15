package com.mango.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.mango.entity.Detail;
import com.mango.entity.Painting;


public interface DetailRepository extends JpaRepository<Detail, Integer>{

	@Transactional
	@Modifying
    @Query("DELETE FROM Detail d WHERE d.painting = :painting")
    void deleteByPainting(@Param("painting") Painting painting);
}

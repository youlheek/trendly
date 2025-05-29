package com.rebootcrew.trendly.trending.repository;

import com.rebootcrew.trendly.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
	Optional<Keyword> findById(Long id);
}

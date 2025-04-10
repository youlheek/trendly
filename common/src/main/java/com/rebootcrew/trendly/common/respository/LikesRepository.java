package com.rebootcrew.trendly.common.respository;

import com.rebootcrew.trendly.common.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {

	int countByTargetId(Long messageId);
}

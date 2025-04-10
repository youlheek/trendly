package com.rebootcrew.trendly.discussion.repository;

import com.rebootcrew.trendly.discussion.domain.ChatRoom;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT cr FROM ChatRoom cr LEFT JOIN FETCH cr.members WHERE cr.id = :roomId")
	Optional<ChatRoom> findChatRoomById(@Param("roomId") Long id);
	Optional<ChatRoom> findByKeywordId(Long keywordId);
}

package com.rebootcrew.trendly.discussion.repository;

import com.rebootcrew.trendly.discussion.domain.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	@Query("""
			SELECT cm
			FROM ChatMessage cm
			JOIN FETCH cm.user
			JOIN FETCH cm.chatRoom
			JOIN FETCH cm.chatRoomMember
			WHERE cm.chatRoom.id = :roomId
			ORDER BY cm.createdAt DESC
			""")
	List<ChatMessage> findRecentMessages(@Param("roomId") Long roomId, Pageable pageable);

	@Query("""
			    SELECT cm
			    FROM ChatMessage cm
			    JOIN FETCH cm.user
			    JOIN FETCH cm.chatRoom
			    JOIN FETCH cm.chatRoomMember
			    WHERE cm.chatRoom.id = :roomId
			    AND cm.createdAt < :createdAt
			    ORDER BY cm.createdAt desc
			""")
	List<ChatMessage> findMessagesBefore(@Param("roomId") Long roomId, LocalDateTime createdAt, Pageable pageable);

	@Query("SELECT m.createdAt FROM ChatMessage m WHERE m.id = :messageId")
	LocalDateTime findCreatedAtById(@Param("messageId") Long messageId);
}

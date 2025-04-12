package com.rebootcrew.trendly.discussion.repository;

import com.rebootcrew.trendly.discussion.domain.ChatRoomMember;
import com.rebootcrew.trendly.discussion.domain.dto.NicknameDto;
import com.rebootcrew.trendly.discussion.domain.enums.ChatRoomMemeberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
//	Optional<ChatRoomMember> findByUserIdAndChatRoomIdAndStatus(Long userId, Long chatRoomId, ChatRoomMemeberStatus status);
	@Query("SELECT new com.rebootcrew.trendly.discussion.domain.dto.NicknameDto(m.user.id, m.nickname) " +
			"FROM ChatRoomMember m " +
			"WHERE m.chatRoom.id = :roomId AND m.user.id IN :userIds " +
			"ORDER BY m.id DESC")
	List<NicknameDto> findNicknamesByChatRoomIdAndUserIds(@Param("roomId")Long chatRoomId, @Param("userIds") Set<Long> userIds);

	Optional<ChatRoomMember> findByChatRoom_IdAndUser_IdAndStatus(Long chatRoomId, Long userId, ChatRoomMemeberStatus status);

	boolean existsByNicknameAndChatRoom_IdAndStatus(String nickname, Long roomId, ChatRoomMemeberStatus status);

	List<ChatRoomMember> findByUser_IdAndStatus(Long userId, ChatRoomMemeberStatus status);

	ChatRoomMember findByChatRoom_IdAndUser_Id(Long chatRoomId, Long userId);

	int countByChatRoom_IdAndStatus(Long chatRoomId, ChatRoomMemeberStatus status);

}

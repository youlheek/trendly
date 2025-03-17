package com.rebootcrew.trendly.discussion.repository;

import com.rebootcrew.trendly.discussion.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}

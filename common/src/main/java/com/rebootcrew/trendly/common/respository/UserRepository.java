package com.rebootcrew.trendly.common.respository;

import com.rebootcrew.trendly.common.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmailAndDeletedAtIsNull(String email);
	Optional<User> findFirstByEmailAndDeletedAtIsNotNullOrderByDeletedAtDesc(String email);

	Optional<User> findById(Long id);
	Optional<User> findByIdAndDeletedAtIsNull(Long id);
	// TODO : findById~ 둘 다 사용해도 되는가?
}

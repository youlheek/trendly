package com.rebootcrew.trendly.user.service;

import com.rebootcrew.trendly.common.domain.User;
import com.rebootcrew.trendly.common.domain.enums.Gender;
import com.rebootcrew.trendly.common.exception.CustomException;
import com.rebootcrew.trendly.common.exception.ErrorCode;
import com.rebootcrew.trendly.common.respository.UserRepository;
import com.rebootcrew.trendly.user.domain.UserDto;
import com.rebootcrew.trendly.common.domain.UserForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;


	/**
	 * 사용자 정보 조회
	 *
	 * @param userId
	 * @return
	 */
	public UserDto getUserInfo(Long userId) {
		UserDto userDto = UserDto.fromEntity(userRepository.findByIdAndDeletedAtIsNull(userId)
				.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER)));
		return userDto;
	}

	/**
	 * 사용자 정보 수정 (
	 *
	 * @param userForm
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class) // 모든 예외에서 롤백처리
	public UserDto updateUser(Long userId, UserForm userForm) {
		User user = userRepository.findByIdAndDeletedAtIsNull(userId)
				.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

		// Gender 유효성 검사 (열거형 값만 허용)
		if (userForm.getGender() != null && EnumUtils.isValidEnum(Gender.class, userForm.getGender())) {
			throw new CustomException(ErrorCode.INVALID_INPUT);
		}
		user.setGender(userForm.getGender());
		// Birthdate 유효성 검사
		if (userForm.getBirthDate() != null && userForm.getBirthDate().isAfter(LocalDate.now())) {
			throw new CustomException(ErrorCode.INVALID_INPUT);
		}
		user.setBirthDate(userForm.getBirthDate());
		user.setMarketingOpt(userForm.isMarketingOpt());

		try {
			return UserDto.fromEntity(userRepository.save(user));
		} catch (DateTimeParseException | DataIntegrityViolationException e) {
			throw new CustomException(ErrorCode.INVALID_INPUT);
		}
	}

	@Transactional(rollbackFor = Exception.class) // 모든 예외에서 롤백처리
	public void deleteUser(Long userId) {
		User user = userRepository.findByIdAndDeletedAtIsNull(userId)
				.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
		user.setDeletedAt(LocalDateTime.now());
		userRepository.save(user);

	}


}

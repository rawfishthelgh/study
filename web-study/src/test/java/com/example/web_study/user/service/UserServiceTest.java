package com.example.web_study.user.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.web_study.user.entity.User;
import com.example.web_study.user.fixture.UserCreateFixture;
import com.example.web_study.user.repository.UserRepository;
import com.example.web_study.user.service.dto.UserDto;

@SpringBootTest
@Transactional
class UserServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Test
	void 회원가입_성공() {
		// given
		UserDto.Create request = UserCreateFixture.valid();

		// when
		userService.register(request);

		// then
		List<User> users = userRepository.findAll();
		assertThat(users).hasSize(1);
		User saved = users.get(0);
		assertThat(saved.getName()).isEqualTo("홍길동");
		assertThat(saved.getEmail()).isEqualTo("hong@example.com");
		assertThat(saved.getUserType().name()).isEqualTo("STUDENT");
	}

	@Test
	void 이름_null이면_예외() {
		UserDto.Create request = UserCreateFixture.with(
			null,
			"hong@example.com",
			"01012345678",
			"Abc123",
			"student");

		assertThatThrownBy(() -> userService.register(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("이름은 비어있을 수 없습니다.");
	}

	@Test
	void 이메일_형식이_잘못되면_예외() {
		UserDto.Create request = UserCreateFixture.with(
			"홍길동",
			"invalid-email",
			"01012345678",
			"Abc123",
			"student"
		);

		assertThatThrownBy(() -> userService.register(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("유효하지 않은 이메일 형식");
	}

	@Test
	void 비밀번호_형식이_잘못되면_예외() {
		UserDto.Create request = UserCreateFixture.with(
			"홍길동",
			"hong@example.com",
			"01012345678",
			"abcdef",
			"student"
		);

		assertThatThrownBy(() -> userService.register(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("비밀번호가 유효하지 않습니다.");
	}

	@Test
	void 휴대폰번호_형식이_잘못되면_예외() {
		UserDto.Create request = UserCreateFixture.with(
			"홍길동",
			"hong@example.com",
			"1234",
			"Abc123",
			"student"
		);

		assertThatThrownBy(() -> userService.register(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("유효하지 않은 휴대폰 번호");
	}

	@Test
	void 존재하지_않는_회원유형이면_예외() {
		UserDto.Create request = UserCreateFixture.with(
			"홍길동",
			"hong@example.com",
			"01012345678",
			"Abc123",
			"admin"
		);

		assertThatThrownBy(() -> userService.register(request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("유효하지 않은 회원 유형");
	}
}

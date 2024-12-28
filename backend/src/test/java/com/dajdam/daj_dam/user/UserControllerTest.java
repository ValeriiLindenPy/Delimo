package com.dajdam.daj_dam.user;

import com.dajdam.daj_dam.error.ValidationMarker;
import com.dajdam.daj_dam.user.dto.UserDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    private Validator validator;

    private WebClient webClient;

    private UserDto user;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        String baseUrl = "http://localhost:" + port + "/users";

        webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();

        user = UserDto.builder().name("Test User")
                .email("testusernew@example.com").build();
    }

    @AfterEach
    void reset() {
        userRepository.clear();
    }

    @Test
    void getById() {
        UserDto savedUser = webClient.post()
                .bodyValue(user)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();

        UserDto retrievedUser = webClient.get()
                .uri("/{id}", savedUser.getId())
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();

        assertThat(retrievedUser).isNotNull();
        user.setId(savedUser.getId());
        assertThat(retrievedUser).isEqualTo(user);
    }

    @Test
    void editById() {
        UserDto savedUser = webClient.post()
                .bodyValue(user)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();

        UserDto updateRequest = UserDto.builder()
                .name("Updated User")
                .email("updateduser@example.com")
                .build();

        UserDto updatedUser = webClient.patch()
                .uri("/{id}", savedUser.getId())
                .bodyValue(updateRequest)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isEqualTo(savedUser.getId());
        assertThat(updatedUser.getName()).isEqualTo(updateRequest.getName());
        assertThat(updatedUser.getEmail()).isEqualTo(updateRequest.getEmail());
    }

    @Test
    void createUser() {
        UserDto savedUser = webClient.post()
                .bodyValue(user)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();

        assertNotNull(savedUser.getId());
        assertThat(savedUser.getName()).isEqualTo("Test User");
        assertThat(savedUser.getEmail()).isEqualTo("testusernew@example.com");
    }

    @Test
    void createUserFailId() {
        UserDto user = UserDto.builder().id(2L).name("Test User")
                .email("testusernew@example.com").build();

        Set<ConstraintViolation<UserDto>> violations = validator
                .validate(user, ValidationMarker.OnCreate.class);

        assertThat(violations).isNotEmpty().extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("Id should be null");
    }

    @Test
    void createUserFailName() {
        UserDto user = UserDto.builder().name("")
                .email("testusernew@example.com").build();

        Set<ConstraintViolation<UserDto>> violations = validator
                .validate(user, ValidationMarker.OnCreate.class);

        assertThat(violations).isNotEmpty().extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("Name can't be blank");
    }

    @Test
    void createUserFailEmail() {
        UserDto user = UserDto.builder().name("Name")
                .email("").build();

        Set<ConstraintViolation<UserDto>> violations = validator
                .validate(user, ValidationMarker.OnCreate.class);

        assertThat(violations).isNotEmpty().extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("Email can't be blank");
    }

    @Test
    void createUserFailEmailFormat() {
        UserDto user = UserDto.builder().name("Name")
                .email("testuserexamplecom").build();

        Set<ConstraintViolation<UserDto>> violations = validator
                .validate(user, ValidationMarker.OnCreate.class,
                        ValidationMarker.OnUpdate.class);

        assertThat(violations).isNotEmpty().extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("Wrong email format");
    }
}

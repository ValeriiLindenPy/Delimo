package com.dajdam.daj_dam.item;

import com.dajdam.daj_dam.error.ValidationMarker;
import com.dajdam.daj_dam.item.dto.ItemDto;
import com.dajdam.daj_dam.item.model.Item;
import com.dajdam.daj_dam.user.UserMapper;
import com.dajdam.daj_dam.user.UserRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;


import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Validator validator;

    private WebClient webClient;

    private ItemDto item;

    private UserDto user;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        String baseUrl = "http://localhost:" + port + "/items";

        webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();

        UserDto testUser = UserDto.builder().name("Test User")
                .email("testusernew@example.com").build();

        user = UserMapper.toUserDto(userRepository.save(UserMapper.toUser(testUser)));

        Item testItem = Item.builder()
                .owner(user)
                .name("Some Item")
                .description("Just a test item")
                .available(true)
                .build();

        item = ItemMapper.toItemDto(itemRepository.save(testItem));
    }

    @AfterEach
    void reset() {
        userRepository.clear();
        itemRepository.clear();
    }

    @Test
    void getAll() {
        List<ItemDto> items = webClient.get()
                .header("X-Sharer-User-Id", String.valueOf(user.getId()))
                .retrieve()
                .bodyToFlux(ItemDto.class)
                .collectList()
                .block();

        assertThat(items).isNotNull();
        assertThat(items).hasSize(1);
        assertThat(items.get(0).getName()).isEqualTo(item.getName());
    }

    @Test
    void getOne() {
        ItemDto newItem = webClient.get()
                .uri("/{id}", item.getId())
                .retrieve()
                .bodyToMono(ItemDto.class)
                .block();

        assertThat(newItem).isNotNull();
        assertThat(newItem.getName()).isEqualTo(item.getName());
    }

    @Test
    void searchAllByText() {
        List<ItemDto> items = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("text", "Some")
                        .build())
                .retrieve()
                .bodyToFlux(ItemDto.class)
                .collectList()
                .block();

        assertThat(items).isNotNull();
        assertThat(items).hasSize(1);
        assertThat(items.get(0).getName()).isEqualTo(item.getName());
    }

    @Test
    void testEditOne() {
        ItemDto testItem = ItemDto.builder()
                .name("Updated Item")
                .description("Updated test item")
                .available(false)
                .build();

        ItemDto newItem  = webClient.patch()
                .uri("/{id}", item.getId())
                .header("X-Sharer-User-Id", String.valueOf(user.getId()))
                .bodyValue(testItem)
                .retrieve()
                .bodyToMono(ItemDto.class)
                .block();

        assertThat(newItem).isNotNull();
        assertThat(newItem.getName()).isEqualTo(testItem.getName());
        assertThat(newItem.getId()).isEqualTo(item.getId());
    }

    @Test
    void create() {
        ItemDto testItem = ItemDto.builder()
                .name("New Item")
                .description("New test item")
                .available(false)
                .build();

        ItemDto newItem  = webClient.post()
                .header("X-Sharer-User-Id", String.valueOf(user.getId()))
                .bodyValue(testItem)
                .retrieve()
                .bodyToMono(ItemDto.class)
                .block();

        assertThat(newItem).isNotNull();
        assertThat(newItem.getName()).isEqualTo(testItem.getName());
        assertThat(newItem.getId()).isEqualTo(2L);
    }

    @Test
    void createFailWithNonExistUSer() {
        ItemDto testItem = ItemDto.builder()
                .name("New Item")
                .description("New test item")
                .available(false)
                .build();


        WebClientResponseException.NotFound exception = assertThrows(
                WebClientResponseException.NotFound.class,
                () -> webClient.post()
                        .header("X-Sharer-User-Id", "100")
                        .bodyValue(testItem)
                        .retrieve()
                        .bodyToMono(ItemDto.class)
                        .block()
        );

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getResponseBodyAsString())
                .contains("User with id - 100 not found");
    }

    @Test
    void createItemFailName() {
        ItemDto item = ItemDto.builder()
                .name("")
                .description("Just a test item")
                .available(true)
                .build();

        Set<ConstraintViolation<ItemDto>> violations = validator
                .validate(item, ValidationMarker.OnCreate.class);

        assertThat(violations).isNotEmpty().extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("Name can't be blank");
    }

    @Test
    void createItemFailIdNull() {
        ItemDto item = ItemDto.builder()
                .id(2L)
                .name("Item")
                .description("Just a test item")
                .available(true)
                .build();

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(item, ValidationMarker.OnCreate.class);

        assertThat(violations).isNotEmpty().extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("Id should be null");
    }

    @Test
    void createItemFailDescription() {
        ItemDto item = ItemDto.builder()
                .name("Item")
                .description("")
                .available(true)
                .build();

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(item, ValidationMarker.OnCreate.class);

        assertThat(violations).isNotEmpty().extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("Description can't be blank");
    }

    @Test
    void createItemFailAvailable() {
        ItemDto item = ItemDto.builder()
                .name("Item")
                .description("Item desc")
                .build();

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(item, ValidationMarker.OnCreate.class);

        assertThat(violations).isNotEmpty().extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("available shouldn't be null");
    }

}
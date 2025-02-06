package rs.delimo.item.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import rs.delimo.error.exception.NotFoundException;
import rs.delimo.error.exception.OwnerException;
import rs.delimo.item.ItemRepository;
import rs.delimo.item.ItemService;
import rs.delimo.item.dto.ItemDto;
import rs.delimo.item.dto.ItemRequestDto;
import rs.delimo.item.dto.ItemTitle;
import rs.delimo.service.ImageManager;
import rs.delimo.user.Role;
import rs.delimo.user.User;
import rs.delimo.user.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ItemServiceImplTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private ImageManager imageManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ItemService itemService;

    private User testUser;

    ItemDto createdItem;

    @BeforeEach
    void setup() {
        testUser = User.builder()
                .email("test@example.com")
                .name("Test User")
                .password("password")
                .city("Test City")
                .street("Test Street")
                .phone("123456789")
                .viber("123456789")
                .role(Role.USER)
                .enabled(true)
                .build();
        userRepository.save(testUser);

        when(imageManager.uploadImages(anyList())).thenReturn(List.of("http://dummy.url/test-image.jpg"));

        ItemRequestDto itemRequest = ItemRequestDto.builder()
                .title("Test Item")
                .description("Item for test")
                .pricePerDay(150)
                .maxPeriodDays(10)
                .city("Test City")
                .street("Test Street")
                .phone("987654321")
                .viber("987654321")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test-getById.jpg",
                "image/jpeg",
                "dummy content".getBytes(StandardCharsets.UTF_8)
        );

        createdItem = itemService.create(itemRequest, testUser, List.of(file));
    }

    @Test
    void contextLoads() {
        assertNotNull(itemService);
        assertNotNull(itemRepository);
        assertNotNull(userRepository);
        assertNotNull(imageManager);
        assertNotNull(objectMapper);
    }

    @Test
    void testCreateItem() {
        ItemRequestDto itemRequest = ItemRequestDto.builder()
                .title("New Item")
                .description("This is a new item")
                .pricePerDay(100)
                .maxPeriodDays(7)
                .city("Test City")
                .street("Test Street")
                .phone("123456789")
                .viber("123456789")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test-image.jpg",
                "image/jpeg",
                "dummy image content".getBytes(StandardCharsets.UTF_8)
        );

        ItemDto newItem = itemService.create(itemRequest, testUser, List.of(file));

        assertNotNull(newItem.getId(), "Item id should not be null after creation");
        assertEquals("New Item", newItem.getTitle(), "Item title should match");
        assertEquals("This is a new item", newItem.getDescription(), "Item description should match");
        assertTrue(newItem.getAvailable(), "Newly created item should be available");
        assertNotNull(newItem.getImages(), "Images list should not be null");
    }

    @Test
    void testGetItemById() {
        ItemDto retrievedItem = itemService.getById(createdItem.getId());
        assertNotNull(retrievedItem, "Retrieved item should not be null");
        assertEquals(createdItem.getId(), retrievedItem.getId(), "IDs should match");
        assertEquals(createdItem.getTitle(), retrievedItem.getTitle(), "Titles should match");
    }

    @Test
    void testGetAllItems() {
        Page<ItemDto> retrievedItems = itemService.getAll(0, 6);
        assertNotNull(retrievedItems.getContent(), "Retrieved items should not be null");
        assertEquals(1, retrievedItems.getNumberOfElements(), "Number of elements should be 1");
    }

    @Test
    void testGetAllItemsWithCity() {
        Page<ItemDto> retrievedItems = itemService.getAll("Test City", 0, 6);
        assertNotNull(retrievedItems.getContent(), "Retrieved items should not be null");
        assertEquals(1, retrievedItems.getNumberOfElements(), "Number of elements should be 1");
        assertTrue(retrievedItems.getContent().stream()
                        .allMatch(itemDto -> itemDto.getOwner().getCity().equals("Test City")),
                "All must be in Test City");
    }

    @Test
    void testGetAllItemsByOwner() {
        Page<ItemDto> retrievedItems = itemService.getAllByOwner(0, 6, testUser);
        assertNotNull(retrievedItems.getContent(), "Retrieved items should not be null");
        assertEquals(1, retrievedItems.getNumberOfElements(), "Number of elements should be 1");
        assertTrue(retrievedItems.getContent().stream()
                        .allMatch(itemDto -> Objects.equals(itemDto.getOwner().getEmail(), testUser.getEmail())),
                "All should have the same email");
    }

    @Test
    void testGetByUserAndId() {
        ItemDto itemByUser = itemService.getByUserAndId(createdItem.getId(), testUser);
        assertNotNull(itemByUser, "Item retrieved by user should not be null");
        assertEquals(createdItem.getId(), itemByUser.getId(), "IDs should match");
        assertEquals(testUser.getEmail(), itemByUser.getOwner().getEmail(), "Owner email should match");
    }

    @Test
    void testEditItem() throws Exception {
        MockMultipartFile newFile = new MockMultipartFile(
                "image",
                "update.jpg",
                "image/jpeg",
                "updated content".getBytes(StandardCharsets.UTF_8)
        );
        String existingImagesJson = "[\"http://dummy.url/existing-image.jpg\"]";
        ItemRequestDto updateRequest = ItemRequestDto.builder()
                .title("Updated Title")
                .description("Updated Description")
                .pricePerDay(200)
                .maxPeriodDays(15)
                .available(false)
                .city("Updated City")
                .street("Updated Street")
                .phone("111111111")
                .viber("222222222")
                .build();

        ItemDto updatedItem = itemService.editOne(createdItem.getId(), updateRequest, testUser, List.of(newFile), existingImagesJson);

        assertEquals("Updated Title", updatedItem.getTitle(), "Title should be updated");
        assertEquals("Updated Description", updatedItem.getDescription(), "Description should be updated");
        assertEquals(200, updatedItem.getPricePerDay(), "Price per day should be updated");
        assertEquals(15, updatedItem.getMaxPeriodDays(), "Max period days should be updated");
        assertFalse(updatedItem.getAvailable(), "Availability should be updated");

        assertTrue(updatedItem.getImages().contains("http://dummy.url/existing-image.jpg"), "Existing image URL should be present");
        assertTrue(updatedItem.getImages().contains("http://dummy.url/test-image.jpg"), "Newly uploaded image URL should be present");
    }

    @Test
    void testEditItemWithWrongUser() {
        User anotherUser = User.builder()
                .email("another@example.com")
                .name("Another User")
                .password("password")
                .city("Another City")
                .street("Another Street")
                .phone("000000000")
                .viber("000000000")
                .role(Role.USER)
                .enabled(true)
                .build();
        userRepository.save(anotherUser);

        MockMultipartFile newFile = new MockMultipartFile(
                "image",
                "update.jpg",
                "image/jpeg",
                "updated content".getBytes(StandardCharsets.UTF_8)
        );
        String existingImagesJson = "[]";
        ItemRequestDto updateRequest = ItemRequestDto.builder()
                .title("Should Fail")
                .build();

        assertThrows(OwnerException.class, () ->
                itemService.editOne(createdItem.getId(), updateRequest, anotherUser, List.of(newFile), existingImagesJson));
    }

    @Test
    void testDeleteItem() {
        ItemRequestDto itemRequest = ItemRequestDto.builder()
                .title("Item to Delete")
                .description("Will be deleted")
                .pricePerDay(50)
                .maxPeriodDays(5)
                .city("Delete City")
                .street("Delete Street")
                .phone("555555555")
                .viber("555555555")
                .build();
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "delete.jpg",
                "image/jpeg",
                "dummy".getBytes(StandardCharsets.UTF_8)
        );
        ItemDto itemToDelete = itemService.create(itemRequest, testUser, List.of(file));

        itemService.delete(itemToDelete.getId(), testUser);

        assertThrows(NotFoundException.class, () -> itemService.getById(itemToDelete.getId()));
    }

    @Test
    void testDeleteItemWithWrongUser() {
        ItemRequestDto itemRequest = ItemRequestDto.builder()
                .title("Item Delete Fail")
                .description("Should not be deleted by wrong user")
                .pricePerDay(75)
                .maxPeriodDays(7)
                .city("Test City")
                .street("Test Street")
                .phone("123123123")
                .viber("321321321")
                .build();
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "deletefail.jpg",
                "image/jpeg",
                "dummy".getBytes(StandardCharsets.UTF_8)
        );
        ItemDto itemToDelete = itemService.create(itemRequest, testUser, List.of(file));

        User anotherUser = User.builder()
                .email("another2@example.com")
                .name("Another User2")
                .password("password")
                .city("Another City")
                .street("Another Street")
                .phone("999999999")
                .viber("999999999")
                .role(Role.USER)
                .enabled(true)
                .build();
        userRepository.save(anotherUser);

        assertThrows(OwnerException.class, () -> itemService.delete(itemToDelete.getId(), anotherUser));
    }

    @Test
    void testSearchTitles() {
        ItemRequestDto itemRequest1 = ItemRequestDto.builder()
                .title("Searchable Item One")
                .description("First searchable")
                .pricePerDay(80)
                .maxPeriodDays(5)
                .city("Search City")
                .street("Search Street")
                .phone("111222333")
                .viber("111222333")
                .build();
        MockMultipartFile file1 = new MockMultipartFile(
                "image",
                "search1.jpg",
                "image/jpeg",
                "dummy".getBytes(StandardCharsets.UTF_8)
        );
        itemService.create(itemRequest1, testUser, List.of(file1));

        ItemRequestDto itemRequest2 = ItemRequestDto.builder()
                .title("Another Searchable Item")
                .description("Second searchable")
                .pricePerDay(90)
                .maxPeriodDays(6)
                .city("Search City")
                .street("Search Street")
                .phone("444555666")
                .viber("444555666")
                .build();
        MockMultipartFile file2 = new MockMultipartFile(
                "image",
                "search2.jpg",
                "image/jpeg",
                "dummy".getBytes(StandardCharsets.UTF_8)
        );
        itemService.create(itemRequest2, testUser, List.of(file2));

        List<ItemTitle> titles = itemService.searchTitles("Searchable", 10);
        assertNotNull(titles, "Titles list should not be null");
        assertTrue(titles.size() >= 2, "At least two items should be returned");
        titles.forEach(title -> assertTrue(title.getTitle().toLowerCase().contains("searchable"),
                "Title should contain 'searchable'"));
    }

    @Test
    void testSearchByTextWithoutCity() {
        String uniqueText = "UniqueSearchText";
        ItemRequestDto itemRequest = ItemRequestDto.builder()
                .title("Item " + uniqueText)
                .description("Description containing " + uniqueText)
                .pricePerDay(120)
                .maxPeriodDays(8)
                .city("Some City")
                .street("Some Street")
                .phone("777888999")
                .viber("777888999")
                .build();
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "searchText.jpg",
                "image/jpeg",
                "dummy".getBytes(StandardCharsets.UTF_8)
        );
        itemService.create(itemRequest, testUser, List.of(file));

        Page<ItemDto> searchResults = itemService.searchByText(uniqueText, 0, 10, null);
        assertNotNull(searchResults, "Search results should not be null");
        assertFalse(searchResults.getContent().isEmpty(), "Should return at least one item");
        boolean found = searchResults.getContent().stream()
                .anyMatch(item -> item.getTitle().contains(uniqueText));
        assertTrue(found, "The item should be found in search results");
    }
}

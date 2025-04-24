package rs.delimo.item.infrastructure.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.delimo.item.domain.Item;
import rs.delimo.user.domain.User;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {

    void deleteByOwner(User owner);

    @Query("select it from Item it where it.owner.id = ?1")
    List<Item> findByUserId(UUID userId);

    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.images WHERE i.id = :itemId")
    Optional<Item> findByIdWithImages(@Param("itemId") UUID itemId);

    @Query(
            value = "select i from Item i left join fetch i.images",
            countQuery = "select count(i) from Item i"
    )
    Page<Item> findAllWithImages(Pageable pageable);

    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.images WHERE i.id = :itemId and i.owner.id = :ownerId")
    Optional<Item> findOneByUserIdAndItemId(@Param("ownerId") UUID ownerId, @Param("itemId") UUID itemId);

    @Query(
            value = "select i from Item i left join fetch i.images where i.owner.id = :ownerId",
            countQuery = "select count(i) from Item i"
    )
    Page<Item> findAllByOwnerWithImages(Pageable pageable,@Param("ownerId") UUID ownerId);

    @Query("select i from Item i left join fetch i.images " +
            "where upper(i.title) like upper(concat('%', :text, '%'))" +
            " or upper(i.description) like upper(concat('%', :text, '%'))")
    Page<Item> search(Pageable pageable, @Param("text") String text);

    @Query("SELECT DISTINCT i FROM Item i " +
            "LEFT JOIN FETCH i.images " +
            "JOIN i.owner o " +
            "WHERE (:city IS NULL OR o.city = :city) " +
            "AND (upper(i.title) LIKE upper(concat('%', :text, '%')) " +
            "OR upper(i.description) LIKE upper(concat('%', :text, '%')))")
    Page<Item> searchWithCity(@Param("city") String city, @Param("text") String text, Pageable pageable);

    List<Item> findByTitleContainingIgnoreCase(String q, Pageable pageable);

    @Query(
            value = "SELECT i FROM Item i " +
                    "LEFT JOIN FETCH i.images " +
                    "JOIN i.owner o " +
                    "WHERE (:city IS NULL OR o.city = :city) " +
                    "ORDER BY i.created DESC",
            countQuery = "SELECT count(i) FROM Item i JOIN i.owner o " +
                    "WHERE (:city IS NULL OR o.city = :city)"
    )
    Page<Item> findAllWithImagesByCity(@Param("city") String city, Pageable pageable);

}

package rs.delimo.item;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select it from Item it where it.owner = ?1")
    List<Item> findByUserId(Long userId);

    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.images WHERE i.id = :itemId")
    Optional<Item> findByIdWithImages(@Param("itemId") Long itemId);

    @Query(
            value = "select i from Item i left join fetch i.images",
            countQuery = "select count(i) from Item i"
    )
    Page<Item> findAllWithImages(Pageable pageable);

    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.images WHERE i.id = :itemId and i.owner.id = :ownerId")
    Optional<Item> findOneByUserIdAndItemId(@Param("ownerId") Long ownerId, @Param("itemId") Long itemId);

    @Query(
            value = "select i from Item i left join fetch i.images where i.owner.id = :ownerId",
            countQuery = "select count(i) from Item i"
    )
    Page<Item> findAllByOwnerWithImages(Pageable pageable,@Param("ownerId") Long ownerId);
}

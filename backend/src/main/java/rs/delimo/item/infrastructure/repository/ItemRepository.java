package rs.delimo.item.infrastructure.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.delimo.common.valueobject.ItemId;
import rs.delimo.common.valueobject.UserId;
import rs.delimo.item.domain.Item;


import java.lang.ScopedValue;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, ItemId>, JpaSpecificationExecutor<Item> {

    void deleteByIdValue(UUID idValue);

    Optional<Item> findOneByOwnerValueAndIdValue(UUID ownerValue, UUID idValue);

    @EntityGraph(attributePaths = "images")
    Page<Item> findAllByOwnerValue(UUID ownerValue, Pageable pageable);

    @Query("select i from Item i left join fetch i.images " +
            "where upper(i.title) like upper(concat('%', :text, '%'))" +
            " or upper(i.description) like upper(concat('%', :text, '%'))")
    Page<Item> search(@Param("text") String text, Pageable pageable);

    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.images WHERE i.owner.value = :ownerValue AND (:city IS NULL OR :city = :city)")
    Page<Item> searchWithCity(@Param("text") String text,
                              @Param("city") String city,
                              Pageable pageable);

    List<Item> findByTitleContainingIgnoreCase(String q, Pageable pageable);


    @Override
    @EntityGraph(attributePaths = "images")
    Page<Item> findAll(Specification<Item> specification, Pageable pageable);

    @EntityGraph(attributePaths = "images")
    Optional<Item> findByIdValue(UUID idValue);

    void deleteByOwner(UserId id);
}

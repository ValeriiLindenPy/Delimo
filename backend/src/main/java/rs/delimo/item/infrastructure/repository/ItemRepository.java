package rs.delimo.item.infrastructure.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import rs.delimo.common.valueobject.ItemId;
import rs.delimo.common.valueobject.UserId;
import rs.delimo.item.domain.Item;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, ItemId>, JpaSpecificationExecutor<Item> {

    @EntityGraph(attributePaths = "images")
    Page<Item> findAllByOwner(UserId userId, Pageable pageable);

    List<Item> findByTitleContainingIgnoreCase(String q, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "images")
    Page<Item> findAll(Specification<Item> specification, Pageable pageable);

    void deleteByOwner(UserId id);

    @EntityGraph(attributePaths = "images")
    Optional<Item> findOneByOwnerAndId(UserId userId, ItemId id);
}

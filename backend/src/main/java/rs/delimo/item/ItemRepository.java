package rs.delimo.item;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select it from Item it where it.owner = ?1")
    List<Item> findByUserId(Long userId);

    @Query(
            value = "select i from Item i left join fetch i.image",
            countQuery = "select count(i) from Item i"
    )
    Page<Item> findAllWithImages(Pageable pageable);

}

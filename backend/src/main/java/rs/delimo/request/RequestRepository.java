package rs.delimo.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<ItemRequest, Long> {

    Page<ItemRequest> findAllByRequesterId(Long id, Pageable pageable);

    Optional<ItemRequest> findByIdAndRequesterId(Long id, Long requesterId);

    @Query("select ir from ItemRequest ir " +
            "where upper(ir.title) like upper(concat('%', :text, '%'))" +
            " or upper(ir.description) like upper(concat('%', :text, '%'))")
    Page<ItemRequest> search(Pageable pageable, @Param("text") String text);
}

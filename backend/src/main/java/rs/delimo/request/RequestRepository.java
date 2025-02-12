package rs.delimo.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.delimo.user.User;

import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<ItemRequest, Long> {

    void deleteByRequester(User requester);

    Page<ItemRequest> findAllByRequesterId(Long id, Pageable pageable);

    Optional<ItemRequest> findByIdAndRequesterId(Long id, Long requesterId);

    @Query("select ir from ItemRequest ir " +
            "where upper(ir.title) like upper(concat('%', :text, '%'))" +
            " or upper(ir.description) like upper(concat('%', :text, '%'))")
    Page<ItemRequest> search(Pageable pageable, @Param("text") String text);

    @Query("SELECT ir FROM ItemRequest ir " +
            "JOIN ir.requester r " +
            "WHERE (:city IS NULL OR r.city = :city) " +
            "AND (upper(ir.title) LIKE upper(concat('%', :text, '%')) " +
            "OR upper(ir.description) LIKE upper(concat('%', :text, '%')))")
    Page<ItemRequest> searchWithCity(Pageable pageable, @Param("text") String text, @Param("city") String city);

    @Query("SELECT ir FROM ItemRequest ir " +
            "WHERE ir.requester.city = :city")
    Page<ItemRequest> findAllByCity(Pageable pageable, @Param("city") String city);
}

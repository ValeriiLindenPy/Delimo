package rs.delimo.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<ItemRequest, Long> {

    Page<ItemRequest> findAllByRequesterId(Long id, Pageable pageable);


    Optional<ItemRequest> findByIdAndRequesterId(Long id, Long requesterId);
}

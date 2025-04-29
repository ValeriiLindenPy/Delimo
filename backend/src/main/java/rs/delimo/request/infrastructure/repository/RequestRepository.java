package rs.delimo.request.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.delimo.common.valueobject.RequestId;
import rs.delimo.common.valueobject.UserId;
import rs.delimo.request.domain.ItemRequest;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RequestRepository extends JpaRepository<ItemRequest, RequestId>, JpaSpecificationExecutor<ItemRequest> {

    void deleteByRequester(UserId requester);

    Page<ItemRequest> findAllByRequester(UserId id, Pageable pageable);

    Optional<ItemRequest> findByIdAndRequester(RequestId id, UserId requesterId);
}

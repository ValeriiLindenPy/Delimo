package rs.delimo.request.domain.specification;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import rs.delimo.api.dto.RequestFilterDto;
import rs.delimo.common.client.UserClient;
import rs.delimo.common.valueobject.UserId;
import rs.delimo.request.domain.ItemRequest;
import rs.delimo.request.domain.ItemRequest_;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class RequestSpecification {
    private final UserClient userClient;

    public Specification<ItemRequest> from(RequestFilterDto filter) {
        Specification<ItemRequest> specification = Specification.where(null);

        if (filter.getRequesterId() != null ) {
            specification = specification.and((root, cq, cb)
                    -> cb.equal(root.get(ItemRequest_.REQUESTER), new UserId(filter.getRequesterId())));
        }

        if (StringUtils.hasText(filter.getCity())) {
            Set<UserId> requesters = userClient.findIdsByCity(filter.getCity().trim());

            if (requesters.isEmpty()) {
                return (root, cq, cb) -> cb.disjunction();
            }
            specification = specification
                    .and((root, cq, cb) ->
                            root.get(ItemRequest_.REQUESTER).in(requesters));
        }


        if (StringUtils.hasText(filter.getText())) {
            String text = filter.getText().toLowerCase();
            String regEx = "%" + text + "%";
            specification = specification.and((root, cq, cb) -> {
                Predicate descriptionContain = cb.like(cb.lower(root.get(ItemRequest_.DESCRIPTION)), regEx);
                Predicate titleContain = cb.like(cb.lower(root.get(ItemRequest_.TITLE)), regEx);

                return cb.or(descriptionContain, titleContain);
            });
        }

        return specification;
    }
}

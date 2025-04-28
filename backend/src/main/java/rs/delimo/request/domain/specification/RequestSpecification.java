package rs.delimo.request.domain.specification;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import rs.delimo.api.dto.RequestFilterDto;
import rs.delimo.request.domain.ItemRequest;
import rs.delimo.request.domain.ItemRequest_;
import rs.delimo.user.domain.User;
import rs.delimo.user.domain.User_;
import java.util.UUID;

@Component
public class RequestSpecification {

    public static Specification<ItemRequest> from(RequestFilterDto filter) {
        Specification<ItemRequest> specification = Specification.where(null);

        if (filter.getRequesterId().isPresent()) {
            specification = specification.and((root, cq, cb)
                    -> cb.equal(root.get(ItemRequest_.REQUESTER).get("value"), filter.getRequesterId()));
        }

        if (StringUtils.hasText(filter.getCity().get())) {
            String city = filter.getCity().get();
            specification = specification.and((root, cq, cb)
                    -> {
                Subquery<UUID> subquery = cq.subquery(UUID.class);
                Root<User> userRoot = subquery.from(User.class);
                subquery.select(userRoot.get(User_.id).get("value"))
                        .where(cb.equal(userRoot.get(User_.CITY), city));

                return root.get(ItemRequest_.REQUESTER).in(subquery);

            });
        }

        if (StringUtils.hasText(filter.getText().get())) {
            String text = filter.getText().get().toLowerCase();
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

package rs.delimo.item.domain.specification;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import rs.delimo.api.dto.ItemFilterDto;
import rs.delimo.common.valueobject.UserId;
import rs.delimo.item.domain.Item;
import rs.delimo.item.domain.Item_;
import rs.delimo.request.domain.ItemRequest_;
import rs.delimo.user.domain.User;
import rs.delimo.user.domain.User_;

import java.util.UUID;

@Component
public class ItemSpecifications {

    public static Specification<Item> from(ItemFilterDto filter) {
        Specification<Item> specification = Specification.where(null);

        if (filter.getOwnerId() != null) {
            specification = specification.and((root, cq, cb)
                    -> cb.equal(root.get(Item_.OWNER), new UserId(filter.getOwnerId())));
        }

        if (StringUtils.hasText(filter.getCity())) {
            String city = filter.getCity();
            specification = specification.and((root, cq, cb) -> {
                Subquery<UUID> subquery = cq.subquery(UUID.class);
                Root<User> userRoot = subquery.from(User.class);
                subquery.select(userRoot.get(User_.id).get("value"))
                        .where(cb.equal(userRoot.get(User_.CITY), city));

                return root
                        .get(Item_.OWNER)
                        .get("value")
                        .in(subquery);
            });
        }

        if (StringUtils.hasText(filter.getText())) {
            String text = filter.getText().toLowerCase();
            String regEx = "%" + text + "%";
            specification = specification.and((root, cq, cb) -> {
                Predicate descriptionContain = cb.like(cb.lower(root.get(Item_.DESCRIPTION)), regEx);
                Predicate titleContain = cb.like(cb.lower(root.get(Item_.TITLE)), regEx);

                return cb.or(descriptionContain, titleContain);
            });
        }

        return specification;
    }

}

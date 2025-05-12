package rs.delimo.item.domain.specification;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import rs.delimo.api.dto.ItemFilterDto;
import rs.delimo.common.client.UserClient;
import rs.delimo.common.valueobject.UserId;
import rs.delimo.item.domain.Item;
import rs.delimo.item.domain.Item_;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class ItemSpecifications {
    private final UserClient userClient;

    public Specification<Item> from(ItemFilterDto filter) {
        Specification<Item> specification = Specification.where(null);

        if (filter.getOwnerId() != null) {
            specification = specification.and((root, cq, cb)
                    -> cb.equal(root.get(Item_.OWNER), new UserId(filter.getOwnerId())));
        }

        if (StringUtils.hasText(filter.getCity())) {
            Set<UserId> owners = userClient.findIdsByCity(filter.getCity().trim());

            if (owners.isEmpty()) {
                return (root, cq, cb) -> cb.disjunction();
            }
            specification = specification
                    .and((root, cq, cb) ->
                            root.get(Item_.OWNER).in(owners));
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

package rs.delimo.email.infrastructure.mapper;

import org.mapstruct.Mapper;
import rs.delimo.api.dto.FeedbackDto;
import rs.delimo.email.domain.FeedBack;

@Mapper(componentModel = "spring")
public interface FeedBackMapper {
    FeedBack toFeedBack(FeedbackDto dto);
}

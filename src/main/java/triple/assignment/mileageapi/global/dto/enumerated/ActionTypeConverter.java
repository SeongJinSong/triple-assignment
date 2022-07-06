package triple.assignment.mileageapi.global.dto.enumerated;

import triple.assignment.mileageapi.global.error.ErrorCode;
import triple.assignment.mileageapi.global.error.exception.InvalidActionTypeException;

import javax.persistence.AttributeConverter;
import java.util.Arrays;

public class ActionTypeConverter implements AttributeConverter<ActionType, String> {
    @Override
    public String convertToDatabaseColumn(ActionType attribute) {
        return attribute != null ? attribute.toString().toLowerCase() : null;
    }

    @Override
    public ActionType convertToEntityAttribute(String dbData) {
        return Arrays.stream(ActionType.values())
                .filter(e -> e.toString().equalsIgnoreCase(dbData))
                .findFirst()
                .orElseThrow(() -> new InvalidActionTypeException(ErrorCode.INVALID_ACTION_TYPE));
    }
}

package triple.assignment.mileageapi.review.domain.enumerated;

import com.fasterxml.jackson.annotation.JsonCreator;
import triple.assignment.mileageapi.global.error.ErrorCode;
import triple.assignment.mileageapi.global.error.exception.InvalidActionTypeException;

import java.util.Arrays;

public enum ActionType {
    ADD, MOD, DELETE, ;

    /**
     * @param valueFromClient 사용자의 요청 역직렬화 시 역직렬화 할 대상
     * @return 맞는 타입의 ActionType 리턴
     * @throws InvalidActionTypeException 맞는 타입이 없는 경우
     */
    @JsonCreator
    public static ActionType typeOf(String valueFromClient) {
        if (valueFromClient == null) {
            return null;
        }
        return Arrays.stream(ActionType.values())
                .filter(e -> e.toString().equalsIgnoreCase(valueFromClient))
                .findFirst()
                .orElseThrow(() -> new InvalidActionTypeException(ErrorCode.INVALID_ACTION_TYPE));
    }

}

package in.kenz.travelagency.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {

    private boolean success;
    private String message;
    private T data;

    public static <T> CommonResponse<T> success() {
        return new CommonResponse<>(true, null, null);
    }

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(true, null, data);
    }

    public static <T> CommonResponse<T> failure(String message) {
        return new CommonResponse<>(false, message, null);
    }
}
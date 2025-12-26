package in.kenz.travelagency.common.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> {

    private boolean success;
    private String message;
    private T data;
}
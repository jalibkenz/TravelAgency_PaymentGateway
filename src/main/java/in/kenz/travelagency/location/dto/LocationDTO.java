package in.kenz.travelagency.location.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LocationDTO {

    private UUID id;
    private String name;
    private String type;
    private UUID parentId;
}
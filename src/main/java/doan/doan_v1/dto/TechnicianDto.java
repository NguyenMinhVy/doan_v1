package doan.doan_v1.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
public class TechnicianDto {
    private Integer id;
    private String technicianCode;
    private UserDto userDto;
    private List<LocationDto> locationDtos;
    private Integer userId;

    public void addLocation(LocationDto locationDto) {
        if (this.locationDtos == null) {
            this.locationDtos = new ArrayList<>();
        }
        this.locationDtos.add(locationDto);
    }

    public void removeLocation(LocationDto locationDto) {
        if (this.locationDtos != null) {
            this.locationDtos.removeIf(loc -> loc.getId() == locationDto.getId());
        }
    }

    public boolean hasLocation(Integer locationId) {
        if (this.locationDtos == null) {
            return false;
        }
        return this.locationDtos.stream().anyMatch(loc -> loc.getId()==locationId);
    }
}

package teamseven.echoeco.user.domain.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import teamseven.echoeco.user.domain.Role;
import teamseven.echoeco.user.domain.User;

@Getter
@Setter
public class UserDto {

    private String name;

    private String email;

    private String picture;

    @NotNull
    private Role role;

    public static UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.name = user.getName();
        userDto.email = user.getEmail();
        userDto.picture = user.getPicture();
        userDto.role = user.getRole();

        return userDto;
    }

    public void updateUserForm(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
        this.role = user.getRole();
    }
}

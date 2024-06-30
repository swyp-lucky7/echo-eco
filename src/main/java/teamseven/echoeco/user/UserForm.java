package teamseven.echoeco.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {

    private String name;

    private String email;

    private String picture;

    private Role role;

    public void updateUserForm(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
        this.role = user.getRole();
    }
}

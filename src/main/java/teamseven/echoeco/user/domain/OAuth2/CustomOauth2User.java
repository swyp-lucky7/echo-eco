package teamseven.echoeco.user.domain.OAuth2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import teamseven.echoeco.user.domain.Dto.UserDto;

public class CustomOauth2User implements OAuth2User {

    private final UserDto userDto;

    public CustomOauth2User(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add((GrantedAuthority) () -> userDto.getRole().getKey());

        return collection;
    }

    @Override
    public String getName() {
        return userDto.getName();
    }

    public String getEmail() {
        return userDto.getEmail();
    }
}

package teamseven.echoeco.user.domain.OAuth2;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;
import teamseven.echoeco.user.domain.Dto.UserDto;

@Getter
public class Oauth2UserImpl extends DefaultOAuth2User {

    UserDto userDto;  // 토큰 만들기 위해 UserDto 객체를 담아서 전달

    public Oauth2UserImpl(Collection<? extends GrantedAuthority> authorities,
                          Map<String, Object> attributes, String nameAttributeKey,
                          UserDto userDto) {
        super(authorities, attributes, nameAttributeKey);
        this.userDto = userDto;
    }
}
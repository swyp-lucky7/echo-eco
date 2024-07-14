package teamseven.echoeco.util;

import org.springframework.security.core.Authentication;
import teamseven.echoeco.user.domain.OAuth2.CustomOauth2User;

public class GetUserEmail {

    public static String get(Authentication authentication) {
        CustomOauth2User principal = (CustomOauth2User) authentication.getPrincipal();
        return principal.getEmail();
    }
}

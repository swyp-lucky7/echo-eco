package teamseven.echoeco.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import teamseven.echoeco.user.service.CustomOAuth2UserService;
import teamseven.echoeco.user.domain.Dto.UserDto;
import teamseven.echoeco.user.domain.Role;
import teamseven.echoeco.user.domain.User;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final CustomOAuth2UserService customOAuth2UserService;

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", customOAuth2UserService.findUsers());
        model.addAttribute("roles", Role.getAll());
        return "users/userList";
    }

    @GetMapping("/users/{userId}/edit")
    public String updateUserRoleForm(@PathVariable("userId") Long userId, Model model) {
        User user = customOAuth2UserService.findOneById(userId);
        UserDto userDto = new UserDto();
        userDto.updateUserForm(user);
        model.addAttribute("userDto", userDto);
        model.addAttribute("roles", Role.getAll());
        return "users/updateUserRole";
    }

    @PostMapping("/users/{userId}/edit")
    public String updateUserRole(@PathVariable("userId") Long userId,
                                 @Valid @ModelAttribute("userDto") UserDto userDto) {
        customOAuth2UserService.updateUserRole(userId, userDto);
        return "redirect:/users";
    }

}

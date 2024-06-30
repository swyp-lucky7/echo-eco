package teamseven.echoeco.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import teamseven.echoeco.login.CustomOAuth2UserService;

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
        UserForm userForm = new UserForm();
        userForm.updateUserForm(user);
        model.addAttribute("userForm", userForm);
        model.addAttribute("roles", Role.getAll());
        return "users/updateUserRole";
    }

    @PostMapping("/users/{userId}/edit")
    public String updateUserRole(@PathVariable("userId") Long userId,
                                 @Validated @ModelAttribute("userForm") UserForm userForm) {
        customOAuth2UserService.updateUserRole(userId, userForm);
        return "redirect:/users";
    }

}

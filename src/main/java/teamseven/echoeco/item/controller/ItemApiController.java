package teamseven.echoeco.item.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.config.exception.NotFoundCharacterUserException;
import teamseven.echoeco.item.domain.Item;
import teamseven.echoeco.item.domain.dto.ItemClickResponse;
import teamseven.echoeco.item.domain.dto.ItemRequest;
import teamseven.echoeco.item.domain.dto.ItemPickResponse;
import teamseven.echoeco.item.domain.dto.ItemResponse;
import teamseven.echoeco.item.service.ItemService;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.service.UserService;

@Controller
@RequiredArgsConstructor
@ResponseBody
public class ItemApiController {
    private final ItemService itemService;
    private final UserService userService;

    @GetMapping("/item/list")
    public ApiResponse<List<ItemResponse>> itemList(@RequestParam(required = false, name = "isUse") Boolean isUse) {
        return ApiResponse.success(itemService.findByIsUse());
    }

    @GetMapping("/item/{itemId}")
    public ApiResponse<ItemClickResponse> itemClick(@PathVariable("itemId") Long itemId, Authentication authentication) {
        User user = userService.getUser(authentication);
        ItemClickResponse itemClickResponse = itemService.clickItem(itemId, user);
        return ApiResponse.success(itemClickResponse);
    }

    @PostMapping("/item/buy")
    public ApiResponse<ItemPickResponse> itemPick(@RequestBody @Valid ItemRequest itemRequest, Authentication authentication) throws NotFoundCharacterUserException {
        long itemId = itemRequest.getItemId();
        User user = userService.getUser(authentication);

        ItemPickResponse itemPickResponse = itemService.pickItem(itemId, user);
        return ApiResponse.success(itemPickResponse);
    }

}

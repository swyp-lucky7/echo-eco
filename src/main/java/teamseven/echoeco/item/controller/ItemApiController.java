package teamseven.echoeco.item.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import teamseven.echoeco.character.domain.CharacterUser;
import teamseven.echoeco.character.service.CharacterService;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.item.domain.Item;
import teamseven.echoeco.item.domain.dto.ItemClickResponse;
import teamseven.echoeco.item.domain.dto.ItemRequest;
import teamseven.echoeco.item.domain.dto.ItemPickResponse;
import teamseven.echoeco.item.domain.dto.ItemResponse;
import teamseven.echoeco.item.service.ItemService;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.domain.UserPoint;
import teamseven.echoeco.user.service.UserPointService;
import teamseven.echoeco.user.service.UserService;

@Controller
@RequiredArgsConstructor
@ResponseBody
public class ItemApiController {
    private final ItemService itemService;
    private final UserService userService;
    private final UserPointService userPointService;
    private final CharacterService characterService;

    @GetMapping("/item/list")
    public ApiResponse<List<ItemResponse>> itemList(@RequestParam(required = false, name = "isUse") Boolean isUse) {
        return ApiResponse.success(itemService.findByIsUse());
    }

    @GetMapping("/item/click")
    public ApiResponse<ItemClickResponse> itemClick(@RequestBody @Valid ItemRequest itemRequest,
                                                    Authentication authentication) {
        long itemId = itemRequest.getItem_id();
        User user = userService.getUser(authentication);
        ItemClickResponse itemClickResponse = itemService.itemClickResponse((long) itemId, user);
        return ApiResponse.success(itemClickResponse);
    }

    // 데이터베이스 설계시 UserItem 을 설계했으나, 사용하지 않고 기능 구현 가능해보임.
    // UserItem 의 기능은 이전 기록 저장(audit) 이라 생각드는데, 없이 구현해도 괜찮을까요?
    @PostMapping("/item/pick")
    public ApiResponse<ItemPickResponse> itemPick(@RequestBody @Valid ItemRequest itemRequest,
                                                  Authentication authentication) {
        long itemId = itemRequest.getItem_id();
        Item byId = itemService.findById(itemId);
        ItemResponse itemResponse = ItemResponse.fromEntity(byId);

        User user = userService.getUser(authentication);
        UserPoint userPoint = userPointService.updateUserPoint(user, -byId.getPrice());
        CharacterUser characterUser = characterService.updateUserCharacter(user, byId.getLevelUp());

        ItemPickResponse itemPickResponse = ItemPickResponse.makeItemPickResponse(itemResponse, userPoint.getUserPoint(), characterUser.getLevel());
        return ApiResponse.success(itemPickResponse);
    }
}
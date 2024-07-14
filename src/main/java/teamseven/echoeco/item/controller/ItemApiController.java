package teamseven.echoeco.item.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.item.domain.dto.ItemResponse;
import teamseven.echoeco.item.service.ItemService;

@Controller
@RequiredArgsConstructor
@ResponseBody
public class ItemApiController {
    private final ItemService itemService;

    @GetMapping("/item/list")
    public ApiResponse<List<ItemResponse>> itemList(@RequestParam(required = false, name = "isUse") Boolean isUse) {
        return ApiResponse.success(itemService.findByIsUse());
    }
}

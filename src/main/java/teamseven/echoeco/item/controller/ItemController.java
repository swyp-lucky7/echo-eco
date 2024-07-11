package teamseven.echoeco.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.item.domain.Item;
import teamseven.echoeco.item.domain.dto.ItemDto;
import teamseven.echoeco.item.service.ItemService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/item")
public class ItemController {
    private final ItemService itemService;

    @GetMapping("")
    public String items(Model model) {
        model.addAttribute("items", itemService.findAllItems());

        return "admin/item/read";
    }

    @GetMapping("/create")
    public String itemPageCreate(Model model) {
        model.addAttribute("item", Item.empty());
        return "admin/item/create";
    }

    @PostMapping("/create")
    @ResponseBody
    public ApiResponse<String> createItem(@Valid @RequestParam ItemDto itemDto) {
        System.out.println("test" + itemDto);
        Item item = itemDto.toEntity();
        itemService.saveItem(item);
        return ApiResponse.success("ok");
    }

    @GetMapping("/{id}/edit")
    public String updateItemPageCreate(Model model, @PathVariable Long id) {
        Item item = itemService.findById(id);
        model.addAttribute("item", item);
        return "admin/item/create";
    }

    @GetMapping("{id}/delete")
    public String deleteItem(@PathVariable Long id, Model model) {
        itemService.deleteById(id);
        model.addAttribute("items", itemService.findAllItems());
        return "admin/item/read";
    }
}

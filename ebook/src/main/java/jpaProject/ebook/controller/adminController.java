package jpaProject.ebook.controller;

import jpaProject.ebook.domain.Member;
import jpaProject.ebook.domain.Order;
import jpaProject.ebook.domain.item.Item;
import jpaProject.ebook.repository.OrderSearch;
import jpaProject.ebook.service.ItemService;
import jpaProject.ebook.service.MemberService;
import jpaProject.ebook.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class adminController {
    private final MemberService memberService;
    private final ItemService itemService;
    private final OrderService orderService;

    @GetMapping("/members")
    public String memberList(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "admin/member";
    }

    @GetMapping("/items")
    public String itemList(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "admin/item";
    }

    @GetMapping("/orders")
    public String orderList(Model model) {
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "admin/orderList";
    }
}

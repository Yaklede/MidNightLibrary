package jpaProject.ebook.controller;

import jpaProject.ebook.domain.Member;
import jpaProject.ebook.domain.Order;
import jpaProject.ebook.domain.OrderStatus;
import jpaProject.ebook.domain.SessionConst;
import jpaProject.ebook.domain.item.Item;
import jpaProject.ebook.repository.MemberRepository;
import jpaProject.ebook.repository.OrderRepository;
import jpaProject.ebook.repository.OrderSearch;
import jpaProject.ebook.service.ItemService;
import jpaProject.ebook.service.MemberService;
import jpaProject.ebook.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    @PostMapping("/order")
    public String order(@RequestParam("itemId") Long itemId,@RequestParam("memberId") Long memberId){
        Long order = orderService.order(memberId,itemId);

        return "redirect:/orders";
    }
    //테스트로 일단 메인페이지로 이동; 5/25 => 주문 조회 페이지 제작

    @GetMapping("/orders")
    public String orderList(Model model) {
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "redirect:/";
    }


}

package jpaProject.ebook.controller;

import jpaProject.ebook.domain.Member;
import jpaProject.ebook.domain.Order;
import jpaProject.ebook.domain.OrderItem;
import jpaProject.ebook.domain.file.FileEntity;
import jpaProject.ebook.domain.file.FileRepository;
import jpaProject.ebook.domain.item.Item;
import jpaProject.ebook.repository.MemberRepository;
import jpaProject.ebook.repository.OrderRepository;
import jpaProject.ebook.service.ItemService;
import jpaProject.ebook.service.MemberService;
import jpaProject.ebook.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.plaf.BorderUIResource;
import javax.validation.Valid;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class userController {

    private final MemberService memberService;
    private final OrderService orderService;
    private final ItemService itemService;

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final FileRepository fileRepository;

    @GetMapping("/user/orderList/{memberId}")
    public String userOrderList(@PathVariable("memberId") Long memberId, Model model) {
        Member member = memberService.findOne(memberId);
        List<Order> byMemberOrder = orderService.findByMemberOrder(memberId);

        ArrayList<Item> items = new ArrayList<>();
        for (Order order : byMemberOrder) {
            List<OrderItem> orderItemBuys = orderRepository.findOrderItemBuys(order.getId());
            for (OrderItem orderItemBuy : orderItemBuys) {
                items.add(orderItemBuy.getItem());
            }
        }

        model.addAttribute("items", items);
        model.addAttribute("orders", byMemberOrder);
        model.addAttribute("member", member);
        return "deluxe/UserOrders";
    }

    @GetMapping("/user/view/{itemId}/{memberId}")
    public String userView(@PathVariable("itemId") Long itemId,@PathVariable("memberId") Long memberId,Model model) {
        Item item = itemService.findOne(itemId);
        Member member = memberRepository.findById(memberId);
        long imageId = itemId + 1;
        FileEntity image = fileRepository.findOne(imageId);

        List<Order> byMemberOrder = orderService.findByMemberOrder(memberId);
        for (Order order : byMemberOrder) {
            List<OrderItem> orderItemBuys = orderRepository.findOrderItemBuys(order.getId());
            for (OrderItem orderItemBuy : orderItemBuys) {
                Order buyOrder = orderItemBuy.getOrder();
                model.addAttribute("order", buyOrder);
            }
        }

        model.addAttribute("item", item);
        model.addAttribute("image", image);
        model.addAttribute("member", member);
        return "deluxe/Book-Read";
    }

    @GetMapping("/user/edit/{memberId}")
    public String userEdit(@PathVariable("memberId") Long memberId, Model model) {
        Member member = memberService.findOne(memberId);
        model.addAttribute("member", member);
        return "user/edit";
    }

    @PostMapping("/user/edit/{memberId}")
    public String userEditAction(@Valid @ModelAttribute("member") Member member, @PathVariable("memberId") Long memberId, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "user/edit";
        }
        HttpSession session = request.getSession(false);
        if(session != null) {
            memberService.update(memberId, member.getNickName(), member.getPassword());
            session.invalidate();
            return "redirect:/";
        }

        return "redirect:/";
    }

    @PostMapping("/user/orderCancel/{orderId}")
    public String postUserOrderCancel(@PathVariable("orderId") Long orderId) {
        Order order = orderRepository.findOne(orderId);
        orderRepository.delete(order);
        return "redirect:/user/orderList/" + order.getMember().getId();
    }
    @GetMapping("/user/orderCancel/{orderId}")
    public String getUserOrderCancel(@PathVariable("orderId") Long orderId) {
        Order order = orderRepository.findOne(orderId);
        orderRepository.delete(order);
        return "redirect:/user/orderList/" + order.getMember().getId();
    }
    //내가 배울 때 조회 > Get 삭제 수정 > post  (영한이 형이 모든것이 http에서 이렇게 쓰는게 보안이 좋대)

    @GetMapping("/test")
    public String testPage() {
        return "deluxe/testPage";
    }
}

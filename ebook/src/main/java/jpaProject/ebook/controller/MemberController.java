package jpaProject.ebook.controller;


import jpaProject.ebook.domain.Member;
import jpaProject.ebook.domain.SessionConst;
import jpaProject.ebook.domain.form.LoginFrom;
import jpaProject.ebook.domain.item.Item;
import jpaProject.ebook.exception.NotEnoughStockException;
import jpaProject.ebook.repository.OrderSearch;
import jpaProject.ebook.service.ItemService;
import jpaProject.ebook.service.MemberService;
import jpaProject.ebook.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final ItemService itemService;
    private final OrderService orderService;

    @GetMapping("/")
    @Nullable
    public String home(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            List<Item> items = itemService.findItems();
            model.addAttribute("items", items);
            return "deluxe/index";
        }

        log.info("admin Test ==== {}", loginMember.getLoginId());

        if(loginMember.getLoginId().equals("admin")) {
            log.info("admin = {}", loginMember.getLoginId());
            List<Item> items = itemService.findItems();
            model.addAttribute("items", items);
            model.addAttribute("member", loginMember);
            return "deluxe/loginAdminIndex";
        }

        List<Item> itemList = itemService.findItems();
        model.addAttribute("items", itemList);
        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "deluxe/loginUserIndex";
    }
    @GetMapping("/add")
    public String addForm(@ModelAttribute Member member) {

        return "member/pages-sign-up";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute Member member, BindingResult bindingResult,Model model) {

        if (bindingResult.hasErrors()) {
            return "/member/pages-sign-up";
        }
        try {
            memberService.join(member);
        } catch (NotEnoughStockException e) {
            String message = e.getMessage();
            model.addAttribute("message", message);
            return "member/pages-sign-up";
        }

        return "redirect:/login";
    }

    @PostMapping("/members/delete/{memberId}")
    public String memberDelete(@PathVariable("memberId") Long memberId) {
        Member findMember = memberService.findOne(memberId);
        memberService.delete(findMember);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginFrom loginFrom) {

        return "member/pages-sign-in";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginFrom loginFrom, BindingResult bindingResult, HttpServletRequest request, HttpSession httpSession)  {
        if (bindingResult.hasErrors()) {
            return "member/pages-sign-in";
        }
        Member loginMember = memberService.login(loginFrom.getLoginId(), loginFrom.getPassword());
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "member/pages-sign-in";
        }

        //로그인 성공 처리
        //세션 생성
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String postLogout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String getLogout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

}

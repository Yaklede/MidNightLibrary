package jpaProject.ebook.controller;

import jpaProject.ebook.domain.Member;
import jpaProject.ebook.domain.file.FileEntity;
import jpaProject.ebook.domain.file.FileRepository;
import jpaProject.ebook.domain.file.FileService;
import jpaProject.ebook.domain.item.Item;
import jpaProject.ebook.repository.MemberRepository;
import jpaProject.ebook.repository.OrderRepository;
import jpaProject.ebook.service.ItemService;
import jpaProject.ebook.service.MemberService;
import jpaProject.ebook.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final FileService fileService;
    private final FileRepository fileRepository;
    private final MemberRepository memberRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final MemberService memberService;


    @GetMapping("/new")
    public String addItem(@ModelAttribute("form") ItemForm form) {
        return "deluxe/Book-Upload";
    }

    @PostMapping("/new")
    public String newItem(@Valid @ModelAttribute("form") ItemForm form,BindingResult bindingResult,@ModelAttribute("file") MultipartFile file) throws IOException  {

        if(bindingResult.hasErrors()) {
            return "deluxe/Book-Upload";
        }

        Item item = new Item(form.getTitle(), form.getPrice(), form.getAuthor(), form.getIsbn(),form.getContent());

        itemService.itemSave(item);
        fileService.saveFile(file);

        return "redirect:/";
    }

    @GetMapping("/List/{itemId}/{memberId}")
    public String itemView(@PathVariable("itemId") Long itemId,@PathVariable("memberId")Long memberId, Model model) {
        Item item = itemService.findOne(itemId);
        Member member = memberRepository.findById(memberId);
        long imageId = itemId + 1;
        FileEntity image = fileRepository.findOne(imageId);
        model.addAttribute("item", item);
        model.addAttribute("image", image);
        model.addAttribute("member", member);

        return "deluxe/Book-Detail";
    }

    @ResponseBody
    @GetMapping("/image/{fileId}")
    public Resource downloadImage(@PathVariable("fileId") Long id, Model model) throws IOException {
        FileEntity file = fileRepository.findOne(id);
        if (file == null) {
            return null;
        }
        UrlResource resource = new UrlResource("file:" + file.getSavePath());
        log.info("resource = {}", resource);
        return resource;
    }


    @GetMapping("/edit/{itemId}")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemService.findOne(itemId);
        Long fileId = itemId + 1;
        FileEntity file = fileRepository.findOne(fileId);
        model.addAttribute("item", item);
        model.addAttribute("file", file);
        return "deluxe/Book-Update";
    }


    @PostMapping("/edit/{itemId}")
    public String updateItem(@PathVariable("itemId") Long itemId,@Valid @ModelAttribute("item") Item item, BindingResult bindingResult, @ModelAttribute("file") MultipartFile file) throws IOException {

        if (bindingResult.hasErrors()) {
            return "deluxe/Book-Update";
        }
        itemService.updateItem(itemId,item.getTitle(), item.getAuthor(), item.getIsbn(), item.getPrice(),item.getContent());
        long fileId = itemId + 1;
        FileEntity findFile = fileRepository.findOne(fileId);
        fileService.updateFile(findFile,file);
        return "redirect:/";
    }

    @PostMapping("/delete/{itemId}")
    public String deleteItem(@PathVariable("itemId") Long itemId) {

        itemService.deleteItem(itemId);
        return "redirect:/";
    }
}

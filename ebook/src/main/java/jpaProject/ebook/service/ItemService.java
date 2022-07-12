package jpaProject.ebook.service;


import jpaProject.ebook.domain.Order;
import jpaProject.ebook.domain.OrderItem;
import jpaProject.ebook.domain.item.Item;
import jpaProject.ebook.repository.ItemRepository;
import jpaProject.ebook.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void itemSave(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long id, String title, String author, String isbn, int price, String content) {
        Item findItem = itemRepository.findOne(id);
        findItem.setTitle(title);
        findItem.setAuthor(author);
        findItem.setIsbn(isbn);
        findItem.setPrice(price);
        findItem.setContent(content);
    }

    @Transactional
    public void deleteItem(Long itemId) {
        Item item = itemRepository.findOne(itemId);
        List<OrderItem> byOrderItem = orderRepository.findByOrderItem(itemId);
        for (OrderItem orderItem : byOrderItem) {
            Long orderId = orderItem.getOrder().getId();
            Order findOrder = orderRepository.findOne(orderId);
            orderRepository.delete(findOrder);
        }
        itemRepository.delete(item);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }

    public List<Item> findItemByMember(Long memberId) {
        return itemRepository.findByMember(memberId);
    }



}

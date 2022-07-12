package jpaProject.ebook.service;

import jpaProject.ebook.domain.Member;
import jpaProject.ebook.domain.Order;
import jpaProject.ebook.domain.OrderItem;
import jpaProject.ebook.domain.item.Item;
import jpaProject.ebook.repository.ItemRepository;
import jpaProject.ebook.repository.MemberRepository;
import jpaProject.ebook.repository.OrderRepository;
import jpaProject.ebook.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long order(Long memberId ,Long itemId) {
        Item findItem = itemRepository.findOne(itemId);
        Member findMember = memberRepository.findById(memberId);
        OrderItem orderItem = OrderItem.createOrderItem(findItem, findItem.getPrice());
        log.info("member = {}", findMember.getId());
        Order order = Order.createOrder(findMember,orderItem);
        order.setOrderDate(LocalDateTime.now());
        orderRepository.save(order);
        return order.getId();
    }
    /*
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }

     */

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<Order> findByMemberOrder(Long memberId) {
        return orderRepository.findByMemberAll(memberId);
    }

}

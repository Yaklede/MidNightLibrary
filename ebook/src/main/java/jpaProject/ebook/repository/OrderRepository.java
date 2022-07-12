package jpaProject.ebook.repository;


import jpaProject.ebook.domain.Member;
import jpaProject.ebook.domain.Order;
import jpaProject.ebook.domain.OrderItem;
import jpaProject.ebook.domain.item.Item;
import jpaProject.ebook.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final MemberRepository memberRepository;

    @PersistenceContext
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }
    @Transactional
    public void delete(Order order) {
        em.remove(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll() {
        return em.createQuery("select o From Order o")
                .getResultList();
        }
    //5월25일 > 회원 목록 별로 가져오게 만들어야 함
    public List<Order> findByMemberAll(Long memberId) {
        return em.createQuery("select o From Order o where o.member.id =: memberId", Order.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public List<OrderItem> findByOrderItem(Long itemId) {
        return em.createQuery("select oi From OrderItem oi where oi.item.id =: itemId",OrderItem.class)
                .setParameter("itemId",itemId)
                .getResultList();
    }

    public List<OrderItem> findOrderItemBuys(Long orderId) {
        return em.createQuery("select oi From OrderItem oi where oi.order.id =: orderId", OrderItem.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }


}

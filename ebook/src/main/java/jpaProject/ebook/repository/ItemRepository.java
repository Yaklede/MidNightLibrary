package jpaProject.ebook.repository;


import jpaProject.ebook.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ItemRepository {

    @PersistenceContext
    private final EntityManager em;

    public void delete(Item item) {
        em.remove(item);
    }
    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i").getResultList();
    }

    public List<Item> findByMember(Long memberId) {
        return em.createQuery("select oi from OrderItem oi where oi.order.member.id := memberId").setParameter("memberId", memberId).getResultList();
    }

}

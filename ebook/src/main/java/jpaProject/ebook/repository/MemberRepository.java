package jpaProject.ebook.repository;

import jpaProject.ebook.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberRepository {

    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public void save(Member member) {
        em.persist(member);
    }

    @Transactional
    public void delete(Member member) {
        em.remove(member);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findByLoginId(String loginId) {
        return em.createQuery("select m from Member m where loginId =: loginId", Member.class)
                .setParameter("loginId", loginId)
                .getResultList();
    }

    public Optional<Member> findByLoginIdOptional(String loginId) {
        List<Member> all = findByLoginId(loginId);
        return all.stream()
                .filter(member -> member.getLoginId().equals(loginId))
                .findFirst();
    }

}

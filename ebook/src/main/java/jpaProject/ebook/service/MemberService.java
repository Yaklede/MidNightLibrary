package jpaProject.ebook.service;

import jpaProject.ebook.domain.Member;
import jpaProject.ebook.exception.NotEnoughStockException;
import jpaProject.ebook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member login(String loginId, String password) {
        Optional<Member> findMember = memberRepository.findByLoginIdOptional(loginId);
        return findMember.filter(m -> m.getPassword().equals(password))
                .orElse(null);

    }
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);//중복 회원 검사
        return member.getId();
    }

    public void delete(Member member) {
        memberRepository.delete(member);
    }

    @Transactional
    public void update(Long id, String NickName, String password) {
        Member member = memberRepository.findById(id);
        member.setNickName(NickName);
        member.setPassword(password);
    }

    private void validateDuplicateMember(Member member) {
        //EXCEPTION
        List<Member> findMembers = memberRepository.findByLoginId(member.getLoginId());
        if (!findMembers.isEmpty()) {
            throw new NotEnoughStockException("이미 존재하는 회원입니다.");
        }
    }
    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //회원 단건 조회
    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

}

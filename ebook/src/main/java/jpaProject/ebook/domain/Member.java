package jpaProject.ebook.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @NotEmpty //필수값
    private String NickName;
    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;

    @OneToMany(mappedBy = "member" ,cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

}

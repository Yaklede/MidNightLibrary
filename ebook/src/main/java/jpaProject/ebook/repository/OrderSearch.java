package jpaProject.ebook.repository;

import jpaProject.ebook.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearch {
    private String title;
    private OrderStatus orderStatus;
}

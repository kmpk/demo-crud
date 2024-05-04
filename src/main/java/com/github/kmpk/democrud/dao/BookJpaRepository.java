package com.github.kmpk.democrud.dao;

import com.github.kmpk.democrud.model.Book;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("JPA")
public interface BookJpaRepository extends JpaRepository<Book,Long> {
}

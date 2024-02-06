package com.example.springBoot.repositories;

import com.example.springBoot.models.Book;
import com.example.springBoot.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByReader(Person reader);

    List<Book> findByTitleStartingWithIgnoreCase(String startString);
}

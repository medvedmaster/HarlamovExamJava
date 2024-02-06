package com.example.springBoot.controllers;

import com.example.springBoot.models.Book;
import com.example.springBoot.models.Person;
import com.example.springBoot.services.BookService;
import com.example.springBoot.services.PeopleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("SpringMVCViewInspection")
@Controller
@RequestMapping("/library/books") // основная ссылка
public class BooksController {
    private final PeopleService peopleService;
    private final BookService bookService;

    @Autowired
    public BooksController(PeopleService peopleService, BookService bookService) {
        this.peopleService = peopleService;
        this.bookService = bookService;
    }

    @GetMapping
    public String showBooks(Model model) // метод вызова всех книг
    {
        List<Book> books; // лист наших книг

        books = bookService.findAll(); // достаем из бд все книги
        model.addAttribute("books", books); // добавляем в MODEL по ключу books для того чтобы использовать во фронте
        return "books/show"; // возвращаем html страницу
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person)
            //этот метод выдает информацию о конкретной книге, на которую мы нажали
    {
        Book book = bookService.findOneById(id);
        model.addAttribute("book", book);
        if (book.getReader() != null){
            model.addAttribute("reader", book.getReader()); //это в случае, если у книги есть читатель
        }else {
            model.addAttribute("people", peopleService.findAll()); // тут у книги нет читателя
        }
        return "books/profile";
    }


    @GetMapping("/new")
    public String addBook(@ModelAttribute("book") Book book)
    //страница для добавления книги
    {
        return "books/new";
    }

    @PostMapping()
    public String createBook(@ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult) //метод для создания новой книги
    {
        if (bindingResult.hasErrors()) return "books/new"; // проверяем на ошибки ввода
        bookService.save(book);
        return "redirect:/library/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model)
            // вывод странички для редактированием книги
    {
        model.addAttribute("book", bookService.findOneById(id)); //добавляем в модель книгу для изменения
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult,
                             @PathVariable("id") int id)
            //метод для изменения книги
    {
        if (bindingResult.hasErrors()) return "books/edit"; // проверяем на ошибки
        bookService.update(id, book);
        return "redirect:/library/books";
    }

    @PatchMapping("/{id}/person")
    public String addBookToPerson(@ModelAttribute("person") Person person,
                                  @PathVariable("id") int id)
            //добавить книгу человеку
    {
        bookService.addBookToPerson(person, id);
        return "redirect:/library/books/{id}";
    }

    @PatchMapping("/{id}/free")
    public String freeBook(@PathVariable("id") int id)
            //освободить книгу
    {
        bookService.freeBook(id);
        return "redirect:/library/books/{id}";
    }

    @GetMapping("/search")
    public String searchBook(@RequestParam(required = false, defaultValue = "") String startString,
                             Model model)
            //метод для поиска книги
    {
        model.addAttribute("startString", startString);
        if (!startString.isEmpty()){
            model.addAttribute("books", bookService.searchBooks(startString));
        }
        return "books/search";
    }


    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id)
            //метод для удаления книги
    {
        bookService.delete(id);
        return "redirect:/library/books";
    }
}

package com.example.springBoot.controllers;

import com.example.springBoot.models.Book;
import com.example.springBoot.models.Person;
import com.example.springBoot.services.BookService;
import com.example.springBoot.services.PeopleService;
import com.example.springBoot.validators.UniquePersonValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/library/people")
public class PeopleController {
    private final PeopleService peopleService;
    private final BookService bookService;

    private final UniquePersonValidator validator;

    @Autowired
    public PeopleController(PeopleService peopleService, BookService bookService, UniquePersonValidator validator) {
        this.peopleService = peopleService;
        this.bookService = bookService;
        this.validator = validator;
    }

    @GetMapping
    public String showPeople(Model model){ // метод для вызова странички с просмотром всех книг
        model.addAttribute("people", peopleService.findAll());
        return "people/show";
    }

    @GetMapping("/{id}")
    public String showPerson(@PathVariable("id") int id, Model model){ // метод для вызова странички с просмотром конкретного человека по его id

        Person person = peopleService.findOneById(id); // объект человека
        List<Book> books = bookService.findByReader(person); // лист принадлежащих ему книг
        model.addAttribute("person", person); // добавляем это для фронта
        if (!books.isEmpty()){
            model.addAttribute("books", books); // тоже для фронта
        }
        return "people/profile"; // выводим страничку
    }


    @GetMapping("/new")
    public String addPerson(@ModelAttribute("person") Person person){ // метод для странички с добавлением нового человека
        return "people/new";
    }

    @PostMapping()
    public String createPerson(@ModelAttribute("person") @Valid Person person,
                             BindingResult bindingResult){ // метод конкретно создания человека
        validator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) return "people/new"; // проверка на ошибки
        peopleService.save(person); // сохранения человека
        return "redirect:/library/people"; // возвращаем страничку со всеми людьми
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){ // вывод странички для изменения человека
        model.addAttribute("person", peopleService.findOneById(id));
        return "people/edit";
    }


    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,
                             BindingResult bindingResult,
                             @PathVariable("id") int id){ // метод конкретно изменения человека
        validator.validate(person, bindingResult);// проверка на ошибки
        if (bindingResult.hasErrors()) return "people/edit"; //проверка на ошибки
        peopleService.update(id, person);
        return "redirect:/library/people";
    }


    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id){ //метод удаления человека
        peopleService.delete(id);
        return "redirect:/library/people";
    }
}

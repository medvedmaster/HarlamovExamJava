package com.example.springBoot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "Book")
@Getter
@Setter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person reader;

    @NotNull(message = "Название не может быть пустым")
    @Pattern(regexp = "[0-9А-ЯЁ][0-9a-zA-Zа-яА-ЯёЁ\\-\\s]+",
            message = "Должен быть формат \"Название\" на русском языке")
    @Column(name = "title")
    private String title;

    @NotNull(message = "Имя автора не может быть пустым")
    @Pattern(regexp = "[0-9А-ЯЁ][0-9a-zA-Zа-яА-ЯёЁ\\-\\s]+",
            message = "Должен быть формат \"Фамилия Имя\" на русском языке")
    @Column(name = "author")
    private String author;

    @Min(value = 1445, message = "Год издания должен быть больше 1445")
    @Column(name = "year")
    private int year;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "taken_at")
    private Date takenAt;

    @Transient
    private boolean isExpired;

    public boolean isExpired(){
        return (new Date().getTime() - takenAt.getTime()) / 86400000 > 10;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return getYear() == book.getYear()
                && getTitle().equals(book.getTitle())
                && getAuthor().equals(book.getTitle());
    }

    @Override
    public int hashCode(){
        return Objects.hash(getTitle(), getAuthor(), getYear());
    }
}

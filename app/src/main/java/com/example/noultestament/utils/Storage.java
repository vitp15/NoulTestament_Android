package com.example.noultestament.utils;

import java.util.ArrayList;

public class Storage {
    private final static Storage instance = new Storage();
    private final ArrayList<Book> books;

    private Storage() {
        books = getALLBooks();
    }

    public static Storage getInstance() {
        return instance;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    private ArrayList<Book> getALLBooks() {
        ArrayList<Book> books = new ArrayList<>();
        books.add(new Book("Matei", 28, 1));
        books.add(new Book("Marcu", 16, 2));
        books.add(new Book("Luca", 24, 3));
        books.add(new Book("Ioan", 21, 4));
        books.add(new Book("Faptele Apostolilor", 28, 5));
        books.add(new Book("Romani", 16, 6));
        books.add(new Book("1 Corinteni", 16, 7));
        books.add(new Book("2 Corinteni", 13, 8));
        books.add(new Book("Galateni", 6, 9));
        books.add(new Book("Efeseni", 6, 10));
        books.add(new Book("Filipeni", 4, 11));
        books.add(new Book("Coloseni", 4, 12));
        books.add(new Book("1 Tesaloniceni", 5, 13));
        books.add(new Book("2 Tesaloniceni", 3, 14));
        books.add(new Book("1 Timotei", 6, 15));
        books.add(new Book("2 Timotei", 4, 16));
        books.add(new Book("Tit", 3, 17));
        books.add(new Book("Filimon", 1, 18));
        books.add(new Book("Evrei", 13, 19));
        books.add(new Book("Iacov", 5, 20));
        books.add(new Book("1 Petru", 5, 21));
        books.add(new Book("2 Petru", 3, 22));
        books.add(new Book("1 Ioan", 5, 23));
        books.add(new Book("2 Ioan", 1, 24));
        books.add(new Book("3 Ioan", 1, 25));
        books.add(new Book("Iuda", 1, 26));
        books.add(new Book("Apocalipsa", 22, 27));
        return books;
    }
}

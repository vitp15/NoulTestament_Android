package com.example.noultestament.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.noultestament.R;

import java.util.ArrayList;
import java.util.Comparator;

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

    public Book getBook(int order) {
        if (order >= 1 && order <= 27) {
            return books.get(order - 1);
        } else {
            return null;
        }
    }

    public void saveCurrentTime(Context context, int order, int chapter, int time) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.MY_APP_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Book book = getBook(order);
        if (book != null) {
            editor.putInt(book.getAudioName(chapter), time);
            editor.apply();
        }
    }

    public int getCurrentTime(Context context, int order, int chapter) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.MY_APP_PREFS, MODE_PRIVATE);
        Book book = getBook(order);
        if (book != null) {
            return sharedPreferences.getInt(book.getAudioName(chapter), 0);
        }
        return 0;
    }

    public void removeCurrentTime(Context context, int order, int chapter) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.MY_APP_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Book book = getBook(order);
        if (book != null) {
            editor.remove(book.getAudioName(chapter));
            editor.apply();
        }
    }

    public void saveForceClosed(Context context, int order, int chapter) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.MY_APP_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.FORCE_CLOSED, order + "_" + chapter);
        editor.apply();
    }

    public String getForceClosed(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.MY_APP_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(Constants.FORCE_CLOSED, "");
    }

    public void removeForceClosed(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.MY_APP_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.FORCE_CLOSED);
        editor.apply();
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
        books.sort(Comparator.comparingInt(Book::getOrder));
        return books;
    }
}

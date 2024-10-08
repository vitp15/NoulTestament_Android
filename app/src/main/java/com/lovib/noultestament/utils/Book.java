package com.lovib.noultestament.utils;

import java.util.HashMap;

public class Book {
    private final String name;
    private final int chapters;
    private final int order;
    private final HashMap<Integer, Boolean> hasNotes;

    public Book(String name, int chapters, int order) {
        this.name = name;
        this.chapters = chapters;
        this.order = order;
        this.hasNotes = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public int getChapters() {
        return chapters;
    }

    public int getOrder() {
        return order;
    }

    public boolean hasNotes(int chapter) {
        return hasNotes.get(chapter) != null && hasNotes.get(chapter);
    }

    public void setHasNotes(int chapter, boolean hasNotes) {
        this.hasNotes.put(chapter, hasNotes);
    }

    public String getAudioName(int chapter) {
        return "b_" + name.replaceAll(" ", "_").toLowerCase() + "_" + String.format("%02d", chapter);
    }
}

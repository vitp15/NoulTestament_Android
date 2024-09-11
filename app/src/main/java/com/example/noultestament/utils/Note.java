package com.example.noultestament.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;

public class Note {
    @SerializedName("character")
    private final char character;
    @SerializedName("atTime")
    private final int atTime;
    @SerializedName("message")
    private String message;

    public Note(char character, int atTime, String message) {
        this.character = character;
        this.atTime = atTime;
        this.message = message;
    }

    public Note(char character, int atTime) {
        this.character = character;
        this.atTime = atTime;
        this.message = "";
    }

    public char getCharacter() {
        return character;
    }

    public int getAtTime() {
        return atTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static String createKey(int order, int chapter) {
        return order + "_" + chapter;
    }

    public static int getOrder(String key) {
        try {
            if (key != null && key.split("_").length == 2) {
                return Integer.parseInt(key.split("_")[0]);
            } else {
                return 0;
            }
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int getChapter(String key) {
        try {
            if (key != null && key.split("_").length == 2) {
                return Integer.parseInt(key.split("_")[1]);
            } else {
                return 0;
            }
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static class NoteDeserializer implements JsonDeserializer<Note> {
        @Override
        public Note deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            char character = jsonObject.get("character").getAsCharacter();
            int atTime = jsonObject.get("atTime").getAsInt();
            String message = jsonObject.get("message").getAsString();
            return new Note(character, atTime, message);
        }
    }
}

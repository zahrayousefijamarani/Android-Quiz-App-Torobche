package com.sharifdev.torobche.model;

import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

/**
 * A Quiz
 */
public class Quiz {
    private String name;
    private List<Object> participants;
    private int time;
    private List<Object> questions;
    public ParseObject quiz;
    public ParseObject currentQ;

    public Quiz(String name, List<Object> participants, int time, List<Object> questions) {
        this.name = name;
        this.participants = participants;
        this.time = time;
        this.questions = questions;
        /*for (Object participant : participants) {
            try {
                System.out.println(((ParseObject) participant).fetchIfNeeded().get("username"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }*/
    }

    public QuizType type;

    public Quiz(ParseObject quiz, String type, ParseObject q) {
        this.quiz = quiz;
        this.currentQ = q;
        switch (type) {
            case "single":
                this.type = QuizType.SINGLE_PLAYER;
                break;
            case "multi":
                this.type = QuizType.MULTI_PLAYER;
                break;
            case "group":
                this.type = QuizType.GROUP_GAME;
                break;
            case "custom":
                this.type = QuizType.CUSTOM;
                break;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Object> participants) {
        this.participants = participants;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<Object> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Object> questions) {
        this.questions = questions;
    }
}

enum QuizType {
    SINGLE_PLAYER,
    MULTI_PLAYER,
    GROUP_GAME,
    CUSTOM
}

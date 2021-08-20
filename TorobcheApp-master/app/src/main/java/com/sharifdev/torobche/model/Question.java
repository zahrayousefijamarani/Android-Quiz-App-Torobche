package com.sharifdev.torobche.model;


import com.parse.ParseException;
import com.parse.ParseObject;
import com.sharifdev.torobche.R;


/**
 * A Question
 */
public class Question {
    String text;
    ParseObject[] choices = new ParseObject[]{};
    String topic;
    String objId;

    public Question(String text, ParseObject choice1,
                    ParseObject choice2,
                    ParseObject choice3,
                    ParseObject choice4,
                    String topic,
                    String objId) {
        this.text = text;
        this.topic = topic;
        this.choices = new ParseObject[]{
                choice1,
                choice2,
                choice3,
                choice4
        };
        this.objId = objId;
    }

    public Question() {
    }

    public String getText() {
        return text;
    }

    public String getTopic() {
        return topic;
    }

    public String getObjId() {
        return objId;
    }

     public String questionText = "";
     public String answerText1 = "";
     public String answerText2 ="";
     public String answerText3 = "";
     public String answerText4 = "";
     public int image1 = R.drawable.no_photo;
     public int image2 = R.drawable.no_photo;
     public int image3 = R.drawable.no_photo;
     public int image4 = R.drawable.no_photo;
     public int questionImage = R.drawable.no_photo;
     public int correctAnswer = 0;
     public int likes = 0;

}

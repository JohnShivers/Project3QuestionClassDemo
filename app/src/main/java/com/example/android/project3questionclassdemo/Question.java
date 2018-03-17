package com.example.android.project3questionclassdemo;

import android.os.Parcel;
import android.os.Parcelable;
import android.transition.Scene;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by John Shivers on 3/15/2018.
 *
 * Demo for Udacity Grow with Google Android Beginner Course Project 3
 */

public class Question {

    // Here we go with the explanation of the Question class and its attributes and constructors!
    // For starters, the following variables are special. They will never change, and are declared
    // final. They will be used as reference variables in other contexts, so we aren't having to
    // remember numbers. If you read the main .java file first, you will have seen these a number
    // of times.
    final static int UNDECLARED = 0;
    final static int MULTIPLE_CHOICE = 1;
    final static int TRUE_OR_FALSE = 2;
    final static int INPUT_ANSWER = 3;
    final static int CHECK_MULTIPLE = 4;

    // This variable is a bit different. It will count up as questions are created so we can reference it
    // when we create our array in the main java file.
    static int totalQuestionsCreated = 0;

    // These attributes are shared by all Questions, so they are in the super class. The superclass
    // is this, which the other question types will inherit these attributes from. Note the question
    // StringId is an ID resource int. Type corresponds with the final declarations above, and questionNumber
    // simply tells the object on the fly what order it is in for numbering.
    int type;
    int questionStringID;

    /***
     * This is the primary constructor, takes the questionStringID as an argument. Should not be used directly,
     * and is only meant to be called from sub-classes.
     ***/
    Question(int questionStringID) {
        this.type = Question.UNDECLARED;
        this.questionStringID = questionStringID;
        totalQuestionsCreated++;
    }

    // This method shuffles the array of Questions. ArrayLists are like enclosures around an array, enabling
    // some functionality, such as shuffling. Or at least its the easiest method to go about doing this, though
    // there are certainly ways to shuffle the array without using ArrayList. Let's ignore this, it's just fluff.
    public static Question[] shuffleQuestions(Question[] questionArray) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < questionArray.length; i++) {
            arrayList.add(questionArray[i]);
        }
        Collections.shuffle(arrayList);
        Question[] shuffledQuestions = (Question[]) arrayList.toArray(new Question[arrayList.size()]);
        return shuffledQuestions;
    }

    //Getter and setters for type and questionStringID.
    public int getType() { return type; }
    public void setType(int type) { this.type = type; }
    public int getQuestionStringID() { return questionStringID; }
    public void setDialogueStringID(int questionStringID) { this.questionStringID = questionStringID; }

    // Fetch the total questions created.
    public static int getTotalQuestionsCreated() { return totalQuestionsCreated; }

}


class MultipleChoice extends Question {

    // The first of our question types, and the one I will cover in the most depth. To begin with, note how it
    // "extends Question". This gives it all of the attributes and methods of the Question class above, plus the MultipleChoice
    // specific stuff setup here. We add the answer (it's actually another String ID even if not labeled), and create an
    // array to hold the possible answer choices. This is important for this question type and dynamic generation.
    int answer;
    int[] answerStringArray;

    /***
     *  Multiple Choice questions are Type 1. They require a questionStringID, the correct answerStringID, and a llow for a
     *  variable int argument for as many answerStringIDs as you would like.
     *  This varying length argument should be limited to no more than 5 answers for practicality's sake.
     */

    MultipleChoice(int questionStringID, int answer, int... answers) {
        // The super() method sends the questionStringID back up the chain to the Question superclass, as the name implies.
        // Essentially, it calls the constructor up on line 36. This may seem unneeded, but with more complex superclasses
        // you will need to use the super() method to not duplicate a ton of code. Notice that type is not an argument of
        // the MC constructor, it is simply declared outright. This serves as an easy identifier for the question type,
        // without delving into methods of determining what type of object it is. It's a shortcut only possilbe pecause of
        // the limited scope of these classes. The answerStringID is self explanatory, so let's consider the array. Notice
        // in the construtor it has int... answers. The int... is a variable argument. Basically, you can pass in as many
        // arguments as you want, and it puts those into ant int array. Well, that is perfect. It gives us a ready made
        // array of all of our potential answers for later!
        super(questionStringID);
        this.type = Question.MULTIPLE_CHOICE;
        this.questionStringID = questionStringID;
        this.answerStringArray = answers;
        this.answer = answer;

    }

    //Getter and setters for MC answerStringArray and answer. Note answer is an int value, not a boolean value like in TF.
    public int[] getAnswerStringArray() { return answerStringArray; }
    public void setAnswerStringArray(int[] answerStringArray) { this.answerStringArray = answerStringArray; }
    public int getAnswer() { return answer; }
    public void setAnswer(int answer) { this.answer = answer; }
}


class TrueOrFalse extends Question {

    boolean answer;

    /***
     *  True or False questions are Type 2. They require a questionStringID and a boolean answer.
     ***/

    // Same concept as the MC subclass, but simpler since we only need a T/F value. Self explanatory.
    TrueOrFalse(int questionStringID, boolean answerTF) {
        super(questionStringID);
        answer = answerTF;
        this.type = Question.TRUE_OR_FALSE;
    }

    //Getter and setter for TF answer. Note this is a BOOLEAN value, NOT an int like MC.
    public boolean getAnswer() { return answer; }
    public void setAnswer(boolean answer) { this.answer = answer; }
}

class InputAnswer extends Question {

    int answerStringID;

    /***
     *  Input Answer questions require the questionStringID and an answerStringID to be compared to player input.
     ***/

    // Also simple compared to MC questions, as we will be creating an EditText view whose text we compare to
    // the answerStringID. I made an error here and had the variable names for anwers inconsistent, but I don't
    // want to break anything so am just leaving it.
    InputAnswer(int questionStringID, int answerStringID) {
        super(questionStringID);
        this.type = Question.INPUT_ANSWER;
        this.answerStringID = answerStringID;
    }

    //Getter and setter for Input answerStringID, which is used to compare against the typed in value.
    public int getAnswerStringID() { return answerStringID; }
    public void setAnswerStringID(int answerStringID) { this.answerStringID = answerStringID; }
}

class CheckMultiple extends Question {

    /***
     *  NOT YET IMPLEMENTED
     */
    CheckMultiple(int questionStringID){
        super(questionStringID);
    }
}
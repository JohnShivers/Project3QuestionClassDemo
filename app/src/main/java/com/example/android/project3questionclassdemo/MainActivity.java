package com.example.android.project3questionclassdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "QuizDemo";

    // A few initial variables.
    int totalQuestions;
    int totalCorrect = 0;
    int totalAnswered = 0;

    // Setting up the Views.
    TextView scoreDisplay;
    LinearLayout primaryView;
    ScrollView scrollView;

    //
    Question[] questionArray;

    // Program control variables
    boolean initiated = false;  // Declares if initiation loop has ran.
    boolean shuffle = false;    // Shuffle questions on/off.
    int maxQuestions = 100;     // Sets maximum number of questions.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Prevents the app from trying to reinitiate everything, crashing it on certain conditions. Not fully functional,
        // and the app must be fully closed out each time. (Reloading state is broken until I implement certain classes for
        // the questions.)
        if (!initiated) {

            // The initalize method is jsut to keep code out of the onCreate method as much as possible.
            initialize();

            // Likewise, separate method to setup the questions.
            setupQuestions();

            // This initializes our total questions, which will equal the length of the array we are creating.
            // Technically in the line above, but you'll see it further down in the code.
            totalQuestions = questionArray.length;

            // This just displays the total number of questions.
            updateScore();

            // Send the scrollView back to the top after we did all that playing with the layout.
            scrollView.scrollTo(0, 0);

            // Take focus away from the EditText vies we created so the keyboard doesn't immediately pop up.
            scrollView.requestFocus();

            // Well, we're initiated.
            initiated = true;
        }

        scrollView.requestFocus();
    }

    private void initialize() {
        // Score view. Not a biggie.
        scoreDisplay = findViewById(R.id.score);

        // Grabs and holds onto the main LinearLayout, since we will be using it as a reference.
        primaryView = findViewById(R.id.primary_view);

        // Likewise for the ScrollView.
        scrollView = findViewById(R.id.scroll_view);

        // These are the constructors for the various questions. Notice that each instantiates an object of the specific question type
        // sub classes, passing in the required variables. Everything is done by reference to string IDs, so its all just numbers behind
        // the scenes. If you need to fix a typo or reword something, just change the string in the xml! Simple as that.
        // Also, note for multiple choice, the constructor is a little more complex. You pass in the question id, the answer id, and then
        // ANY NUMBER of possible answers (though the correct answer should be among them, there is currently no control enforcing this in the
        // code. For practicality's sake, keep it to 3-5.
        MultipleChoice question1 = new MultipleChoice(R.string.question1, R.string.question1b, R.string.question1a, R.string.question1b, R.string.question1c);
        TrueOrFalse question2 = new TrueOrFalse(R.string.question2, false);
        InputAnswer question3 = new InputAnswer(R.string.question3, R.string.question3a);
        InputAnswer question4 = new InputAnswer(R.string.question4, R.string.question4a);
        MultipleChoice question5 = new MultipleChoice(R.string.question5, R.string.question5c,
                R.string.question5a, R.string.question5b, R.string.question5c, R.string.question5d, R.string.question5e);
        TrueOrFalse question6 = new TrueOrFalse(R.string.question6, true);

        // This initiates our array based on total questions created, noted by the static variable in the Question class.
        questionArray = new Question[Question.getTotalQuestionsCreated()];

        // These add the questions to an array of the Question super class (don't worry about arrays, just know that it holds multiple
        // things and is referenced by index starting at 0. Hence question1 being in questionArray[0]. Arrays are a bit more intermediate.
        questionArray[0] = question1;
        questionArray[1] = question2;
        questionArray[2] = question3;
        questionArray[3] = question4;
        questionArray[4] = question5;
        questionArray[5] = question6;

        if (shuffle) questionArray = Question.shuffleQuestions(questionArray);

    }

    // This is where the magic happens programmatically. Each question object is parsed by the code, and the views are dynamically created in the
    // layout! Once its coded once, you never have to hand code a question again, just create the object, stick it in the question array, and you're
    // off to the races!
    private void setupQuestions() {
        // This loops through the questionArray. Dont worry about for loops if you dont understand them yet. Just know it cyclces through the array
        // and handles each question in the array one by one, stopping when the array runs out.
        for (int i = 0; i < questionArray.length; i++) {

            // Note how the array has the int i = 0. This will have the array initialize a variable, i, set to 0. We will use this variable to
            // determine which index of the array to deal with, and at the end of each loop it will increment the i variable, moving to the next
            // index in the array. The format is: for (potential initial variable; when to stop running the loop; what to do at the end of each
            // loop); Note that you don't even need all the three parts of the for loop, but that is a bit more complex.

            // Get current question type. This calls the type, set to a constant int value in the Question class, and stores it for the following
            // code's usage.
            int questionType = questionArray[i].getType();

            // Setup generic views. Parameters are required when setting up a view programmatically, it's equivalent to the XML auto popping up
            // the layout_height and layout_width attributes when you create a new view, as these are required. Don't include them, the code
            // crashes, just as if you tried to not include them in the XML.
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            // just adds margins to the parameters. Note that paramters are required to set any attribute that in the xml begins with layout_.
            // This is to set the margins between each RadioGroup (the rounded blue boxes are the RadioGroups just btw).
            params.setMargins(0, 0, 0, 25);

            // The following is simply a roundabout method in earlier APIs to apply a theme to a view object just to make it pretty, don't
            // worry about it.
            // The important part is to note that a RadioGroup is being created, and the parameters we made above are set.
            final RadioGroup outerView = (RadioGroup) getLayoutInflater().inflate(R.layout.question_holder, null);
            outerView.setLayoutParams(params);

            // The following creates a new TextView for the questionString text, and fetches the stringID from the object
            // we created earlier to set it as the text. Again, just roundabout code because I'm using the pretty
            // template, don't worry about it. The second part fetches the actual String via the String's ID, and places
            // a number before it to mark the question number, then assigns the concataned string to the textView.
            TextView questionView = (TextView) getLayoutInflater().inflate(R.layout.question_template, null);
            String questionText = (i + 1) + ". " + getResources().getString(questionArray[i].getQuestionStringID());
            questionView.setText(questionText);

            // This will add the questionView as a child of the outerView, as if it were nested in the XML.
            outerView.addView(questionView);

            // Setup question type specific views by comparing type to constant. (Literally just an int to an int.)
            if (questionType == Question.MULTIPLE_CHOICE) {
                MultipleChoice currentQuestion = (MultipleChoice) questionArray[i];

                // Cycle through answer array and create a radio button for each. If you don't know how loops work,
                // just know that it will cycle through the question array and run the code for each individual object
                // in the array. It will not create redundant variables because it is a local variable inside the one
                // iteration of the loop.
                for (int j = 0; j < currentQuestion.getAnswerStringArray().length; j++) {

                    // Create and setup the answer radio buttons. It cycles through another inner for loop (note the
                    // j instead of i, as we can't reuse the same variable since the i is still in play!).
                    RadioButton answerChoice = (RadioButton) getLayoutInflater().inflate(R.layout.answer_template, null);
                    answerChoice.setText(currentQuestion.getAnswerStringArray()[j]);
                    outerView.addView(answerChoice);
                }

            } else if (questionType == Question.TRUE_OR_FALSE) {

                // Same as for the multiple choice questions, but just doing the two RadioButtons at once since they will
                // always be the same. Then we are setting the labels (remember, even though its most likely going to be
                // unchanging in the case of true and false, this will allow us to do translations of the strings.xml!
                // Lastly, add them to the RadioGroup.
                RadioButton trueChoice = (RadioButton) getLayoutInflater().inflate(R.layout.answer_template, null);
                RadioButton falseChoice = (RadioButton) getLayoutInflater().inflate(R.layout.answer_template, null);
                trueChoice.setText(R.string.true_label);
                falseChoice.setText(R.string.false_label);
                outerView.addView(trueChoice);
                outerView.addView(falseChoice);

            } else if (questionType == Question.INPUT_ANSWER) {
                // For now we aren't setting a custom template for this, so just doing it the easy way.
                // It will look ugly, but let's just go with it.
                EditText inputAnswer = new EditText(this);

                // New Params with no margins for the EditText.
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                inputAnswer.setLayoutParams(params2);

                // Limit it to a single line, set a hint, and set an ID based on current question number. These will be
                // in the 4000 range. The point of this is to have something to reference later in the onClick method. We will
                // give it a value of 4000 + the current index value to try to avoid duplicates. It isn't foolproof, but its
                // good enough for our purposes.
                inputAnswer.setSingleLine();
                inputAnswer.setHint("Type Answer Here");
                inputAnswer.setId(4000 + i);
                outerView.addView(inputAnswer);

            } else if (questionType == Question.CHECK_MULTIPLE) {
                // UNUSED FOR THE MOMENT
            } else Log.v(TAG, "Improper type for array position: " + i); // Just an error output if no type declared.

            // Don't forget the answer button! Same deal as before. We set the params, set the text to a string, add it to the
            // ever growing RadioGroup. You should know the first three commands by now. This gets tricky though.
            final Button submitAnswer = new Button(this);
            submitAnswer.setLayoutParams(params);
            submitAnswer.setText(R.string.answer_button);

            // We are setting an ID so the submitButtons don't jumble together. This is not a fool proof method, and there are
            // slim chances this could double up with a randomly generated ID, but it is extremely unlikely so we will use this
            // for the time being. The id will be set to 2000 + the current point in the loop. So the first question will have
            // an ID for its submit button of 2000 (remember, the first index in an array is 0), the second will be 2001, etc.
            submitAnswer.setId(2000 + i);

            // Here we go. This is a biggie. So this is creating a method on the fly, assigned to the button we are creating.
            // There are a few ways to do this, but I am using this method as it is the easiest to see what is going on.
            // I'm not entirely sure how Android view onClicks work, so there is probably a better way to do what I am about
            // to with IDs as well, but if it works, it works.
            submitAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Retrieving the ID we stored before.
                    int currentQuestion = view.getId();
                    // This is the potentially roundabout part. This essentially gets back our original question.
                    currentQuestion -= 2000;

                    if (questionArray[currentQuestion].getType() == Question.MULTIPLE_CHOICE) {

                        // Lets nab the ID of the correct answer string. Note that since this is made on the fly
                        // like this, it will set these variables to the appropriate IDs at the time it is being
                        // ran in the loop. So no matter how many buttons are made, the one we are making now will
                        // only run on the current question! Also, we're grabbing the selected Radio Button from
                        // our view group!
                        int correctAnswerID = ((MultipleChoice) questionArray[currentQuestion]).getAnswer();
                        RadioButton selectedRadioButton = findViewById(outerView.getCheckedRadioButtonId());

                        // This first is just to prevent anything from happening if no button is selected.
                        if (selectedRadioButton == null) return;
                        // Now we get to the fun part, comparing the string of the correct answer with the String
                        // of the selectedRadioButton. If they are the same, it's a winner! Afterall, it's technically
                        // referencing the exact same string in the xml variables, so it should always match. We are
                        // just coming at it from different direction. The complicated looking stuff like equalsIgnoreCase
                        // is just a method built into Java to compare strings reliably. You can read more about it online.
                        // Don't confused it with compareTo.
                        else if (getString(correctAnswerID).equalsIgnoreCase(selectedRadioButton.getText().toString())) {

                            // Change the background of the radioGroup from neutral blue to CORRECTAMUNDO green! Just flair.
                            outerView.setBackgroundResource(R.drawable.rounded_green);

                            // Lets clean up the view a bit. We can do other things, but to show the question has been answered
                            // and prevent trying to reanswer it, let's just remove all the child views in the radioGroup after
                            // the question itself. Since that was the first one we added, it will be index 0. Using this version
                            // of the removeViews method that takes two index locations, we can go from 1 (the second view we added,
                            // which is the first answer choice) to the end. We can figure out the end by using the getChildCount
                            // method, and then subtracting 1 for the appropriate index. (As again, all indexes start with 0, so
                            // a length of 10 means that the final object is at index 9.
                            outerView.removeViews(1, outerView.getChildCount()-1);

                            // Increment our scoring variables and update.
                            totalCorrect++;
                            totalAnswered++;
                            updateScore();
                        } else {
                            // Booooooooooo. Answer wrong, box go red. Same deal with removing views.
                            outerView.setBackgroundResource(R.drawable.rounded_red);
                            outerView.removeViews(1, outerView.getChildCount()-1);
                            totalAnswered++;
                        }

                    }
                    else if (questionArray[currentQuestion].getType() == Question.TRUE_OR_FALSE){
                        // Same idea here with True/False questions. But we need to first establish a string
                        // to compare to. There is absolutely easier ways to do this, but I am tired and just
                        // amd reusing the way I can do without trouble. :P So we are going to brute force it
                        // basically by creating a true/false string and comparing it to the RadioButton text.
                        boolean correctAnswerState = ((TrueOrFalse) questionArray[currentQuestion]).getAnswer();
                        RadioButton selectedRadioButton = findViewById(outerView.getCheckedRadioButtonId());
                        String trueFalseString;
                        // The next two lines just are a quick if else to set the string to the correct value.
                        if (correctAnswerState) trueFalseString = "true";
                        else trueFalseString = "false";

                        // Compar time! Same as before.
                        if (trueFalseString.equalsIgnoreCase(selectedRadioButton.getText().toString()))
                        {
                            // Same as before.
                            outerView.setBackgroundResource(R.drawable.rounded_green);
                            outerView.removeViews(1, outerView.getChildCount()-1);
                            totalCorrect++;
                            totalAnswered++;
                            updateScore();
                        }
                        else {
                            // Annnnnnnnnd same as before.
                            outerView.setBackgroundResource(R.drawable.rounded_red);
                            outerView.removeViews(1, outerView.getChildCount()-1);
                            totalAnswered++;
                        }
                    }
                    else if (questionArray[currentQuestion].getType() == Question.INPUT_ANSWER){

                        // Remember that ID we set to the EditText field creating it? Lets call for it here.
                        EditText answerInputField = findViewById(4000 + currentQuestion);

                        // Let's get what the player typed into that EditText view. This isn't strictly neccessary,
                        // and we can reference the answerInputField.getText().toString() in the if statement directly,
                        // but it's neater for the sake of explanation. Likewise for the second string we are pulling from
                        // the question object.
                        String inputString = answerInputField.getText().toString();
                        String correctAnswer = getString(((InputAnswer)questionArray[currentQuestion]).getAnswerStringID());
                        // Comparison time yet again!
                        if (inputString.equalsIgnoreCase(correctAnswer)) {
                            // Same as before. Copy...
                            outerView.setBackgroundResource(R.drawable.rounded_green);
                            outerView.removeViews(1, outerView.getChildCount()-1);
                            totalCorrect++;
                            totalAnswered++;
                            updateScore();
                        }
                        else {
                            // Annnnnnnnnd same as before. ...paste.
                            outerView.setBackgroundResource(R.drawable.rounded_red);
                            outerView.removeViews(1, outerView.getChildCount()-1);
                            totalAnswered++;
                        }
                    }
                    else if (questionArray[currentQuestion].getType() == Question.CHECK_MULTIPLE){
                        // NOT YET IMPLEMENTED AS PER OTHER CHECK MULTIPLE CODE!!
                    }
                }
            });

            // Forgot we were in the middle of creating a button, didn't you? Well, we're done. And now we add it to
            // the RadioGroup!
            outerView.addView(submitAnswer);

            // This adds everything we just did, nested in the RadioGroup, to the main LinearLayout in the XML. I
            // did this last just so we have a complete package for the question before displaying it, even though
            // in practice it will be adding the pieces too quickly for a human to notice anyway if you had done this
            // step first, then started adding views to it.
            primaryView.addView(outerView);
        }
    }

    private void updateScore() {
        // Let's piece together the score string first, then set the text. This method is simple, so I'll leave it at that.
        String scoreString = getString(R.string.score_string) + " " + totalCorrect + " " + getString(R.string.score_string2) + " " + totalQuestions;
        scoreDisplay.setText(scoreString);
    }


    // Ignore the following methods, they are android stuff I haven't fully implemented.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putBoolean("initiated", initiated);
        savedInstanceState.putInt("total questions", totalQuestions);
        savedInstanceState.putInt("total correct", totalCorrect);
        savedInstanceState.putInt("total answered", totalAnswered);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        totalQuestions = savedInstanceState.getInt("total questions");
        totalCorrect = savedInstanceState.getInt("total correct");
        totalAnswered = savedInstanceState.getInt("total answered");
        initiated = savedInstanceState.getBoolean("initiated");

        updateScore();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

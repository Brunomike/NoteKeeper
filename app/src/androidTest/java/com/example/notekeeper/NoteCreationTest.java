package com.example.notekeeper;

import android.view.View;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static org.junit.Assert.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import  static  org.hamcrest.Matchers.*;
import  static  androidx.test.espresso.Espresso.pressBack;

@RunWith(AndroidJUnit4.class)
public class NoteCreationTest {
    static  DataManager sDataManager;
    @BeforeClass
    public static  void classSetup(){
        sDataManager=DataManager.getInstance();
    }
    @Rule
 public ActivityTestRule<NoteListActivity> mNoteListActivityRule=
         new ActivityTestRule<>(NoteListActivity.class);
 @Test
    public void createNewNote(){
     final CourseInfo course=sDataManager.getCourse("java_lang");
     final String noteTitle="Test note title";
     final String noteText="This is the body of our test note";
//     ViewInteraction fabNewNote=onView(withId(R.id.fab));
//     fabNewNote.perform(click());
     onView(withId(R.id.fab)).perform(click());

     onView(withId(R.id.spinner_courses)).perform(click());
     onData(allOf(instanceOf(CourseInfo.class),equalTo(course))).perform(click());
     onView(withId(R.id.spinner_courses)).check(matches(withSpinnerText
             (containsString(course.getTitle()))));

     onView(withId(R.id.text_note_title)).perform(typeText(noteTitle))
            .check(matches(withText(containsString(noteTitle))));
     onView(withId(R.id.text_note_text)).perform(typeText(noteText),
             closeSoftKeyboard());

     onView(withId(R.id.text_note_text)).check(matches(withText(containsString(noteText))));

     pressBack();

     int noteIndex=sDataManager.getNotes().size()-1;
     NoteInfo note=sDataManager.getNotes().get(noteIndex);
     assertEquals(course,note.getCourse());
     assertEquals(noteTitle,note.getTitle());
     assertEquals(noteText,note.getText());
 }




}
/*
Instrumented testing relies on Android JUnit test runner
            -Class must have @RunWith annotation
                    -Pass AndroidJUnit4.class
             -Requires tha android environment

UI tests require a series of view interactions
    -Need way to specify view of interest
    -Need way to specify action on the view
Expresso.onView method
    -Accepts a Mathcher parameter
        -Specifies view matching criteria
    -Returns a ViewInteraction reference
        -Associated with matched view
        -Used to perform action on view
    -Uses Hamcrest matchers
        -Provides declarative matching
        -General purpose Java framework
        -http://hamcrest.org
     -ViewMatchers class
        -Provides matchers for Android Views
        -Method returns a Hamcrest matcher
        -We can easily combine with hamcrest general purpose matchers with these view specific view mathchers

      -Example of ViewMatchers methods
        -withId
            -Match views based on id property
        -withText
            -Match views based on text property
        -isDisplayed
            -Match views currently on screen
        -isChecked
            -Match currently checked checkable views(Switch,Checkbox,e.t.c)
      -Example of Hamcrest Matchers
         -equalTo
            -Match based on equals method
         -instanceOf
            -Match based on object type
         -allOf
            -Accepts multiple Matchers
            -Match if all Matchers Match
         -anyOf
            -Accepts multiple Matchers
            -Match if any Matchers match

      ==Performing view action
        -ViewInteraction.perform method
            -Performs one or more specified actions
            -Specific action passed as a parameter
         -ViewActions class
            -Provides action methods
            -Each method returns specific action

         -Example ViewActions methods
            -click
                -Click on the view
            -typeText
                -Type text into view
             -replaceText
                -Replaces view's text
             -closeSoftKeyboard
                -Closes the soft keyboard


ActivityTestRule
    -Automates test activity lifetime
    -Starts activity before each test
    -Terminates activity after each test
    -Activity life includes @Before/@After methods

Using ActivityTestRuled
    -Declare and initialize as test class field
    -Desired activity as type parameter
    -Mark field with @Rule annotation-activityTestRule will then handle creating and destroying the activity appropriately


 */

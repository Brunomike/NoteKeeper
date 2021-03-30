package com.example.notekeeper;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.navigation.NavigationView;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;


public class NextThroughNotesTest {
//Starts our activity before running any tests inside this class
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule=
        new ActivityTestRule(MainActivity.class);
    @Test
    public  void  NextThroughNotes(){
     onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
     //DrawerActions is not part of the espresso core library-we'll need to add some dependency
    onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_notes));
        //ensure views are selected...nav_notes..notes selected
    onView(withId(R.id.list_items)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        //select something on the recycler view


    List<NoteInfo> notes=DataManager.getInstance().getNotes();
    for(int index=0;index<notes.size();index++) {
        NoteInfo note = notes.get(index);

        onView(withId(R.id.spinner_courses)).check(
                matches(withSpinnerText(note.getCourse().getTitle())));
        onView(withId(R.id.text_note_title)).check(
                matches(withText(note.getTitle())));
        onView(withId(R.id.text_note_text)).check(
                matches(withText(note.getText())));
        //onView(withId(R.id.action_next)).perform(click());
        if (index<notes.size()-1)
        onView(allOf(withId(R.id.action_next),isEnabled())).perform(click());
        }
    onView(withId(R.id.action_next)).check(matches(not(isEnabled()))) ;
    pressBack();

    }

}
package com.example.notekeeper;

import android.provider.ContactsContract;
import android.util.Log;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DataManagerTest  {
    static  DataManager sDataManager;
@BeforeClass //has to be static
    public static void  classSetup(){
        sDataManager=DataManager.getInstance();
    }

@Before
    public void setUp(){
        //DataManager dm=DataManager.getInstance();
        sDataManager.getNotes().clear();
        sDataManager.initializeExampleNotes();
    }
@Test
    public void testCreateNewNote() {
    //DataManager dm = DataManager.getInstance();
    final CourseInfo course= sDataManager.getCourse("android_async");
    final String noteTitle="Test note title";
    final String noteText="This is the body text of my test note";

    int noteIndex=sDataManager.createNewNote();
    NoteInfo newNote=sDataManager.getNotes().get(noteIndex);
    newNote.setCourse(course);
    newNote.setTitle(noteTitle);
    newNote.setText(noteText);

    NoteInfo compareNote=sDataManager.getNotes().get(noteIndex);

    //assertSame(newNote,compareNote);
    assertEquals(course,compareNote.getCourse());
    assertEquals(noteTitle,compareNote.getTitle());
    assertEquals(noteText,compareNote.getText());
    }
@Test
    public void testFindSimilarNotes() {
        //DataManager dm = DataManager.getInstance();
        final CourseInfo course = sDataManager.getCourse("android_async");
        final String noteTitle = "Test note title";
        final String noteText1 = "This is the body text of my test note";
        final String noteText2  = "This is the body of my second test note";

        int noteIndex1 = sDataManager.createNewNote();
        NoteInfo newNote1 = sDataManager.getNotes().get(noteIndex1);
        newNote1.setCourse(course);
        newNote1.setTitle(noteTitle);
        newNote1.setText(noteText1);

        int noteIndex2 = sDataManager.createNewNote();
        NoteInfo newNote2 = sDataManager.getNotes().get(noteIndex2);
        newNote2.setCourse(course);
        newNote2.setTitle(noteTitle);
        newNote2.setText(noteText2);

        int foundIndex1 = sDataManager.findNote(newNote1);
        assertEquals(noteIndex1, foundIndex1);

        int foundIndex2 = sDataManager.findNote(newNote2);
        assertEquals(noteIndex2, foundIndex2);
    }

@Test
    public void testCreateNewNoteOneStepCreation() {
        final CourseInfo course = sDataManager.getCourse("android_async");
        final String noteTitle = "Test note title";
        final String noteText = "This is the body of my test note";

        int noteIndex = sDataManager.createNewNote(course, noteTitle, noteText);

        NoteInfo compareNote = sDataManager.getNotes().get(noteIndex);
        assertEquals(course, compareNote.getCourse());
        assertEquals(noteTitle, compareNote.getTitle());
        assertEquals(noteText, compareNote.getText());
    }

    @Test//Test Driven Development
    public void createNewNoteOneStepCreation() {
        final CourseInfo course = sDataManager.getCourse("android_async");
        final String noteTitle = "Test note title";
        final String noteText = "This is the body of my test note";

        int noteIndex = sDataManager.createNewNote(course, noteTitle, noteText);

        NoteInfo compareNote = sDataManager.getNotes().get(noteIndex);
        assertEquals(course, compareNote.getCourse());
        assertEquals(noteTitle, compareNote.getTitle());
        assertEquals(noteText, compareNote.getText());

    }
/*
Android Applications
        -Java-based behavior
        -Android-based behavior

Testing Java-based behavior
        -Local JVM tests-Unit tests
        
Testing Android-based behavior
        -Instrumented Unit tests-Run on an emulator or physical device
        -Rely on Andorid features/capabilities

Automated user interface tests
        -Integration test
        -App behaviors in response to UI actions/interactions

 */

}
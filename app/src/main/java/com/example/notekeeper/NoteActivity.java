package com.example.notekeeper;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
    private  final String TAG=getClass().getSimpleName();
    public static final String NOTE_POSITION ="com.example.notekeeper.NOTE_POSITION";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo mNote;
    private boolean mIsNewNote;
    private Spinner mSpinnerCourses;
    private EditText mTextNoteTitle;
    private EditText mTextNoteText;
    private int mNotePosition;
    private boolean mIsCancelling;
    private  NoteActivityViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);//loads the layout includes content_notes
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewModelProvider viewModelProvider=new ViewModelProvider(getViewModelStore(),ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        //boilerplate code
        mViewModel=viewModelProvider.get(NoteActivityViewModel.class);

        if (mViewModel.mIsNewlyCreated  && savedInstanceState != null ){
            mViewModel.restoreState(savedInstanceState);
        }
        //There is no need to restore ViewModel state in response to a configuration change
        //We only need to restore ViewModel state when it gets destroyed along with the activity

        mViewModel.mIsNewlyCreated=false;


        mSpinnerCourses = findViewById(R.id.spinner_courses);
        List<CourseInfo> courses=DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses=new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,courses);;
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCourses.setAdapter((adapterCourses));
        
        readDisplayStateValues();
        saveOriginalNoteValues();//break
        mTextNoteTitle = findViewById(R.id.text_note_title);
        mTextNoteText = findViewById(R.id.text_note_text);

        if (!mIsNewNote){
            displayNote(mSpinnerCourses, mTextNoteText, mTextNoteTitle);
        }

        Log.d(TAG,"onCreate");
    }

    private void saveOriginalNoteValues() {
        if (mIsNewNote)
            return;
        mViewModel.mOriginalNoteCourseId = mNote.getCourse().getCourseId();
        mViewModel.mOriginalNoteTitle = mNote.getTitle();
        mViewModel.mOriginalNoteText = mNote.getText();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mIsCancelling){//break
            Log.d(TAG,"Cancelling note at position "+mNotePosition);
            if(mIsNewNote) {
                DataManager.getInstance().removeNote(mNotePosition);
            }else {
                storePreviousNoteValues();
            }
        }else {
            saveNote();
        }
        Log.d(TAG,"onPause");
    }

    private void storePreviousNoteValues() {
        CourseInfo course=DataManager.getInstance().getCourse(mViewModel.mOriginalNoteCourseId);
        mNote.setCourse(course);
        mNote.setTitle(mViewModel.mOriginalNoteTitle);
        mNote.setText(mViewModel.mOriginalNoteText);

    }

    private void saveNote() {
        mNote.setCourse((CourseInfo) mSpinnerCourses.getSelectedItem());//break
        mNote.setTitle(mTextNoteTitle.getText().toString());
        mNote.setText(mTextNoteText.getText().toString());
    }

    private void displayNote(Spinner spinnerCourses, EditText textNoteText, EditText textNoteTitle) {
        List<CourseInfo> courses=DataManager.getInstance().getCourses();
        int courseIndex=courses.indexOf(mNote.getCourse());
        spinnerCourses.setSelection(courseIndex);

        textNoteTitle.setText(mNote.getTitle());
        textNoteText.setText(mNote.getText());

    }

//    private void readDisplayStateValues() {
//        Intent intent=getIntent();
//        //mNote = intent.getParcelableExtra((NOTE_POSITION));
//        int position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);//-1 to indicate extra not found in the intent
//        //Extras that are value-types require a second argument that provides a default value
//        //mIsNewNote = mNote==null;
//        mIsNewNote=position==POSITION_NOT_SET;
//        if (mIsNewNote){
//            createNewNote();
//        }else {
//            mNote=DataManager.getInstance().getNotes().get(position);
//        }
//
//    }
private void readDisplayStateValues() {
    Intent intent=getIntent();
    //mNote = intent.getParcelableExtra((NOTE_POSITION));
     mNotePosition = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);//-1 to indicate extra not found in the intent
    //Extras that are value-types require a second argument that provides a default value
    //mIsNewNote = mNote==null;
    mIsNewNote=mNotePosition==POSITION_NOT_SET;
    if (mIsNewNote){
        createNewNote();
    }
    Log.i(TAG,"mNotePosition: "+mNotePosition);
        mNote=DataManager.getInstance().getNotes().get(mNotePosition);


}

    private void createNewNote() {
        DataManager dm=DataManager.getInstance();
        mNotePosition = dm.createNewNote();
        // mNote=dm.getNotes().get(mNotePosition);//break
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sendMail) {
            sendEmail();
            return true;
        }else if(id== R.id.action_cancel){
            mIsCancelling = true;
            finish();//Activity Exits
        }else if(id==R.id.action_next)
            moveNext();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.action_next);
        int lastNoteIndex=DataManager.getInstance().getNotes().size()-1;
        item.setEnabled(mNotePosition<lastNoteIndex);
        return super.onPrepareOptionsMenu(menu);
    }

    private void moveNext() {
        saveNote();
        ++mNotePosition;
        mNote=DataManager.getInstance().getNotes().get(mNotePosition);
        saveOriginalNoteValues();
        displayNote(mSpinnerCourses,mTextNoteTitle,mTextNoteText);
        invalidateOptionsMenu();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState != null){
            mViewModel.saveState(outState);
        }
    }

    private void sendEmail() {
        CourseInfo course=(CourseInfo) mSpinnerCourses.getSelectedItem();
        String subject =mTextNoteTitle.getText().toString();
        //getText() method returns an editable we need to add toString() method
        String text="Check what I learned in the Pluralsight course \""+
                course.getTitle() +"\"\n"+mTextNoteText.getText();

        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        //message/rfc2822 is a standard Internet mime type for sending email
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,text);
        //Our app will provide the email subject and body
        startActivity(intent);
    }
    //Our email should have a subject that is the title of our note and a body message that talks about the note's course and the note itself
}
/**Late-binding Components
 * intents describe a desired operation
 * -identify operation target
 *
 * ==Explicit intents
 * -Target is explicitly identified
 * -Specify the activity class to use
 *
 * ==Implicit intents provide late-binding
 *-Target is implied
 * -Specify the activity characteristics
 *
 * -Match is determined at runtime
 * -System finds the best match,often comes from another app e.g gmail,contact
 * -Specific match may vary depending on apps installed on user device
 * -Prompts user if tie
 *
 * Decouples sender and receiver-sender may not know the receiver
 *
 *
 * ==IMPLICIT INTENT CHARACTERISTICS
 * -Action-Action string(string identifying some kind of action)
 *        -Many standard constants available e.g Intent.ACTION_VIEW
 *        -Commonly set in intent constructor
 *        -Only required characteristic
 * -Category-Provides extended qualification
 *          -Not normally used by sender
 * -Data-Provides the URI of data to be acted upon e.g
 *      -Set data with Intent.setData
 * MIME type-Identify what kind of data is associated with the URI
 *          -Common or app-specific mime type e.g text/html
 *          -Set with Intent.setType
 *          -Multipurpose Internet Mail Extensions
 *   Setting data and mime type
 *      -Use Intent.setDataAndType
 *      -setType and setData cancels each other out
 *
 * Common implicit Examples http://bit.ly/commonintents
 */
/**Activities with Results
 * -Some activity classes return results
 * -Camera activity
 *      -Presents camera functionality UI
 *      -Stores full quality image in a file
 *      -Returns image thumbnail  as a result
 *    Starting the Activity
 *      -Intent action
 *              -MediaStore.ACTION_IMAGE_CAPTURE
 *      -Extra
 *              -MediaStore.EXTRA_OUTPUT
 *              -File in which to save full quality image--passed as an URI
 *
 *     Receiving the Result
 *      -Check for request code of SHOW_CAMERA
 *          -Identifies the result is for our request
 *      -Check for result code of RESULT_OK
 *          -Indicates success
 *          -Full quality image stored in specified file
 *      -Retrieve thumbnail
 *          -Stored in result intent as a bitmap
 *          -Name is "data"
 *         http://bit.ly/commonintents
 *
 *
 * -Contact activity
 *      -Presents contact UI
 *      -Returns selected contact info
 *and many others
 *
 * They are started differently than other activities
 *      -Use startActivityForResult
 * Parameters passed to startActivityForResult
 *      -intent
 *      -App defined integer identifier
 *              -Differentiates results within your app
 *
 * RECEIVING RESULTS
 * The way we get results,is by a call being made back to our activity
 *      -Override your Activity's onActivityResult() method
 *      -Parameters received by onActivityResult
 *              -App defined integer identifier
 *              -Result code .RESULT_OK indicates success
 *              -intent  .Contains activity results
 */
/**Application Experience and Tasks
 * Generally our app experience is going to be composed of multiple Activities
 *      -Most probably come from your app
 *      -Others may come from other apps
 *      -Android needs to manage this flow that could potentially cross-application somehow
 *      -Flow is managed by tasks--A task is a collection of activities that users interact with when performing a certain job
 *
 *   -Task
 *          -Managed as a stack--known as the back stack
 *          -Activities added going forward
 *          -Back button removes activities from the task--causes activity to be destroyed
 *
 *   -Managing persistent state
 *          -Use edit-in-place model
 *          -Changes saved with no special action
 *   -Saving changes
 *          -Write to backing store when leaving
 *          -Handle in onPause
 *
 *    -Handling new entries
 *          -New entries created right away
 *          -Handle in onCreate
 *
 *
 */
/**
 * ///----------ACTIVITY LIFECYCLE--------------///
 * --Activity Death
 * -Common causes of Activity destruction
 * 	-Leaving with the back button
 * 	-Calling finish method
 * 	-System initiated destruction
 *
 * ==System Initiated destruction
 * 	-Generally to reclaim resources
 * 	-Prolonged period in the background
 * 	-System experiencing resource pressure
 *
 *
 *
 * ///-------ACTIVITY LIFECYCLE METHODS---------///
 * --Lifetime within Activity lifecycle
 * 	-Total lifetime--begins at onCreate
 * 	-Visible lifetime-begins at onStart
 * 	-Foreground lifetime-begins at onResume
 * --Activity lifecycle methods
 * 	-Methods for start/end of each lifetime
 * 	-A few additional methods for transitions
 *
 * Activity launched--->onCreate-->onStart-->onResume-->Activity Running-->onPause-->onStop-->onDestroy-->Activity Shutdown
 */
/**Activity State Management
 * --Activities provide state management
 *      -Opportunity to save before destroy
 *      -Saved state provided on restore
 * --Saving state
 *      -onSaveInstanceState method
 *      -Write activity state to passed Bundle
 * --Restoring state
 *      -onCreate method
 *      -Receives saved Bundle on restore
 *      -Bundle is null on initial create
 *      -Intent remains available on restore
 *
 *
 * --Maintaining activity state
 *      -Writing to a persistent store is expensive
 *      -Need a better solution for maintaining state across configuration changes
 *
 *
 * --ViewModel
 *      -Stores activity state in process
 *      -State stored separately from the activity
 *      -We create our own class which Extends ViewModel class to customize
 *      -Add properties and methods specific to your activity's state requirements
 *
 * --ViewModelProvider
 *      -Manages ViewModel instances
 *      -Creates new instance when needed
 *      -Retrieves existing when available
 *
 */
//Default activity lifetime management makes testing state management difficult
//A complete state management solution requires also using onSaveInstanceState

/**Logcat
 * Is a system for recording log information
 *
 * By using Log class
 * -which provides methods for writing to logcat
 * -Includes a tag with each message-commonly use class name as tag
 *
 * Android monitor
 * -Displays logcat messages
 * -Filterable-filter through the message
 * -Searchable-search through the message
 *
 * -Messages are organized into levels-indicating relative severity
 * -Log class provides methods for each level
 *
 * -When viewing messaged-messages are labeled with level severity(each)
 * -We can limit which levels are displayed within the android monitor to make it easy zero in an issue
 * -Whenever we are viewing a message within the android monitor we select the severity to display,and messages with that severity or greater are displayed
 *
 * Severity increases down the group
 * Severity decreases up the group
 *
 *  Level       Label           Log Method
 *  Verbose      V               Log.v
 *  Debug        D               Log.d
 *  Info         I               Log.i
 *  Warning      W               Log.w
 *  Error        E               Log.e
 *  Assert       A               Log.wtf      (what a terrible failure)    Errors we don't expect to see
 *
 */
//Logcat messages are captured regardless of how the program is run or buit


/**Android Build Process
 *
 * App sources and Resource files
 *              |
 *  ---gradle--Compilers->Intermediate Files->APK Packager--gradle--->APK
 *              |
 *         Dependencies
 *
 * Gradle uses a domain specific language
 * We deal with build.gradle
 */

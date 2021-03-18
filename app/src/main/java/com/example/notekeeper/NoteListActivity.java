 package com.example.notekeeper;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;
//Android is component-oriented and components interact through intents which describe operation target and additional info passed as extras
//Components run within a process
/**Process Lifetime
 * -Driven by component lifetime
 * -Launched for first component accessed
 * Terminates after last component exits
 *
 *
 * Application Process
 * -Each app has its own process
 * -App components run in the same process
 *      -When simultaneously active
 */
public class NoteListActivity extends AppCompatActivity {
    private NoteRecyclerAdapter mNoteRecyclerAdapter;

    //private ArrayAdapter<NoteInfo> mAdapterNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((view)->{
    startActivity(new Intent(NoteListActivity.this,NoteActivity.class));
        });
        initializeDisplayContent();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //mAdapterNotes.notifyDataSetChanged();
        //notifying the adapter in case of changes to it
        mNoteRecyclerAdapter.notifyDataSetChanged();
    }

    private void initializeDisplayContent() {
//        final ListView listNotes = findViewById(R.id.list_notes);
//        List<NoteInfo> notes = DataManager.getInstance().getNotes();
//        mAdapterNotes = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1, notes);
//
//        listNotes.setAdapter(mAdapterNotes);
//        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);
//                //NoteInfo note=(NoteInfo) listNotes.getItemAtPosition(position);
//                intent.putExtra(NoteActivity.NOTE_POSITION, position);
//                startActivity(intent);
//            }
//        });
        final RecyclerView recyclerNotes= findViewById(R.id.list_notes);
        final LinearLayoutManager notesLayoutManager=new LinearLayoutManager((this));//this reference to the linearlayoutmanager constructor which requires a context which is our user activity
        recyclerNotes.setLayoutManager(notesLayoutManager);//bind them together

        List<NoteInfo> notes=DataManager.getInstance().getNotes();
        mNoteRecyclerAdapter = new NoteRecyclerAdapter(this,notes);
        recyclerNotes.setAdapter(mNoteRecyclerAdapter);

        CardView cardView=findViewById(R.id.card_view);

    }

///Reference types need to be flattened-converted to a bunch of bytes i.e java serialization and parcelable
//Java serialization supported but not preferred as runtime expensive--hard to implement
        //Instead Parcelable API is used,which is more efficient then serialization
        //Parcelable must be explicitly implemented
//We are going to pass info to our note activity to indicate which note we want displayed
/*LayoutManager Implementations
-GridLayoutManager
-LinearLayoutManager
-StaggeredGridLayoutManager

 */

}


package com.hanzhisoft.signinapp.note;

import java.util.List;

import com.hanzhisoft.signinapp.R;
import com.hanzhisoft.signinapp.data.NoteItem;
import com.hanzhisoft.signinapp.data.NotesDataSource;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
* @title: NoteMainActivity.java
* @description: 笔记列表主界面
* @copyright: Copyright (c) 2014
* @company: HanZhiSoft 
* @author HuangXiaoPeng
* @date 2014-12-7
* @version 1.0
*/
public class NoteMainActivity extends ListActivity {

	private static final int EDITOR_ACTIVITY_REQUEST = 1001;
	private static final int MENU_DELETE_ID = 1002;
	private int currentNoteId;
	private NotesDataSource datasource;
	List<NoteItem> notesList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_pre3);
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		// setContentView(R.layout.note_activity_main);
		// }
		//
		// else {
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// setContentView(R.layout.activity_main_pre3);
		// }

		registerForContextMenu(getListView());

		datasource = new NotesDataSource(this);

		refreshDisplay();

	}

	private void refreshDisplay() {
		notesList = datasource.findAll();
		ArrayAdapter<NoteItem> adapter = new ArrayAdapter<NoteItem>(this,
				R.layout.list_item_layout, notesList);
		setListAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.note_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.add_note) {
			createNote(null);
		} else if (item.getItemId() == android.R.id.home) {
			finish();

		}
		return super.onOptionsItemSelected(item);
	}

	public void createNote(View v) {
		NoteItem note = NoteItem.getNew();
		Intent intent = new Intent(this, NoteEditorActivity.class);
		intent.putExtra(NoteItem.KEY, note.getKey());
		intent.putExtra(NoteItem.TEXT, note.getText());
		startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		NoteItem note = notesList.get(position);
		Intent intent = new Intent(this, NoteEditorActivity.class);
		intent.putExtra(NoteItem.KEY, note.getKey());
		intent.putExtra(NoteItem.TEXT, note.getText());
		startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == EDITOR_ACTIVITY_REQUEST && resultCode == RESULT_OK) {
			NoteItem note = new NoteItem();
			note.setKey(data.getStringExtra(NoteItem.KEY));
			note.setText(data.getStringExtra(NoteItem.TEXT));
			datasource.update(note);
			refreshDisplay();
		}
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		currentNoteId = (int) info.id;
		menu.add(0, MENU_DELETE_ID, 0, "Delete");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		if (item.getItemId() == MENU_DELETE_ID) {
			NoteItem note = notesList.get(currentNoteId);
			datasource.remove(note);
			refreshDisplay();
		}

		return super.onContextItemSelected(item);
	}

}

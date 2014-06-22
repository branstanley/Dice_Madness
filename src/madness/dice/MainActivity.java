package madness.dice;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.Inflater;

import madness.dice.MainFragment.main_communication;
import madness.dice.Pattern.die_crafter;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends ActionBarActivity 
		implements Pattern.die_crafter, main_communication{
	
	/*
	 * Static Variables
	 */
	private static final String DATA_TASK_FRAGMENT = "data_fragment"; 
	/*
	 * Member Variables
	 */
	private MainFragment main_frag = null;
	private DataFragment data_frag;
	/*
	 * Member Methods
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 
		
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		data_frag = (DataFragment) fm.findFragmentByTag(DATA_TASK_FRAGMENT);
		
		if(main_frag == null){
			main_frag = new MainFragment(); 
			//fm.beginTransaction().add(main_frag, MAIN_TASK_FRAGMENT).commit();
			
			ft.add(R.id.container, main_frag);
			ft.commit();
		}
		if(data_frag == null){
			data_frag = new DataFragment();
			fm.beginTransaction().add(data_frag, DATA_TASK_FRAGMENT).commit();
			data_frag.add_pattern("Fuck yo momma");
		}
		
	}
	protected void onResume(){
		super.onResume();

	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    //No call for super(). Bug on API Level > 11.
	}
	
	protected void onStop(){
		super.onStop();
		

		FragmentManager fragmentManager = getFragmentManager();
		
		if(main_frag != null)
			fragmentManager.beginTransaction().remove(main_frag).commit();
		
	}
	
	/*
	 * Top Options Menu Stuff
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.save, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.single_save:
	            main_frag.single_save();
	            return true;
	        case R.id.save_all:
	            main_frag.save_all();
	            return true;
	        case R.id.loader:
	            main_frag.loader();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	

	/*
	 * Screen changing button methods
	 */
	
	public void help(View view){
		main_frag.help();
	}
	public void roll(View view){
		main_frag.roll();
	}
	public void pattern(View view){
		main_frag.pattern();
		
	}
	
	
	public Die new_die(LinearLayout in){
		return new Die((LinearLayout)getLayoutInflater().inflate(R.layout.new_die, in, false));
	}
	
	public void add_dice(View view){
		main_frag.add_dice();
	}
	public void remove_dice(View view){
		main_frag.remove_dice(view);
	}
	public void add_pattern(View view){
		main_frag.add_pattern(); 
	}

	
	/*
	 * main_communication interface
	 */
	@Override
	public ScrollView get_new_scrollview(LinearLayout display_window){
		return null;
	}
	
	@Override
	public LayoutInflater get_inflater() {
		return getLayoutInflater();
	}
	
	@Override
	public View get_view(int id){
		return this.findViewById(id);
	}
	@Override
	public FileOutputStream open_output(String in) throws FileNotFoundException {
		return openFileOutput(in, Context.MODE_PRIVATE);
	}
	@Override
	public FileInputStream open_input(String in) throws FileNotFoundException {
		return openFileInput(in);
	}
	@Override
	public die_crafter get_die_crafter() {
		return this;
	}
	@Override
	public ArrayAdapter<Pattern> get_pattern_adapter() {
		return new ArrayAdapter<Pattern>(this, android.R.layout.simple_spinner_item);
	}
	
	
	public void submit(View view){
		main_frag.submit(data_frag.get_pattern_data(0));
	}

}

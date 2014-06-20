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
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
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
	private static final String MAIN_TASK_FRAGMENT = "main_fragment"; 
	/*
	 * Member Variables
	 */
	private MainFragment main_frag;
	/*
	 * Member Methods
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 
		
		FragmentManager fm = getFragmentManager();
		main_frag = (MainFragment) fm.findFragmentByTag(MAIN_TASK_FRAGMENT);
		
		if(main_frag == null){
			main_frag = new MainFragment();
			fm.beginTransaction().add(main_frag, MAIN_TASK_FRAGMENT).commit();
		}
		
	}
	protected void onResume(){
		super.onResume();
		
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
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		
		
		public PlaceholderFragment() {
		}
		
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	
	/*
	 * main_communication interface
	 */
	@Override
	public ScrollView get_new_scrollview(LinearLayout display_window){
		return (ScrollView)getLayoutInflater().inflate(R.layout.display, display_window, false);
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
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayAdapter<Pattern> get_pattern_adapter() {
		return new ArrayAdapter<Pattern>(this, android.R.layout.simple_spinner_item);
	}

}

package madness.dice;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
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
		implements Pattern.die_crafter{
	
	/*
	 * Member Variables
	 */
	protected int selection = 0;  //0 is roll page, 1 is Pattern, 2 is Help
	
	LinearLayout display_window = null;
	ArrayAdapter<Pattern> patterns = null;
	Spinner spinner = null;
	LinearLayout roll_window = null;
	
	/*
	 * Member Methods
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
	}
	protected void onResume(){
		super.onResume();
		
		if(spinner == null){//the drop down menu
			spinner = (Spinner)findViewById(R.id.roll_selector);
			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 
			    	if(selection == 1)
			    		pattern(view);
			    } 

			    public void onNothingSelected(AdapterView<?> adapterView) {
			        return;
			    } 
			}); 
		}
		if(display_window == null)//the part that changes
			display_window = (LinearLayout)findViewById(R.id.display_window);
		if(patterns == null){
			patterns = new ArrayAdapter<Pattern>(this, android.R.layout.simple_spinner_item);
			patterns.add(new Pattern("No Pattern", get_new_scrollview(), this));
		}
		if(roll_window == null)
			roll_window = (LinearLayout)getLayoutInflater().inflate(R.layout.display_diddly, display_window, false);
		
		if(display_window.getChildCount() == 0){
			selection = 0;
			display_window.addView(roll_window);
		}
		spinner.setAdapter(patterns);
	}
	
	public ScrollView get_new_scrollview(){
		return (ScrollView)getLayoutInflater().inflate(R.layout.display, display_window, false);
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
	            single_save();
	            return true;
	        case R.id.save_all:
	            save_all();
	            return true;
	        case R.id.loader:
	            loader();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/*
	 * Save/Load stuff
	 */
	protected void single_save(){
		saver((Pattern)spinner.getSelectedItem());
		
	}
	protected void save_all(){
		for(int i = 0; i < patterns.getCount(); ++i){
			saver(patterns.getItem(i));
		}
	}
	protected void saver(Pattern in){
		display_window.removeAllViews();
		display_window.addView(roll_window);
		TextView tv = (TextView)findViewById(R.id.display);
		String data = in.save_pattern();
		String file_name = "Dice_"+ in.get_saved_number();
		
		try {
			FileOutputStream stream = openFileOutput(file_name, Context.MODE_PRIVATE);
			stream.write(data.getBytes());
			stream.close();
			stream = openFileOutput("Dice_0", Context.MODE_PRIVATE);
			data = "" + Pattern.get_saves();
			stream.write(data.getBytes());
			stream.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}
	}
	protected void loader(){
		display_window.removeAllViews();
		display_window.addView(roll_window);
		TextView tv = (TextView)findViewById(R.id.display);
		
		try {
			FileInputStream in = openFileInput("Dice_0");
			if(in == null)
				return;
			
			String temp ;
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			int count = Integer.parseInt(br.readLine());
			in.close();

			for(int i = 1;i <= count;++i){
				in = openFileInput("Dice_"+i);
				br = new BufferedReader(new InputStreamReader(in));
				patterns.add(new Pattern(br, get_new_scrollview(), this, true));
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		spinner.setAdapter(patterns);
	}
	
	/*
	 * 
	 */
	public void submit(View view){
		Spinner spinner = (Spinner)findViewById(R.id.roll_selector);
		
		if(selection == 1){
			if(spinner.getSelectedItem() == patterns.getItem(0)){
				EditText display = (EditText)findViewById(R.id.display);
				//EditText input = (EditText)findViewById(R.id.input);
				//display.setText(input.getText().toString());
			}
		}
	
	}
	
	/*
	 * Screen changing button methods
	 */
	
	public void help(View view){
		display_window.removeAllViews();
		display_window.addView(roll_window);
		TextView tv = (TextView)findViewById(R.id.display);
		
		if(selection == 0){
			tv.setText("This is the Roll screen help menu.\n\t1) You must set a pattern in the pattern screen.\n\t2) Come back to this Roll Screen.\n\t3) Press Roll Again to make a roll.");
		}
		else if(selection ==1){
			tv.setText("This is the pattern help menu.");
		}
		
		selection = 2;
	}
	public void roll(View view){
		if(selection != 0){
			selection = 0;
			display_window.removeAllViews();
			display_window.addView(roll_window);
		}
		else{
			((EditText)roll_window.findViewById(R.id.display)).setText(((Pattern)spinner.getSelectedItem()).roll());
		}
	}
	public void pattern(View view){
		if(selection != 1)
			selection = 1;
		display_window.removeAllViews();
		display_window.addView(((Pattern)spinner.getSelectedItem()).get_scroller());
		((Pattern)spinner.getSelectedItem()).refresh();
		
	}
	
	/*
	 * Patterns and Dice specific stuff
	 */
	
	public void add_dice(View view){
		((Pattern)spinner.getSelectedItem()).add_dice();
	}
	public void remove_dice(View view){
		((Pattern)spinner.getSelectedItem()).remove_dice((LinearLayout)(view.getParent().getParent()));
	}
	public void add_pattern(View view){
		selection = 1;
		
		Pattern temp = new Pattern("Pattern " + patterns.getCount(), (ScrollView)getLayoutInflater().inflate(R.layout.display, display_window, false), this);
		patterns.add(temp);
		display_window.removeAllViews();
		
		spinner.setAdapter(patterns);
		spinner.setSelection(patterns.getCount()-1);
		
		display_window.addView(temp.get_scroller());
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

}

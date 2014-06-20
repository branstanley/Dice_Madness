package madness.dice;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.Inflater;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainFragment extends Fragment{
	
	static interface main_communication{
		LayoutInflater get_inflater();
		View get_view(int id);
		ScrollView get_new_scrollview(LinearLayout display_window);
		FileOutputStream open_output(String in) throws FileNotFoundException;
		FileInputStream open_input(String in) throws FileNotFoundException;
		Pattern.die_crafter get_die_crafter();
		ArrayAdapter<Pattern> get_pattern_adapter();
	}
	
	/*
	 * Member Variables
	 */
	main_communication link;
	
	protected int selection = 0;  //0 is roll page, 1 is Pattern, 2 is Help
	
	LinearLayout display_window = null;
	ArrayAdapter<Pattern> patterns = null;
	Spinner spinner = null;
	LinearLayout roll_window = null;
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		link  = (main_communication)activity;
	}
	
	@Override
	public void onCreate(Bundle state){
		super.onCreate(state);
		
		setRetainInstance(true);
		
		if(spinner == null){//the drop down menu
			spinner = (Spinner)link.get_view(R.id.roll_selector);
			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 
			    	if(selection == 1)
			    		pattern();
			    } 

			    public void onNothingSelected(AdapterView<?> adapterView) {
			        return;
			    } 
			}); 
		}
		if(display_window == null)//the part that changes
			display_window = (LinearLayout)link.get_view(R.id.display_window);
		if(patterns == null){
			patterns = link.get_pattern_adapter();
			patterns.add(new Pattern("No Pattern", link.get_new_scrollview(display_window), link.get_die_crafter()));
		}
		if(roll_window == null)
			roll_window = (LinearLayout)link.get_inflater().inflate(R.layout.display_diddly, display_window, false);
		
		if(display_window.getChildCount() == 0){
			selection = 0;
			display_window.addView(roll_window);
		}
		spinner.setAdapter(patterns);
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
		TextView tv = (TextView)link.get_view(R.id.display);
		String data = in.save_pattern();
		String file_name = "Dice_"+ in.get_saved_number();
		
		try {
			FileOutputStream stream = link.open_output(file_name);
			stream.write(data.getBytes());
			stream.close();
			stream = link.open_output("Dice_0");
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
		TextView tv = (TextView)link.get_view(R.id.display);
		
		try {
			FileInputStream in = link.open_input("Dice_0");
			if(in == null)
				return;
			
			String temp ;
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			int count = Integer.parseInt(br.readLine());
			in.close();

			for(int i = 1;i <= count;++i){
				in = link.open_input("Dice_"+i);
				br = new BufferedReader(new InputStreamReader(in));
				patterns.add(new Pattern(br, link.get_new_scrollview(display_window), link.get_die_crafter()));
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
	 * Screen changing button methods
	 */
	
	public void help(){
		display_window.removeAllViews();
		display_window.addView(roll_window);
		TextView tv = (TextView)link.get_view(R.id.display);
		
		if(selection == 0){
			tv.setText("This is the Roll screen help menu.\n\t1) You must set a pattern in the pattern screen.\n\t2) Come back to this Roll Screen.\n\t3) Press Roll Again to make a roll.");
		}
		else if(selection ==1){
			tv.setText("This is the pattern help menu.");
		}
		
		selection = 2;
	}
	public void roll(){
		if(selection != 0){
			selection = 0;
			display_window.removeAllViews();
			display_window.addView(roll_window);
		}
		else{
			((EditText)roll_window.findViewById(R.id.display)).setText(((Pattern)spinner.getSelectedItem()).roll());
		}
	}
	public void pattern(){
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
		
		Pattern temp = new Pattern("Pattern " + patterns.getCount(), (ScrollView)link.get_inflater().inflate(R.layout.display, display_window, false), link.get_die_crafter());
		patterns.add(temp);
		display_window.removeAllViews();
		
		spinner.setAdapter(patterns);
		spinner.setSelection(patterns.getCount()-1);
		
		display_window.addView(temp.get_scroller());
	}
	
	
	
	/*
	 * 
	 */
	public void submit(View view){
		Spinner spinner = (Spinner)link.get_view(R.id.roll_selector);
		
		if(selection == 1){
			if(spinner.getSelectedItem() == patterns.getItem(0)){
				EditText display = (EditText)link.get_view(R.id.display);
				//EditText input = (EditText)findViewById(R.id.input);
				//display.setText(input.getText().toString());
			}
		}
	
	}
}

package madness.dice;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import android.widget.LinearLayout;
import android.widget.ScrollView;

public class Pattern {
	/*
	 * This class holds a pattern of dice.
	 */
	String name;
	ArrayList<Die> dice = new ArrayList<Die>();
	ScrollView scroller;
	LinearLayout scroll_box;
	die_crafter crafter;
	
	int saved_number = -1;
	static int saves = 0;
	
	public static int get_saves(){
		return saves;
	}
	
	public Pattern(BufferedReader in, ScrollView view, die_crafter craft){
		/*
		 * This constructor is used when building from load
		 */
		scroller = view;
		scroll_box = (LinearLayout)view.findViewById(R.id.scroller_display);
		
		try {
			name = in.readLine();
			String temp;
			crafter = craft;
			while((temp = in.readLine()) != null){
				Die temp_die = craft.new_die(scroll_box);
				temp_die.load_die(temp);
				dice.add(temp_die);
				scroll_box.addView(temp_die.get_die());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Pattern(String name, ScrollView in, die_crafter craft){
		scroller = in;
		scroll_box = (LinearLayout)in.findViewById(R.id.scroller_display);
		
		this.name = name;
		crafter = craft;
		
		dice.add(crafter.new_die(scroll_box));
		scroll_box.addView(dice.get(0).get_die());
		
	}
	
	public String roll(){
		String temp = "";
		
		for(int i = 0; i < dice.size(); ++i)
			temp += dice.get(i).roll_dice() + "\n";
		
		return temp;
	}
	public void add_dice(){
		Die temp = crafter.new_die(scroll_box);
		dice.add(temp);
		scroll_box.addView(temp.get_die());
	}
	public void remove_dice(LinearLayout in){
		scroll_box.removeView(in);
		for(int i = 0; i < dice.size();++i){
			if(dice.get(i).dice == in){
				dice.remove(i);
				return;
			}
		}
	}
	public void refresh(){
		for(int i = 0; i < dice.size();++i){
			dice.get(i).refresh();
		}
	}
	
	public ScrollView get_scroller(){
		return scroller;
	}
	
	public String save_pattern(){
		String temp = name + "\n";
		for(int i = 0; i < dice.size(); ++i){
			temp += dice.get(i).get_save_string() + "\n";
		}
		return temp;
	}
	public int get_saved_number(){
		/*
		 * files will be saved 'Dice_X' where X is the saved number
		 */
		if(saved_number == -1){
			saved_number = ++saves;
		}
		return saved_number;
	}
	
	
	public String toString(){
		return name;
	}
	

	public interface die_crafter{
		Die new_die(LinearLayout in);
	}
}

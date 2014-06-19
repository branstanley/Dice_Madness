package madness.dice;

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
	
	public Pattern(String in, ScrollView view, die_crafter craft, Boolean isLoading){
		/*
		 * This constructor is used when building from load
		 */
		String [] temp = in.split("\n");
		scroller = view;
		scroll_box = (LinearLayout)view.findViewById(R.id.scroller_display);
		
		name = temp[0];
		for(int i = 1; i < temp.length;++i){
			Die temp_die = craft.new_die(scroll_box);
			temp_die.load_die(temp[i]);
			dice.add(temp_die);
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

package madness.dice;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import android.widget.LinearLayout;
import android.widget.ScrollView;

public class Pattern implements Die.updater{
	/*
	 * This class holds a pattern of dice.
	 */
	String name;
	ArrayList<Die> dice = new ArrayList<Die>();
	ScrollView scroller;
	LinearLayout scroll_box;
	die_crafter crafter;
	
	int saved_number = -1, link_number = -1;
	static int saves = 0;
	
	public static int get_saves(){
		return saves;
	}
	
	public Pattern(BufferedReader in, ScrollView view, die_crafter craft, int linker_number){
		/*
		 * This constructor is used when building from load
		 */
		link_number = linker_number;
		scroller = view;
		scroll_box = (LinearLayout)view.findViewById(R.id.scroller_display);
		String [] temp;
		String temp2;
		
		try {
			temp  = in.readLine().split(",");
			name = temp[0];
			saved_number = Integer.parseInt(temp[1]);
			
			crafter = craft;
			while((temp2 = in.readLine()) != null){
				Die temp_die = craft.new_die(scroll_box);
				temp_die.load_die(temp2);
				temp_die.set_updater(this);
				dice.add(temp_die);
				scroll_box.addView(temp_die.get_die());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public String roll(){
		String temp = "";
		
		for(int i = 0; i < dice.size(); ++i)
			temp += dice.get(i).roll_dice() + "\n";
		
		return temp;
	}
	public void add_dice(){
		Die temp = crafter.new_die(scroll_box);
		temp.set_updater(this);
		dice.add(temp);
		scroll_box.addView(temp.get_die());
		crafter.update_pattern(save_pattern(), link_number);
	}
	public void remove_dice(LinearLayout in){
		scroll_box.removeView(in);
		for(int i = 0; i < dice.size();++i){
			if(dice.get(i).dice == in){
				dice.remove(i);
				crafter.update_pattern(save_pattern(), link_number);
				return;
			}
		}
		crafter.update_pattern(save_pattern(), link_number);
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
		String temp = name + "," + saved_number +"\n";
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
	
	public void update(){
		crafter.update_pattern(save_pattern(), link_number);
	}
	

	public interface die_crafter{
		Die new_die(LinearLayout in);
		public void update_pattern(String in, int link_num);
	}
}

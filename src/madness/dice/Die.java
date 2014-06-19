package madness.dice;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Die{
	String name;
	int num_dice, dice_sides, bonus;
	boolean bonus_is_per_roll;
	LinearLayout dice;
	
	public Die(LinearLayout in){
		dice = in;
	}
	
	public LinearLayout get_die(){
		return dice;
	}
	public String get_save_string(){
		String temp = "";
		roll_dice();
		temp = name + ","+ num_dice + "," + dice_sides + "," + bonus + "," + bonus_is_per_roll;
		return temp;
	}
	
	public void load_die(String in){
		String [] temp = in.split(",");
		name = temp[0];
		num_dice = Integer.parseInt(temp[1]);
		dice_sides = Integer.parseInt(temp[2]);
		bonus = Integer.parseInt(temp[3]);
		bonus_is_per_roll = Boolean.parseBoolean(temp[4]);
	}
	public String roll_dice(){
		
		name = ((EditText)dice.findViewById(R.id.name)).getText().toString();
		String out =  name + "\n";
		int total = 0, temp;

		try{
			num_dice = Integer.parseInt(((EditText)dice.findViewById(R.id.num_dice)).getText().toString());
		}catch(Exception e){
			return "Error in '" + name +"'.  Invalid number of dice value: " + ((EditText)dice.findViewById(R.id.num_dice)).getText().toString() + "\n";
		}
		try{
			dice_sides = Integer.parseInt(((EditText)dice.findViewById(R.id.dice_sides)).getText().toString());
		}catch(Exception e){
			return "Error in '" + name +"'.  Invalid number of sides value: " + ((EditText)dice.findViewById(R.id.dice_sides)).getText().toString() + "\n";
		}
		try{
			bonus = Integer.parseInt(((EditText)dice.findViewById(R.id.bonus)).getText().toString());
		}catch(Exception e){
			return "Error in '" + name +"'.  Invalid bonus value: " + ((EditText)dice.findViewById(R.id.bonus)).getText().toString() + "\n";
		}
		bonus_is_per_roll = ((CheckBox)dice.findViewById(R.id.all_bonus)).isChecked();
		
		for(int i = 0; i < num_dice; ++i){
			temp = (int)Math.ceil(Math.random() * dice_sides);
			if(bonus_is_per_roll)
				temp += bonus;
			
			out += " " + temp;
			total += temp;
		}
		if(!bonus_is_per_roll)
			total += bonus;
		out += " = " + total + "\n";
		return out;
	}
	
}

package madness.dice;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Die implements TextWatcher{
	protected String name = "Roll Name";
	int num_dice = 0, dice_sides = 0, bonus = 0;
	boolean bonus_is_per_roll = false;
	LinearLayout dice;
	updater update;
	
	Boolean refresh_flag = false;
	
	public interface updater{
		void update();
	}
	
	public Die(LinearLayout in){
		dice = in;
		((EditText)dice.findViewById(R.id.name)).addTextChangedListener(this);
		((EditText)dice.findViewById(R.id.num_dice)).addTextChangedListener(this);
		((EditText)dice.findViewById(R.id.dice_sides)).addTextChangedListener(this);
		((EditText)dice.findViewById(R.id.bonus)).addTextChangedListener(this);
		((CheckBox)dice.findViewById(R.id.all_bonus)).setChecked(bonus_is_per_roll);
		
	}
	
	public void set_updater(updater in){
		update = in;
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
		name = "" + temp[0];
		num_dice = Integer.parseInt(temp[1]);
		dice_sides = Integer.parseInt(temp[2]);
		bonus = Integer.parseInt(temp[3]);
		bonus_is_per_roll = Boolean.parseBoolean(temp[4]);
		
	}
	public void refresh(){
		refresh_flag = true;
		Log.i("refresh", "Check: "+ name);
		
		((EditText)dice.findViewById(R.id.name)).setText(name);
		
		((EditText)dice.findViewById(R.id.num_dice)).setText("" + num_dice);
		((EditText)dice.findViewById(R.id.dice_sides)).setText("" + dice_sides);
		((EditText)dice.findViewById(R.id.bonus)).setText("" + bonus);
		((CheckBox)dice.findViewById(R.id.all_bonus)).setChecked(bonus_is_per_roll);
		refresh_flag = false;
	}
	
	public String grab_current_values(){
		name = ((EditText)dice.findViewById(R.id.name)).getText().toString();
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
		return "";
	}
	public String roll_dice(){
		String out = grab_current_values();
		if(out != "")
			return out;
		
		out =  name + "\n";
		int total = 0, temp;

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

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		if(!refresh_flag){
			grab_current_values();
			update.update();
		}
	}
	
}

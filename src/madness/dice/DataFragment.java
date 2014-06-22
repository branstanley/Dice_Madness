package madness.dice;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

import madness.dice.MainFragment.main_communication;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class DataFragment extends Fragment{
	ArrayList<String>  data = new ArrayList<String> ();
	String whatever = null;
	
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
	}
	
	@Override
	public void onDetach(){
		super.onDetach();
		
	}
	
	@Override
	public void onCreate(Bundle state){
		super.onCreate(state);
		setRetainInstance(true);
	}
	
	public void add_pattern(String in){
		data.add(in);
		whatever = in;
	}
	
	public BufferedReader get_pattern_data(int pos){
		//return new BufferedReader(new StringReader(data.get(pos)));
		return new BufferedReader(new StringReader(whatever));
	}
	

}

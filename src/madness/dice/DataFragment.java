package madness.dice;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

import madness.dice.MainFragment.main_communication;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle; 
import android.util.Log;
import android.widget.ArrayAdapter;

public class DataFragment extends Fragment{
	ArrayList<String>  data = new ArrayList<String> ();
	
	public DataFragment(){
		
	}
	
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
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	public void add_pattern(String in){
		data.add(in);
	}
	public void update(String in, int number){
		data.set(number, in);
	}
	
	public BufferedReader get_pattern_data(int pos){
		return new BufferedReader(new StringReader(data.get(pos)));
	}
	
	public int size(){
		return data.size();
	}
	
	

}

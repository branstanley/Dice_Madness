package madness.dice;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class pattern_window extends ListFragment{
	pattern_interface mCallback;

	interface pattern_interface{
		
	}
	
	public void add_dice(View view){
		LinearLayout pattern_box = (LinearLayout) getView().findViewById(R.id.pattern_box);
		
		
		//pattern_box.addView(child);
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (pattern_interface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}

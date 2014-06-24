package madness.dice;

public class Test {
	public static String test(String in){
		String out = "";
		if(in.length() == 1)
			return in + "\n";
		for(int i = 0; i < in.length();++i){
			StringBuilder sb = new StringBuilder(in);
			sb.deleteCharAt(i);
			out += test(sb.toString());
		}
		
		return out;
	}
	
}

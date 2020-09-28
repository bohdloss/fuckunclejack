package com.bohdloss.fuckunclejack.main;

import java.io.PrintStream;
import java.util.ArrayList;

public class PrintListener {
	
static {
		edited = new PrintStream(System.out) {
			
			static final String prefix="\033[38;5;39m";
			static final String suffix="";//"\033[0m";
			
			public void print(boolean b) {
				try {
					listener.execute(String.valueOf(b));
				} catch (Throwable e) {
					e.printStackTrace();
				}
		        super.print(prefix+b+suffix);
		    }
			
			public void print(char c) {
				try {
					listener.execute(String.valueOf(c));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				super.print(prefix+c+suffix);
		    }
			
			public void print(int i) {
				try {
					listener.execute(String.valueOf(i));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				super.print(prefix+i+suffix);
		    }
			
			public void print(long i) {
				try {
					listener.execute(String.valueOf(i));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				super.print(prefix+i+suffix);
		    }
			
			public void print(float i) {
				try {
					listener.execute(String.valueOf(i));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				super.print(prefix+i+suffix);
		    }
			
			public void print(double i) {
				try {
					listener.execute(String.valueOf(i));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				super.print(prefix+i+suffix);
		    }
			
			public void print(char[] i) {
				try {
					listener.execute(String.valueOf(i));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				super.print(prefix+String.valueOf(i)+suffix);
		    }
			
			public void print(String i) {
				try {
					listener.execute(String.valueOf(i));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				super.print(prefix+i+suffix);
		    }
			
			public void print(Object i) {
				try {
					listener.execute(String.valueOf(i));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				super.print(prefix+i+suffix);
		    }
			
			public void println(boolean b) {
				String val = String.valueOf(b)+'\n';
		        print(val);
		    }
			
			public void println(char c) {
				String val = String.valueOf(c)+'\n';
		        print(val);
		    }
			
			public void println(int i) {
				String val = String.valueOf(i)+'\n';
		        print(val);
		    }
			
			public void println(long i) {
				String val = String.valueOf(i)+'\n';
				print(val);
		    }
			
			public void println(float i) {
				String val = String.valueOf(i)+'\n';
		        print(val);
		    }
			
			public void println(double i) {
				String val = String.valueOf(i)+'\n';
		        print(val);
		    }
			
			public void println(char[] i) {
				String val = String.valueOf(i)+'\n';
		        print(val);
		    }
			
			public void println(String i) {
				String val = i+'\n';
		        print(val);
		    }
			
			public void println(Object i) {
				String val = String.valueOf(i)+'\n';
		        print(val);
		    }
			
		};
		
		err = new PrintStream(System.out) {
			
			static final String prefix="\033[38;5;198m";
			static final String suffix="";//"\033[0m";
			
			public void print(boolean b) {
				try {
					listener.execute(String.valueOf(b));
				} catch (Throwable e) {
					e.printStackTrace();
				}
		        super.print(prefix+b+suffix);
		    }
			
			public void print(char c) {
				try {
					listener.execute(String.valueOf(c));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				super.print(prefix+c+suffix);
		    }
			
			public void print(int i) {
				try {
					listener.execute(String.valueOf(i));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				super.print(prefix+i+suffix);
		    }
			
			public void print(long i) {
				try {
					listener.execute(String.valueOf(i));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				super.print(prefix+i+suffix);
		    }
			
			public void print(float i) {
				try {
					listener.execute(String.valueOf(i));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				super.print(prefix+i+suffix);
		    }
			
			public void print(double i) {
				try {
					listener.execute(String.valueOf(i));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				super.print(prefix+i+suffix);
		    }
			
			public void print(char[] i) {
				try {
					listener.execute(String.valueOf(i));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				super.print(prefix+String.valueOf(i)+suffix);
		    }
			
			public void print(String i) {
				try {
					listener.execute(String.valueOf(i));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				super.print(prefix+i+suffix);
		    }
			
			public void print(Object i) {
				try {
					listener.execute(String.valueOf(i));
				} catch (Throwable e) {
					e.printStackTrace();
				}
				super.print(prefix+i+suffix);
		    }
			
			public void println(boolean b) {
				String val = String.valueOf(b)+'\n';
		        print(val);
		    }
			
			public void println(char c) {
				String val = String.valueOf(c)+'\n';
		        print(val);
		    }
			
			public void println(int i) {
				String val = String.valueOf(i)+'\n';
		        print(val);
		    }
			
			public void println(long i) {
				String val = String.valueOf(i)+'\n';
				print(val);
		    }
			
			public void println(float i) {
				String val = String.valueOf(i)+'\n';
		        print(val);
		    }
			
			public void println(double i) {
				String val = String.valueOf(i)+'\n';
		        print(val);
		    }
			
			public void println(char[] i) {
				String val = String.valueOf(i)+'\n';
		        print(val);
		    }
			
			public void println(String i) {
				String val = i+'\n';
		        print(val);
		    }
			
			public void println(Object i) {
				String val = String.valueOf(i)+'\n';
		        print(val);
		    }
			
		};
		
		listener = new ArgFunction<Object>() {
			public Object execute(Object...objects) {
				
				return null;
			}
		};
		
		replace();
}

public static void replace() {
	System.setOut(edited);
	System.setErr(err);
}

private static final PrintStream edited;
private static final PrintStream err;
private static ArgFunction<Object> listener;

public static ArgFunction<Object> getListener() {
	return listener;
}
public static void setListener(ArgFunction<Object> listener) {
	if(listener==null) return;
	PrintListener.listener = listener;
}

}

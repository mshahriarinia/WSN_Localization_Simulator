package utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

//interface I {
//}
//
//class A {
//}
//
//class B extends A implements I {
//}

public class a {
	public static void main(String[] args) {
		// A ab = new A();
		// B b = new B();
		// a c = new a();
		// if (ab instanceof B)
		// System.out.print("1");
		// if (b instanceof A)
		// System.out.print("2");
		// if (c instanceof a)
		// System.out.print("3");
		// if (c instanceof I)
		// System.out.print("4");

		// int k = 0;
		// for(int i=47; i < 342; i=i+2){
		// k+=i;
		// }
		// System.out.println(k);

		// String a = null;
		// boolean b =(a != null) & a.length() > 10;
		// System.out.println(b);

		// keyVal();

		isPalindrome("Ah, Satan sees Natasha");
		
		ArrayList<Object> list = new ArrayList<Object>();
		list.add("");

	}

	private static void keyVal() {
		try {
			Scanner scanner = new Scanner(new File("c:/temp/a.txt"));

			HashMap hashMap = new HashMap();

			while (scanner.hasNextLine()) {
				String s = scanner.nextLine();
				if (!s.trim().equals("")) {
					String[] split = s.split(",");
					String key = split[0];
					int count = Integer.parseInt(split[1]);

					if (hashMap.containsKey(key)) {
						hashMap.put(key, count + (Integer) hashMap.get(key));
					} else
						hashMap.put(key, count);
				}
			}

			for (Object o : hashMap.keySet()) {
				System.out.print("The total for " + o + " is " + hashMap.get(o)
						+ ". ");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static boolean isPalindrome(String s) {
		s = s.toLowerCase();
		String pureString = "";
		for (int i = 0; i < s.length(); i++)
			if (Character.isLetterOrDigit(s.charAt(i)))
				pureString += s.charAt(i);
		
		for (int i = 0; i < pureString.length(); i++) {
			if (pureString.charAt(i) != pureString.charAt(pureString.length() - 1 - i))
			{
				System.out.println("Is not Palindrome.");
				return false;
			}
		}
		System.out.println("Is Palindrome.");

		return true;		
	}
}
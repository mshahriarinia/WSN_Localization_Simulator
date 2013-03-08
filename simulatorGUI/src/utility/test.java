package utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import control.ZXControl;

import simulator.Simulator;

public class test {
	public static void main(String[] args) {

		String path = "C:/z Personal/ShahriariNia/Workbench/Workspaces/WorkspaceSim/outputs/1/Graphs/plain";
		path = ZXControl.OUTPUT_DIRECTORY + "1005";
		
		String pathAlreadyRun = ZXControl.OUTPUT_DIRECTORY+"run 1/";
//		path= pathAlreadyRun;
		
		File f = new File(path);
		File[] listFiles = f.getAbsoluteFile().listFiles();

		
		
		File fAlreadyRun = new File(pathAlreadyRun);
		File[] listFilesAlreadyRun;
		listFilesAlreadyRun = fAlreadyRun.getAbsoluteFile().listFiles();
		listFilesAlreadyRun =null;
		//
		List<String> listNames = new ArrayList<String>();
		if(listFilesAlreadyRun!= null)
		for (int i = 0; i < listFilesAlreadyRun.length; i = i + 1) {
			if (listFilesAlreadyRun[i].getName().contains("Network"))
				listNames.add(listFilesAlreadyRun[i].getName().split(" ")[0]);
		}

		PrintStream ps = null;
		try {
			ps = new PrintStream(new File(ZXControl.OUTPUT_DIRECTORY
					+ "Exceptions" + Math.random() + ".txt"));

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		Scanner sc;
		try {
			// sc = new Scanner(listFiles[0].listFiles()[0]);
			// while(sc.hasNext())
			// System.out.println(sc.nextLine());
			// 
//			for (int i = 3 * listFiles.length / 4 - 2; i < listFiles.length; i = i + 2) {

				for (int i = 0; i < listFiles.length; i = i + 1) {
					
				
				File temp = listFiles[i];
				// File temp = new -------------1644, 1645
				// File("C:/z Personal/ShahriariNia/Workbench/Workspaces/WorkspaceSim/outputs/1/Graphs/plain/1 - Network.txt");

				System.out.println(temp.getName());
				String prefix = temp.getName().split(" ")[0];
				System.out.println(prefix);
				if (!isAlreadyVisited(prefix, listNames)) {

					try {
						Simulator s = new Simulator(null, temp, prefix);
						s.run();
					} catch (Exception e) {
						ps.println(temp);
						e.printStackTrace(ps);
						ps.println();

					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		ps.close();
		System.out.println();
	}

	private static boolean isAlreadyVisited(String name, List<String> listNames) {
//		for (int i = listNames.size() - 1; i > 0; i--) {
//			String temp = listNames.get(i);
//			if (temp.equals(name)) {
//				System.out.println("already "+temp);
//				return true;
//			}
//		}
		return false;
	}

	// 1{
	// int n = 100;
	//
	// int anchorPercent = 11;
	//
	// int anchorCount = (int) Math.ceil(((n * anchorPercent) / 100.0));
	//
	// System.out.println(anchorCount);
	//
	// anchorPercent = (int) Math.floor(anchorCount * 100.0 / n);
	//
	// System.out.println(anchorPercent);
	// }1

	double fact(int n) {

		double multiplier = 1;
		for (int i = 1; i <= n; i++) {
			multiplier *= i;
		}
		return multiplier;
	}

	double rfact(int n) {
		if (n == 0)
			return 1;
		else
			return n * rfact(n - 1);
	}

	static double fib(int n) {
		double biggest = 1, prev = 1;
		double temp, sum = 0;
		while (biggest < n) {
			temp = prev;
			sum += biggest;
			prev = biggest;
			biggest = biggest + temp;
		}
		return sum;
	}

	boolean ana(String s, List<String> l) {
		for (String ls : l) {
			boolean hasAllChars = true;
			for (int i = 0; i < s.length(); i++) {
				if (l.get(i).indexOf(s.charAt(i)) == -1) {
					hasAllChars = false;
					break;
				}
			}
			if (hasAllChars)
				return true;
		}
		return false;
	}

}

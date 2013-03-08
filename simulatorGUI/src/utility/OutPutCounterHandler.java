package utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import control.ZXControl;

public class OutPutCounterHandler {
	private int outputFileCounter = -1;

	private static String FILE_DIRECTORY = ZXControl.OUTPUT_DIRECTORY
			+ "Reference Files//";

	private static String FILE_NAME = "Output File Counter" + ".txt";

	public OutPutCounterHandler() {
		loadAndIncCount();
	}

	private void loadAndIncCount() {

		Scanner scanner;
		try {
			scanner = new Scanner(new File(FILE_DIRECTORY + FILE_NAME));
			outputFileCounter = scanner.nextInt();
			scanner.close();

			FileOutputStream fos = new FileOutputStream(FILE_DIRECTORY
					+ FILE_NAME);
			PrintStream ps = new PrintStream(fos);

			ps.print(++outputFileCounter);

			scanner.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * if not already loaded, load and inc
	 * 
	 * @return
	 */
	public int getCurrentOutputFileCounter() {
		if (outputFileCounter == -1)
			loadAndIncCount();
		return outputFileCounter;
	}
}

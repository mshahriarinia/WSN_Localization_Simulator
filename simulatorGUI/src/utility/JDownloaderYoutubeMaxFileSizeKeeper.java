package utility;

import java.io.File;
import java.util.HashMap;

/**
 * JDownloader downloads multiple versions of a video file on youtube, in the
 * following name formats: <li>FILE_NAME(240p_H.264-AAC).mp4</li> <li>
 * FILE_NAME(240p_H.264-AAC).flv</li> <li>FILE_NAME(360p_H.264-AAC).flv</li> <li>
 * FILE_NAME(480p_H.264-AAC).flv</li> <li>FILE_NAME(720p_H.264-AAC).mp4</li> <li>
 * ... or any other file-pixel-combination different versions of the video
 * available.</li>
 * <p>
 * <br>
 * This program will remove from the last opening parenthesis to the end of the
 * file name (which is the format and extension description) and among files
 * with similar names will pick the one with maximum size and move the rest to
 * the trash directory which will be created under the directory of the video
 * files. <br>
 * Date: 04/26/2011
 */
public class JDownloaderYoutubeMaxFileSizeKeeper {

	public static void main(String[] args) {
		String directory = "C:\\Users\\Morteza\\Downloads\\Youtube Download";
		// directory += "\\Cartoon";
		File dir = new File(directory);
		File trashDir = new File(directory + "\\trash");
		trashDir.mkdir();

		HashMap h = new HashMap();

		File[] dirList = dir.listFiles();
		for (File f : dirList) {
			String s = f.getName();
			if (s.endsWith("mp4") || s.endsWith("flv")) {
				String s0 = strip(s);
				// System.out.println(s + " __ " + f.length() + " __ " +
				// strip(s));
				if (!h.containsKey(s0)) {
					File fileMax = getMax(dirList, s0);
					h.put(s0, fileMax);
				}
			}

		}

		for (File f : dirList) {
			String s = f.getName();
			if (s.endsWith("mp4") || s.endsWith("flv")) {
				String s0 = strip(s);

				File hVal = (File) h.get(s0);
				if (!(hVal.equals(f))) {
					boolean success = f
							.renameTo(new File(trashDir, f.getName()));
					if (!success) {
						System.err.println("NO SUCCESS.");
					}
				}
			}
		}
	}

	private static File getMax(File[] farr, String baseName) {
		File fmax = null;
		long fmaxSize = 0;
		for (File f : farr) {
			String s = f.getName();
			if (s.contains("mp4") || s.contains("flv")) {
				if (s.contains(baseName)) {
					if (f.length() > fmaxSize) {
						fmax = f;
						fmaxSize = f.length();
					}
				}
			}
		}
		return fmax;
	}

	static String strip(String s) {
		System.out.println(s);
		return s.substring(0, s.lastIndexOf('('));
	}
}

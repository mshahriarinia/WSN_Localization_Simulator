package utility;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

public class PointDistanceCalculator {

	public static void main(String[] args) {

		int[][] o = {{1, 2}, {2, 3}};
		
		List p = Arrays.asList(o);
		
		double dist = 0;

		Point2D p1 = null, p2 = null;

		p1 = new Point2D.Double(322.63622118672913, 90.14125240778897);

		p2 = new Point2D.Double(0, 450.0);
		//
		// dist += p1.distance(p2);
		//
		// p1 = new Point2D.Double(104.17633867438725, 335.4187105932173);
		//
		// dist += p1.distance(p2);
		//
		// p1 = new Point2D.Double(74.21328801053784, 406.90083647432795);
		//
		dist += p1.distance(p2);
		System.out.println(dist);
	}

}

package algorithms.dv;

import java.awt.geom.Point2D;
import java.util.List;

import node.type.Anchor;
import simulator.localizable.MSLostNode;
import algorithms.dv.sysEqSolver.jama.Matrix;

public class DV {

	/**
	 * provide the equations and get position estimation.
	 * 
	 * @param node
	 * @return
	 */
	public static Point2D getInitDV(MSLostNode node) {

		List<Anchor> anchors = null;
		try {
			anchors = node.getMemory().getAnchors();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int n = anchors.size();
		double[][] A = new double[n][2];
		double[] B = new double[n];

		double dn_1 = node.getMemory().getAnchorsNPDs()[n - 1].getHopCount()
				* node.getAverageHopSize();

		for (int i = 0; i < n; i++) {

			// subtract one of the equations from the others to remove the x^2
			// and y^2 from the rest

			A[i][0] = 2 * (anchors.get(i).getCalculatedCenter().getX() - anchors
					.get(n - 1).getCalculatedCenter().getX());
			A[i][1] = 2 * (anchors.get(i).getCalculatedCenter().getY() - anchors
					.get(n - 1).getCalculatedCenter().getY());

			double di = node.getMemory().getAnchorsNPDs()[i].getHopCount()
					* node.getAverageHopSize();

			// (x - a)^2 + (y-b)^2 = r2

			// B[i] = anchors.get(i).getCalculatedCenter().getX()
			// * anchors.get(i).getCalculatedCenter().getX()
			// - anchors.get(n - 1).getCalculatedCenter().getX()
			// * anchors.get(n - 1).getCalculatedCenter().getX()
			// + anchors.get(i).getCalculatedCenter().getY()
			// * anchors.get(i).getCalculatedCenter().getY()
			// - anchors.get(n - 1).getCalculatedCenter().getY()
			// * anchors.get(n - 1).getCalculatedCenter().getY() + dn_1
			// * dn_1 - di * di;

			B[i] = pow2(anchors.get(i).getCalculatedCenter().getX())
					- pow2(anchors.get(n - 1).getCalculatedCenter().getX())
					+ pow2(anchors.get(i).getCalculatedCenter().getY())
					- pow2(anchors.get(n - 1).getCalculatedCenter().getY())
					+ pow2(dn_1) - pow2(di);
		}

		Matrix mA = new Matrix(A);

		Matrix mB = new Matrix(B, B.length);

		Matrix mX = null;
		try {
			mX = mA.solve(mB); // A*X = B
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("A: ");
			for (int i = 0; i < A.length; i++) {
				for (int j = 0; j < A[0].length; j++) {
					System.err.print(A[i][j] + " ");
				}
				System.err.println();
			}

			System.err.println("B: ");
			for (int i = 0; i < B.length; i++) {

				System.err.print(B[i] + " ");

			}
			System.out.println();
		}

		return new Point2D.Double(mX.get(0, 0), mX.get(1, 0));
	}

	private static double pow2(double d) {
		return d * d;
	}

}

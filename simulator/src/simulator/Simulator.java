package simulator;

import guiInterface.IPrimitiveGUIInterface;

import java.io.File;

import node.ICycler;
import node.Node;
import node.NodeList;
import node.Resetable;
import reporter.ConsoleReporter;
import reporter.ExecutionReporter;
import reporter.MSAngularReporter;
import reporter.NetworkErrorCycleReporter;
import reporter.NetworkMapReporter;
import simulator.localizable.field.FieldGenerator;
import control.ZXControl;
import control.ZXControl.ETYPE;

/**
 * 
 * We have four major projects:
 * <p>
 * <b>1 - simulatorGeometry:</b> which deals with the geometric issues of nodes,
 * signals, areas, constants, ...
 * <p>
 * <b>2 - simulator:</b> which contains the main process of simulations and the
 * execution of localization protocol
 * <p>
 * <b>3 - guiInterface:</b> is a generic gui that is encoded and used in the
 * simulation process but makes it indepent of the actual implementation of the
 * GUI
 * <p>
 * <b>4 - simulatorGUI:</b> is the SWT implementation of a GUI for the simulator
 */

public class Simulator implements Runnable, ICycler, Resetable {

	private int cycleCount;

	private NodeList nodeList;

	private FieldGenerator fieldGenerator;

	private SimulatorGUIInterface simulatorGUI;

	private IPrimitiveGUIInterface windowGUIInterface;

	private String fileNameIndex;

	private MSAngularReporter msAngularReporter;

	private ExecutionReporter executionReporter;

	/**
	 * continue simulation until all nodes have nothing more to execute
	 */
	private void simulate() {
		boolean totalExecutionflag = true;

		while (totalExecutionflag) {
			// totalExecutionflag = false;

			if (cycleCount == ZXControl.MAX_ANCHORS_BROADCAST)
				if (simulatorGUI != null) {
					// simulatorGUI.drawLogNodeCircles();
				}

			for (int i = 0; i < nodeList.getListNodes().size(); i++) {
				Node node = (Node) nodeList.getListNodes().get(i);
				executionReporter.writeln(">>> " + node);
				if (cycleCount > ZXControl.MAX_ANCHORS_BROADCAST)
					if (node.isLogged())
						System.out.print("");
				boolean exec = node.execute(cycleCount);
				executionReporter.writeln("End " + node);

				totalExecutionflag = exec || totalExecutionflag;

				if (simulatorGUI != null)
					simulatorGUI.drawNode(node);
			}

			report(cycleCount);

			cycleCount++;

			// totalExecutionflag = (cycleCount <= 15);

		}
		/*
		 * System.out.println("\ndispose");
		 * executionReporter.writeln("\ndispose"); dispose();
		 */
		// ConsoleReporter.consoleStatictics(nodeList.getListNodes());
	}

	private void report(int cycleNo) {

		executionReporter.writeEndOfCycle(cycleNo);
		executionReporter.reportDistanceToNeighbors(nodeList.getListNodes());

		msAngularReporter.report(cycleNo);

		if (cycleNo >= ZXControl.MAX_ANCHORS_BROADCAST) {
			NetworkErrorCycleReporter.networkErrorReportFile(nodeList,
					cycleCount);

			String report = "f" + fileNameIndex + " Cycle " + cycleNo
					+ " done.";
			System.out.print(report);
			executionReporter.write(report);
		}
	}

	public Simulator(IPrimitiveGUIInterface windowGUIInterface, File file,
			String fileNamePrefix) throws Exception {
		cycleCount = 1;
		this.fileNameIndex = fileNamePrefix;
		this.windowGUIInterface = windowGUIInterface;

		executionReporter = new ExecutionReporter(fileNameIndex);

		nodeList = new NodeList(this);
		fieldGenerator = new FieldGenerator(nodeList, ZXControl.FIELD_WIDTH,
				ZXControl.FIELD_HEIGHT);

		// initNetwork(loadFile);
		initNetwork(file);

		ConsoleReporter.consoleStatictics(nodeList.getListNodes());

		drawGUI(windowGUIInterface);

		NetworkMapReporter.printNetwork(nodeList.getZXControl(),
				nodeList.getListNodes(), fileNamePrefix);

		String report = "f#(" + fileNamePrefix + ") AVG_HOP_SIZE : "
				+ nodeList.getAverageHopSize();
		System.out.println(report);
		executionReporter.writeln(report);
	}

	@Override
	public void reset() {
		cycleCount = 1;
		nodeList.reset();
		msAngularReporter = new MSAngularReporter(fileNameIndex, nodeList);
		drawGUI(windowGUIInterface);
	}

	@Override
	public void run() {

		// reset();
		// simulate();
		//

		// while(true)
		// System.out.println("ddddddddddddddd");
		//
		ZXControl.Initial_POSITION_ALGORITHM = ETYPE.CENTRALIZED_BEST_ANCHORED_FIRST;
		reset();
		simulate();

		// ZXControl.Initial_POSITION_ALGORITHM = ETYPE.DISCRETE_DV;
		// reset();
		// simulate();
		/*
		 * ZXControl.Initial_POSITION_ALGORITHM = ETYPE.LPD; reset();
		 * simulate();
		 */
//		ZXControl.Initial_POSITION_ALGORITHM = ETYPE.DV;
//		reset();
//		simulate();
		/**
		 * ZXControl.Initial_POSITION_ALGORITHM = ETYPE.CENTROID; reset();
		 * simulate();
		 * 
		 * ZXControl.Initial_POSITION_ALGORITHM = ETYPE.PROXIMITY; reset();
		 * simulate();
		 */
		//

	}

	private void initNetwork(File file) throws Exception {
		if (file != null) {
			new MSLoader().loadFile(file, fieldGenerator);
		} else {
			fieldGenerator.generateAllGraph();
		}
	}

	private void drawGUI(IPrimitiveGUIInterface windowGUIInterface) {
		if (windowGUIInterface != null) {
			simulatorGUI = new SimulatorGUIInterface(windowGUIInterface,
					nodeList);
			simulatorGUI.drawAll();
		}
	}

	public String getFileNamePostFix() {
		return fileNameIndex;
	}

	@Override
	public ExecutionReporter getExecutionReporter() {
		return executionReporter;
	}

	public SimulatorGUIInterface getSimulatorGraphics() {
		return simulatorGUI;
	}

	@Override
	public int getCycleNo() {
		return cycleCount;
	}

	public boolean isCurrent(int cycleNo) {
		return getCycleNo() == cycleNo;
	}

	private void dispose() {

		msAngularReporter.dispose();
		// ConsoleReporter.consoleStatictics(nodeList.getListNodes());
		executionReporter.dispose();
	}
}

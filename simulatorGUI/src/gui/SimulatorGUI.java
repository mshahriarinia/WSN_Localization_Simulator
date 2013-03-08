package gui;

import java.io.File;

import guiInterface.IPrimitiveGUIInterface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import simulator.Simulator;
import utility.OutPutCounterHandler;
import control.ZXControl;

public class SimulatorGUI {

	private final GC gcWindow;

	private Simulator simulator;

	private Shell she;

	private IPrimitiveGUIInterface windowGUIInterface;

	private Thread simulatorThread;

	public static void main(String[] args) {
		try {
			new SimulatorGUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	final Text text;

	public SimulatorGUI() {

		Display display = new Display();
		final Shell shell = new Shell(display);
		she = shell;
		shell.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(final MouseEvent e) {
				text.setText("X: " + e.x + "       Y: " + e.y);
			}
		});

		// shell.addMouseListener(new MouseListener(){
		// @Override
		// public void mouseDoubleClick(MouseEvent e) {
		// }
		//
		// @Override
		// public void mouseDown(MouseEvent e) {
		// shell.close();
		// }
		//
		// @Override
		// public void mouseUp(MouseEvent e) {
		// }
		// });

		shell.setBounds(0, 0, 809, 545);

		shell.setLayout(new FormLayout());

		gcWindow = new GC(shell);

		shell.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				getSimulator().getSimulatorGraphics().drawAll();
			}
		});

		text = new Text(shell, SWT.BORDER);
		final FormData fd_text = new FormData();
		fd_text.top = new FormAttachment(0, 25);
		fd_text.bottom = new FormAttachment(0, 45);
		fd_text.left = new FormAttachment(0, 575);
		fd_text.right = new FormAttachment(0, 750);
		text.setLayoutData(fd_text);

		Button newButton;
		newButton = new Button(she, SWT.NONE);
		newButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				newRun();
			}
		});
		final FormData fd_newButton = new FormData();
		fd_newButton.top = new FormAttachment(0, 25);
		fd_newButton.bottom = new FormAttachment(0, 50);
		fd_newButton.left = new FormAttachment(0, 754);
		fd_newButton.right = new FormAttachment(0, 790);
		newButton.setLayoutData(fd_newButton);
		newButton.setText("New");

		final Button loadButton = new Button(she, SWT.NONE);
		loadButton.setEnabled(false);
		loadButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				newRun();
			}
		});
		final FormData fd_loadButton = new FormData();
		fd_loadButton.left = new FormAttachment(0, 757);
		fd_loadButton.right = new FormAttachment(0, 795);
		fd_loadButton.top = new FormAttachment(0, 55);
		loadButton.setLayoutData(fd_loadButton);
		loadButton.setText("Load");

		// initially load from
		File temp = new File(
		// "C:/z Personal/ShahriariNia/Workbench/Workspaces/WorkspaceSim/outputs/1/Graphs/plain/1311 - Network.txt"
			//	"C:/z Personal/ShahriariNia/Workbench/Workspaces/WorkspaceSim/outputs/Reference Files/Good graphs/15-/graph.txt"
				"C:/temp/outputs/ - Network.txt");
		if (!ZXControl.LOAD_FROM_FILE)
			initBusiness(null);
		else
			initBusiness(temp);

		shell.open();

		execBusiness();

		// finish(display, shell);
	}

	@SuppressWarnings("deprecation")
	private void newRun() {
		// if (simulatorThread != null)
		// simulatorThread.stop();// TODO REMEMBER: deprecated thread usage
		//
		// initBusiness(temp);
		// she.redraw();
		// execBusiness();
	}

	private void execBusiness() {
		simulatorThread = new Thread(simulator);
		simulatorThread.start();// simulator.simulate();
	}

	@SuppressWarnings("deprecation")
	private void finish(Display d, Shell s) {
		while (!s.isDisposed()) {
			if (!d.readAndDispatch()) {
				d.sleep();
			}
		}

		simulatorThread.stop();

		// gcWindow.dispose();
		// gcImages.dispose();

		d.dispose();
	}

	private void initBusiness(File loadFile) {
		try {
			windowGUIInterface = new SWTUIInterface(she, gcWindow);

			// OutPutCounterHandler outPutCounterHandler = new
			// OutPutCounterHandler();
			simulator = new Simulator(windowGUIInterface, loadFile, "");
			// outPutCounterHandler.getCurrentOutputFileCounter()+"");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Simulator getSimulator() {
		return simulator;
	}
}

package utility;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TestSWTAnimation {

	class cthread implements Runnable {

		public cthread() {

		}

		@Override
		public void run() {
			cir();
		}

		@SuppressWarnings("static-access")
		void cir() {

			int w = 100;
			int h = 100;

			Color f = gc.getForeground();
			Color b = gc.getBackground();

			gc.setForeground(b);
			for (int i = 0; i < 100; i++) {
				gc.drawRectangle(0 + i, 0, w, h);
				try {
					Thread.currentThread().sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (i != 100 - 1) {
					gc.setForeground(b);
					gc.drawRectangle(0 + i, 0, w, h);
					gc.setForeground(f);
				}
			}

		}

	}

	private final GC gc;

	public static void main(String[] args) {
		new TestSWTAnimation();
	}

	final Text text;

	public TestSWTAnimation() {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(final MouseEvent e) {
				text.setText("X: " + e.x + "       Y: " + e.y);
			}
		});
		shell.setLayout(new FormLayout());

		gc = new GC(shell);

		shell.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {

			}
		});

		text = new Text(shell, SWT.BORDER);
		final FormData fd_text = new FormData();
		fd_text.right = new FormAttachment(0, 705);
		fd_text.bottom = new FormAttachment(0, 60);
		fd_text.top = new FormAttachment(0, 40);
		fd_text.left = new FormAttachment(0, 530);
		text.setLayoutData(fd_text);

		shell.open();

		cthread c = new cthread();
		new Thread(c).start();
		finish(display, shell);
	}

	private void finish(Display d, Shell s) {
		while (!s.isDisposed()) {
			if (!d.readAndDispatch()) {
				d.sleep();
			}
		}

		d.dispose();
	}

}

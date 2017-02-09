package smalltool;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JFrame;

public class WindowEventListen implements WindowListener{
	public BookComponent bookComponent;
	public FileIO fileIO;
	public JFrame window;
	public WindowEventListen(BookComponent bookComponent,FileIO fileIO,JFrame window) {
		 this.bookComponent=bookComponent;
		 this.fileIO=fileIO;
		 this.window=window;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		/* try {
			fileIO=new FileIO("./", "data.txt");
			bookComponent.addList((fileIO.readEventList(fileIO)));
			
		} catch (IOException e1) {
			 
			e1.printStackTrace();
		}*/

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO 自动生成的方法存根
		System.out.println(fileIO.ifSave);
		if(false==fileIO.ifSave)
			
		fileIO.saveAll(bookComponent.eventListen);
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO 自动生成的方法存根

	}

	
}

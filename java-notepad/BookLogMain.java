package smalltool;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;

public class BookLogMain extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 253856041467031975L;

	public static void main(String[] args) {	
		try {
			JFrame window = new BookLogMain();	
			window.setSize(550, 350);
			FileIO fileIO;
			fileIO = new FileIO("./", "data.txt");
			BookComponent bookComponent=new BookComponent(window,fileIO);//传window对象 目的是控件大小根据窗口大小自行改变
			window.add(bookComponent, BorderLayout.CENTER);
			WindowEventListen windowEventListen = new WindowEventListen(bookComponent,fileIO,window);//传bookComponent对象
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setVisible(true);
			window.setLocationRelativeTo(null);
			window.setTitle("事件记录");
			window.setResizable(false);
			window.addWindowListener(windowEventListen);
			
			 
		} catch (IOException e) {
			 
			e.printStackTrace();
		}
		
	}
}
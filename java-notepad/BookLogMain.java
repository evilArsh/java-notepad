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
			BookComponent bookComponent=new BookComponent(window,fileIO);//��window���� Ŀ���ǿؼ���С���ݴ��ڴ�С���иı�
			window.add(bookComponent, BorderLayout.CENTER);
			WindowEventListen windowEventListen = new WindowEventListen(bookComponent,fileIO,window);//��bookComponent����
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setVisible(true);
			window.setLocationRelativeTo(null);
			window.setTitle("�¼���¼");
			window.setResizable(false);
			window.addWindowListener(windowEventListen);
			
			 
		} catch (IOException e) {
			 
			e.printStackTrace();
		}
		
	}
}
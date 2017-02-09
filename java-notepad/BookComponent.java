package smalltool;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.JList;

public class BookComponent extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -832404708390209304L;
	private JFrame window;
	public JLabel mLabelTip ;
	public DefaultListModel model;
	public JScrollPane mJScrollPane;
	public JScrollPane mJScrollPane2;
	public JList mListEvent;
	public JTextArea mTextAreaDescribe;
	public JButton mButtonAddevent;
	public JButton mButtonSave;
	public FileIO fileIO;
	public Vector<String> listCollection;
	public JComponent mButtonComponent = new JPanel();// 为按钮新增容器
	public EventListen eventListen;
	public static final Font BUTTONFONT = new Font("宋体", Font.LAYOUT_NO_LIMIT_CONTEXT, 18);
	public static final Font TEXTFONT = new Font("宋体", Font.LAYOUT_NO_LIMIT_CONTEXT, 15);
	public static final Font LISTFONT = new Font("宋体", Font.LAYOUT_NO_LIMIT_CONTEXT, 12);

	public BookComponent(JFrame window, FileIO fileIO) {
		this.window = window;
		this.fileIO = fileIO;
		setLayout(null);
		// this.setSize(window.getSize());
		eventListen = new EventListen(this);

		init();
	}

	public void init() {
		model = new DefaultListModel();
		mListEvent = new JList();
		mListEvent.setModel(model);
		setTextArea();
		setButton();

		add(mJScrollPane2);
		add(mJScrollPane);
		add(mButtonComponent);

	}

	public void setTextArea() {
		mTextAreaDescribe = new JTextArea();
		// 如果在没有点击JList时直接点击文本框然后保存会导致Vector数组下标越界从而引发异常，
		// 因为会触发文本框的 insertUpdate事件而在Map集合中添加数据，计数器会增加，
		// 但是Vector并没有新添数据
		mTextAreaDescribe.setEnabled(false);
		mTextAreaDescribe.addMouseListener(eventListen);
		mTextAreaDescribe.getDocument().addDocumentListener(eventListen);
		mJScrollPane = new JScrollPane(mTextAreaDescribe);
		mJScrollPane2 = new JScrollPane(mListEvent);

		mJScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		mTextAreaDescribe.setLineWrap(true);// 换行
		/*
		 * 布局为null，都设置其大小,mButtonEvent的定位是根据mTextAreaDescribe
		 */
		mTextAreaDescribe.setBounds((int) (window.getWidth() * 0.305), 0, (int) (window.getWidth() * 0.5),
				window.getHeight());
		mJScrollPane.setBounds((int) (window.getWidth() * 0.305), 0, (int) (window.getWidth() * 0.5),
				(int) (window.getHeight() * 0.9));
		
		mJScrollPane.getViewport().add(mTextAreaDescribe);//
		addList(fileIO.readEventList());
		
		mListEvent.setFont(LISTFONT);
		mListEvent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// 选择模式
		mListEvent.setBounds(0, 0, (int) (window.getWidth() * 0.3), window.getHeight());
		mJScrollPane2.setBounds(0, 0, (int) (window.getWidth() * 0.3), (int) (window.getHeight() * 0.9));
		mJScrollPane2.getViewport().add(mListEvent);
		mListEvent.setBackground(new Color(204, 232, 207));
		/*
		 * 添加监听
		 */
		mListEvent.addListSelectionListener(eventListen);

	}

	/**
	 * 添加列表
	 * 
	 */
	public void addList(Vector<String> listCollection) {
		model.clear();
		this.listCollection = listCollection;
		for (String string : listCollection) {
			model.addElement(string);
		}
	}

	public void setButton() {

		mButtonComponent.setLayout(new GridLayout(5, 1));//
		mButtonComponent.setBounds(mListEvent.getWidth() + mTextAreaDescribe.getWidth() + 2, 0,
				window.getWidth() - mListEvent.getWidth() - mTextAreaDescribe.getWidth() - 8, window.getHeight());
		mButtonAddevent = new JButton("添加");
		mButtonSave = new JButton("保存");
		mLabelTip= new JLabel("  保存成功! ");
	
		mButtonAddevent.setSize(window.getWidth() - mListEvent.getWidth() - mTextAreaDescribe.getWidth() - 2,
				window.getHeight() / 5);
		mButtonSave.setSize(window.getWidth() - mListEvent.getWidth() - mTextAreaDescribe.getWidth() - 2,
				window.getHeight() / 5);
		mLabelTip.setBounds(mButtonSave.getX(),
				 mButtonAddevent.getHeight() +  mButtonSave.getHeight(),
				 mButtonSave.getWidth(), 20);

		
		mButtonComponent.add(mButtonAddevent);
		mButtonComponent.add(mButtonSave);
		mButtonComponent.add(mLabelTip);
		mButtonAddevent.setFont(BUTTONFONT);
		mButtonSave.setFont(BUTTONFONT);
		mLabelTip.setFont(TEXTFONT);
		mLabelTip.setVisible(false);
		/*
		 * 添加事件监听
		 */
		mButtonAddevent.addMouseListener(eventListen);
		mButtonSave.addMouseListener(eventListen);
		/*
		 * mButtonAddevent.addActionListener(eventListen);
		 * mButtonSave.addActionListener(eventListen);
		 */

	}
}

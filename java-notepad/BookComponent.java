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
	public JComponent mButtonComponent = new JPanel();// Ϊ��ť��������
	public EventListen eventListen;
	public static final Font BUTTONFONT = new Font("����", Font.LAYOUT_NO_LIMIT_CONTEXT, 18);
	public static final Font TEXTFONT = new Font("����", Font.LAYOUT_NO_LIMIT_CONTEXT, 15);
	public static final Font LISTFONT = new Font("����", Font.LAYOUT_NO_LIMIT_CONTEXT, 12);

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
		// �����û�е��JListʱֱ�ӵ���ı���Ȼ�󱣴�ᵼ��Vector�����±�Խ��Ӷ������쳣��
		// ��Ϊ�ᴥ���ı���� insertUpdate�¼�����Map������������ݣ������������ӣ�
		// ����Vector��û����������
		mTextAreaDescribe.setEnabled(false);
		mTextAreaDescribe.addMouseListener(eventListen);
		mTextAreaDescribe.getDocument().addDocumentListener(eventListen);
		mJScrollPane = new JScrollPane(mTextAreaDescribe);
		mJScrollPane2 = new JScrollPane(mListEvent);

		mJScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		mTextAreaDescribe.setLineWrap(true);// ����
		/*
		 * ����Ϊnull�����������С,mButtonEvent�Ķ�λ�Ǹ���mTextAreaDescribe
		 */
		mTextAreaDescribe.setBounds((int) (window.getWidth() * 0.305), 0, (int) (window.getWidth() * 0.5),
				window.getHeight());
		mJScrollPane.setBounds((int) (window.getWidth() * 0.305), 0, (int) (window.getWidth() * 0.5),
				(int) (window.getHeight() * 0.9));
		
		mJScrollPane.getViewport().add(mTextAreaDescribe);//
		addList(fileIO.readEventList());
		
		mListEvent.setFont(LISTFONT);
		mListEvent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// ѡ��ģʽ
		mListEvent.setBounds(0, 0, (int) (window.getWidth() * 0.3), window.getHeight());
		mJScrollPane2.setBounds(0, 0, (int) (window.getWidth() * 0.3), (int) (window.getHeight() * 0.9));
		mJScrollPane2.getViewport().add(mListEvent);
		mListEvent.setBackground(new Color(204, 232, 207));
		/*
		 * ��Ӽ���
		 */
		mListEvent.addListSelectionListener(eventListen);

	}

	/**
	 * ����б�
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
		mButtonAddevent = new JButton("���");
		mButtonSave = new JButton("����");
		mLabelTip= new JLabel("  ����ɹ�! ");
	
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
		 * ����¼�����
		 */
		mButtonAddevent.addMouseListener(eventListen);
		mButtonSave.addMouseListener(eventListen);
		/*
		 * mButtonAddevent.addActionListener(eventListen);
		 * mButtonSave.addActionListener(eventListen);
		 */

	}
}

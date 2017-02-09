package smalltool;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EventListen implements ListSelectionListener, MouseListener, DocumentListener {

	public BookComponent bookComponent;
	public FileIO fileIO;
	public int listFlag;
	public boolean ifChanged = false;
	public boolean ifSaved = false;

	public EventListen(BookComponent bookComponent) {
		this.bookComponent = bookComponent;
		fileIO = bookComponent.fileIO;
		fileIO.readEventText();// �ȶ�ȡ�ı������¼�������
		fileIO.readEventList();// ��ȡ�¼����б�
		System.out.println("�Ѿ�д���ڴ�");
	}

	public static String getDate() {
		Date date = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("(yy/MM/dd HH:mm:ss):\r\n");
		return sd.format(date);
	}

	// List�¼�����
	@Override
	public void valueChanged(ListSelectionEvent es) {
		/*
		 * if (!es.getValueIsAdjusting()) {
		 * System.out.println("First"+es.getFirstIndex());
		 * System.out.println("Last"+es.getLastIndex());
		 * System.out.println("------------------------"); }
		 */
		if(false==bookComponent.mTextAreaDescribe.isEnabled()){
			bookComponent.mTextAreaDescribe.setEnabled(true);
			 
		}
		listFlag = bookComponent.mListEvent.getSelectedIndex();
		bookComponent.mTextAreaDescribe.setText("");
		bookComponent.mTextAreaDescribe.setText(fileIO.evenText.get(new Integer(listFlag)));

	}

	@Override
	public void mouseExited(java.awt.event.MouseEvent e) {

	}

	@Override
	public void mouseClicked(java.awt.event.MouseEvent em) {

		Object object = em.getSource();

		if (object.equals(bookComponent.mTextAreaDescribe))
		{
			if(bookComponent.mTextAreaDescribe.isEnabled()) 
			bookComponent.mTextAreaDescribe.append("\r\n" + getDate());
		}
		else if (object.equals(bookComponent.mButtonAddevent)) {
			JLabel label = new JLabel("������¼�:");
			label.setFont(BookComponent.TEXTFONT);
			String tmp;
			tmp = JOptionPane.showInputDialog(bookComponent, label, "��ʾ", JOptionPane.PLAIN_MESSAGE);// ��һ�Ըı䰴ť���壿
			if (tmp != null) {
				if (tmp.trim().length() != 0) {
					fileIO.writeEvenList(tmp);
					bookComponent.addList(fileIO.eventList);
					fileIO.writeEvenText(getDate());
					bookComponent.mListEvent.setSelectedIndex(bookComponent.mListEvent.getLastVisibleIndex());
					bookComponent.mTextAreaDescribe.requestFocus();
				}
			}

		} else if (object.equals(bookComponent.mButtonSave)) {
			/*
			 * else if ((JButton) em.getSource() == bookComponent.mButtonSave) {
			 */
			if (fileIO.eventKey >= 0) {
				if (!ifSaved || ifChanged) {//�����ظ�ִ�б���
					boolean judge;
					judge = fileIO.saveAll(this);
					fileIO.ifSave = judge;
					System.out.println("�Ƿ񱣴�:"+judge);
					ifChanged = false;
				}

			} else {
				JOptionPane.showMessageDialog(bookComponent,
						new JLabel("<html> <h3><font color='black'>��ӵ�ʲô��</font><h3></html>"));
			}

		}

	}

	/**
	 * �ص�������������ȫ��д���ı��ļ�ʱִ�лص��������û���ΰ����水ť���ж�α����˷��ڴ���Դ
	 */
	public void ifSavedCallback() {
		ifSaved = true;
	}

	/**
	 * �ص����������ı��������޸�ʱִ�лص�
	 */
	public void ifChangedCallback() {
		ifChanged = true;
	}

	@Override
	public void mousePressed(java.awt.event.MouseEvent e) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {
		if(e.getSource().equals(bookComponent.mButtonSave)){
			fileIO.exec.execute(new Runnable() {
				public void run() {
					try {
					if(!fileIO.eventList.isEmpty()){
						if(!bookComponent.mLabelTip.isVisible())
						 bookComponent.mLabelTip.setVisible(true);	
							Thread.sleep(1200);
							bookComponent.mLabelTip.setVisible(false);
						}
					} catch (InterruptedException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
				}
			});
			
		}

	}

	@Override
	public void mouseEntered(java.awt.event.MouseEvent e) {
		/*if (e.getSource().equals(bookComponent.mTextAreaDescribe)) {
			int n = bookComponent.mListEvent.getSelectedIndex();
			fileIO.changeEvenText(n, bookComponent.mTextAreaDescribe.getText(), this);
		}*/
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		// TODO �Զ����ɵķ������

		int n = bookComponent.mListEvent.getSelectedIndex();
		fileIO.changeEvenText(n, bookComponent.mTextAreaDescribe.getText(), this);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {

	}

	@Override
	public void changedUpdate(DocumentEvent e) {

	}

}

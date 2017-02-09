package smalltool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
 

public class FileIO extends File  {
	public String path;
	public String name;
	public Vector<String> eventList;
	public Map<Integer, String> evenText;
	public int eventKey = -1;// �ı�����������ÿ���¼������ݵļ�ֵ
	public Lock lock;
	public boolean ifSave=false;// �Ƿ񱣴����ļ������û�û�б����ļ����˳�ʱ�Զ�����
	public String[] texts;
	public String[] lists;
	public int eFlag = 0;
	public int lFlag = 0;
	public ExecutorService exec=Executors.newFixedThreadPool(3);
	public FileIO(String path, String name) throws IOException {
		super(path, name);
		this.path = path;
		this.name = name;
		this.createNewFile();
		evenText = new HashMap<Integer, String>();

	}

	
	public boolean saveAll(EventListen eventListen) {
		boolean judge;
		boolean a,b;
		lock = new ReentrantLock();
		texts = new String[eventKey+1];
		lists = new String[eventKey+1];
		a=saveEventList();
		b=saveEventText();
		if(a&&b){
		judge=saveToFile(eventListen);
		 
		return judge;}
		else{
			return false;
		}
		 
	}
	
	
	/**
	 * �����¼����ݺ��б��ı��ĵ���
	 * @param eventListen ���лص��Ĳ���
	 * @return 
	 */
	public boolean saveToFile(EventListen eventListen){
		try {
			FileWriter fWriter;
			//File file=new File("./","data.txt");
			fWriter = new FileWriter(this);
			BufferedWriter bWriter=new BufferedWriter(fWriter);
			for(int i=0;i<texts.length;i++){
				bWriter.write("\r\n"+lists[i]+"\r\n");
				bWriter.write(texts[i]);
			}
			bWriter.flush();
			bWriter.close();
			fWriter.close();
			eventListen.ifSavedCallback(); //�ص�
			return true;
		} catch (Exception e) {		
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	 /**
	  * ִ�б����¼����ݵ������е� һ���߳�
	  */
	public boolean saveEventText() {
	 
		/*new Thread(new Runnable() {
			public void run() {
				try {
					lock.lock();
					for (String s : eventList) {
						lists[lFlag] = s;
						lFlag++;
						 
					}
				} finally {
					lFlag=0;
					 lock.unlock();
				}
			}
		}).start();*/
		try {
			boolean b;
			b=exec.submit(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					try {
						lock.lock();
						for (String s : eventList) {
							lists[lFlag] = s;
							lFlag++;
							 
						}
					} finally {
						lFlag=0;
						 lock.unlock();
					}
					return true;
				}
				
			}).get();
			 
			return b;
		} catch (InterruptedException e) {
			// TODO �Զ����ɵ� catch ��
			
			e.printStackTrace();
			return false;
		} catch (ExecutionException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			return false; 
		}
	}
	
	
	/**
	 * ִ�б����¼��б������е� һ���߳�
	 */
	public boolean saveEventList() {
		
		/*new Thread(new Runnable() {
			public void run() {
				try {
					 lock.lock();
					for (String s : evenText.values()) {
						if(s.trim().length()!=0){//Ҫȥ���ı�������һ�У�����cao
						texts[eFlag] = s;
						eFlag++;}			
						 
					}
				} finally {
					eFlag=0;
					 lock.unlock();
				}
			}
		}).start();*/
		try {
			boolean b;
			b=exec.submit(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					try {
						 lock.lock();
						for (String s : evenText.values()) {
							if(s.trim().length()!=0){//Ҫȥ���ı�������һ�У�����cao
							texts[eFlag] = s ;
							eFlag++;}			
							 
						}
					} finally {
						eFlag=0;
						 lock.unlock();
					}
					return true;
				}
			}).get();
			
			return b;
		} catch (InterruptedException e) {
			 
			e.printStackTrace();
			return false;
		} catch (ExecutionException e) {		 
			e.printStackTrace();
			return false;
		}
		 
	}
	
	
	/**
	 * ���������¼�����д��Map������
	 * @param str �������¼�����
	 */
	public void writeEvenText(String str) {
		eventKey++;
		evenText.put(new Integer(eventKey), str);

	}
	
	
	/**
	 * �޸��¼�����
	 * @param n ѡ�е��¼��б������
	 * @param s	ѡ�е��¼��б�����������
	 */
	public void changeEvenText(int n, String s,EventListen eventListen) {
		System.out.println("ifchanged:"+eventListen.ifChanged);
		evenText.put(new Integer(n), s);
		//System.out.println(s);
		eventListen.ifChangedCallback();
	}
	
	
	/**
	 * ���������¼��б�д��Vector����
	 * @param s �������¼��б�����
	 */
	public void writeEvenList(String s) {
		eventList.add("#"+s+"#"+EventListen.getDate());

		/*
		 * for (String string : eventList) { System.out.println(string); }
		 */
		 
	}
	
	
	/**
	 * ���ı��ļ��ж�ȡ�¼��б�
	 * @return ������װ�������б�Vector����
	 */
	public Vector<String> readEventList() {

		try {
			String signal;
			eventList = new Vector<String>();// ��ӵ�JList����
			 
			FileReader fReader = new FileReader(this);
			BufferedReader bReader = new BufferedReader(fReader);
			while ((signal = bReader.readLine()) != null) {
				if (signal.trim().length() != 0) {
					if ('#' == (signal.trim().charAt(0))) {

						eventList.add(signal);
						 
					}
					signal = null;
				}
			}

			fReader.close();
			bReader.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
		return eventList;
	}
	
	
	/**
	 * ���ı��ļ��ж�ȡ�¼����ݵ�Map����
	 */
	public void readEventText() {

		ExecutorService executorService = Executors.newSingleThreadExecutor();// �¼���ȡ�ĵ����߳�
		executorService.execute(new Thread(new Runnable() {
			public void run() {
				try {

					String signal;
					StringBuffer signalBuffer = new StringBuffer();
					FileReader fReader = new FileReader(FileIO.this);
					BufferedReader bReader = new BufferedReader(fReader);
					while ((signal = bReader.readLine()) != null) {
						if (signal.trim().length() != 0) {// ����һ��ʲô��û�е����
							if ((signal.trim().charAt(0) != '#')) {
								signalBuffer.append(signal + "\r\n");
							}
							if ('#' == (signal.trim().charAt(0))) {
								if (eventKey != -1) {
									signal = signalBuffer.toString();
									signalBuffer = new StringBuffer();
									evenText.put(new Integer(eventKey), signal);
								}
								eventKey++;
							}
						}
					}
					signal = signalBuffer.toString();
					evenText.put(new Integer(eventKey), signal);

					fReader.close();
					bReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}));
		executorService.shutdown();
	}

}

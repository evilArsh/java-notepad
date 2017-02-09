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
	public int eventKey = -1;// 文本中所描述的每个事件的内容的键值
	public Lock lock;
	public boolean ifSave=false;// 是否保存了文件，当用户没有保存文件就退出时自动保存
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
	 * 保存事件内容和列表到文本文档中
	 * @param eventListen 进行回调的参数
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
			eventListen.ifSavedCallback(); //回调
			return true;
		} catch (Exception e) {		
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	 /**
	  * 执行保存事件内容到数组中的 一个线程
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
			// TODO 自动生成的 catch 块
			
			e.printStackTrace();
			return false;
		} catch (ExecutionException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return false; 
		}
	}
	
	
	/**
	 * 执行保存事件列表到数组中的 一个线程
	 */
	public boolean saveEventList() {
		
		/*new Thread(new Runnable() {
			public void run() {
				try {
					 lock.lock();
					for (String s : evenText.values()) {
						if(s.trim().length()!=0){//要去掉文本框的最后一行！！！cao
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
							if(s.trim().length()!=0){//要去掉文本框的最后一行！！！cao
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
	 * 将新增的事件内容写到Map集合中
	 * @param str 新增的事件内容
	 */
	public void writeEvenText(String str) {
		eventKey++;
		evenText.put(new Integer(eventKey), str);

	}
	
	
	/**
	 * 修改事件内容
	 * @param n 选中的事件列表的索引
	 * @param s	选中的事件列表所含的内容
	 */
	public void changeEvenText(int n, String s,EventListen eventListen) {
		System.out.println("ifchanged:"+eventListen.ifChanged);
		evenText.put(new Integer(n), s);
		//System.out.println(s);
		eventListen.ifChangedCallback();
	}
	
	
	/**
	 * 将新增的事件列表写到Vector容器
	 * @param s 新增的事件列表名称
	 */
	public void writeEvenList(String s) {
		eventList.add("#"+s+"#"+EventListen.getDate());

		/*
		 * for (String string : eventList) { System.out.println(string); }
		 */
		 
	}
	
	
	/**
	 * 从文本文件中读取事件列表
	 * @return 返回所装的所有列表到Vector容器
	 */
	public Vector<String> readEventList() {

		try {
			String signal;
			eventList = new Vector<String>();// 添加到JList的项
			 
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
	 * 从文本文件中读取事件内容到Map集合
	 */
	public void readEventText() {

		ExecutorService executorService = Executors.newSingleThreadExecutor();// 事件读取的单独线程
		executorService.execute(new Thread(new Runnable() {
			public void run() {
				try {

					String signal;
					StringBuffer signalBuffer = new StringBuffer();
					FileReader fReader = new FileReader(FileIO.this);
					BufferedReader bReader = new BufferedReader(fReader);
					while ((signal = bReader.readLine()) != null) {
						if (signal.trim().length() != 0) {// 避免一行什么都没有的情况
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

package task7;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class NetClass{
	private static final int CLIENT_PORT = 1556;
	private static final int SERVER_PORT = 4507;
	private static final String SERVER_ADDRESS = "192.168.100.7";
	private String work_type;
	private ArrayList<MessageListener> listeners = new ArrayList<MessageListener>(5);
	
	public NetClass(/*boolean type*/){
		new Thread(new Runnable(){
			public void run() {
				try{
					final ServerSocket serverSocket = new ServerSocket(CLIENT_PORT);
					while (!Thread.interrupted()){
						final Socket socket = serverSocket.accept();
						final DataInputStream in = new DataInputStream(socket.getInputStream());
						// Читаем имя отправителя
						String answer = in.readUTF();
						// Читаем сообщение
						// Закрываем соединение
						socket.close();
						notifyListeners(answer);
						// Выделяем IP-адрес
						//return answer.equals("true");
					}
				} 
				catch (IOException e) {
				}
			}
		}).start();
	}
	
	public int send(String login, String password){
		try{
			final Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
			final DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF("CHECK_IN");
			out.writeUTF(login);
			out.writeUTF(password);
			Integer port = CLIENT_PORT;
			out.writeUTF(port.toString());
			socket.close();
			out.close();
			return 0;
		}
		catch (UnknownHostException e){
			e.printStackTrace();
			return 1;
		} 
		catch (IOException e){
			e.printStackTrace();
			return 2;
		}
	}
	
	public void addMessageListener(MessageListener listener) {
		synchronized (listeners){
			listeners.add(listener);
		}
	}
	
	public void removeMessageListener(MessageListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
	
	private void notifyListeners(String message) {
		synchronized (listeners){
			for (MessageListener listener:listeners) {
				listener.messageReceived(message);
			}
		}
	}
	
	public String get_work_type(){
		return work_type;
	}
}

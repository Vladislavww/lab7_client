package task7;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class NetClass{
	private static final int CLIENT_PORT = 1556;
	private static final int SERVER_PORT = 4511;
	private static final String SERVER_ADDRESS = "192.168.100.7";
	//private String work_type;
	private ArrayList<MessageListener> listeners = new ArrayList<MessageListener>(5);
	
	public NetClass(String type){
		//work_type = type;
		//режими работы объекта. В зависимости от режима выбирается соответствующий вид потока и его обработка
		if(type.equals("Authorization")){
			new Thread(new Runnable(){
				public void run() {
					try{
						final ServerSocket serverSocket = new ServerSocket(CLIENT_PORT);
						while (!Thread.interrupted()){
							final Socket socket = serverSocket.accept();
							final DataInputStream in = new DataInputStream(socket.getInputStream());
							String answer = in.readUTF();
							socket.close();
							notifyListeners(answer);
						}
					} 
					catch (IOException e) {
					}
				}
			}).start();
		}
		else if(type.equals("ONLINE_MENU")){
			new Thread(new Runnable(){
				public void run() {
					try{
						final ServerSocket serverSocket = new ServerSocket(CLIENT_PORT+1);
						while (!Thread.interrupted()){
							final Socket socket = serverSocket.accept();
							final DataInputStream in = new DataInputStream(socket.getInputStream());
							final String work_type = in.readUTF();
							LinkedList<String> usersOnline = new LinkedList<String>();
							int size = 0;
							if(work_type.equals("TAKE_USERS_ONLINE")){
								size = Integer.parseInt(in.readUTF());
							}
							for(int i=0; i<size; i++){
								usersOnline.add(in.readUTF());
							}
							socket.close();
							notifyListeners(usersOnline);
						}
					} 
					catch (IOException e){
					}
				}
			}).start();
		}
	}
	
	public int send(String worktype, String login, String password){
		try{
			final Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
			final DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF(worktype);
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
	
	public void send(String worktype, String name){
		try{
			final Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
			final DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF(worktype);
			out.writeUTF(name);
		}
		catch (UnknownHostException e){//реагировать не нужно
		} 
		catch (IOException e){
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
	private void notifyListeners(LinkedList<String> message) {
		synchronized (listeners){
			for (MessageListener listener:listeners) {
				listener.messageReceived(message);
			}
		}
	}
	
}

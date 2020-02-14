package task7;

import java.util.LinkedList;
//интерфейс, чтобы сделать слушатель получения сообщений
public interface MessageListener{
	void messageReceived(String message);
	void messageReceived(LinkedList<String> message);
}

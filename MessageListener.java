package task7;

import java.util.LinkedList;
//���������, ����� ������� ��������� ��������� ���������
public interface MessageListener{
	void messageReceived(String message);
	void messageReceived(LinkedList<String> message);
}

package task7;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.GroupLayout.Alignment;

@SuppressWarnings("serial")
public class DialogFrame extends JFrame {
	private static final String FRAME_TITLE = "Клиент мгновенных сообщений";
	private static final int FRAME_MINIMUM_WIDTH = 500;
	private static final int FRAME_MINIMUM_HEIGHT = 500;
	private static final int INCOMING_AREA_DEFAULT_ROWS = 10;
	private static final int OUTGOING_AREA_DEFAULT_ROWS = 0;
	private static final int SMALL_GAP = 5;
	private static final int MEDIUM_GAP = 10;
	private final JTextArea textAreaIncoming;
	private final JTextArea textAreaOutgoing;
	private NetClass NetManager;
	private String UserName;
	private String FriendName; //логин собеседника
	
	public DialogFrame(String name1, String name2, NetClass NetManager) {
		super(FRAME_TITLE);
		UserName = name1;
		FriendName = name2;
		this.NetManager = NetManager;
		setMinimumSize(new Dimension(FRAME_MINIMUM_WIDTH, FRAME_MINIMUM_HEIGHT));
		// Центрирование окна
		final Toolkit kit = Toolkit.getDefaultToolkit();
		setLocation((kit.getScreenSize().width - getWidth()) / 2, (kit.getScreenSize().height - getHeight()) / 2);
		// Текстовая область для отображения полученных сообщений
		textAreaIncoming = new JTextArea(INCOMING_AREA_DEFAULT_ROWS, 0);
		textAreaIncoming.setEditable(false);
		// Контейнер, обеспечивающий прокрутку текстовой области
		final JScrollPane scrollPaneIncoming = new JScrollPane(textAreaIncoming);
		// Текстовая область для ввода сообщения
		textAreaOutgoing = new JTextArea(OUTGOING_AREA_DEFAULT_ROWS, 0);
		// Контейнер, обеспечивающий прокрутку текстовой области
		final JScrollPane scrollPaneOutgoing = new JScrollPane(textAreaOutgoing);
		// Панель ввода сообщения
		final JPanel messagePanel = new JPanel();
		messagePanel.setBorder(BorderFactory.createTitledBorder("Сообщение"));
		// Кнопка отправки сообщения
		final JButton sendButton = new JButton("Отправить");
		sendButton.addActionListener(new ActionListener(){
			//@Override
			public void actionPerformed(ActionEvent e){
				sendMessage();
			}
		});
		// Компоновка элементов панели "Сообщение"
		final GroupLayout layout2 = new GroupLayout(messagePanel);
		messagePanel.setLayout(layout2);
		layout2.setHorizontalGroup(layout2.createSequentialGroup()
			.addContainerGap()
			.addGroup(layout2.createParallelGroup(Alignment.TRAILING)
				.addGroup(layout2.createSequentialGroup()
					.addGap(SMALL_GAP))
				.addComponent(scrollPaneOutgoing)
				.addComponent(sendButton))
			.addContainerGap());
		layout2.setVerticalGroup(layout2.createSequentialGroup()
			.addContainerGap()
			.addGroup(layout2.createParallelGroup(Alignment.BASELINE))
			.addGap(MEDIUM_GAP)
			.addComponent(scrollPaneOutgoing)
			.addGap(MEDIUM_GAP)
			.addComponent(sendButton)
			.addContainerGap());
// Компоновка элементов фрейма
		final GroupLayout layout1 = new GroupLayout(getContentPane());
		setLayout(layout1);
		layout1.setHorizontalGroup(layout1.createSequentialGroup()
			.addContainerGap()
			.addGroup(layout1.createParallelGroup()
				.addComponent(scrollPaneIncoming)
				.addComponent(messagePanel))
			.addContainerGap());
		layout1.setVerticalGroup(layout1.createSequentialGroup()
			.addContainerGap()
			.addComponent(scrollPaneIncoming)
			.addGap(MEDIUM_GAP)
			.addComponent(messagePanel)
			.addContainerGap());
		
		NetManager.addMessageListener(new MessageListener(){
			public void messageReceived(String message) {
				//ничего
			}
			public void messageReceived(LinkedList<String> message){
				//ничего
			}
			public void messageReceived(String name, String message) {
				if(FriendName.equals(name)){
					writeMessage(message);
				}
			}
		});
	}
	
	private void sendMessage(){
		final String message = textAreaOutgoing.getText();
		int result = NetManager.send("DIALOG", UserName, FriendName, message);
		if(result==1){
			JOptionPane.showMessageDialog(DialogFrame.this,"Не удалось отправить сообщение: узел-адресат не найден","Ошибка", JOptionPane.ERROR_MESSAGE);
		}
		else if(result==2){
			JOptionPane.showMessageDialog(DialogFrame.this,"Не удалось отправить сообщение", "Ошибка", JOptionPane.ERROR_MESSAGE);
		}
		textAreaIncoming.append("Я: " + message + '\n');
	}
	
	private void writeMessage(String text){
		textAreaIncoming.append(FriendName + ": " + text + '\n');
	}
	
	
}
				
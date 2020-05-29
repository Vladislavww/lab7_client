package task7;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.GroupLayout.Alignment;


public class MenuFrame extends JFrame {
	private static final String FRAME_TITLE = "Клиент мгновенных сообщений";
	private static final int FRAME_MINIMUM_WIDTH = 500;
	private static final int FRAME_MINIMUM_HEIGHT = 500;
	private static final int SMALL_GAP = 5;
	private static final int MEDIUM_GAP = 10;
	private JTextField NameField;
	private JTextArea textAreaUsers;
	private static final int USERS_AREA_DEFAULT_ROWS = 10;
	private NetClass NetManager;
	private boolean written = false;
	private Timer onlineActionTimer = new Timer(1000, new ActionListener(){
		public void actionPerformed(ActionEvent ev){
			// Задача обработчика события ActionEvent - запрос пользователей онлайн и передача информации о себе
			informServer();
			requestInformation();
		}
	});
	private String UserName;
	public MenuFrame(String name, NetClass NetManager){
		super(FRAME_TITLE);
		UserName = name;
		this.NetManager = NetManager;
		final Toolkit kit = Toolkit.getDefaultToolkit();
		setLocation((kit.getScreenSize().width - getWidth()) / 2, (kit.getScreenSize().height - getHeight()) / 2);
		setMinimumSize(new Dimension(FRAME_MINIMUM_WIDTH, FRAME_MINIMUM_HEIGHT));
		textAreaUsers = new JTextArea(USERS_AREA_DEFAULT_ROWS, 0);
		textAreaUsers.setEditable(false);
		// Контейнер, обеспечивающий прокрутку текстовой области
		final JScrollPane scrollPaneUsers = new JScrollPane(textAreaUsers);
		// Подписи полей
		final JLabel onlineLabel = new JLabel("Пользователи в онлайне");
		final JLabel buttonLabel = new JLabel("Введите имя пользователя для диалога");
		// Поля ввода имени пользователя и адреса получателя
		NameField = new JTextField("", 30);
		NameField.setMaximumSize(NameField.getPreferredSize());
		final JButton enterButton = new JButton("OK");
		enterButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				enter();
			}
		});
		final JPanel MainPanel = new JPanel();
		final GroupLayout layout2 = new GroupLayout(MainPanel);
		MainPanel.setLayout(layout2);
		layout2.setHorizontalGroup(layout2.createSequentialGroup()
			.addContainerGap()
			.addGroup(layout2.createParallelGroup(Alignment.LEADING)
				.addComponent(onlineLabel)
				.addComponent(scrollPaneUsers)
				.addComponent(buttonLabel)
				.addComponent(NameField)
				.addComponent(enterButton))
			.addContainerGap());
		layout2.setVerticalGroup(layout2.createSequentialGroup()
			.addContainerGap()
			.addComponent(onlineLabel)
			.addGap(SMALL_GAP)
			.addComponent(scrollPaneUsers)
			.addGap(MEDIUM_GAP)
			.addComponent(buttonLabel)
			.addGap(SMALL_GAP)
			.addComponent(NameField)
			.addGap(MEDIUM_GAP)
			.addComponent(enterButton)
			.addContainerGap());
		final GroupLayout layout1 = new GroupLayout(getContentPane());
		setLayout(layout1);
		layout1.setHorizontalGroup(layout1.createSequentialGroup()
			.addContainerGap()
			.addGroup(layout1.createParallelGroup()
				.addComponent(MainPanel))
			.addContainerGap());
		layout1.setVerticalGroup(layout1.createSequentialGroup()
			.addContainerGap()
			.addGap(MEDIUM_GAP)
			.addComponent(MainPanel)
			.addContainerGap());
		
		onlineActionTimer.start();
		NetManager.addMessageListener(new MessageListener(){
			public void messageReceived(String message) {
				//ничего
			}
			public void messageReceived(LinkedList<String> message){
				writeOnlineUsers(message);
			}
			public void messageReceived(String name, String message) {
				//ничего
			}
		});
	}
	
	private void requestInformation(){
		NetManager.send("TAKE_USERS_ONLINE", UserName);
	}
	
	private void informServer(){
		NetManager.send("USER_IS_ONLINE", UserName);
	}
	
	private void writeOnlineUsers(LinkedList<String> list){
		textAreaUsers.setText("");
		for(String name:list){
			if(name.equals(UserName)){
				textAreaUsers.append(name + "(вы)" + '\n');
			}
			else{
				textAreaUsers.append(name + '\n');
			}
		}
	}
	
	private void enter(){
		final DialogFrame dialog_frame = new DialogFrame(UserName, NameField.getText(), NetManager);
		dialog_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dialog_frame.setVisible(true);
	}

}

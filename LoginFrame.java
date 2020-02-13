package task7;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.GroupLayout.Alignment;


public class LoginFrame extends JFrame{
	private static final String FRAME_TITLE = "������ ���������� ���������";
	private JTextField textFieldUsername;
	private JTextField textFieldPassword;
	private JCheckBox NewUserFlag;
	private static final int SMALL_GAP = 5;
	private static final int MEDIUM_GAP = 10;
	private static final int LARGE_GAP = 15;
	private static final int FRAME_MINIMUM_WIDTH = 500;
	private static final int FRAME_MINIMUM_HEIGHT = 500;
	private NetClass NetManager = new NetClass();
	public LoginFrame(){
		super(FRAME_TITLE);
		final Toolkit kit = Toolkit.getDefaultToolkit();
		setLocation((kit.getScreenSize().width - getWidth()) / 2, (kit.getScreenSize().height - getHeight()) / 2);
		setMinimumSize(new Dimension(FRAME_MINIMUM_WIDTH, FRAME_MINIMUM_HEIGHT));
		JLabel UsernameLabel = new JLabel("Login:");
		JLabel PasswordLabel = new JLabel("Password:");
		JLabel NewUserLabel = new JLabel("���� �� ����� ������������");
		textFieldUsername = new JTextField("Vlad", 30);
		textFieldUsername.setMaximumSize(textFieldUsername.getPreferredSize());
		textFieldPassword = new JTextField("1234", 30);
		textFieldPassword.setMaximumSize(textFieldPassword.getPreferredSize());
		final JButton enterButton = new JButton("�����");
		enterButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				enter();
			}
		});
		NewUserFlag = new JCheckBox();
		final JPanel MainPanel = new JPanel();
		final GroupLayout layout2 = new GroupLayout(MainPanel);
		MainPanel.setLayout(layout2);
		layout2.setHorizontalGroup(layout2.createSequentialGroup()
			.addContainerGap()
			.addGroup(layout2.createParallelGroup(Alignment.LEADING)
				.addComponent(UsernameLabel)
				.addComponent(textFieldUsername)
				.addComponent(PasswordLabel)
				.addComponent(textFieldPassword)
				.addGroup(layout2.createSequentialGroup()
					.addComponent(NewUserFlag)
					.addGap(SMALL_GAP)
					.addComponent(NewUserLabel))
			.addComponent(enterButton))
			.addContainerGap());
		layout2.setVerticalGroup(layout2.createSequentialGroup()
			.addContainerGap()
			.addComponent(UsernameLabel)
			.addGap(SMALL_GAP)
			.addComponent(textFieldUsername)
			.addGap(MEDIUM_GAP)
			.addComponent(PasswordLabel)
			.addGap(SMALL_GAP)
			.addComponent(textFieldPassword)
			.addGap(MEDIUM_GAP)
			.addGroup(layout2.createParallelGroup(Alignment.BASELINE)
				.addComponent(NewUserFlag)
				.addComponent(NewUserLabel))
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
		
		NetManager.addMessageListener(new MessageListener(){
			public void messageReceived(String message) {
				System.out.println("Received "+message);
				//TODO ����� �������-��������
			}
		});
	}
	
	private void enter(){
		String login = textFieldUsername.getText();
		String password = textFieldPassword.getText();
		if(NewUserFlag.isSelected() == false){
			int result = NetManager.send(login, password);
			if(result==1){
				JOptionPane.showMessageDialog(LoginFrame.this,"�� ������� ��������� ���������: ����-������� �� ������","������", JOptionPane.ERROR_MESSAGE);
			}
			else if(result==2){
				JOptionPane.showMessageDialog(LoginFrame.this,"�� ������� ��������� ���������", "������", JOptionPane.ERROR_MESSAGE);
			}
			else if(result==0){ //��������� ���������
				//TODO �������� ��������� �������
			}
		}
		else{
		}
	}

}
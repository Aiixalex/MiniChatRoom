package frame;

import java.awt.*;
import javax.swing.border.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;

public class UserConf extends JDialog {
	JPanel panelUserConf = new JPanel();
	JButton save = new JButton();
	JButton cancel = new JButton();
	JLabel DLGINFO=new JLabel("Default User Name: Alex", JLabel.CENTER);

	JPanel panelSave = new JPanel();
	JLabel message = new JLabel();
	public String userInputName;

	JTextField userName ;

	MainFrame mainFrame;
	
	public UserConf(JFrame frame,String str) {
		super(frame, true);
		this.mainFrame = (MainFrame)frame;
		this.userInputName = str;
		try {
			jbInit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation( (int) (screenSize.width - 400) / 2 + 60,
						(int) (screenSize.height - 700) / 2 + 200);
		this.setResizable(false);
	}

	private void jbInit() throws Exception {
		this.setSize(new Dimension(300, 120));
		this.setTitle("User Connection");
		message.setText("Input your User Name: ");
		userName = new JTextField(10);
		userName.setText(userInputName);
		save.setText("Save");
		cancel.setText("Cancel");

		panelUserConf.setLayout(new FlowLayout());
		panelUserConf.add(message);
		panelUserConf.add(userName);

		panelSave.add(new Label(""));
		panelSave.add(save);
		panelSave.add(cancel);
		panelSave.add(new Label(""));

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(panelUserConf, BorderLayout.NORTH);
		contentPane.add(DLGINFO, BorderLayout.CENTER);
		contentPane.add(panelSave, BorderLayout.SOUTH);

		//save
		save.addActionListener(
			new ActionListener() {
				public void actionPerformed (ActionEvent a) {
					if(userName.getText().equals("")){
						DLGINFO.setText("User Name cannot be Blank!");
						userName.setText(userInputName);
						return;
					}
					userInputName = userName.getText();
					mainFrame.setTitle(userInputName);
					dispose();
				}
			}
		);

		//close window
		this.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					DLGINFO.setText("Default User Name: Alex");
				}
			}
		);

		//cancel
		cancel.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					DLGINFO.setText("Default User Name: Alex");
					dispose();
				}
			}
		);
	}
}


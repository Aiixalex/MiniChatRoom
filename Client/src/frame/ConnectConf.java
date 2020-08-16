package frame;

import java.awt.*;
import javax.swing.border.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;

public class ConnectConf extends JDialog {
	JPanel panelUserConf = new JPanel();
	JButton save = new JButton();
	JButton cancel = new JButton();
	JLabel DLGINFO=new JLabel("The default connection setting is 127.0.0.1:8888", JLabel.CENTER);

	JPanel panelSave = new JPanel();

	public String userInputIp;
	public int userInputPort;

	JTextField inputIp;
	JTextField inputPort;

	public ConnectConf(JFrame frame,String ip,int port) {
		super(frame, true);
		this.userInputIp = ip;
		this.userInputPort = port;
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
		this.setSize(new Dimension(300, 200));
		this.setTitle("Connection Settings");
		inputIp = new JTextField(20);
		inputIp.setText(userInputIp);
		inputPort = new JTextField(10);
		inputPort.setText(userInputPort+"");
		save.setText("Save");
		cancel.setText("Cancel");

		panelUserConf.setLayout(new GridLayout(3,2));
		panelUserConf.add(new JLabel(""));
		panelUserConf.add(new JLabel(""));
		panelUserConf.add(new JLabel("  Please Input the IP: "));
		panelUserConf.add(inputIp);
		panelUserConf.add(new JLabel("  Please Input the Port: "));
		panelUserConf.add(inputPort);

		panelSave.add(save);
		panelSave.add(cancel);

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(panelUserConf, BorderLayout.NORTH);
		contentPane.add(DLGINFO, BorderLayout.CENTER);
		contentPane.add(panelSave, BorderLayout.SOUTH);

		save.addActionListener(
			new ActionListener() {
				public void actionPerformed (ActionEvent a) {
					int savePort;
					try{
						userInputIp = "" + InetAddress.getByName(inputIp.getText());
						userInputIp = userInputIp.substring(1);
					}
					catch(UnknownHostException e){
						DLGINFO.setText("Wrong IP Address!");
						return;
					}

					try{
						savePort = Integer.parseInt(inputPort.getText());

						if(savePort<1 || savePort>65535){
							DLGINFO.setText("Port must be an Integer between 0-65535!");
							inputPort.setText("");
							return;
						}
						userInputPort = savePort;
						dispose();
					}
					catch(NumberFormatException e){
						DLGINFO.setText("Port must be an Integer between 0-65535!");
						inputPort.setText("");
						return;
					}
				}
			}
		);

		this.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					//DLGINFO.setText("The default connection setting is 127.0.0.1:8888");
				}
			}
		);

		cancel.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					dispose();
				}
			}
		);
	}
}


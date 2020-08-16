package frame;


import java.awt.*;
import javax.swing.border.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;

import listener.ServerListener;

public class PortConf extends JDialog {
	JPanel panelPort = new JPanel();
	JButton save = new JButton();
	JButton cancel = new JButton();
	public static JLabel DLGINFO=new JLabel("The default connection setting is 127.0.0.1:8888", JLabel.CENTER);

	JPanel panelSave = new JPanel();
	JLabel message = new JLabel();

	public static JTextField portNumber ;

	public PortConf(JFrame frame) {
		super(frame, true);
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
		this.setTitle("Port Settings");
		message.setText("Please Input the Port: ");
		portNumber = new JTextField(10);
		portNumber.setText(""+ServerListener.port);
		save.setText("Save");
		cancel.setText("Cancel");

		panelPort.setLayout(new FlowLayout());
		panelPort.add(message);
		panelPort.add(portNumber);

		panelSave.add(new Label(""));
		panelSave.add(save);
		panelSave.add(cancel);
		panelSave.add(new Label(""));

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(panelPort, BorderLayout.NORTH);
		contentPane.add(DLGINFO, BorderLayout.CENTER);
		contentPane.add(panelSave, BorderLayout.SOUTH);

		//ActionListener of save button
		save.addActionListener(
			new ActionListener() {
				public void actionPerformed (ActionEvent a) {
					int savePort;
					try{
						
						savePort=Integer.parseInt(PortConf.portNumber.getText());

						if(savePort<1 || savePort>65535){
							PortConf.DLGINFO.setText("Port must be an Integer between 0-65535!");
							PortConf.portNumber.setText("");
							return;
						}
						ServerListener.port = savePort;
						dispose();
					}
					catch(NumberFormatException e){
						PortConf.DLGINFO.setText("Port must be an Integer between 0-65535!");
						PortConf.portNumber.setText("");
						return;
					}
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


package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import thread.ClientReceiveThread;

import frame.ConnectConf;
import frame.MainFrame;
import frame.UserConf;

public class ClientListener implements ActionListener {

	String ip = "127.0.0.1";
	int port = 8888;
	public String userName = "Alex";
	public int isConnected = 0;
	
	Socket socket;
	ObjectOutputStream output;
	ObjectInputStream input;
	
	ClientReceiveThread recvThread;
	
	MainFrame mainFrame;
	
	public ClientListener(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		
		mainFrame.loginItem.addActionListener(this);
		mainFrame.logoffItem.addActionListener(this);
		mainFrame.exitItem.addActionListener(this);
		mainFrame.userItem.addActionListener(this);
		mainFrame.connectItem.addActionListener(this);
		
		mainFrame.loginButton.addActionListener(this);
		mainFrame.logoffButton.addActionListener(this);
		mainFrame.userButton.addActionListener(this);
		mainFrame.connectButton.addActionListener(this);
		mainFrame.exitButton.addActionListener(this);
		
		mainFrame.clientMessage.addActionListener(this);
		mainFrame.clientMessageButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		if (obj == mainFrame.userItem || obj == mainFrame.userButton) { 
			UserConf userConf = new UserConf(mainFrame,userName); //set user
			userConf.setVisible(true);
			userName = userConf.userInputName;
		}
		else if (obj == mainFrame.connectItem || obj == mainFrame.connectButton) {
			ConnectConf conConf = new ConnectConf(mainFrame,ip,port); // set connection
			conConf.setVisible(true);
			ip = conConf.userInputIp;
			port = conConf.userInputPort;
		}
		else if (obj == mainFrame.loginItem || obj == mainFrame.loginButton) { 
			Connect(); // log in
		}
		else if (obj == mainFrame.logoffItem || obj == mainFrame.logoffButton) { 
			DisConnect(); // log out
			mainFrame.showStatus.setText("");
		}
		else if (obj == mainFrame.clientMessage || obj == mainFrame.clientMessageButton) {
			SendMessage();
			mainFrame.clientMessage.setText("");
		}
		else if (obj == mainFrame.exitButton || obj == mainFrame.exitItem) {
			int j=mainFrame.showConfirmDialog("Exit?","Exit");
			
			if (j == 1){
				if(isConnected == 1){
					DisConnect();
				}
				System.exit(0);
			}
		}
	}
	
	public void Connect(){
		try{
			socket = new Socket(ip,port);
		}
		catch (Exception e){
			mainFrame.showConfirmDialog("No connection with server.\nPlease confirm connection settings.","Error");
			return;
		}

		try{
			output = new ObjectOutputStream(socket.getOutputStream());
			output.flush();
			input  = new ObjectInputStream(socket.getInputStream() );
			
			output.writeObject(userName);
			output.flush();
			
			recvThread = new ClientReceiveThread(socket,output,input,mainFrame.combobox,mainFrame.messageShow,mainFrame.showStatus);
			recvThread.start();
			
			mainFrame.loginButton.setEnabled(false);
			mainFrame.loginItem.setEnabled(false);
			mainFrame.userButton.setEnabled(false);
			mainFrame.userItem.setEnabled(false);
			mainFrame.connectButton.setEnabled(false);
			mainFrame.connectItem.setEnabled(false);
			mainFrame.logoffButton.setEnabled(true);
			mainFrame.logoffItem.setEnabled(true);
			mainFrame.clientMessage.setEnabled(true);
			mainFrame.messageShow.append("Successfully Connect Server "+ip+":"+port+"\n");
			isConnected = 1;
		}
		catch (Exception e){
			System.out.println(e);
			return;
		}
	}
	
	public void DisConnect(){
		mainFrame.loginButton.setEnabled(true);
		mainFrame.loginItem.setEnabled(true);
		mainFrame.userButton.setEnabled(true);
		mainFrame.userItem.setEnabled(true);
		mainFrame.connectButton.setEnabled(true);
		mainFrame.connectItem.setEnabled(true);
		mainFrame.logoffButton.setEnabled(false);
		mainFrame.logoffItem.setEnabled(false);
		mainFrame.clientMessage.setEnabled(false);
		
		if(socket.isClosed()){
			return ;
		}
		
		try{
			output.writeObject("Client Logout");
			output.flush();
		
			input.close();
			output.close();
			socket.close();
			mainFrame.messageShow.append("Disconnected with server.\n");
			isConnected = 0;
		}
		catch (Exception e){
		}
	}
	
	public void SendMessage(){
		String toSomebody = mainFrame.combobox.getSelectedItem().toString();
		String status  = "";
		if(mainFrame.checkbox.isSelected()){
			status = "private";
		}
		
		String action = mainFrame.actionlist.getSelectedItem().toString();
		String message = mainFrame.clientMessage.getText();
		
		if(socket.isClosed()){
			return ;
		}
		
		try{
			output.writeObject("Chatting Message");
			output.flush();
			output.writeObject(toSomebody);
			output.flush();
			output.writeObject(status);
			output.flush();
			output.writeObject(action);
			output.flush();
			output.writeObject(message);
			output.flush();
		}
		catch (Exception e){
		}
	}


}

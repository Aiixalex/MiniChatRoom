package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import thread.ServerListenThread;

import entity.Node;
import entity.UserLinkList;
import frame.MainFrame;
import frame.PortConf;

public class ServerListener implements ActionListener {
	
	public static int port = 8888;
	ServerSocket serverSocket;
	UserLinkList userLinkList;
	ServerListenThread listenThread;

	MainFrame mainFrame;
	
	public ServerListener(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		
		mainFrame.portItem.addActionListener(this);
		mainFrame.startItem.addActionListener(this);
		mainFrame.stopItem.addActionListener(this);
		mainFrame.exitItem.addActionListener(this);
		
		mainFrame.portSet.addActionListener(this);
		mainFrame.startServer.addActionListener(this);
		mainFrame.stopServer.addActionListener(this);
		mainFrame.exitButton.addActionListener(this);
		
		mainFrame.sysMessage.addActionListener(this);
		mainFrame.sysMessageButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == mainFrame.startServer || obj == mainFrame.startItem) {
			startService(); //start service
		}
		else if (obj == mainFrame.stopServer || obj == mainFrame.stopItem) { 
			int j=mainFrame.showConfirmDialog("Stop the service?","StopService");
			
			if (j == 1){
				stopService(); //Stop Service
			}
		}
		else if (obj == mainFrame.portSet || obj == mainFrame.portItem) { 
			PortConf portConf = new PortConf(mainFrame);
			portConf.setVisible(true);
		}
		else if (obj == mainFrame.exitButton || obj == mainFrame.exitItem) { 
			int j=mainFrame.showConfirmDialog("Exit?","Exit");
			
			if (j == 1){
				stopService();
				System.exit(0);
			}
		}
		else if (obj == mainFrame.sysMessage || obj == mainFrame.sysMessageButton) {
			sendSystemMessage(); //send message in the TextArea
		}
	}
	
	public void startService(){
		try{
			serverSocket = new ServerSocket(port,10);
			mainFrame.messageShow.append("The server is already started, listening on port " + port + "\n");
			
			mainFrame.startServer.setEnabled(false);
			mainFrame.startItem.setEnabled(false);
			mainFrame.portSet.setEnabled(false);
			mainFrame.portItem.setEnabled(false);

			mainFrame.stopServer .setEnabled(true);
			mainFrame.stopItem .setEnabled(true);
			mainFrame.sysMessage.setEnabled(true);
		}
		catch (Exception e){
		}
		
		userLinkList = new UserLinkList();
		
		listenThread = new ServerListenThread(serverSocket,mainFrame.combobox,
				mainFrame.messageShow,mainFrame.showStatus,userLinkList);
		listenThread.start();
	}
	
	public void stopService(){
		try{
			sendStopToAll();
			listenThread.isStop = true;
			serverSocket.close();
			
			int count = userLinkList.getCount();
			
			int i =0;
			while( i < count){
				Node node = userLinkList.findUser(i);
				
				node.getInput() .close();
				node.getOutput().close();
				node.getSocket().close();
				
				i ++;
			}

			mainFrame.stopServer .setEnabled(false);
			mainFrame.stopItem .setEnabled(false);
			mainFrame.startServer.setEnabled(true);
			mainFrame.startItem.setEnabled(true);
			mainFrame.portSet.setEnabled(true);
			mainFrame.portItem.setEnabled(true);
			mainFrame.sysMessage.setEnabled(false);

			mainFrame.messageShow.append("The service has stopped.\n");

			mainFrame.combobox.removeAllItems();
			mainFrame.combobox.addItem("Everyone");
		}
		catch(Exception e){
		}
	}
	
	public void sendStopToAll(){
		int count = userLinkList.getCount();
		
		int i = 0;
		while(i < count){
			Node node = userLinkList.findUser(i);
			if(node == null) {
				i ++;
				continue;
			}
			
			try{
				node.getOutput().writeObject("Service Stopped");
				node.getOutput().flush();
			}
			catch (Exception e){
			}
			
			i++;
		}
	}
	
	public void sendMsgToAll(String msg){
		int count = userLinkList.getCount();
		
		int i = 0;
		while(i < count){
			Node node = userLinkList.findUser(i);
			if(node == null) {
				i++;
				continue;
			}
			
			try{
				node.getOutput().writeObject("Server Message");
				node.getOutput().flush();
				node.getOutput().writeObject(msg);
				node.getOutput().flush();
			}
			catch (Exception e){
			}
			
			i++;
		}

		mainFrame.sysMessage.setText("");
	}

	public void sendSystemMessage(){
		String toSomebody = mainFrame.combobox.getSelectedItem().toString();
		String message = mainFrame.sysMessage.getText() + "\n";
		
		mainFrame.messageShow.append(message);
		
		if(toSomebody.equalsIgnoreCase("Everyone")){
			sendMsgToAll(message);
		}
		else{
			Node node = userLinkList.findUser(toSomebody);
			
			try{
				node.getOutput().writeObject("Server Message");
				node.getOutput().flush();
				node.getOutput().writeObject(message);
				node.getOutput().flush();
			}
			catch(Exception e){
			}
			mainFrame.sysMessage.setText("");//empty the message textfield
		}
	}

}

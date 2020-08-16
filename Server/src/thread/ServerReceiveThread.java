package thread;

import javax.swing.*;

import entity.Node;
import entity.UserLinkList;

import java.io.*;
import java.net.*;

public class ServerReceiveThread extends Thread {
	JTextArea textarea;
	JTextField textfield;
	JComboBox<String> combobox;
	Node client;
	UserLinkList userLinkList;
	
	public boolean isStop;
	
	public ServerReceiveThread(JTextArea textarea,JTextField textfield,
		JComboBox<String> combobox,Node client,UserLinkList userLinkList){

		this.textarea = textarea;
		this.textfield = textfield;
		this.client = client;
		this.userLinkList = userLinkList;
		this.combobox = combobox;
		
		isStop = false;
	}
	
	public void run(){
		
		sendUserList(); //send the list of clients online
		
		while(!isStop && !client.getSocket().isClosed()){
			try{
				String type = (String)client.getInput().readObject();
				
				if(type.equalsIgnoreCase("Chatting Message")){
					String toSomebody = (String)client.getInput().readObject();
					String status  = (String)client.getInput().readObject();
					String action  = (String)client.getInput().readObject();
					String message = (String)client.getInput().readObject();
					
					String msg = client.getUserName() + " to " + toSomebody + ": " + message + "\n";
					
					textarea.append(msg);
					
					if(toSomebody.equalsIgnoreCase("Everyone")){
						sendToAll(msg);
					}
					else{
						try{
							client.getOutput().writeObject("Chatting Message");
							client.getOutput().flush();
							client.getOutput().writeObject(msg);
							client.getOutput().flush();
						}
						catch (Exception e){
						}
						
						Node node = userLinkList.findUser(toSomebody);
						
						if(node != null){
							node.getOutput().writeObject("Chatting Message"); 
							node.getOutput().flush();
							node.getOutput().writeObject(msg);
							node.getOutput().flush();
						}
					}
				}
				else if(type.equalsIgnoreCase("Client Logout")){
					Node node = userLinkList.findUser(client.getUserName());
					userLinkList.delUser(node);
					
					String msg = "Client " + client.getUserName() + " Log out\n";
					int count = userLinkList.getCount();

					combobox.removeAllItems();
					combobox.addItem("Everyone");
					int i = 0;
					while(i < count){
						node = userLinkList.findUser(i);
						if(node == null) {
							i ++;
							continue;
						} 
			
						combobox.addItem(node.getUserName());
						i++;
					}
					combobox.setSelectedIndex(0);

					textarea.append(msg);
					textfield.setText("The Number of Users Online: " + userLinkList.getCount() + "\n");
					
					sendToAll(msg);
					sendUserList();
					
					break;
				}
			}
			catch (Exception e){}
		}
	}
	

	public void sendToAll(String msg){
		int count = userLinkList.getCount();
		
		int i = 0;
		while(i < count){
			Node node = userLinkList.findUser(i);
			if(node == null) {
				i ++;
				continue;
			}
			
			try{
				node.getOutput().writeObject("Chatting Message");
				node.getOutput().flush();
				node.getOutput().writeObject(msg);
				node.getOutput().flush();
			}
			catch (Exception e){
			}
			
			i++;
		}
	}

	public void sendUserList(){
		String userlist = "";
		int count = userLinkList.getCount();

		int i = 0;
		while(i < count){
			Node node = userLinkList.findUser(i);
			if(node == null) {
				i ++;
				continue;
			}
			
			userlist += node.getUserName();
			userlist += '\n';
			i++;
		}
		
		i = 0;
		while(i < count){
			Node node = userLinkList.findUser(i);
			if(node == null) {
				i ++;
				continue;
			} 
			
			try{
				node.getOutput().writeObject("Client List");
				node.getOutput().flush();
				node.getOutput().writeObject(userlist);
				node.getOutput().flush();
			}
			catch (Exception e){
			}
			i++;
		}
	}
}

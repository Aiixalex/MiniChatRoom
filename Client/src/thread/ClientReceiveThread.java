package thread;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class ClientReceiveThread extends Thread {
	private JComboBox<String> combobox;
	private JTextArea textarea;
	
	Socket socket;
	ObjectOutputStream output;
	ObjectInputStream  input;
	JTextField showStatus;

	public ClientReceiveThread(Socket socket,ObjectOutputStream output,
		ObjectInputStream  input,JComboBox<String> combobox,JTextArea textarea,JTextField showStatus){

		this.socket = socket;
		this.output = output;
		this.input = input;
		this.combobox = combobox;
		this.textarea = textarea;
		this.showStatus = showStatus;
	}
	
	public void run(){
		while(!socket.isClosed()){
			try{
				String type = (String)input.readObject();
				
				if(type.equalsIgnoreCase("Server Message")){
					String sysmsg = (String)input.readObject();
					textarea.append("Server Message: "+sysmsg);
				}
				else if(type.equalsIgnoreCase("Service Stopped")){
					output.close();
					input.close();
					socket.close();
					
					textarea.append("Service Stopped.\n");
					
					break;
				}
				else if(type.equalsIgnoreCase("Chatting Message")){
					String message = (String)input.readObject();
					textarea.append(message);
				}
				else if(type.equalsIgnoreCase("Client List")){
					String userlist = (String)input.readObject();
					String usernames[] = userlist.split("\n");
					combobox.removeAllItems();
					
					int i =0;
					combobox.addItem("Everyone");
					while(i < usernames.length){
						combobox.addItem(usernames[i]);
						i++;
					}
					combobox.setSelectedIndex(0);
					showStatus.setText("The Number of Users Online: " + usernames.length);
				}
			}
			catch (Exception e ){
				System.out.println(e);
			}
		}
	}
}


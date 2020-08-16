package frame;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import listener.ServerListener;

public class MainFrame extends JFrame {
	
	ServerListener sendInfo;
	
	public JComboBox<String> combobox;//list of clients
	public JTextArea messageShow;
	JScrollPane messageScrollPane;//Scroll Pane
	public JTextField showStatus;//message to show the number of online clients
	JLabel sendToLabel,messageLabel;
	public JTextField sysMessage;//TextField to send Message
	public JButton sysMessageButton;//send button

	JMenuBar jMenuBar = new JMenuBar(); 
	
	JMenu serviceMenu = new JMenu ("Service"); 
	
	public JMenuItem portItem = new JMenuItem ("Port Setting");
	public JMenuItem startItem = new JMenuItem ("Start Service");
	public JMenuItem stopItem=new JMenuItem ("Stop Service");
	public JMenuItem exitItem=new JMenuItem ("Exit");
	
	JToolBar toolBar = new JToolBar();

	public JButton portSet;
	public JButton startServer;
	public JButton stopServer;
	public JButton exitButton;
	
	Dimension faceSize = new Dimension(400,700);

	JPanel downPanel;
	GridBagLayout girdBag;
	GridBagConstraints girdBagCon;
	
	public MainFrame(){
		init();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		
		this.setSize(faceSize);
		this.setVisible(true);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation( (int) (screenSize.width - faceSize.getWidth()) / 2,
						  (int) (screenSize.height - faceSize.getHeight()) / 2);
		this.setResizable(false);

		this.setTitle("Chat Room Client"); 
		
	}
	
	public void init(){

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		serviceMenu.add (portItem);
		serviceMenu.add (startItem);
		serviceMenu.add (stopItem);
		serviceMenu.add (exitItem);
		jMenuBar.add (serviceMenu); 
		setJMenuBar (jMenuBar);

		portSet = new JButton("Port Setting");
		startServer = new JButton("Start Service");
		stopServer = new JButton("Stop Service" );
		exitButton = new JButton("Exit" );
		
		toolBar.add(portSet);
		toolBar.addSeparator();
		toolBar.add(startServer);
		toolBar.add(stopServer);
		toolBar.addSeparator();
		toolBar.add(exitButton);
		contentPane.add(toolBar,BorderLayout.NORTH);

		stopServer.setEnabled(false);
		stopItem .setEnabled(false);
		
		combobox = new JComboBox<String>();
		combobox.insertItemAt("Everyone",0);
		combobox.setSelectedIndex(0);
		
		messageShow = new JTextArea();
		messageShow.setEditable(false);
		
		messageScrollPane = new JScrollPane(messageShow,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		messageScrollPane.setPreferredSize(new Dimension(400,400));
		messageScrollPane.revalidate();
		
		showStatus = new JTextField(35);
		showStatus.setEditable(false);
		
		sysMessage = new JTextField(20);
		sysMessage.setEnabled(false);
		sysMessageButton = new JButton();
		sysMessageButton.setText("Send");

		sendToLabel = new JLabel("Send to: ");
		messageLabel = new JLabel("Message: ");
		downPanel = new JPanel();
		girdBag = new GridBagLayout();
		downPanel.setLayout(girdBag);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 0;
		JLabel none = new JLabel(" ");
		girdBag.setConstraints(none,girdBagCon);
		downPanel.add(none);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 2;
		girdBag.setConstraints(sendToLabel,girdBagCon);
		downPanel.add(sendToLabel);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx =1;
		girdBagCon.gridy = 2;
		girdBagCon.anchor = GridBagConstraints.LINE_START;
		girdBag.setConstraints(combobox,girdBagCon);
		downPanel.add(combobox);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 3;
		girdBag.setConstraints(messageLabel,girdBagCon);
		downPanel.add(messageLabel);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 1;
		girdBagCon.gridy = 3;
		girdBag.setConstraints(sysMessage,girdBagCon);
		downPanel.add(sysMessage);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 2;
		girdBagCon.gridy = 3;
		girdBag.setConstraints(sysMessageButton,girdBagCon);
		downPanel.add(sysMessageButton);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 4;
		girdBagCon.gridwidth = 3; //the whole 4th line is covered by showStatus
		girdBag.setConstraints(showStatus,girdBagCon);
		downPanel.add(showStatus);
		
		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 5;
		JLabel none1 = new JLabel(" ");
		girdBag.setConstraints(none1,girdBagCon);
		downPanel.add(none1);

		contentPane.add(messageScrollPane,BorderLayout.CENTER);
		contentPane.add(downPanel,BorderLayout.SOUTH);
		
		sendInfo = new ServerListener(this);
		
		this.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					sendInfo.stopService();
					System.exit(0);
				}
			}
		);
	}

	public int showConfirmDialog(String title, String msg) {
		int j=JOptionPane.showConfirmDialog(
				this,title,msg,
				JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE);
			
		if (j == JOptionPane.YES_OPTION){
			return 1;
		}
		return 0;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainFrame mainFrame = new MainFrame();
	}

}

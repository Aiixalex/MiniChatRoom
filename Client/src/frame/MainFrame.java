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
import javax.swing.JCheckBox;
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

import listener.ClientListener;

public class MainFrame extends JFrame {

	ClientListener clientListener;
	
	public JComboBox<String> combobox;
	public JTextArea messageShow;
	JScrollPane messageScrollPane;

	JLabel express,sendToLabel,messageLabel ;

	public JTextField clientMessage;
	public JCheckBox checkbox;
	public JComboBox<String> actionlist;
	public JButton clientMessageButton;
	public JTextField showStatus;
	
	JMenuBar jMenuBar = new JMenuBar(); 
	
	JMenu operateMenu = new JMenu ("User");
	
	public JMenuItem loginItem = new JMenuItem ("Login");
	public JMenuItem logoffItem = new JMenuItem ("Logout");
	public JMenuItem exitItem=new JMenuItem ("Exit");

	JMenu conMenu=new JMenu ("Settings");
	public JMenuItem userItem=new JMenuItem ("User Settings");
	public JMenuItem connectItem=new JMenuItem ("Connect Settings");

	JToolBar toolBar = new JToolBar();
	
	//Buttons
	public JButton loginButton;
	public JButton logoffButton;
	public JButton userButton;
	public JButton connectButton;
	public JButton exitButton;

	//set the size of TextField
	Dimension faceSize = new Dimension(400, 700);

	JPanel downPanel ;
	GridBagLayout girdBag;
	GridBagConstraints girdBagCon;
		
	public MainFrame() {
		init();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		
		this.setSize(faceSize);
		this.setVisible(true);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation( (int) (screenSize.width/2 - faceSize.getWidth()/2),
						 (int) (screenSize.height/2 - faceSize.getHeight()/2));
		this.setResizable(false);
		this.setTitle(clientListener.userName);
	}
	
	//init
	public void init() {

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		operateMenu.add (loginItem);
		operateMenu.add (logoffItem);
		operateMenu.add (exitItem);
		jMenuBar.add (operateMenu); 
		
		conMenu.add (userItem);
		conMenu.add (connectItem);
		jMenuBar.add (conMenu);
		
		setJMenuBar (jMenuBar);

		//init buttons
		loginButton = new JButton("Login");
		logoffButton = new JButton("Logout");
		userButton  = new JButton("Set User");
		connectButton  = new JButton("Set connection");
		exitButton = new JButton("Exit");

		//add buttons to the toolBar
		toolBar.add(userButton);
		toolBar.add(connectButton);
		toolBar.addSeparator();
		toolBar.add(loginButton);
		toolBar.add(logoffButton);
		toolBar.addSeparator();
		toolBar.add(exitButton);
		contentPane.add(toolBar,BorderLayout.NORTH);

		checkbox = new JCheckBox("private");
		checkbox.setSelected(false);

		actionlist = new JComboBox<String>();
		actionlist.addItem(" ");
		
		actionlist.setSelectedIndex(0);

		loginButton.setEnabled(true);
		logoffButton.setEnabled(false);
		
		combobox = new JComboBox<String>();
		combobox.insertItemAt("Everyone",0);
		combobox.setSelectedIndex(0);
		
		messageShow = new JTextArea();
		messageShow.setEditable(false);
		
		//scroll bar
		messageScrollPane = new JScrollPane(messageShow,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		messageScrollPane.setPreferredSize(new Dimension(400,400));
		messageScrollPane.revalidate();
		
		clientMessage = new JTextField(20);
		clientMessage.setEnabled(false);
		clientMessageButton = new JButton();
		clientMessageButton.setText("Send");

		sendToLabel = new JLabel("Send to: ");
		messageLabel = new JLabel("Message: ");
		downPanel = new JPanel();
		girdBag = new GridBagLayout();
		downPanel.setLayout(girdBag);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 0;
		girdBagCon.gridwidth = 5;
		girdBagCon.gridheight = 2;
		JLabel blankLine1 = new JLabel("    ");
		girdBag.setConstraints(blankLine1,girdBagCon);
		downPanel.add(blankLine1);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 2;
		girdBagCon.insets = new Insets(1,0,0,0);
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
		girdBagCon.gridwidth = 3;
		girdBagCon.gridheight = 1;
		girdBag.setConstraints(clientMessage,girdBagCon);
		downPanel.add(clientMessage);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 4;
		girdBagCon.gridy = 3;
		girdBag.setConstraints(clientMessageButton,girdBagCon);
		downPanel.add(clientMessageButton);

		showStatus = new JTextField(35);
		showStatus.setEditable(false);
		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 5;
		girdBagCon.gridwidth = 5;
		girdBag.setConstraints(showStatus,girdBagCon);
		downPanel.add(showStatus);
		
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 6;
		girdBagCon.gridwidth = 5;
		girdBagCon.gridheight = 2;
		JLabel blankLine = new JLabel("    ");
		girdBag.setConstraints(blankLine,girdBagCon);
		downPanel.add(blankLine);

		contentPane.add(messageScrollPane,BorderLayout.CENTER);
		contentPane.add(downPanel,BorderLayout.SOUTH);
		
		clientListener = new ClientListener(this);
		
		this.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					if(clientListener.isConnected == 1){
						clientListener.DisConnect();
					}
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

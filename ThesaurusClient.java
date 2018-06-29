

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**
 * @author Niya Jaison | UTA ID : 1001562701 | Net ID:nxj2701
 * References:	https://github.com/sumanthl158/Thesaurus/tree/master/src/com/client/server
 *				https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
 *				http://docs.oracle.com/javase/tutorial/uiswing/components/frame.html
 * The class contains the client GUI creation, client server connection initiation and displaying the server output
 */
public class ThesaurusClient  extends JFrame implements Observer{

	private static final long serialVersionUID = 1L;
	/** 
	 * Author: Niya Jaison | UTA ID : 1001562701 
	 * The Member variables of the class
	 * blockSentence - A Text Area to display the block of text from which the user can select any word
	 * searchWord    - A text field to enter the required word for searching the synonym
	 * sendButton    - A button to initiate the request to Server
	 * synonymList   - A label to display the synonym list/message from the server 
	 * lblNewLabel, verticalStrut,verticalStrut_1,verticalStrut_2,verticalStrut_3 - swing components for adding labels/spaces 
	 */
	private JTextArea blockSentence;
	private JTextField searchWord;
	private JButton sendButton;
	private JLabel synonymList;
	private JLabel lblNewLabel;
	private Component verticalStrut;
	private Component verticalStrut_1;
	private Component verticalStrut_2;
	private Component verticalStrut_3;
	static Socket clientSocket;

	/**
	 * Author: Niya Jaison | UTA ID : 1001562701 
	 * Constructor for Calling the function to build the UI of the client application and
	 * setting the common properties of the frame
	 * 
	 */
	public ThesaurusClient() {

		createAppUI();
		setTitle("Thesaurus Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true); 

	}

	/**
	 * Author: Niya Jaison | UTA ID : 1001562701 
	 * The method is for creating the UI of the client part in the Thesaurus Application
	 * Includes: Initializing the member variables, adding the components to the frame
	 *           adding listeners to the required components
	 */

	private void createAppUI() {

		Box box = Box.createVerticalBox();
		getContentPane().add(box, BorderLayout.NORTH);


		lblNewLabel = new JLabel("Double Click A Word From the below Block of Text or Enter "
				+ "a Word in the Text box");
		box.add(lblNewLabel);

		verticalStrut = Box.createVerticalStrut(20);
		box.add(verticalStrut);
		
		
		blockSentence = new JTextArea(10, 10);
		blockSentence.setText("With the miniaturization of computing resources, and advancements in portable battery life,"
				+ "portable computers grew in popularity in the 2000s.The same developments that spurred the growth of laptop computers and other "
				+ "portable computers allowed manufacturers to integrate computing resources into cellular phones. ");
		blockSentence.setToolTipText("Double click to add to field");
		blockSentence.setWrapStyleWord(true);
		blockSentence.setEditable(true);
		blockSentence.setLineWrap(true);

		JScrollPane scrollPane = new JScrollPane(blockSentence);
		box.add(scrollPane);

		searchWord = new JTextField();
		box.add(searchWord);

		verticalStrut_1 = Box.createVerticalStrut(20);
		box.add(verticalStrut_1);

		sendButton = new JButton("Synonyms");
		box.add(sendButton);

		verticalStrut_2 = Box.createVerticalStrut(20);
		box.add(verticalStrut_2);

		synonymList = new JLabel("");
		box.add(synonymList);
		synonymList.setEnabled(false);

		verticalStrut_3 = Box.createVerticalStrut(20);
		box.add(verticalStrut_3);

/*		addToFile= new JButton("Add To File");
		box.add(addToFile);
		addToFile.setVisible(false);*/


		/**
		 * Author: Niya Jaison | UTA ID : 1001562701 
		 * Input: MouseListner instance
		 * Output : void
		 * Function : Adding a Mouse Listener to the block of text.
		 * This is to implement the functionality that on double clicking any word form the sentence
		 * the word is populated in the text field.
		 */
		blockSentence.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2) {
					synonymList.setText("");
					//System.out.println("double clisk recognized");
					searchWord.setText(blockSentence.getSelectedText()); /**To populate the text field with the doubled clicked word in the block text.*/
				}

			}
		});

		/**
		 * Author: Niya Jaison | UTA ID : 1001562701 
		 * Input: ActionListner instance
		 * Output : void
		 * Function : Adding an Action Listener to the Search button.
		 * This is to initiate the server search for the Synonym.
		 * Also displays error message when the text-box doesn't contain any word to initiate search
		 */

		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String textInField = searchWord.getText(); /**Getting the word populated/entered in the text field*/
				//System.out.println("in actionlistner");
				/**
				 * The below loop is to display the error message when no word is selected or entered
				 */
				if(textInField.isEmpty()) {
					//System.out.println("Please enter a word or Double click one from above sentence");
					synonymList.setText("Please enter a word or Double click one from above sentence");
				}
				else {
					sendToServerToSearch(searchWord.getText());/**Calling the user defined function for creating connection and sending request to the client*/
				}
			}
		});

		
	}

	/**
	 * Author: Niya Jaison | UTA ID : 1001562701 
	 * displayServerOutput() function is used to display the
	 * Input: messageFromServer - the synonym list send from the server.
	 * Output: void
	 */
	public void displayServerOutput(String messageFromServer) {
		synonymList.setVisible(true);
		synonymList.setText("Synonyms:"+messageFromServer);
	
		if(messageFromServer.isEmpty()) { /**To display the message when no synonym is returned by the server.*/
			//System.out.println("in No Synonym in the system");
			synonymList.setText("No Synonym present for the word");
		}
		else if(messageFromServer.equalsIgnoreCase("Server Not connected")){ /**To display the error message when server connection is lost*/
			synonymList.setText("Server has shut down or has lost the connection");

		}
		pack();
	}

	@Override public void update(Observable o, Object arg) {}
	
	/**
	 *@author  Niya Jaison | UTA ID : 1001562701 
	 * Input : A String Parameter, which is the input word
	 * Output : void
	 * Function: Initializes the socket ,a bufferedReader to read the text send from the server, 
	 * a DataOutputStream to write the request to the server.
	 */
	public void sendToServerToSearch(String wordFromUI)  {
		//System.out.println("in here to intate the server connection"+wordFromUI);
		String synonymList;
		//BufferedReader console = new BufferedReader( new InputStreamReader(System.in));
		
		try {
			clientSocket = new Socket("localhost", 6790); /**Initializing the socket with localhost and the port*/
			DataOutputStream dataToServer = new DataOutputStream(clientSocket.getOutputStream());
			//System.out.println("done with dataOuput in client");
			dataToServer.writeBytes(wordFromUI+"\n");/**Writing the request to the server*/
			//System.out.println("after writeTo in CLient");
			BufferedReader dataFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			//System.out.println("after bufferresader - client");

			synonymList = dataFromServer.readLine();/**Reading the response from the server*/
			//System.out.println("FROM SERVER: " + synonymList);
			
			displayServerOutput(synonymList);/**Calling the user defined function to display the synonym list/error in the client GUI part*/
			//clientSocket.close();
		} catch (IOException ioExcpetion) {
			// TODO Auto-generated catch block
			
			//ioExcpetion.printStackTrace();
			displayServerOutput("Server Not connected");
		}catch (Exception exception) {
			// TODO: handle exception
			//exception.printStackTrace();
			displayServerOutput("Server Not connected");
		}

	}
	/***
	 * @author Niya Jaison | UTA ID : 1001562701
	 * The main method for starting the execution. 
	 */
	public static void main(String[] args) {
		JFrame theasurusClient=new ThesaurusClient();
		
		theasurusClient.addWindowListener(new WindowListener() {
			
			@Override public void windowOpened(WindowEvent e) {}
			@Override public void windowIconified(WindowEvent e) {}
			@Override public void windowDeiconified(WindowEvent e) {}
			@Override public void windowDeactivated(WindowEvent e) {}
			@Override public void windowClosing(WindowEvent e) {}
			@Override public void windowActivated(WindowEvent e) {}
			
			/**
			 *@author  Niya Jaison | UTA ID : 1001562701 
			 * Input : WindowEvent
			 * Output : void
			 * Function: CLoses the client socket when the client GUI is closed
			 */
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				try {
					clientSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
			}
			
		});


	}
	



}

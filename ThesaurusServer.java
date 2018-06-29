

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


/**
 * @author Niya Jaison | UTA ID : 1001562701 | Net ID:nxj2701
 * References:	https://github.com/sumanthl158/Thesaurus/tree/master/src/com/client/server
 *				https://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
 *				http://docs.oracle.com/javase/tutorial/uiswing/components/frame.html
 * The server class includes the Server GUI creation , socket creation, accepting connection from client
 * processing the client request- finding the synonym list from the file system.
 * 
 */
public class ThesaurusServer extends JFrame {

	private static final long serialVersionUID = 8353727450905398345L;
	/**
	 * @author Niya Jaison | UTA ID : 1001562701 
	 * The Member variables of the class
	 * dataFromClients - A Text Area to display the messages received from the client
	 * dataToClients - A Text Area to display the messages received from the client
	 * lblClientRequest, lblServerResponse,horizontalStrut - swing components for adding labels/spaces 
	 */
	private static JTextArea dataFromClients;
	private JLabel lblClientRequest;
	private JLabel lblServerResponse;
	private static JTextArea dataToClients;
	private Component horizontalStrut;
	static ServerSocket serverSocket;

	/**
	 * @author Niya Jaison | UTA ID : 1001562701 
	 * Constructor ThesaurusServer() is for Calling the function to build the UI of the server application and
	 * setting the common properties of the frame
	 */
	public ThesaurusServer() {

		createAppUI();
		setTitle("Thesaurus Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true); 

	}

	/**
	 * @author Niya Jaison | UTA ID : 1001562701 
	 * The method createAppUI() is for creating the UI of the server part in the Thesaurus Application
	 * Includes: Initializing the member variables, adding the components to the frame
	 */

	private void createAppUI() {

		Box box = Box.createHorizontalBox();
		getContentPane().add(box, BorderLayout.CENTER);


		lblClientRequest = new JLabel("Client Requests");
		box.add(lblClientRequest);
		dataFromClients = new JTextArea(20, 20);
		dataFromClients.setTabSize(30);
		dataFromClients.setToolTipText("Double click to add to field");
		dataFromClients.setWrapStyleWord(true);
		dataFromClients.setEditable(false);
		dataFromClients.setLineWrap(true);

		JScrollPane scrollPane = new JScrollPane(dataFromClients);
		box.add(scrollPane);

		horizontalStrut = Box.createHorizontalStrut(20);
		box.add(horizontalStrut);

		lblServerResponse = new JLabel("Server Responses");
		box.add(lblServerResponse);

		dataToClients = new JTextArea(20, 20);
		dataToClients.setWrapStyleWord(true);
		dataToClients.setToolTipText("Double click to add to field");
		dataToClients.setLineWrap(true);
		dataToClients.setEditable(false);
		JScrollPane scrollPane_1 = new JScrollPane(dataToClients);
		box.add(scrollPane_1);	
	}

	/**
	 * @author Niya Jaison | UTA ID : 1001562701 
	 * Input : @param messageFromClient -  message received from the client , 
	 * @param addressOfClient - the Host address of the client
	 * Output :  void
	 * Function: Display the word received from the client in server window
	 *           Each of the request is appended to the TextArea
	 */
	public static void displayServerInput(String messageFromClient, InetAddress addressOfClient) {
		dataFromClients.append("Request from Client: "+addressOfClient.getHostAddress()+" : Word for serach :"+messageFromClient+"\n");
	}

	/**
	 * @author Niya Jaison | UTA ID : 1001562701
	 * Input : @param messageFromServer -  message to be sent to the client , 
	 * @param addressOfClient - the InetAddress of the client
	 * Output :  void 
	 * Function: Display the synonym list/response message to be send to client in server window.
	 *           Each of the response is appended to the TextArea
	 */
	public static void displayServerOutput(String messageFromServer, InetAddress addressOfClient) {
		if(messageFromServer.isEmpty()) {
			dataToClients.append("Response to Client: "+addressOfClient.getHostAddress()+" No Synonym found in the file system\n");
		}
		else {
			dataToClients.append("Response to Client: "+addressOfClient.getHostAddress()+" : Synonym List:"+messageFromServer+"\n");
		}

	}
	
	/***
	 * @author Niya Jaison | UTA ID : 1001562701
	 * The main method for starting the execution. 
	 */
	public static void main(String argv[]) 
	{
		JFrame thesaurusServer=new ThesaurusServer();
		String wordForSearch;
		String synonymList;
		try {
			serverSocket = new ServerSocket(6790); /***creating a server socket that listens to the port 6790*/
			while(true)
			{
				/**
				 * Initializes the socket to accept the connection from the client,
				 * a bufferedReader to read the text send via the client, a DataOutputStream to write the response to the client
				 * and a ReadFile class object to initiate 
				 * reading from the file system.
				 * 
				 * */
				Socket connectionSocket = serverSocket.accept(); /***Accepting the connection request from the client socket*/
				//System.out.println("in here server");
				ReadFromFile readFromFile=new ReadFromFile();
				BufferedReader dataFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));/*** The Buffered reader to get the data send from the client.*/ 
				//System.out.println("after buffered reader in server");
				wordForSearch = dataFromClient.readLine();/*** Reading the string send by the client.i.e, the word to be searched*/
				//System.out.println("read the word from client in server");
				//System.out.println("Received: " + wordForSearch);
				//connectionSocket.getInetAddress();

				displayServerInput(wordForSearch,connectionSocket.getInetAddress()); /*** Invoking a user-defined function to display the client message in server UI.*/
				synonymList=readFromFile.getSynonymFromFile(wordForSearch);/*** calling a user-defined function to check the file system for "synonym" of the received word.*/

				DataOutputStream dataToClient = new DataOutputStream(connectionSocket.getOutputStream());
				displayServerOutput(synonymList, connectionSocket.getInetAddress());/*** Calling a user-defined function to display the server response (synonym list) in server UI.*/
				dataToClient.writeBytes(synonymList+"\n");/***Writing the synonym list fetched from the file system to the DataOutputstream(to be send to client) */
				thesaurusServer.addWindowListener(new WindowListener() {
					/**
					 *@author  Niya Jaison | UTA ID : 1001562701 
					 * Input : WindowEvent
					 * Output : void
					 * Function: CLoses the server socket when the server GUI is closed
					 */
					@Override
					public void windowClosed(WindowEvent e) {
						// TODO Auto-generated method stub
						try {
							serverSocket.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							//e1.printStackTrace();
						}
					}
					/***
					 * The below set of functions are to be implemented as part of implementing 
					 * the WindowEvent Interface
					 * */
					@Override public void windowOpened(WindowEvent e) {}					
					@Override public void windowIconified(WindowEvent e) {}					
					@Override public void windowDeiconified(WindowEvent e) {}				
					@Override public void windowDeactivated(WindowEvent e) {}
					@Override public void windowClosing(WindowEvent e) {}
					@Override public void windowActivated(WindowEvent e) {}

					
					
				});

			}
		}catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
		}

	}


}

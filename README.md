# Thesaurus-Client-Server
The client process will have a simple GUI interface and will allow the user to select a word in a block of text and have the system send a query to a server to look up the word in a thesaurus file and return a list of alternative words from the server. Your client process will connect to the server and send the word that the user has selected in an input text box. The server will return a string that will contain alternative words that the user might use instead of the input word. These alternative words will be separated by commas. The string returned for the word “abandon” might look like this: “drops, dumps, ditches, discards.
Once the client has returned a result, it will allow the user to input another word to reference against the thesaurus.
Your server will have a file or database that it will read to find the string of words related to the input word. It will return the associated string to the client process. It will return the string to the client and keep the connection open. The client and server will each have an Exit option that will close the program.

The main modules are:
1. The client module: A GUI created using JFrame and swing components. The GUI consist of Text area which hold the block of text, a TextField to either enter the word for search or to display the word selected from the block of text and a Button to initiate the connection and send the request to the server.
2. The Server module: A GUI created using JFrame and swing components. The GUI consist of two Text areas - one to display the client request message and one to display the server response list.

The File system: An excel file which contains the synonym words (row wise).

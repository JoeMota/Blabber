import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.*;
/* ------------------------
----------- */
public class CL2_MOTA{
	public static void main(String [] args) throws Exception {
		boolean blabbering = true;//to blab
		Scanner keyboard  = new Scanner(System.in);//keyborad for user
		welcomeHeader("blabber-header.txt");
		
		while(blabbering){
			System.out.println("Give a command, or type [quit] to exit, or type [help] to see all the " + 
			"commands and their descriptions");//asking user on which commands they want to use
			String input = keyboard.nextLine();//input
			String [] splitCommand = input.split(" ");

			if(splitCommand.length == 1){
				if(input.equalsIgnoreCase("help"))
					commandList();
				else if(input.equalsIgnoreCase("quit"))
					blabbering = false;
				else{
					System.out.println(input + " is not a command");
				}
			}// end of length 1 if statement

			else if(splitCommand.length == 2){
				if(splitCommand[0].equalsIgnoreCase("CreateAccount")){
					if(createAccount(splitCommand[1]) == 0)
						System.out.println("Success: " + splitCommand[1] + " now has an account.");
					else if(createAccount(splitCommand[1]) == 1)
						System.out.println("Error: " + splitCommand[1] + " is already taken.");
					else if(createAccount(splitCommand[1]) == 2)
						System.out.println("Error: " + splitCommand[1] + " is not alphanumeric.");
					else{
						System.out.println("Invalid Entry");
					}
				}//create account if
				else if(splitCommand[0].equalsIgnoreCase("ViewTimeline")){
					welcomeHeader("viewtimeline.txt");
					if(viewTimeline(splitCommand[1]) == 0){
						//viewTimeline(splitCommand[1]);
					}
					else{
						System.out.println("error");
					}

				}//else if for timeline
			}// else if of 2

			else if(splitCommand.length == 3){
				if(splitCommand[0].equalsIgnoreCase("FollowAccount")){
					if(followAccount(splitCommand[1],splitCommand[2]) == 0){
						System.out.println("Success: " + splitCommand[1] + " is now following " + splitCommand[2]);
					}//end of if
					else if(followAccount(splitCommand[1],splitCommand[2]) == 1){
						System.out.println("Username(s) doesnt exist");
					}//end of else it
					else if(followAccount(splitCommand[1],splitCommand[2]) == 2){
						System.out.println("You cannot follow yourself");
					}//end of else if
					else if(followAccount(splitCommand[1],splitCommand[2]) == 3){
						System.out.println("You already follow the user");
					}//end of else if
				}//end of if FollowAccount

				else if(splitCommand[0].equalsIgnoreCase("PostBlab")){
					String [] s = input.split(" ", 3); //PostBlab someuser1 "this is a message"
					System.out.println("Message you're trying to post: " + s[2]);
					if(postBlab(s[1], s[2]) == 0){
						System.out.println("Success: " + s[2] + " was posted!");
					}
					else{ 
						System.out.println("Error");}
				}// end of post blab

			}//end of else 3
		}//end of while blabbering
	}//END main

	/* -------------------------------------------------------------------------------- FILE EXISTS  
	   This method will take a fileName as a parameter, and
	   return true if it exists, false otherwise.
	*/
	public static boolean fileExists( String fileName ){
		File fileOne = new File (fileName + ".txt");
		//System.out.println("In fileExists method. checking if this file exists: " + fileOne);
		if(fileOne.exists()){
			return true;
		}
		return false;
	}//END fileExists


	/* -------------------------------------------------------------------------------- NUM LINES 
	   	This method will take a fileName as a parameter and return
	   	the number of lines this file has.
	 */
	public static int numLines( String fileName )throws Exception{
		int count = 0;

		if( !fileExists(fileName)){
			System.out.println(fileName + " does not exist.");
			return -1;
		}

		File file = null;
		Scanner sc = null;
		try{
				
			file = new File(fileName + ".txt");
			sc = new Scanner(file);
			while( sc.hasNextLine() ){
				sc.nextLine();
				count++;
			}

			sc.close();
			//return count;
						
		}catch(Exception errorMsg){
			//System.out.println("There was an error reading your file.");
			return 0;
		}
		
		return count;
	}//END numLines

	/* -------------------------------------------------------------------------------- READ FILE
	   	This method will take a fileName as a parameter and return an 
	   	array of Strings that stores each line in the file onto the array. 
	*/
	public static String [] readFile( String fileName )throws Exception{

		String [] contents = new String[ numLines(fileName) ];

		try{
			int i = 0;
			File file = new File(fileName + ".txt");
			Scanner fileReader = new Scanner(file);
			while(fileReader.hasNextLine()){
				contents[i] = fileReader.nextLine();
				i++;
				
			
			}//end of while



			return contents;
		} catch( Exception errorMsg ){
			System.out.println("There was an error with your file.");

		}
		

		//end of if
		return contents;
	
	}//END readFile

	/* -------------------------------------------------------------------------------- ACCOUNT EXISTS
		This method will take a username as a parameter and return true if
		the username text file exists, false otherwise.
	*/
	public static boolean accountExists(String username){
		if(fileExists(username)){
			return true;
		}
	    return false;
  	}//END accountExists

  	/*  -------------------------------------------------------------------------------- IS ALPHANUMERIC
  		This method will take a username as a parameter and check if it is
  		alphanumeric. A phrase is alphanumeric if it only contains uppercase 
  		letters, lowercase letters, and numbers. 
	 	Return true if the phrase is alphanumeric, false otherwise.
	*/ 
	public static boolean isAlphanumeric( String username ) {
		 if(username.matches("^[a-zA-Z0-9]*$")){
		 	return true;
		 }
		 return false;
	}//END isAlphanumeric

  	/* -------------------------------------------------------------------------------- CREATE ACCOUNT  
		This method will take a username as a parameter and either:
			- return 0 if the account is successfully created.
			- return 1 if an account already exists with the given username
			- return 2 if the username entered is invalid (not alphanumeric)
	*/
	public static int createAccount(String username){
		if(!isAlphanumeric(username))
			return 2;
		else if(accountExists(username))
			return 1;
		else if(isAlphanumeric(username) && !accountExists(username)){
			File file = new File(username + ".txt");
			try{
				file.createNewFile();
			} catch(IOException errorMsg){
				System.out.println("IO Error: " + errorMsg );
			}
			return 0;
		}//end of else if for success
		else{
			return 2;
		}
	}//END createAccount

	/* -------------------------------------------------------------------------------- FOLLOW ACCOUNT  
		This method will take username1 and username2. It will make 
		username1 follow username2.
			- Return 0 upon success.
	   		- Return 1 if either username doesnt exist or is invalid
	   		- Return 2 if a user is	trying to follow themselves
	   		- Return 3 if the user already follows the other user 
	*/
	public static int followAccount(String username, String usernameToFollow) throws Exception {
		if(username.equalsIgnoreCase(usernameToFollow)){
			return 2;//follow themselves
		}
		if(!accountExists(username) || !accountExists(usernameToFollow)){
			return 1;//accounts dont exists
		}



		
		String file = username + ".txt";
		PrintWriter writeToFile = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));;
  		
  		try {
			String [] following = readFile(username);
		for( int i = 0; i < following.length; i++){
			if(following[i].equalsIgnoreCase(usernameToFollow)){		
				return 3;
			}//end of if
		}//end of for loop

			writeToFile.println(usernameToFollow);
			writeToFile.close();
			return 0;
  		} catch (Exception e) {
     		System.out.println( "ERROR: " + e ); 
  		}
  		return 0;

	}//END followAccount

	/* -------------------------------------------------------------------------------- POST BLAB  
		This method will take a username and a blabMessage as a parameter
		and append the blabMessage into the blabs.txt file.
			- Return 0 upon success
	 		- Return 1 if the username doesn't exist or is invalid
	*/
	public static int postBlab(String username, String blab) throws Exception {
		if( !accountExists(username)){
			System.out.println(username + "does not exist");
	  		return 1;
	  	}

  		try {
			PrintWriter writeToFile = new PrintWriter(new BufferedWriter(new FileWriter("blabs.txt", true)));
			writeToFile.println(username + "-" + blab );
			writeToFile.close();
			return 0;
			
  		} catch (IOException e) {
     		System.out.println("ERROR: Could not post your blab at this time.");
  		}
  		return 1;
  		
	}//END postBlab

	/* -------------------------------------------------------------------------------- DISPLAY ARRAY 
		This method will take an array of strings containing the blabs
		from the blabs.txt file and an array of strings containing the
		names of the accounts a specific user follows.
		It will print out each element in the given array on it's own 
		line as shown on the assignment for viewTimeline.
	*/
  	public static void displayArray(String [] blabs, String [] followedAccounts) {
  		for(int i = 0; i < blabs.length; i++){
  			String [] divBlab = blabs[i].split("-");
  			for(int j = 0; j < followedAccounts.length; j++){
  				if(divBlab[0].equals(followedAccounts[j])){
  					System.out.println(divBlab[0]);
  					System.out.println(divBlab[1]);
  					System.out.println("--------------------------");
  				}
  			}
  		}
  		return;
  	}//END displayArray

	/* -------------------------------------------------------------------------------- VIEW TIMELINE
		This method will take a username as a parameter and will display
		all the blabs this user follows in a timeline.
			- Return 0 upon success OR if the user is not following anyone (as it is still successfull)
				- if the user is not following anyone, display a message as such:
				>> You may want to consider following someone to view a timeline.
			- Return 1 if the username doesn't exist or is invalid
	*/
	public static int viewTimeline( String username ) throws Exception {
		if(!accountExists(username)){
				return 1;
		}
		String [] blabs = readFile("blabs");
		String [] following = readFile(username);
		try{

			displayArray(blabs,following);
			return 0;
		}catch (Exception e){
			System.out.println("ERROR with file");
		}
		return 0;
	}//END viewTimeline
	/* -------------------- APPEND BLAB  */
	public static String [] appendBlab( String userInput ){
		userInput = userInput.replace("\"", ""); //gets rid of quotations
		String [] blab = userInput.split(" ");

		String [] request = new String[3];
		request[0] = blab[0]; //store command in first position of request
		request[1] = blab[1]; //stores username in second position of request
		request[2] = "";

		//Ignore first position as that contains the command
		for( int i = 2; i < blab.length; i++ ){
			request[2] += blab[i] + " "; //appends message in third position
		}

		return request;
	}//END appendBlab

	/* -------------------- WELCOME HEADER  */
	public static void welcomeHeader( String fileName ){
		try{
			Scanner blabberHeader = new Scanner( new File( fileName ) );
			while( blabberHeader.hasNext() ){
				System.out.println( blabberHeader.nextLine() );
			}
		}
		catch( FileNotFoundException msg ){
			System.out.println(msg);
		}
	}//END welcomeHeader

	/* -------------------- COMMAND LIST  */
	public static void commandList(){
		System.out.println(
			"\t[CreateAccount username]\n\n\tThis command expects you to enter" 
			+"\n\ta unique username. Your username must contain the following alpha-"
			+"\n\tnumeric values: a-z, A-Z, and 0-9 and must be greater than 4 in length."
			+"\n\n\n"
			+"\t[FollowAccount username username_to_follow]\n\n\tThis command expects you to"
			+"\n\tto enter the name of the user you would like to follow. This will allow"
			+"\n\tyou to see their blabs on your feed. You may NOT follow yourself."
			+"\n\n\n"
			+"\t[PostBlab username \"your message in quotations\"]\n\n\tThis command will allow you to"
			+"\n\tpost a blab."
			+"\n\n\n"
			+"\t[ViewTimeline userName]\n\n\tThis command will allow the current logged in user to view"
			+"\n\tall of the blabs of the user's they follow. By default they will view the"
			+"\n\tblabs in chronological order, unless specified."
			+"\n\n\n"
			+"\t[quit]\n\n\tThis command will end the program\n"
		);
		return;
	}//END commandList
}
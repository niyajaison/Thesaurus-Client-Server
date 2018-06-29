

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * @author Niya Jaison | UTA ID : 1001562701 | Net ID:nxj2701
 * References:	https://stackoverflow.com/questions/1516144/how-to-read-and-write-excel-file
 * The ReadFromFile class is used to read the file system(excel file), find whether synonym exist for the entered word
 * and to return the list of synonyms(if they exist in the file system).
 * 
 */
public class ReadFromFile {
	/**
	 * @author Niya Jaison | UTA ID : 1001562701 
	 * The member variables of the class.
	 * FILE_NAME 		- The name and path of the file system-excel file
	 * thesaurusFile 	- File input stream for reading from the file
	 * thesaurusWorkbook	- A XSSFWorkbook to access the workbook of the excel file
	 * thesaurusSheet	- A XSSFSheet to access the sheet in the workbook of the excel file
	 */
	private static final String FILE_NAME = "ThesaurusFileSystem.xlsx";
	static FileInputStream thesaurusFile;
	static XSSFWorkbook thesaurusWorkbook;
	static XSSFSheet thesaurusSheet;
	
	/**
	 * @author Niya Jaison | UTA ID : 1001562701 
	 * Input		-none
	 * Output	-none
	 * Function	- Initializes the member variables
	 */
	ReadFromFile() throws IOException {
		// TODO Auto-generated constructor stub
		thesaurusFile = new FileInputStream(new File(FILE_NAME));
		thesaurusWorkbook = new XSSFWorkbook(thesaurusFile);
		thesaurusSheet = thesaurusWorkbook.getSheetAt(0);
		
	}
	
	/**
	 * @author Niya Jaison | UTA ID : 1001562701 
	 * Input		-A String parameter which is the word to be searched for synonym 
	 * Output	-A String variable which will be the synonym list for the searched word or null if no such word is found
	 * Function	-Reads the file system data and compare with the searched text to check whether the file system has the word.
	 * 			 If word is present the remaining words in the corresponding row is sent back as synonym list.
	 * 			 otherwise the null string is returned
	 * Exception - FileNotFoundException.
	 */
	public String getSynonymFromFile(String searchedWord) {
		String synonymList= "";
		try {
			
			//System.out.println("here");
			int foundRow = -1;		
			
			for(Row thesaurusRow: thesaurusSheet){/** iterating through each row of the excel file*/
				//System.out.println("in here");
				Integer cellCount=0;
				int totalCellCount = (int)(thesaurusRow.getLastCellNum());/**Getting the last cell count hence by getting the total cells present*/
				//System.out.println(totalCellCount + "in row: "+thesaurusRow.getRowNum());
				while(totalCellCount>cellCount && foundRow ==-1) {/***Iterating through each cell per row and only if the word has not yet been found */
					//System.out.println("in here"+totalCellCount + "in row: "+thesaurusRow.getRowNum());
					if((thesaurusRow.getCell(cellCount).toString()).equalsIgnoreCase(searchedWord)){ /**Comparing each cell value to the word recieved from client*/
						foundRow=thesaurusRow.getRowNum(); /**storing the row number in which the word was found*/
						break;
					}
					cellCount++;

				}
			}
			/**
			 * The below loop is used to extract the synonyms of the word from the row
			 */
			
			if(foundRow!=-1) {
				Row thesaurusMatchedRow = thesaurusSheet.getRow(foundRow);
				int totalCellCount = (int)(thesaurusMatchedRow.getLastCellNum());
				for(int i=0;i<totalCellCount;i++) {
					if(thesaurusMatchedRow.getCell(i).toString().equalsIgnoreCase(searchedWord)==false) {
						synonymList+=(thesaurusMatchedRow.getCell(i).toString())+",";
					}
				}
				//System.out.println("Synonyms: "+synonymList);

			}
			//return synonymList;
		}
		catch(Exception e){
			Logger.getLogger(searchedWord, searchedWord) ;
		}
		return synonymList;

	}

}
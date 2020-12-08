package com.mediabibliotek;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Collections;
//import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.collections.ArrayList;
import com.collections.LinkedList;

import com.collections.*;

/**
 * Controller klassen för applikationen som sköter all logik i Bibliotekssystemet. 
 */

public class LibraryController
{
	Borrower currentBorrower;
	GUI theGUI;
	ArrayList<Media> allMediaObjects;
	ArrayList<String> borrowed;
	ArrayList<Borrower> allBorrowers;

	LinkedList<Media> mediaSearchResults;
	
	GUI alert = new GUI();

	/**
	 * Konstruktor som ser till att boota systemet med samtliga Media, Borrowers, Borrowed och MediaSearchResults i ArrayList 
	 */
	public LibraryController()
	{
		allMediaObjects = new ArrayList<Media>(24);
		allBorrowers = new ArrayList<Borrower>();
		borrowed = new ArrayList<String>();
		mediaSearchResults = new LinkedList<Media>();
		boot();
	}
	
	/**
	 * Konstruktor som ser till att boota systemet med samtliga Media, Borrowers, Borrowed och MediaSearchResults i ArrayList 
	 * samt instantiera GUI fönstret
	 */
	LibraryController(GUI parGUI)
	{
		theGUI = parGUI;
		allMediaObjects = new ArrayList<Media>(25);
		allBorrowers = new ArrayList<Borrower>();
		borrowed = new ArrayList<String>();
		mediaSearchResults = new LinkedList<Media>();

		boot();
	}
	

	/**
	 * Kollar om användaren matar in data i felaktig format och meddelar om detta.
	 * @param inputString. Strängen som tas emot som input  
	 * @return. True om det är korrekt dataformat, annars False.
	 */
	public boolean checkUserInput(String inputString)
	{
		String regex = "^[\\w.-]+$";
		try
		{
			if(inputString.matches(regex)==false)
			{
				return false;
			}
		}
		catch(Exception e)
		{
			return false;
		}
		
		return true;
	}
	/*
	 * Samma som ovan fast strängen får enbart innehålla siffro, används till Media-ObjectID
	 */
	
	public boolean checkInputOnlyDigits(String inputString)
	{
		// Får endbart innehålla heltal
		String regex = "^-?\\d*?\\d+$";
		if(inputString==null)
		{
			//JOptionPane.showMessageDialog(null, "Incorrect characters only 0-9 are allowed", "Error", JOptionPane.ERROR_MESSAGE);
			alert.alertError("Incorrect characters only 0-9 are allowed");
			return false;
		}
		else if(!inputString.matches(regex))
		{
			//JOptionPane.showMessageDialog(null, "Incorrect characters only 0-9 are allowed", "Error", JOptionPane.ERROR_MESSAGE);
			alert.alertError("Incorrect characters only 0-9 are allowed");
			return false;
		}
		try
		{
			if(Integer.parseInt(inputString)<Integer.MIN_VALUE)
			{
				//JOptionPane.showMessageDialog(null, "The number is too low, min is " + Integer.MIN_VALUE, "Error", JOptionPane.ERROR_MESSAGE);
				alert.alertError("The number is too low, min is " + Integer.MIN_VALUE + " Error");
				return false;
			}
		}
		catch(Exception e)
		{
			//JOptionPane.showMessageDialog(null, "The number is too high or low, only this is allowed: " + Integer.MIN_VALUE + " - " +  Integer.MAX_VALUE, "Error", JOptionPane.ERROR_MESSAGE);
			alert.alertError("The number is too low, min is " + Integer.MIN_VALUE + " Error");
			return false;
		}	
		
		return true;
	}


	/**
	 * Skapar en fil och skriver innehållet i Content i filen.
	 * @param Content. Innehehållet som ska skrivas över till filen. 
	 * @param FileNameIncPath. Sökvägen till filen.
	 */
	public void writeToFile()
	{
		System.out.println("writeToFile");
		
		try
		{
			PrintWriter theOutPutf = new PrintWriter(new FileOutputStream(new File("files/Utlanade.txt")));
			
			Iterator<String> iter = borrowed.iterator();
			
			while(iter.hasNext())
			{
				theOutPutf.println(iter.next());
				theOutPutf.flush();
			}
				
			theOutPutf.close();
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.out.println(System.err);
		}

	}
	
	/**
	 * Lånar media
	 * @param theMedia. Media objekt
	 */
	public void borrowMedia(Media theMedia)
	{
		System.out.println("borrowMedia");
		
		borrowed.add(currentBorrower.getSsn()+";"+theMedia.objectID);
		
		writeToFile();
			
		
		theMedia.setBorrowed(true);
		theMedia.setThisMediaBorrower(currentBorrower);
	}
	
	/**
	 * Lämnar tillbaka Media
	 * @param theMedia. Media objekt
	 */
	public void returnMedia(Media theMedia)
	{
		borrowed.remove(borrowed.indexOf(0, currentBorrower.getSsn()+";"+theMedia.getObjectID()));
		writeToFile();
		theMedia.setBorrowed(false);
		theMedia.setThisMediaBorrower(null);
	}
	
	


	/**
	 * Kollar om en Låntagare existerar
	 * @param borrowerID. Låntagarens ID
	 * @return. True om denne existerar, annars false
	 */
	public boolean checkIfBorrowerExist(String borrowerID)
	{
		Iterator theIterator = allBorrowers.iterator();

		while (theIterator.hasNext())
		{
			Borrower tempBorrower = (Borrower) theIterator.next();

			if (tempBorrower.getSsn().equals(borrowerID))
			{
				currentBorrower = tempBorrower;
				return true;
			}

		}

		return false;
	}

	/**
	 * Sorteting av all Media 
	 */
	 public void sortMedia()
	{
		

		for (int i = 0; i < allMediaObjects.size() - 1; i++)
		{
			for (int j = allMediaObjects.size() - 1; j > i; j--)
			{
				if (allMediaObjects.get(j).compareTo(allMediaObjects.get(j-1))<0)
				{
					Media temp = allMediaObjects.get(j);

					allMediaObjects.set(j, allMediaObjects.get(j-1));
					allMediaObjects.set(j - 1, temp);
				}
			}
			
		}
	} 
	
	
	
	
	/**
	 * Hämta Media objekt med ObjectID genom att använda binär sökning
	 * @param ID. Media ObjectID
	 * @return. Media objektet som söks. null om det sökta objektet inte existerar
	 */
	public Media getMedia(String ID)
	{
		//mediaSearchResults = new LinkedList<Media>();
		// Binary search
        int min = 0, max = allMediaObjects.size() - 1, pos;
        
        int intID = Integer.parseInt(ID);
        
        
        while( min <= max ) 
        {
        	pos = (min + max) / 2;
            if( intID == Integer.parseInt(allMediaObjects.get(pos).objectID))
            {
                return allMediaObjects.get(pos);
            }
            else if( intID < Integer.parseInt(allMediaObjects.get(pos).objectID))
            {
                max = pos - 1;
            }
            else
                min = pos + 1;
        }
       

		return null;
	}
	
	/**
	 * Visa detaljerat informatiom om ett visst Media
	 * @param theString. Texten på det sökta Media objektet
	 * @return. Media objektet som man vill ha detaljerat information om 
	 */
	public void showSelectedMediaInfo(String theString)
	{
		
		Iterator<Media> mediaIterator = mediaSearchResults.iterator();

		
		

		while (mediaIterator.hasNext())
		{
			Media tempMedia = mediaIterator.next();
			
			

			if (tempMedia.toString().equals(theString))
			{
				//System.out.println(tempMedia.toString());
				//JOptionPane.showMessageDialog(null, tempMedia.listInfo());
				alert.alertError(tempMedia.listInfo());
				mediaSearchResults.restartList();
				break;
			}

		}

		
	}
	
	/**
	 * Sök Media genom att skriva in valfritt sträng, jämför flera olika attribut.  
	 * @param theSearchString. Texten till det sökta Media
	 */
	public void searchMediaAllByString(String theSearchString)
	{
		//mediaSearchResults = new LinkedList<Media>();
		Iterator<Media> mediaIterator = allMediaObjects.iterator();
		
		
		theSearchString=theSearchString.toLowerCase();

		while (mediaIterator.hasNext())
		{
			Media tempSearch = mediaIterator.next();

			if (tempSearch.getTitle().toLowerCase().contains(theSearchString))
			{
				mediaSearchResults.add(tempSearch);
				theGUI.setTheTextArea(tempSearch.toString());
			}
			else if (tempSearch.getObjectID().equals(theSearchString))
			{
				mediaSearchResults.add(tempSearch);
				theGUI.setTheTextArea(tempSearch.toString());
			}
			else if(tempSearch.mediaType.toLowerCase().equals(theSearchString))
			{
				mediaSearchResults.add(tempSearch);
				theGUI.setTheTextArea(tempSearch.toString());
			}
			else if(tempSearch.mediaType.equals("DVD"))
			{
				DVD tempSearchDVD = (DVD) tempSearch;
				if(tempSearchDVD.getActors().toString().toLowerCase().contains(theSearchString))
				{
					mediaSearchResults.add(tempSearch);
					theGUI.setTheTextArea(tempSearch.toString());
				}
			}
			else if(tempSearch.mediaType.equals("Bok"))
			{
				Book tempSearchDVD = (Book) tempSearch;
				if(tempSearchDVD.getAuthor().toString().toLowerCase().contains(theSearchString))
				{
					mediaSearchResults.add(tempSearch);
					theGUI.setTheTextArea(tempSearch.toString());
				}
			}
		}
		
	}
	public void sortList(Boolean asc){
	
		if(!asc) {
		 
			//method for sorting list by title desc
			System.out.println("Sort desc");
			
		}else {
			
			//method for sorting list by title
			System.out.println("Sort asc");
			//Collections.sort(() mediaSearchResults);
			

		} 
		 	// works just activate when sorting is dune
		Iterator<Media> mediaIterator = mediaSearchResults.iterator();
		while (mediaIterator.hasNext())
		{
			Media tempMedia = mediaIterator.next();
			theGUI.setTheTextArea(tempMedia.toString());		
		}
		mediaSearchResults.restartList(); 

	
	}
	/**
	 * Returnera vald Media från sökresultat
	 * @param theString. Valda texten från sökta media
	 * @return Media objekt
	 */
	public Media getMediaFromSearchResult(String theString)
	{
		Iterator<Media> mediaIterator = mediaSearchResults.iterator();

		while (mediaIterator.hasNext())
		{
			Media tempMedia = mediaIterator.next();

			if (tempMedia.toString().equals(theString))
			{
				mediaSearchResults.restartList();
				return tempMedia;
			}
		}
		mediaSearchResults.restartList();
		
		return null;
	}
	
	/**
	 * Sök Media-titel genom att skriva in en sträng
	 * @param theSearchString. Texten till sökta Media
	 */
	public void searchMediaTitleByString(String theSearchString)
	{
		mediaSearchResults = new LinkedList<Media>();
		Iterator<Media> mediaIterator = allMediaObjects.iterator();
		
		while (mediaIterator.hasNext())
		{
			Media tempSearch = mediaIterator.next();

			if (tempSearch.getTitle().toLowerCase().contains(theSearchString.toLowerCase()))
			{
				mediaSearchResults.add(tempSearch);
				theGUI.setTheTextArea(tempSearch.toString());
			}

		}
	}

	/**
	 * Inläsning av de olika textfilerna i systemet
	 */

	private void boot()
	{
		if (loadFileBorrowers() == false)
		{
			//JOptionPane.showMessageDialog(null, "files/Lantagare.txt not found");
			alert.alertError("files/Lantagare.txt not found");
		}
		if (loadFileMedia() == false)
		{
			//JOptionPane.showMessageDialog(null, "files/Media.txt not found");
			alert.alertError("files/Media.txt not found");
		}
		if(loadBorrowedMedia() == false)
		{
			alert.alertError("files/Utlanade.txt not found");
			//JOptionPane.showMessageDialog(null, "files/Utlanade.txt not found");
		}
	}
	
	/**
	 * returnera låntagare
	 * @param Ssn. personnummer
	 * @return Låntagare
	 */
	public Borrower getBorrower(String Ssn)
	{
		Iterator<Borrower> iter = allBorrowers.iterator();
		
		while(iter.hasNext())
		{
			Borrower temp = iter.next();
			
			if(temp.getSsn().equals(Ssn))
				return temp;
			
		}
		
		return null;
	}
	
	/**
	 * Läs in samtliga utlånade Media objekt av nuvarande användare
	 * @return true om filen finns, annars false
	 */
	public boolean loadBorrowedMedia()
	{
		try
		{
			Scanner theScanner = new Scanner(new File("files/Utlanade.txt"));
			StringTokenizer theTokenizer;

			while (theScanner.hasNext())
			{
				String theLine = theScanner.nextLine();
				theTokenizer = new StringTokenizer(theLine, ";");

				String borrower = theTokenizer.nextToken();
				String theMedia = theTokenizer.nextToken();
				
				Media temp = getMedia(theMedia);
				Borrower tempBorrower = getBorrower(borrower);
				
				temp.setBorrowed(true);
				
				temp.setThisMediaBorrower(tempBorrower);
				
				borrowed.add(borrower +";"+theMedia);
			}
			
			
			theScanner.close();
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Läs in samtliga Låntagare
	 * @return true om filen finns, annars false
	 */
	private boolean loadFileBorrowers()
	{
		try
		{

			Scanner theScanner = new Scanner(new File("files/Lantagare.txt"));
			StringTokenizer theTokenizer;

			while (theScanner.hasNext())
			{
				String theLine = theScanner.nextLine();
				theTokenizer = new StringTokenizer(theLine, ";");

				String ssn = theTokenizer.nextToken();
				String name = theTokenizer.nextToken();
				String phoneNbr = theTokenizer.nextToken();

				allBorrowers.add(new Borrower(name, ssn, phoneNbr));
			}

		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}
	/**
	 * Läs in Media.text filen
	 * @return. True om filen existerar, annars False.
	 */
	private boolean loadFileMedia()
	{

		try
		{
			StringTokenizer theTokenizer;
			Scanner theScanner = new Scanner(new File("files/Media.txt"));

			while (theScanner.hasNext())
			{

				String theLine = theScanner.nextLine();
				theTokenizer = new StringTokenizer(theLine, ";");
				String mediaFormat = theTokenizer.nextToken();

				if (mediaFormat.equals("Bok"))
				{
					String objectID = theTokenizer.nextToken();
					String author = theTokenizer.nextToken();
					String title = theTokenizer.nextToken();
					String year = theTokenizer.nextToken();

					allMediaObjects.add(new Book("Bok", title, objectID, Integer.parseInt(year), author));
				} else
				{
					String objectID = theTokenizer.nextToken();
					String title = theTokenizer.nextToken();
					String year = theTokenizer.nextToken();
					LinkedList<String> theActorList = new LinkedList<String>();
					while (theTokenizer.hasMoreTokens())
					{
						theActorList.add(theTokenizer.nextToken());
					}

					allMediaObjects.add(new DVD("DVD", title, objectID, Integer.parseInt(year), theActorList));

				}

			}
			theScanner.close();
			
			 sortMedia();

		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	/**
	 * Sök efter utlånade Media objekt
	 */
	public void searchBorrowed()
	{
		Iterator iter = borrowed.iterator();
		StringTokenizer theTokenizer;
		
		while(iter.hasNext())
		{
			theTokenizer = new StringTokenizer((String)iter.next(), ";");
			
			String Ssn = theTokenizer.nextToken();
			String ID = theTokenizer.nextToken();
			
			if(currentBorrower.getSsn().equals(Ssn))
			{
				Media theMedia = getMedia(ID);
				mediaSearchResults.add(theMedia);
				theGUI.setTheTextArea(theMedia.toString());
			}
		}
		
		
		
	}

	

}

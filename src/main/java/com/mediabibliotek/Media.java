package com.mediabibliotek;

/**
 * Huvudklassen som representerar de attribut som finns gemensamt hos de olika konkreta media subklasserna i 
 * Bibliotekssystemet, dvs DVD och Bok. 
 * Implementerar Comparable för att kunna sortera Media efter ObjectID 
 */
public abstract class Media implements Comparable
{
	String mediaType;
	String title;
	String objectID;
	Borrower thisMediaBorrower;
	int year;
	boolean borrowed=false;
	
	/**
	 * @param mediaType. Sätter mediatypen, Bok eller DVD
	 * @param title. Sätter titeln till median
	 * @param objectID. Sätter det unika ID kopplat till median 
	 * @param year. Sätter året för median
	 */
	public Media(String mediaType, String title, String objectID, int year)
	{
		this.mediaType = mediaType;
		this.title = title;
		this.objectID = objectID;
		this.year = year;
	}

	public String getMediaType()
	{
		return mediaType;
	}

	public void setMediaType(String mediaType)
	{
		this.mediaType = mediaType;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getObjectID()
	{
		return objectID;
	}

	public void setObjectID(String objectID)
	{
		this.objectID = objectID;
	}

	public int getYear()
	{
		return year;
	}

	public void setYear(int year)
	{
		this.year = year;
	}

	public Borrower getThisMediaBorrower()
	{
		return thisMediaBorrower;
	}

	public void setThisMediaBorrower(Borrower thisMediaBorrower)
	{
		this.thisMediaBorrower = thisMediaBorrower;
	}

	public boolean isBorrowed()
	{
		return borrowed;
	}

	public void setBorrowed(boolean borrowed)
	{
		this.borrowed = borrowed;
	}
	
	/**
	 * Abstract, behövs ej implementeras se subklasserna
	 * @return Konkatenerad sträng med all info rörande ett visst Media
	 */
	
	public String listInfo()
	{
		return "";
	}
	
	/**
	 * Här görs en jämförelse mellan två olika Media objekt genom att ange deras ObjectID attribut.
	 * Sökningen ger antingen värdet -1, 0 eller 1 beroende på vilken storlek på ObjectID det jämförande
	 * objektet får. Om det man jämför med inte är en instans av Media så returneras -2. 
	 * @return. int positionen på compareTo metoden. -1, 0, 1 eller 2.
	 */
	 @Override
	public int compareTo( Object obj ) 
	{
		
		 if(obj instanceof Media) 
		 {
		 Media media = (Media)obj;
		 
		 if(Integer.parseInt(objectID)==Integer.parseInt(media.getObjectID()))
			 return 0;
		 
		 
		 if(Integer.parseInt(objectID)<Integer.parseInt(media.getObjectID()))
		 	return -1;
		 	
		 if(Integer.parseInt(objectID)>Integer.parseInt(media.getObjectID()))
		 	return 1;
		 }	
		 	
		 return -2;
		
	} 

    
	/**
	 * Equals metoden jämför om två Media objekt är lika och returnerar true eller false.
	 * @return. True om objectID är lika eller False om inte.
	 */
	@Override
	public boolean equals( Object obj ) 
	{
		 if(obj instanceof Media) {
		 Media media = (Media)obj;
		 return Integer.parseInt(objectID)==Integer.parseInt(media.getObjectID());
		 }
		 return false;
	}
	
}

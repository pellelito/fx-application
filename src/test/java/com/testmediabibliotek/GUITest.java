/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.testmediabibliotek;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.AWTException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.mediabibliotek.GUI;
import com.mediabibliotek.LibraryController;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.robot.Robot;
import net.sourceforge.marathon.javadriver.JavaDriver;
import net.sourceforge.marathon.javadriver.JavaProfile;
import net.sourceforge.marathon.javadriver.JavaProfile.LaunchMode;
import net.sourceforge.marathon.javadriver.JavaProfile.LaunchType;

public class GUITest extends JavaFXTest {
	private WebDriver driver;
	private GUI stage = new GUI();
	private LibraryController lc = new LibraryController();
	@Before
	public void setup() {
		JavaProfile profile = new JavaProfile(LaunchMode.EMBEDDED);
		profile.setLaunchType(LaunchType.FX_APPLICATION);
		driver = new JavaDriver(profile);
	}

	@After
	public void teardown() {
		driver.quit();
	}
	
	@Test
	public void testLogin() {
		
		stage.login("821223-6666"); //throws IlligalStateException Not on FX application thread if wrong user SSN
	
	}
	@Test
	public void testSearch() throws InterruptedException {
		WebElement search = driver.findElement(By.cssSelector("text-field"));
		WebElement searchButton = driver.findElement(By.cssSelector("button[text='Search']"));
		search.sendKeys("nil");
		searchButton.click();
		assertNotNull(stage.theTextArea.getItems());
	}
	
	 @Test
		public void testItemClicked() throws InterruptedException, AWTException {
		 	WebElement search = driver.findElement(By.cssSelector("text-field"));
			WebElement searchButton = driver.findElement(By.cssSelector("button[text='Search']"));
			search.sendKeys("nil");
			searchButton.click();
			
			Platform.runLater(new Runnable(){
				@Override
				public void run() {
					Robot bot = new Robot();
					bot.mouseMove(780, 240);    
					bot.mouseClick(MouseButton.PRIMARY);
				}
				});
			assertTrue(stage.rightLabel.getText().isEmpty());
		}
	 
	@Test
	public void testSearchBorrowed() throws InterruptedException {
		WebElement searchBorrowedButton = driver.findElement(By.cssSelector("button[text='Search Borrowed']"));
		searchBorrowedButton.click();
		assertNotNull(stage.theTextArea.getItems());
	} 
	
	@Test
	public void testClearTextArea() throws InterruptedException {
		WebElement search = driver.findElement(By.cssSelector("text-field"));
		WebElement searchButton = driver.findElement(By.cssSelector("button[text='Search']"));
		search.sendKeys("nil");
		searchButton.click();
		stage.clearTheTextArea();
		assertTrue(stage.theTextArea.getItems().isEmpty());
	}
	
	@Test
	public void testCheckUserInput( ) {	
		assertTrue(lc.checkUserInput("kalle"));		
		}
	
	@Test
	public void testCheckInputOnlyDigits( ) {
		assertTrue(lc.checkInputOnlyDigits("88888"));	
		}
	
	@Test
	public void testCheckIfBorrowerExist() {
		// 730421-7777
		assertTrue(lc.checkIfBorrowerExist("730421-7777"));	
	}
	
	@Test 
	public void testGetMedia() {
		// 399898
		assertNotNull(lc.getMedia("399898"));
	}
	
	@Override
	protected Scene getScene() {
		
		return stage.createScene();
	}
}

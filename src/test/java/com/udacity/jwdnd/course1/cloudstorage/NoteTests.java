package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoteTests {
    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    private void doMockSignUp(String firstName, String lastName, String userName, String password) {
        // Create a dummy account for logging in later.

        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        driver.get("http://localhost:" + this.port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(firstName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(lastName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys(password);

        // Attempt to sign up.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();

		/* Check that the sign up was successful.
		// You may have to modify the element "success-msg" and the sign-up
		// success message below depening on the rest of your code.
		*/
        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
    }


    private void doLogIn(String userName, String password) {
        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));

    }

    // test that creates a note, and verifies it is displayed
    @Test
    public void testCreateNote() {
        String testNoteTitle = "note title";
        String testNoteDesc = "note description to test create note";

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        signUpAndLoginWithNewTestAccount("testCreateNote");

        createNewNote(testNoteTitle, testNoteDesc, webDriverWait);

        Assertions.assertTrue(driver.getPageSource().contains(testNoteTitle));
        Assertions.assertTrue(driver.getPageSource().contains(testNoteDesc));
    }

    // test that edits an existing note and verifies that the changes are displayed
    @Test
    public void testEditNote() {
        String testNoteTitle = "noteTitle";
        String testNoteDesc = "note description to test edit note";
        String editedTestNoteTitle = "editedTitle";
        String editedTestNoteDesc = "note description is edited";

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        signUpAndLoginWithNewTestAccount("testEditNote");

        createNewNote(testNoteTitle, testNoteDesc, webDriverWait);
        Assertions.assertTrue(driver.getPageSource().contains(testNoteTitle));
        Assertions.assertTrue(driver.getPageSource().contains(testNoteDesc));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonEditNote")));
        WebElement buttonEditNote = driver.findElement(By.id("buttonEditNote"));
        buttonEditNote.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement inputNoteTitle = driver.findElement(By.id("note-title"));
        inputNoteTitle.click();
        inputNoteTitle.clear();
        inputNoteTitle.sendKeys(editedTestNoteTitle);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        WebElement inputNoteDesc = driver.findElement(By.id("note-description"));
        inputNoteDesc.click();
        inputNoteDesc.clear();
        inputNoteDesc.sendKeys(editedTestNoteDesc);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonNoteSubmit")));
        WebElement buttonSubmit = driver.findElement(By.id("buttonNoteSubmit"));
        buttonSubmit.click();

        Assertions.assertTrue(driver.getPageSource().contains("Your changes were successfully saved"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("linkBackToHome")));
        WebElement linkBackToHome = driver.findElement(By.id("linkBackToHome"));
        linkBackToHome.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement navNoteTab = driver.findElement(By.id("nav-notes-tab"));
        navNoteTab.click();

        Assertions.assertTrue(driver.getPageSource().contains(editedTestNoteTitle));
        Assertions.assertTrue(driver.getPageSource().contains(editedTestNoteDesc));
    }

    // test that deletes a note and verifies that the note is no longer displayed
    @Test
    public void testDeleteNote() {
        String testNoteTitle = "note title";
        String testNoteDesc = "note description to test delete note";

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        signUpAndLoginWithNewTestAccount("testDeleteNote");

        createNewNote(testNoteTitle, testNoteDesc, webDriverWait);

        Assertions.assertTrue(driver.getPageSource().contains(testNoteTitle));
        Assertions.assertTrue(driver.getPageSource().contains(testNoteDesc));


        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonDeleteNote")));
        WebElement buttonDeleteNote = driver.findElement(By.id("buttonDeleteNote"));
        buttonDeleteNote.click();

        Assertions.assertTrue(driver.getPageSource().contains("Your changes were successfully saved"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("linkBackToHome")));
        WebElement linkBackToHome = driver.findElement(By.id("linkBackToHome"));
        linkBackToHome.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement navNoteTab = driver.findElement(By.id("nav-notes-tab"));
        navNoteTab.click();

        Assertions.assertFalse(driver.getPageSource().contains(testNoteTitle));
        Assertions.assertFalse(driver.getPageSource().contains(testNoteDesc));
    }


    public void signUpAndLoginWithNewTestAccount(String username) {
        doMockSignUp("testNotes", "Test", username, "123");
        doLogIn(username, "123");

        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));

        driver.findElement(By.id("nav-notes-tab")).click();
    }

    public void createNewNote(String noteTitle, String noteDescription, WebDriverWait webDriverWait) {
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonNewNote")));
        WebElement buttonNewNote = driver.findElement(By.id("buttonNewNote"));
        buttonNewNote.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement inputNoteTitle = driver.findElement(By.id("note-title"));
        inputNoteTitle.click();
        inputNoteTitle.sendKeys(noteTitle);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        WebElement inputNoteDesc = driver.findElement(By.id("note-description"));
        inputNoteDesc.click();
        inputNoteDesc.sendKeys(noteDescription);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonNoteSubmit")));
        WebElement buttonSubmit = driver.findElement(By.id("buttonNoteSubmit"));
        buttonSubmit.click();

        Assertions.assertTrue(driver.getPageSource().contains("Your changes were successfully saved"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("linkBackToHome")));
        WebElement linkBackToHome = driver.findElement(By.id("linkBackToHome"));
        linkBackToHome.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement navNoteTab = driver.findElement(By.id("nav-notes-tab"));
        navNoteTab.click();
    }
}

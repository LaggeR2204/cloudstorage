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
public class CredentialTests {
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

    // test that creates a set of credentials, verifies that they are displayed, and verifies that the displayed password is encrypted.
    @Test
    public void testCreateCredential() {
        String url = "http://google.com";
        String username = "testuser";
        String password = "123";

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        signUpAndLoginWithNewTestAccount("testCreateCredential");

        createNewCredential(url, username, password, webDriverWait);

        Assertions.assertEquals(driver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/th")).getAttribute("textContent"), url);
        Assertions.assertNotEquals(driver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/td[3]")).getAttribute("textContent"), password);
    }

    // test that views an existing set of credentials, verifies that the viewable password is unencrypted, edits the credentials, and verifies that the changes are displayed.
    @Test
    public void testEditCredential() {
        String url = "http://google.com";
        String username = "testuser";
        String password = "123";

        String editedUrl = "http://newgoogle.com";
        String editedUsername = "testusernew";
        String editedPassword = "456";

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        signUpAndLoginWithNewTestAccount("testEditCredential");

        createNewCredential(url, username, password, webDriverWait);

        Assertions.assertEquals(driver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/th")).getAttribute("textContent"), url);
        Assertions.assertNotEquals(driver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/td[3]")).getAttribute("textContent"), password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonEditCredential")));
        WebElement buttonEditCredential = driver.findElement(By.id("buttonEditCredential"));
        buttonEditCredential.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement inputUrl = driver.findElement(By.id("credential-url"));
        inputUrl.click();
        inputUrl.clear();
        inputUrl.sendKeys(editedUrl);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        WebElement inputUsername = driver.findElement(By.id("credential-username"));
        inputUsername.click();
        inputUsername.clear();
        inputUsername.sendKeys(editedUsername);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        WebElement inputPassword = driver.findElement(By.id("credential-password"));
        inputPassword.click();
        inputPassword.clear();
        inputPassword.sendKeys(editedPassword);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonCredentialSubmit")));
        WebElement buttonCredentialSubmit = driver.findElement(By.id("buttonCredentialSubmit"));
        buttonCredentialSubmit.click();

        Assertions.assertTrue(driver.getPageSource().contains("Your changes were successfully saved"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("linkBackToHome")));
        WebElement linkBackToHome = driver.findElement(By.id("linkBackToHome"));
        linkBackToHome.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement navCredentialTab = driver.findElement(By.id("nav-credentials-tab"));
        navCredentialTab.click();

        Assertions.assertEquals(driver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/th")).getAttribute("textContent"), editedUrl);
        Assertions.assertNotEquals(driver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/td[3]")).getAttribute("textContent"), editedPassword);
    }

    // test that deletes an existing set of credentials and verifies that the credentials are no longer displayed.
    @Test
    public void testDeleteCredential() {
        String url = "http://google.com";
        String username = "testuser";
        String password = "123";

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        signUpAndLoginWithNewTestAccount("testDeleteCredential");

        createNewCredential(url, username, password, webDriverWait);

        Assertions.assertEquals(driver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/th")).getAttribute("textContent"), url);
        Assertions.assertNotEquals(driver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/td[3]")).getAttribute("textContent"), password);


        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonDeleteCredential")));
        WebElement buttonDeleteCredential = driver.findElement(By.id("buttonDeleteCredential"));
        buttonDeleteCredential.click();

        Assertions.assertTrue(driver.getPageSource().contains("Your changes were successfully saved"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("linkBackToHome")));
        WebElement linkBackToHome = driver.findElement(By.id("linkBackToHome"));
        linkBackToHome.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement navNoteTab = driver.findElement(By.id("nav-credentials-tab"));
        navNoteTab.click();

        Assertions.assertFalse(driver.getPageSource().contains(url));
    }


    public void signUpAndLoginWithNewTestAccount(String username) {
        doMockSignUp("testNotes", "Test", username, "123");
        doLogIn(username, "123");

        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));

        driver.findElement(By.id("nav-credentials-tab")).click();
    }

    public void createNewCredential(String url, String username, String password, WebDriverWait webDriverWait) {
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonNewCredential")));
        WebElement buttonNewCredential = driver.findElement(By.id("buttonNewCredential"));
        buttonNewCredential.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement inputUrl = driver.findElement(By.id("credential-url"));
        inputUrl.click();
        inputUrl.sendKeys(url);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        WebElement inputUsername = driver.findElement(By.id("credential-username"));
        inputUsername.click();
        inputUsername.sendKeys(username);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        WebElement inputPassword = driver.findElement(By.id("credential-password"));
        inputPassword.click();
        inputPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonCredentialSubmit")));
        WebElement buttonCredentialSubmit = driver.findElement(By.id("buttonCredentialSubmit"));
        buttonCredentialSubmit.click();

        Assertions.assertTrue(driver.getPageSource().contains("Your changes were successfully saved"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("linkBackToHome")));
        WebElement linkBackToHome = driver.findElement(By.id("linkBackToHome"));
        linkBackToHome.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement navCredentialTab = driver.findElement(By.id("nav-credentials-tab"));
        navCredentialTab.click();
    }
}

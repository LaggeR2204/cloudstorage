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
public class AuthenticationTests {
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


    // test that verifies that an unauthorized user can only access the login and signup pages.
    @Test
    public void testUnauthorizedUserCanAccessLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    @Test
    public void testUnauthorizedUserCanAccessSignupPage() {
        driver.get("http://localhost:" + this.port + "/signup");
        Assertions.assertEquals("http://localhost:" + this.port + "/signup", driver.getCurrentUrl());
    }

    @Test
    public void testUnauthorizedUserCannotAccessHomePage() {
        driver.get("http://localhost:" + this.port + "/signup");
        Assertions.assertNotEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
    }

    // test that signs up a new user, logs in, verifies that the home page is accessible, logs out, and verifies that the home page is no longer accessible.
    @Test
    public void testAuthorizedUserCanAccessHomePage() {
        // Create a test account
        doMockSignUp("testAuthorizedUserCanAccessHomePage", "Test", "testAuthorizedUserCanAccessHomePage", "123");
        doLogIn("testAuthorizedUserCanAccessHomePage", "123");

        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
    }

    @Test
    public void testUserCannotAccessHomePageAfterLogout() {
        // Create a test account
        doMockSignUp("testUserCannotAccessHomePageAfterLogout", "Test", "testUserCannotAccessHomePageAfterLogout", "123");
        doLogIn("testUserCannotAccessHomePageAfterLogout", "123");

        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonLogout")));
        WebElement buttonLogout = driver.findElement(By.id("buttonLogout"));
        buttonLogout.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));

        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertNotEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
    }
}

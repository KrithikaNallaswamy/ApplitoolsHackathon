import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;

import static com.google.common.base.Strings.isNullOrEmpty;

import org.junit.jupiter.api.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


public class VisualAITests {
    private static EyesRunner runner;
    private Eyes eyes;
    private static BatchInfo batch;
    private WebDriver driver;
    private String testName;

    private String URL = "https://demo.applitools.com/hackathon.html" ;
    private String showAdURL ="https://demo.applitools.com/hackathon.html?showAd=true";

    //private String URL = "https://demo.applitools.com/hackathonV2.html" ;
    //private String showAdURL ="https://demo.applitools.com/hackathonV2.html?showAd=true";

    @BeforeAll
    public static void setBatch() {
        // Must be before ALL tests (at Class-level)
        batch = new BatchInfo("Hackathon V1");
        //batch = new BatchInfo("Hackathon V2");
    }

    @BeforeEach
    public void beforeEach(TestInfo testInfo) {
        // Initialize the Runner for your test.
        runner = new ClassicRunner();

        // Initialize the eyes SDK
        eyes = new Eyes(runner);

        // Raise an error if no API Key has been found.
        if(isNullOrEmpty(System.getenv("APPLITOOLS_API_KEY"))) {
            throw new RuntimeException("No API Key found; Please set environment variable 'APPLITOOLS_API_KEY'.");
        }

        // Set your personal Applitols API Key from your environment variables.
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

        // set batch name
        eyes.setBatch(batch);

       // Use Chrome browser
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\knallaswamy\\Downloads\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();

        // Set AUT's name, test name and viewport size (width X height)
        //get test name
        testName = testInfo.getDisplayName();

       // Navigate the browser to the "ACME" demo app.
        driver.get(URL);
    }

    @Test
    @DisplayName("Login Page UI")
    public void loginPageUiElementTest() {
        System.out.println("Using Applitools: Login Page UI Elements Test");
        eyes.open(driver, "Demo App", testName, new RectangleSize(800, 800));
        // Visual checkpoint #1 - Check the login page.
        eyes.checkWindow("Login Window");
        // End the test.
        eyes.closeAsync();
    }

    @Test
    @DisplayName("Login Error")
    public void dataDrivenTest() {
        System.out.println("Using Applitools: Data-Driven Test");
        eyes.open(driver, "Demo App", "Login Alert 1", new RectangleSize(800, 800));
        driver.findElement(By.id("log-in")).click();
        eyes.checkWindow("Login Alert 1");
        eyes.closeAsync();

        eyes.open(driver, "Demo App", "Login Alert 2", new RectangleSize(800, 800));
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.id("log-in")).click();
        eyes.checkWindow("Login Alert 2");
        eyes.closeAsync();


        eyes.open(driver, "Demo App", "Login Alert 3", new RectangleSize(800, 800));
        driver.findElement(By.id("username")).sendKeys("testuser");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("log-in")).click();
        eyes.checkWindow("Login Alert 3");
        eyes.closeAsync();


        eyes.open(driver, "Demo App", "App Window", new RectangleSize(800, 800));
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("testuser");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.id("log-in")).click();
        Helper.waitForLoad(driver);
        eyes.checkWindow("App Window");
        eyes.closeAsync();

    }

    @Test
    @DisplayName("Transaction Table")
    public void tableSortTest() {
        System.out.println("Using Applitools: Table Sort Test");
        eyes.open(driver, "Demo App", testName, new RectangleSize(800, 800));
        eyes.setForceFullPageScreenshot(true);

        driver.findElement(By.id("username")).sendKeys("testuser");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.id("log-in")).click();
        Helper.waitForLoad(driver);
        //WebElement table=driver.findElement(By.id("transactionsTable"));
        eyes.checkWindow("TransactionsTable");
        eyes.closeAsync();
    }

    @Test
    @DisplayName("Canvas Chart")
    public void canvasChartTest() {
        System.out.println("Canvas Chart Test");
        eyes.open(driver, "Demo App", testName, new RectangleSize(800, 800));

        driver.findElement(By.id("username")).sendKeys("testuser");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.id("log-in")).click();
        Helper.waitForLoad(driver);

        driver.findElement(By.linkText("Compare Expenses")).click();
        Helper.waitForLoad(driver);
        eyes.checkWindow("Canvas Chart 2018");
        driver.findElement(By.cssSelector("button[id='addDataset']")).click();
        eyes.checkWindow("Canvas Chart 2019");
        eyes.closeAsync();
    }

    @Test
    @DisplayName("Dynamic Ad")
    public void dynamicContentTest() {
        System.out.println("Dynamic Content Test");
        eyes.open(driver, "Demo App", testName, new RectangleSize(800, 800));
        driver.get(showAdURL);
        Helper.waitForLoad(driver);
        driver.findElement(By.id("username")).sendKeys("testuser");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.id("log-in")).click();
        Helper.waitForLoad(driver);
        eyes.checkWindow("Dynamic Ad");
        eyes.closeAsync();
    }

    @AfterEach
    public void afterEach() {
        driver.quit();
        eyes.abortIfNotClosed();
    }

    @AfterAll
    public static void afterAll() {
        // Wait and collect all test results
        TestResultsSummary allTestResults = runner.getAllTestResults();
        // Print results
        System.out.println(allTestResults);
    }
}


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TraditionalTests {
    private WebDriver driver;
   private String URL = "https://demo.applitools.com/hackathon.html" ;
   private String showAdURL ="https://demo.applitools.com/hackathon.html?showAd=true";

    //private String URL = "https://demo.applitools.com/hackathonV2.html" ;
   // private String showAdURL ="https://demo.applitools.com/hackathonV2.html?showAd=true";

    @BeforeEach
    public void setup(){
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\knallaswamy\\Downloads\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get(URL);
        Helper.waitForLoad(driver);
    }

    @Test
    public  void loginPageUiElementTest(){
        System.out.println("Login Page UI Elements Test");
        String usernamePlaceholder= driver.findElement(By.id("username")).getAttribute("placeholder");
        String pwdPlaceholder= driver.findElement(By.id("password")).getAttribute("placeholder");

        Assertions.assertAll("Login Page UI Elements",
                () -> Assertions.assertTrue(Helper.isElementPresent(driver, By.cssSelector("div.logo-w a img[src='img/logo-big.png']")),"Home page logo doesn't exist"),
                () -> Assertions.assertTrue(Helper.isElementPresent(driver, By.xpath("//h4[contains(text(),'Login Form')]")),"Correct Page header doesn't exist"),
                () -> Assertions.assertTrue(Helper.isElementPresent(driver, By.xpath("//label[text()='Username']")),"'Username' label doesn't exist"),
                () -> Assertions.assertTrue(Helper.isElementPresent(driver, By.id("username")),"Username field doesn't exist"),
                () -> Assertions.assertTrue(Helper.isElementPresent(driver, By.cssSelector("#username + .os-icon-user-male-circle")),"Username icon doesn't exist"),
                () -> Assertions.assertTrue(Helper.isElementPresent(driver, By.xpath("//label[text()='Password']")),"'Password' label doesn't exist"),
                () -> Assertions.assertTrue(Helper.isElementPresent(driver, By.id("password")),"Password field doesn't exist"),
                () -> Assertions.assertTrue(Helper.isElementPresent(driver, By.cssSelector("#password + .os-icon-fingerprint")),"Password icon doesn't exist"),
                () -> Assertions.assertTrue(Helper.isElementPresent(driver, By.id("log-in")),"Sign In button doesn't exist"),
                () -> Assertions.assertTrue(Helper.isElementPresent(driver, By.xpath("//label[text()='Remember Me']")),"'Remember Me' text doesn't exist"),
                () -> Assertions.assertTrue(Helper.isElementPresent(driver, By.xpath("//label[text()='Remember Me']/descendant::input[@class='form-check-input']")),"'Remember Me' checkbox doesn't exist"),
                () -> Assertions.assertTrue(Helper.isElementPresent(driver, By.cssSelector("img[src='img/social-icons/twitter.png']")),"Twitter logo doesn't exist"),
                () -> Assertions.assertTrue(Helper.isElementPresent(driver, By.cssSelector("img[src='img/social-icons/facebook.png']")),"Facebook logo doesn't exist"),
                () -> Assertions.assertTrue(Helper.isElementPresent(driver, By.cssSelector("img[src='img/social-icons/linkedin.png']")),"linkedIn logo doesn't exist"),
                () -> Assertions.assertTrue(usernamePlaceholder.equals("Enter your username"),"Username Placeholder is incorrect"),
                () -> Assertions.assertTrue(pwdPlaceholder.equals("Enter your password"),"Password Placeholder is incorrect")


        );
    }

    @Test
    public void dataDrivenTest(){
        System.out.println("Data-Driven Test");

        driver.findElement(By.id("log-in")).click();
        WebElement alert = driver.findElement(By.xpath("//div[@class='alert alert-warning']"));
        Assertions.assertTrue(Helper.isElementPresent(driver, By.xpath("//div[@class='alert alert-warning']")),"Username and Password error message is not displayed");
        String alertText1= alert.getText();

        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.id("log-in")).click();
        Assertions.assertTrue(Helper.isElementPresent(driver, By.xpath("//div[@class='alert alert-warning']")),"Username error message is not displayed");
        String alertText2= alert.getText();

        driver.findElement(By.id("username")).sendKeys("testuser");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("log-in")).click();
        Assertions.assertTrue(Helper.isElementPresent(driver, By.xpath("//div[@class='alert alert-warning']")),"Password error message is not displayed");
        String alertText3= alert.getText();
        System.out.println(alertText3);

        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("testuser");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.id("log-in")).click();
        Helper.waitForLoad(driver);

        Assertions.assertAll("Data Driven Test assertions for Login functionality",
                () -> Assertions.assertTrue(alertText1.contains("Both Username and Password must be present"),"Alert message is not as expected when username and password fields are blank"),
                () -> Assertions.assertTrue(alertText2.contains("Username must be present"),"Alert message is not as expected when username is blank"),
                () -> Assertions.assertTrue(alertText3.contains("Password must be present"),"Alert message is not as expected when Password is blank"),
                () -> Assertions.assertTrue(Helper.isElementPresent(driver, By.cssSelector("div.avatar-w img[src='img/avatar1.jpg']")),"User avatar doesn't exist and Login is not successful when both username and password is specified.")
        );

    }

    @Test
    public void tableSortTest() {
        System.out.println("Table Sort Test");

        driver.findElement(By.id("username")).sendKeys("testuser");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.id("log-in")).click();
        Helper.waitForLoad(driver);

        WebElement table=driver.findElement(By.id("transactionsTable"));
        table.findElement(By.xpath("//table[@id='transactionsTable']/thead/tr/th[@id='amount']")).click();

        ArrayList<Double> obtainedList = new ArrayList<>();
        List<WebElement> amountList=table.findElements(By.xpath("//table[@id='transactionsTable']/tbody/tr/td[5]"));
        for(WebElement we:amountList){
            String amount = we.getText();
            amount = amount.replace(" ","")
                    .replace(",","")
                    .replace("USD","");
            double valid_amount = Double.parseDouble(amount);
            obtainedList.add(valid_amount);
        }
        ArrayList<Double> sortedList = new ArrayList<>();
        for(Double d:obtainedList){
            sortedList.add(d);
        }
        Collections.sort(sortedList);
        Assertions.assertTrue(sortedList.equals(obtainedList),"In the Recent Transactions table, the Amount column is not sorted in ascending order after clicking it.");
    }

    @Test
    public void canvasChartTest() {
        System.out.println("Canvas Chart Test");
        //Through selenium we cannot automate graphs.
        //Details on canvas is rendered as an image. So you wont be able to read data on canvas.
        //We will have to take screenshots and do image comparison for the same.
        //Selenium can provide screenshots (images) but you need to use other tools or image validation libraries to work with such images.
    }

    @Test
    public void dynamicContentTest() {
        System.out.println("Dynamic Content Test");
        driver.get(showAdURL);
        Helper.waitForLoad(driver);

        driver.findElement(By.id("username")).sendKeys("testuser");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.id("log-in")).click();
        Helper.waitForLoad(driver);

        Assertions.assertAll("Data Driven Test assertions for Login functionality",
                () -> Assertions.assertTrue(Helper.isElementPresent(driver, By.xpath("//div[@id='flashSale']/img")),"Flashsale1 Image doesn't exist"),
                () -> Assertions.assertTrue(Helper.isElementPresent(driver, By.xpath("//div[@id='flashSale2']/img")),"Flashsale2 Image doesn't exist")
        );


    }


        @AfterEach
    public void teardown(){
        driver.close();
    }


}

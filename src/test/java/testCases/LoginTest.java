package testCases;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;

public class LoginTest {
	WebDriver driver;
	String BrowserPath;
	
	@BeforeTest
	@Parameters("Browser")
	public void setup(String Browser){
		if(Browser.equalsIgnoreCase("firefox")){
			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("permissions.default.desktop-notification",1);
			DesiredCapabilities capabilities=DesiredCapabilities.firefox();
			capabilities.setCapability(FirefoxDriver.PROFILE, profile);
			
			BrowserPath = System.getProperty("user.dir")+"\\src\\main\\java\\resources\\geckodriver.exe";
			System.setProperty("webdriver.gecko.driver",BrowserPath);
			driver = new FirefoxDriver(capabilities);
		}
		else if(Browser.equalsIgnoreCase("chrome")){
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-notifications");
			options.addArguments("--start-maximized");
			BrowserPath = System.getProperty("user.dir")+"\\src\\main\\java\\resources\\chromedriver.exe";
			System.setProperty("webdriver.chrome.driver",BrowserPath);
		  	driver = new ChromeDriver(options);
		}
		
		else if(Browser.equalsIgnoreCase("ie")){
			
			BrowserPath = System.getProperty("user.dir")+"\\src\\main\\java\\resources\\IEDriverServer.exe";
			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			System.setProperty("webdriver.ie.driver",BrowserPath);
			driver = new InternetExplorerDriver(capabilities);
			
		}
	}
	
  @Test(dataProvider="LoginDetails")
  public void LoginMethod(String userName,String passWord) throws InterruptedException {
	  	
	    //driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
	  	driver.get("http://www.facebook.com");
	    WebElement email = driver.findElement(By.id("email"));
	    email.clear();
	    email.sendKeys(userName);

	    WebElement password = driver.findElement(By.id("pass"));
	    password.clear();
	    password.sendKeys(passWord);

	    WebElement loginbutton = driver.findElement(By.id("loginbutton"));
	    loginbutton.click();
	    
	    Thread.sleep(10000);
	    String title = driver.getTitle();
	    System.out.println("Title of Page:"+title);
	    
	    if(!title.equals("Log in to Facebook | Facebook")){
	    	WebElement navigableLable = driver.findElement(By.xpath("//*[@id='userNavigationLabel']"));
	    	JavascriptExecutor js = (JavascriptExecutor)driver;
	    	js.executeScript("arguments[0].click();", navigableLable);
	    	Thread.sleep(5000);
	  
	    	driver.findElement(By.partialLinkText("Log Out")).click();
	    	System.out.println("Logged Out of the Facebook...!!!");
	    	Thread.sleep(5000);
	    }
	    else{
	    	System.out.println("UserName or PassWord entered is incorrect..!!!");
	    	System.out.println("Login FAILED..!!!");
	    }
	    
	    driver.navigate().to("http://www.facebook.com");
  }

@AfterTest
public void closeBrowser(){
	driver.close();
}
@DataProvider(name="LoginDetails")
public Object[][] getData(){
	return new Object[][]{{"user1","pass1"},{"user2","pass2"}, {"user3","pass3"}};
}
}

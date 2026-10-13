package test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TestBase {
    //headless--things happen behibd the scene
    Page page;
    Browser browser;
    Playwright playwright;
    String base_url;
    @BeforeMethod(alwaysRun = true)
    public void setup() throws IOException {
        Properties prop=new Properties();
        FileInputStream fis=new FileInputStream("src/test/resourses/config.properties");
        prop.load(fis);

        //mvn test -Pframework -Dbrowser=chrome -Denv=dev
        //of we have give browser name at runtime test run at taht broeser otherwise it run on proporty level
       String browserName= System.getProperty("browser") != null ?System.getProperty("browser") : prop.getProperty("browser");
       // String browserName=prop.getProperty("browser");
        String envName= System.getProperty("env") != null ?System.getProperty("env") : prop.getProperty("env");

        playwright= Playwright.create();


        if ("firefox".equals(browserName))
         {

            browser=playwright.firefox().launch();
        }
        else if("safari".equals(browserName))
        {
            browser=playwright.webkit().launch();
        }
        else {
            // headmode
//Browser browser =playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false)) ;
//to run on physical browser
            //  Browser browser =playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false)) ;
            browser =playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)) ;
        }

        //headless mode
        //Browser browser =playwright.chromium().launch();


        page=browser.newPage();
        page.setDefaultTimeout(5000);//applied to all the actions
         base_url = prop.getProperty(envName+".baseurl");
        PlaywrightAssertions.setDefaultAssertionTimeout(7000);//global assertion timeout
    }
}

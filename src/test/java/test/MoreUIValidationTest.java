package test;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

public class MoreUIValidationTest {

    Playwright playwright;
    Browser browser;
    Page page;
    BrowserContext context;
    @BeforeMethod(alwaysRun = true)
    public void setup()
    {
         playwright= Playwright.create();
        browser =playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)) ;

         //open incognito window
          context= browser.newContext();
        // Start tracing before creating / navigating a page.
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
         page= context.newPage();
         page.navigate("https://rahulshettyacademy.com/loginpagePractise/");//navifate method itself waitForLoadState That why we dont drfine wait

    }

    @Test
    public void ChildWindowHandle()
    {
       Locator blingKingText= page.locator(".blinkingText");
       //to get first result--we apply filter also but thete is another way to do this
        Page newPage= context.waitForPage(()->blingKingText.first().click());//this page open dynamically while click on link
        newPage.waitForLoadState();//wait until page fully loaded
        String childText= newPage.locator(".red").textContent();//textcontent method grab complete sentece
        String emailId= childText.split("at ")[1].split(" ")[0];
        System.out.println(emailId);
        page.getByLabel("Username").fill(emailId);
        page.getByLabel("Username").inputValue();//grab the value from textbox
        page.waitForTimeout(3000);



    }
    @Test(groups = {"smoke"})
    public void UiControls()
    {
        Locator userRdoBtn=page.getByRole(AriaRole.RADIO,new Page.GetByRoleOptions().setName("User"));
        userRdoBtn.click();
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Okay")).click();
       Assert.assertTrue(userRdoBtn.isChecked());
      Locator checkBoxTerms= page.getByRole(AriaRole.CHECKBOX,new Page.GetByRoleOptions().setName(" I Agree to the terms and conditions"));
       checkBoxTerms.check();
       Assert.assertTrue(checkBoxTerms.isChecked());
       page.getByRole(AriaRole.COMBOBOX).selectOption("Teacher");//other way to handle dropdown
        page.waitForTimeout(3000);
    }
    @AfterMethod
    public void tearDown()
    {
        // Stop tracing and export it into a zip archive.
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
    }
}

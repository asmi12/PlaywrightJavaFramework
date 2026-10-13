package test;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class UiValidationContinueTest {
    Playwright playwright;
    Browser browser;
    Page page;
    BrowserContext context;
    @BeforeMethod(alwaysRun = true)
    public void setup()
    {
        playwright= Playwright.create();
        browser =playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)) ;
       page= browser.newPage();

        page.navigate("https://rahulshettyacademy.com/AutomationPractice/");//navifate method itself waitForLoadState That why we dont drfine wait

    }

    @Test(groups = {"smoke"})
    public void popUpValidation()
    {
      assertThat(page.getByPlaceholder("Hide/Show Example")).isVisible();
      page.locator("#hide-textbox").click();
        assertThat(page.getByPlaceholder("Hide/Show Example")).isHidden();
        page.onDialog(dialog -> dialog.accept());//handele windows popup
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Alert")).click();
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Mouse Hover")).hover();
        //page.waitForTimeout(3000);
       // page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions().setName("Top")).click();
       FrameLocator framePage= page.frameLocator("#courses-iframe");//handle frame
        framePage.getByRole(AriaRole.LINK,new FrameLocator.GetByRoleOptions().setName("Learning Paths")).click();
        String textCheck=framePage.locator(".inner-box h1").textContent();
        System.out.println(textCheck);


    }
    @Test
    public void ScreenshotTest()
    {
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("pagescreenshot.png")));//page level
       Locator displayEditBox= page.getByPlaceholder("Hide/Show Example");
       displayEditBox.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get("editBoxscreenshot.png")));//loactor level
        page.locator("#hide-textbox").click();
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("pagePostscreenshot.png")));//page level


    }

}

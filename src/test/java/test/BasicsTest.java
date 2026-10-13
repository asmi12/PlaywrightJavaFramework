package test;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasicsTest {
//Invoke browsers-->tab-->typr url
    //headmode--browser open separtely
    //headless--things happen behibd the scene
    Page page;
    Browser browser;
    Playwright  playwright;
    @BeforeMethod
    public void setup()
    {
          playwright= Playwright.create();
        //headless mode
        //Browser browser =playwright.chromium().launch();

        // headmode
//Browser browser =playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false)) ;
//to run on physical browser
        //  Browser browser =playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false)) ;
         browser =playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)) ;
         page=browser.newPage();
        page.setDefaultTimeout(5000);//applied to all the actions

        page.navigate("https://eventhub.rahulshettyacademy.com/login");
        PlaywrightAssertions.setDefaultAssertionTimeout(7000);//global assertion timeout
    }

    @Test(description = "create event-book that ecent and verify if its booked")
    public void DemoTest() {

        System.out.println(page.title());
        assertThat(page).hasTitle("EventHub — Discover & Book Events");
        //page.getByLabel("Email").fill("rahulshetty1@yahoo.com");
        page.getByPlaceholder("you@email.com").fill("rahulshetty1@yahoo.com");
        page.getByLabel("Password").fill("Magiclife1!");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        assertThat
                (page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        //create event from admin page
        //for actions (fill,selectoptions,click etc) default timeout is 10sec
        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");

        //applied timepou for paticular locators
        page.locator("#event-title-input").fill("Rahul Shetty QA Summit", new Locator.FillOptions().setTimeout(1000));
        page.locator("#admin-event-form textarea").fill("QA Event");
        page.getByLabel("Category").selectOption("Concert");
        page.getByLabel("City").fill("Pune");
        page.getByLabel("Venue").fill("Test Venue");
        page.getByLabel("Event Date & Time").fill("2026-10-21T06:33");
        page.getByLabel("Price ($)").fill("100");
        page.getByLabel("Total Seats").fill("100");
        page.locator("#add-event-btn").click(new Locator.ClickOptions().setTimeout(10000));

        //assertion defsult time 5 sec wait
        assertThat(page.getByText("Event created")).isVisible();

        //Finf newly created event in event page
        page.locator("#nav-events").click();
        Locator eventCards = page.getByTestId("event-card");
        System.out.println(eventCards.count());
        Locator targetCard = eventCards.filter(new Locator.FilterOptions().setHasText("Rahul Shetty QA Summit"));
        //customise timeoutfor 1sec
        assertThat(targetCard).isVisible();
        String seatsTextBeforBook = targetCard.getByText("seats").innerText();
        System.out.println(seatsTextBeforBook);
        int seatNumberBeforeBooking= Integer.parseInt(seatsTextBeforBook.split(" ")[0]);

        targetCard.getByTestId("book-now-btn").click();
        page.getByLabel("Full Name").fill("Test student");
        page.locator("#customer-email").fill("test@gmail.com");
        page.getByPlaceholder("+91 98765 43210").fill("9876543210");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm Booking")).click();
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
        String bookingref = page.locator(".booking-ref").innerText();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View My Bookings")).click();

        //verify booking history
        Locator bookingCards = page.locator("#booking-card");
        Locator targetBookingCard = bookingCards.filter(new Locator.FilterOptions().setHasText(bookingref));
        assertThat(targetBookingCard).isVisible();

        //seat count reduction check
        page.locator("#nav-events").click();
        page.waitForTimeout(1000);
        Locator eventCardsAfterBooking = page.getByTestId("event-card");
//        System.out.println(eventCards.count());
        Locator targetCardAfterBooking = eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText("Rahul Shetty QA Summit"));


        String seatsTextAfterBook = targetCard.getByText("seats").innerText();
        System.out.println(seatsTextAfterBook);
        //afterbook seats less thar beforbook seat
      int seatNumberAfterBooking= Integer.parseInt(seatsTextAfterBook.split(" ")[0]);
        Assert.assertTrue(seatNumberBeforeBooking>seatNumberAfterBooking);
    }


    @AfterMethod
    public void tearDown()
    {

    }
}

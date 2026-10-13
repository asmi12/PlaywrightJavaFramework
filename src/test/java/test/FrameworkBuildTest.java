package test;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FrameworkBuildTest extends TestBase {
//Invoke browsers-->tab-->typr url
    //headmode--browser open separtely


    @Test(groups = {"framework"} ,description = "create event-book that ecent and verify if its booked")
    public void DemoTest() {

        String eventtitle="Rahul Shetty QA Summit";
        LoginPage loginPage=new LoginPage(page,base_url);
        DashboardPage dashboardPage=loginPage.loginToApplication();

        dashboardPage.waitForEventsToLoad();
        AdminEventPage adminEventPage=new AdminEventPage(page);
        adminEventPage.goTo();
        adminEventPage.createEvent(eventtitle,
                              "QA Event",
                                    "Pune",
                                   "Test Venues",
                                 "2026-10-21T06:33",
                                    "100",
                                "50");

        EventsPage eventPage =new EventsPage(page);
        eventPage.goTo();
        Locator targetCard=eventPage.findEventCard(eventtitle);
        int seatNumberBeforeBooking=eventPage.getSeatsCount(targetCard);
        BookingForm bookingFoem=eventPage.proceedtoBookingEvent(targetCard);
        bookingFoem.fillAndConfirm("Test Student","test@gmail.com","9876543210");



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

package test;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;
import utils.DataProviderUtils;

import java.io.IOException;
import java.util.HashMap;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FrameworkBuildDatadrivenTest extends TestBase {
//Invoke browsers-->tab-->typr url
    //headmode--browser open separtely


    @DataProvider(name="eventbookingDAta")
    public Object[][] eventBookingData() throws IOException {
       return DataProviderUtils.getJsonDataToMap("/src/test/resourses/eventBookingData.json");

    }
    @Test(groups = {"framework"}, dataProvider = "eventbookingDAta", description = "create event-book that ecent and verify if its booked")
    public void DemoTest(HashMap<String,String>data) {


        LoginPage loginPage=new LoginPage(page,base_url);
        DashboardPage dashboardPage=loginPage.loginToApplication();

        dashboardPage.waitForEventsToLoad();
        AdminEventPage adminEventPage=new AdminEventPage(page);
        adminEventPage.goTo();
        adminEventPage.createEvent(data.get("titlePrefix"),
                                  data.get("description"),
                                   data.get("city"),
                                   data.get("venue"),
                                   data.get("dateTime"),
                                   data.get("price"),
                                   data.get("totalSeats"))
                                   ;

        EventsPage eventPage =new EventsPage(page);
        eventPage.goTo();
        Locator targetCard=eventPage.findEventCard(data.get("titlePrefix"));
        int seatNumberBeforeBooking=eventPage.getSeatsCount(targetCard);
        BookingForm bookingFoem=eventPage.proceedtoBookingEvent(targetCard);
        bookingFoem.fillAndConfirm(data.get("fullName"),data.get("email"),data.get("phone"));



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

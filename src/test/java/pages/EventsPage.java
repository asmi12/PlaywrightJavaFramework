package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EventsPage
{
    Page page;
    public EventsPage(Page page)
    {
      this.page=page;
    }

    public void goTo()
    {
        page.locator("#nav-events").click();

    }

    public Locator waitForEventsToLoad()
    {
        //Finf newly created event in event page
        Locator eventCards = page.getByTestId("event-card");
        assertThat(eventCards.first()).isVisible();
        return eventCards;
    }

    public Locator findEventCard(String titleCard)
    {
        Locator eventCards=waitForEventsToLoad();
        Locator targetCard= eventCards.filter(new Locator.FilterOptions().setHasText(titleCard));
        assertThat(targetCard).isVisible();
        return targetCard;


    }
    public int getSeatsCount(Locator targetCard)
    {
        //assertThat(targetCard).isVisible();

        String seatsTextBeforBook = targetCard.getByText("seats").innerText();
        System.out.println(seatsTextBeforBook);
        return Integer.parseInt(seatsTextBeforBook.split(" ")[0]);
    }
 public BookingForm proceedtoBookingEvent(Locator targetCard)
 {
     targetCard.getByTestId("book-now-btn").click();
     return new BookingForm(page);
 }

}

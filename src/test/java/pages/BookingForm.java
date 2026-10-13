package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;

public class BookingForm {

    Page page;
    private static final String FULLNAME= "Full Name";
    private static final String CUST_EMAIL= "#customer-email";
    private static final String PHONE= "+91 98765 43210";
    private static final String CONFIRM_BTN= "Confirm Booking";



    public  BookingForm(Page page)
    {
        this.page=page;
    }
    public void fillAndConfirm(String fullname,String email,String PhoneNo)
    {
        page.getByLabel(FULLNAME).fill(fullname);
        page.locator(CUST_EMAIL).fill(email);
        page.getByPlaceholder(PHONE).fill(PhoneNo);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(CONFIRM_BTN)).click();
        PlaywrightAssertions.assertThat(page.getByText("Your tickets are reserved.")).isVisible();

    }
}

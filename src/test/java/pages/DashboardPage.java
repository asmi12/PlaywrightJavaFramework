package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;

public class DashboardPage {

    Page page;
    public DashboardPage(Page page)
    {
         this.page=page;
    }
    public void  waitForEventsToLoad()
    {
        PlaywrightAssertions.assertThat
                (page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

    }

}

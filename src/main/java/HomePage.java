import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage {
    public static String baseUrl = "http://demo.testfire.net/";
    public static String loginUrl = baseUrl + "login.jsp";
    private WebDriver homeDriver;
    public static String czechitasUrl = "http://czechitas-shopizer.azurewebsites.net/shop/customer/";

    private WebElement inputSearch;
    private WebElement goSearch;
    private WebElement personalTab;

    HomePage(WebDriver driver) {
        this.homeDriver = driver;
    }

    public void open() {
        homeDriver.navigate().to(baseUrl);
        inputSearch = homeDriver.findElement(By.id("query"));
        goSearch = homeDriver.findElement(By.cssSelector("input[value='Go']"));
        personalTab = homeDriver.findElement(By.id("LinkHeader2"));
    }

    public void openPersonal() {
        this.personalTab.click();
    }
}

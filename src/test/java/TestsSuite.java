import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestsSuite {
    private static WebDriver driver;

    @Before
    public void setUp() {
        this.driver = new ChromeDriver();
    }

    @After
    public void tearDown() {
        if(driver!=null) {
            System.out.println("Closing chrome browser");
            driver.quit();
        }
    }

    @Test
    public void OpenPageTest(){
        // given
        HomePage homePage = new HomePage(driver);

        // act
        homePage.open();

        //assert
        Assert.assertTrue(driver.findElement(By.id("query")).isDisplayed());
    }

    @Test
    public void OpenPersonalTab(){
        // given
        HomePage homePage = new HomePage(driver);

        // act
        homePage.open();

        //assert
        Assert.assertTrue(driver.findElement(By.id("query")).isDisplayed());
    }
}


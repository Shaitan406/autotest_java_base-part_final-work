import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class CatalogPageInternetShopTests {
    private WebDriver driver;
    private WebDriverWait wait;

    private String url = "https://intershop5.skillbox.ru/";

    @Before
    public void setUp()
    {
        System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }
    @After
    public void tearDown() throws IOException
    {
        var sourseFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(sourseFile, new File("D:\\tmp\\screenshot.png"));
        driver.quit();
    }
    private By titlePageLocator = By.cssSelector("h1.entry-title.ak-container");
    private By tvProductCatalogLocator = By.xpath("//*[@class='product-categories']//a[text()='Телевизоры']");
    private By catalogMainPageLocator = By.xpath("//*[contains(@class,'menu')]/*[contains(@class,'menu-item')]/a[text()='Каталог']");

    @Test
    public void сatalogOfHouseholdAppliancesCheckAvailabilityOfhTheElementAfterTheTransition() //проверка заголовков(товаров) через котолог
    {
        //arrange
        driver.navigate().to(url);
        //act
        driver.findElement(catalogMainPageLocator).click();
        driver.findElement(tvProductCatalogLocator).click();
        //assert
        Assert.assertEquals("Записи после клика нету или она не совпадает", "ТЕЛЕВИЗОРЫ", driver.findElement(titlePageLocator).getText());
    }
    @Test
    public void сatalogOfHouseholdAppliancesCheckAvailabilityOfhTheElementAfterTheTransitionTest() //поиск товаров через поисковую строку
    {
        //arrange
        driver.navigate().to(url);
        var productNamesLocator = By.xpath("//*[@placeholder='Введите название товара...']");
        //act
        driver.findElement(productNamesLocator).sendKeys("Книги");
        driver.findElement(productNamesLocator).sendKeys(Keys.ENTER);
        //assert
        Assert.assertEquals("После ввода в поиске товара, найденый товар не совпадает","РЕЗУЛЬТАТЫ ПОИСКА: “КНИГИ”",driver.findElement(titlePageLocator).getText());
    }
    @Test
    public void mainPageClickOrderingTrainsitionTheOrderingSection() //выбор товара переход в корзину и проверка его в корзине
    {
        //arrange
        driver.navigate().to(url);
        var tabletsMainPageLocator = By.xpath("//*[@class='widget-title'][text()='Планшеты']");
        var buttonToTheBasket = By.xpath("(//div[@class='price-cart']/a[text()='В корзину'])[1]");
        var basketMainPageLocator = By.xpath("//*[contains(@class,'menu')]/*[contains(@class,'menu-item')]/a[text()='Корзина']");
        var orderAvailabilityInTheBasket = By.cssSelector(".woocommerce-cart-form");
        //act
        driver.findElement(tabletsMainPageLocator).click();
        driver.findElement(buttonToTheBasket).click();
        driver.findElement(basketMainPageLocator).click();
        //assert
        Assert.assertTrue("Записи после клика нету или она не совпадает", driver.findElement(orderAvailabilityInTheBasket).isDisplayed());
    }
    @Test
    public void basketAddTwoProductsPresense() //добавление двух разных товаров и наличие их в корзине
    {
        //arrange
        driver.navigate().to(url);
        var addBasketTvLocator = By.xpath("(//a[contains(@class,'add_to_cart_button')])[1]");// тв в корзину
        var washingMachineLocator = By.xpath("//*[@class='product-categories']//a[text()='Стиральные машины']"); //стиральная машина
        var addBasketWashingLocator = By.xpath("(//a[contains(@class,'add_to_cart_button')])[3]"); // в корзину машину
        var basketMainPageLocator = By.xpath("//*[contains(@class,'menu')]/*[contains(@class,'menu-item')]/a[text()='Корзина']"); //лик по корзине
        var productNameLocator1 = By.xpath("(//*[@data-title='Товар'])[1]");// локаторы тв
        var productNameLocator2 = By.xpath("(//*[@data-title='Товар'])[2]");// локаторы машины
        //act
        driver.findElement(catalogMainPageLocator).click();
        driver.findElement(tvProductCatalogLocator).click();
        driver.findElement(addBasketTvLocator).click();
        driver.findElement(washingMachineLocator).click();
        driver.findElement(addBasketWashingLocator).click();
        driver.findElement(basketMainPageLocator).click();
        //assert
        Assert.assertEquals("в корзину не добавлен товар №1","LED телевизор LG 65NANO956NA Ultra HD 8K",driver.findElement(productNameLocator1).getText());
        Assert.assertEquals("в корзину не добавлен товар №2","Стиральная машина BEKO WRS55P2BWW, фронтальная, 5кг, 1000об/мин",driver.findElement(productNameLocator2).getText());
    }
}

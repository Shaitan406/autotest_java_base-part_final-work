import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class MainPageInternetShopTests {
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
        var sourseFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(sourseFile, new File("D:\\tmp\\screenshot.png"));
        driver.quit();
    }
    @Test
    public void mainPageClickBooksTrainsitionTheBooksSection()
    {
        //arrange
        driver.navigate().to(url);
        var booksMainPageLocator = By.xpath("//*[@class='widget-title'][text()='Книги']");
        var titlePageLocator = By.cssSelector("h1.entry-title.ak-container");
        //act
        driver.findElement(booksMainPageLocator).click();
        //assert
        Assert.assertEquals("Записи после клика нету или она не совпадает" , "КНИГИ",driver.findElement(titlePageLocator).getText());
    }
    @Test
    public void mainPageClickTabletsTrainsitionTheTabletsSection()
    {
        //arrange
        driver.navigate().to(url);
        var tabletsMainPageLocator = By.xpath("//*[@class='widget-title'][text()='Планшеты']");
        var titlePageLocator = By.cssSelector("h1.entry-title.ak-container");
        //act
        driver.findElement(tabletsMainPageLocator).click();
        //assert
        Assert.assertEquals("Записи после клика нету или она не совпадает" , "ПЛАНШЕТЫ",driver.findElement(titlePageLocator).getText());
    }
    @Test
    public void mainPageClickPhotoGadgetTrainsitionThePhotoGadgetSection()
    {
        //arrange
        driver.navigate().to(url);
        var photoGadgetMainPageLocator = By.xpath("//*[@class='widget-title'][text()='Фотоаппараты']");
        var titlePageLocator = By.cssSelector("h1.entry-title.ak-container");
        //act
        driver.findElement(photoGadgetMainPageLocator).click();
        //assert
        Assert.assertEquals("Записи после клика нету или она не совпадает" , "ФОТО/ВИДЕО",driver.findElement(titlePageLocator).getText());
    }
    @Test
    public void mainPageClickCatalogTrainsitionTheCatalogSection()
    {
        //arrange
        driver.navigate().to(url);
        var catalogMainPageLocator = By.xpath("//*[contains(@class,'menu')]/*[contains(@class,'menu-item')]/a[text()='Каталог']");
        var titlePageLocator = By.cssSelector("h1.entry-title.ak-container");
        //act
        driver.findElement(catalogMainPageLocator).click();
        //assert
        Assert.assertEquals("Записи после клика нету или она не совпадает" , "КАТАЛОГ",driver.findElement(titlePageLocator).getText());
    }
    @Test
    public void mainPageClickMyAccountTransitionsTheMyAccountSection()
    {
        //arrange
        driver.navigate().to(url);
        var myAccountMainPageLocator = By.xpath("//*[contains(@class,'menu')]/*[contains(@class,'menu-item')]/a[text()='Мой аккаунт']");
        var titlePageLocator = By.cssSelector("span.current");
        //act
        driver.findElement(myAccountMainPageLocator).click();
        //assert
        Assert.assertEquals("Записи после клика нету или она не совпадает" , "Мой Аккаунт",driver.findElement(titlePageLocator).getText());
    }
    @Test
    public void mainPageClickBasketTransitionsTheBasketSection()
    {
        //arrange
        driver.navigate().to(url);
        var basketMainPageLocator = By.xpath("//*[contains(@class,'menu')]/*[contains(@class,'menu-item')]/a[text()='Корзина']");
        var titlePageLocator = By.cssSelector("span.current");
        //act
        driver.findElement(basketMainPageLocator).click();
        //assert
        Assert.assertEquals("Записи после клика нету или она не совпадает" , "Корзина",driver.findElement(titlePageLocator).getText());
    }
    @Test
    public void mainPageLogoContactsLogoAccountAreDisplayed()
    {
            //arrange
            driver.navigate().to(url);
            var mainPageLogoLocator = By.cssSelector(".site-title");
            var mainPageContactsLocater = By.cssSelector(".header-callto");
            var mainPageAccountLocator = By.cssSelector(".account");
            //assert
            Assert.assertTrue("Нет логотипа на главное странице",driver.findElement(mainPageLogoLocator).isDisplayed());
            Assert.assertTrue("Нет контактов на главное странице",driver.findElement(mainPageContactsLocater).isDisplayed());
            Assert.assertTrue("Нет логотипа войти в аккаунт или ник-нейм действующего аккаунта",driver.findElement(mainPageAccountLocator).isDisplayed());
    }
    @Test
    public void discountLabelsProductPresence() {

        driver.navigate().to(url);
        var allProductLabelsLocator = By.cssSelector("#product1 li[aria-hidden='false'] .item-img span");

        var allProductsLabelsOnSale = driver.findElements(allProductLabelsLocator);

        for (var productsLabel : allProductsLabelsOnSale) {
            Assert.assertTrue("Не у всех товаров из раздела \"Распродажа\" есть значок скидки", productsLabel.getAttribute("class").contains("onsale"));
        }
    }
    @Test
    public void newLabelsProductPresence()
    {
        driver.navigate().to(url);
        var labelsNewLocator = By.cssSelector("#product2 li[aria-hidden='false'] .item-img .label-new");

        var allProductOnLabelNew = driver.findElements(labelsNewLocator);

        for (var productLabel : allProductOnLabelNew) {
            Assert.assertTrue("Не у всех товаров из раздела \"Новые поступление\" есть значок новый!", productLabel.getAttribute("class").contains("label-new"));
        }
    }
}

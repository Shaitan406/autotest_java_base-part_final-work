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

public class BasketInternetShopTests {
    private WebDriver driver;
    private WebDriverWait wait;

    private String url = "https://intershop5.skillbox.ru/";

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }
    @After
    public void tearDown() throws IOException {
        var sourseFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(sourseFile, new File("D:\\tmp\\screenshot.png"));
        driver.quit();
    }

    private By catalogMainPageLocator = By.xpath("//*[contains(@class,'menu')]/*[contains(@class,'menu-item')]/a[text()='Каталог']");
    private By tvProductCatalogLocator = By.xpath("//*[@class='product-categories']//a[text()='Телевизоры']"); //товар в каталоге
    private By addBasketTvLocator = By.xpath("(//a[contains(@class,'add_to_cart_button')])[1]"); //товар в корзину
    private By moreDetailsLocator = By.xpath("//*[@title='Подробнее']"); //подробнее кнопка-клик
    private By couponCodeLocator = By.cssSelector("#coupon_code"); //графа для ввода купона
    private By buttonCouponLocator = By.xpath("//*[@value='Применить купон']"); //кнопка купона

    @Test
    public void deleteFromBascketRemoveAndBasket() { //удаление и возвращение товара в корзине
        //arrange
        driver.navigate().to(url);
        var deleteProductLocator = By.xpath("(//*[@class='remove'])[1]"); //удалить товар
        var returnDeleteProductLocator = By.xpath("//*[@class='restore-item']"); //вернуть товар
        var productNameLocator2 = By.xpath("(//*[contains(@class,'cart_item')])[1]"); //локатор первого товара
        //act
        driver.findElement(catalogMainPageLocator).click();
        driver.findElement(tvProductCatalogLocator).click();
        driver.findElement(addBasketTvLocator).click();
        driver.findElement(moreDetailsLocator).click();
        driver.findElement(deleteProductLocator).click();
        driver.findElement(returnDeleteProductLocator).click();
        //assert
        Assert.assertTrue("Товар в корзину не вернулся",driver.findElement(productNameLocator2).isDisplayed());
    }
    @Test
    public void couponActivationAndFinalPrice() { //правильный купон и цена с купоном совпадает
        //arrange
        driver.navigate().to(url);
        var invisiblDiskountElementLocator = By.xpath("//*[contains(@class,'coupon-sert500')]"); //елемент появления активации купона
        var priceLocator = By.xpath("(//bdi)[3]");
        //act
        driver.navigate().to(url);
        driver.findElement(catalogMainPageLocator).click();
        driver.findElement(tvProductCatalogLocator).click();
        driver.findElement(addBasketTvLocator).click();
        driver.findElement(moreDetailsLocator).click();
        driver.findElement(couponCodeLocator).sendKeys("sert500");
        driver.findElement(buttonCouponLocator).click();
        //assert
        Assert.assertTrue("Элемента скидки не появилось", driver.findElement(invisiblDiskountElementLocator).isDisplayed());
        Assert.assertEquals("Итоговая оплата не совпадает","219490,00₽",driver.findElement(priceLocator).getText());
    }
    @Test
    public void checkForInvalidCoupon() { //не верный купон
        //arrange
        driver.navigate().to(url);
        var invalidCouponLocator = By.xpath("//*[@class='woocommerce-error']"); //елемент - неверный купон.
        //act
        driver.navigate().to(url);
        driver.findElement(catalogMainPageLocator).click();
        driver.findElement(tvProductCatalogLocator).click();
        driver.findElement(addBasketTvLocator).click();
        driver.findElement(moreDetailsLocator).click();
        driver.findElement(couponCodeLocator).sendKeys("momo1000");
        driver.findElement(buttonCouponLocator).click();
        //assert
        Assert.assertTrue("Элемент отсутствия купона не найден", driver.findElement(invalidCouponLocator).isDisplayed());
        Assert.assertEquals("Купон добавлен успешно добавлен.", "Неверный купон.", driver.findElement(invalidCouponLocator).getText());
    }
}








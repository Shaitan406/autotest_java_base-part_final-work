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

public class RegisterTheProductInternetShopTests {
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
    private By placinglocator = By.xpath("(//a[text()='Оформление заказа'])[1]"); //локатор меню оформления заказов
    private By authorizationLocator = By.xpath("//*[@class='showlogin']"); //локатор авторизации
    private By nameUserInputLocator = By.xpath("//*[@name='username']"); //ввод логина в авторизации
    private By passwordInputLocator = By.xpath("//*[@name='password']"); //ввод пароля
    private By buttonLoginToAccount = By.xpath("//*[@value='Войти']"); // кнопка входа в аккаунт
    private By elementOrderReceivedLocator = By.xpath("//*[@class='post-title']"); //заголовок заказ получен
    private By buttonAllLocator = By.cssSelector("#place_order"); //кнопка оформить заказ

    @Test
    public void onlineShopAuthorizationTransitionFromBasket() { //авторизация с товарами в корзине до момента где надо вводить город/улицу
        //arrange
        driver.navigate().to(url);
        //act
        driver.findElement(catalogMainPageLocator).click();
        driver.findElement(tvProductCatalogLocator).click();
        driver.findElement(addBasketTvLocator).click();
        driver.findElement(placinglocator).click();
        driver.findElement(authorizationLocator).click();
        driver.findElement(nameUserInputLocator).sendKeys("Vadim Valentin");
        driver.findElement(passwordInputLocator).sendKeys("12345qwert");
        driver.findElement(buttonLoginToAccount).click();
        //assert
        Assert.assertTrue("Нету заголовка офрмления заказа", driver.findElement(elementOrderReceivedLocator).isDisplayed());
        Assert.assertEquals("Заголовка оформления товара нету", "Оформление заказа", driver.findElement(elementOrderReceivedLocator).getText());
    }
    @Test
    public void onlineShopAddAuthorizationCouponPrice() { //добавление товара - в ОТ - авторизация акк - прим купона - итоговая сумма
        //arrange
        driver.navigate().to(url);
        var clickInEnterCoupon = By.xpath("//*[@class='showcoupon'][text()='Нажмите для ввода купона']");//в от применить купон клик
        var inputInEnterCoupon = By.xpath("//*[@placeholder='Введите код купона...']"); //в от ввод купона проверка
        var applyСouponButtonLocator = By.xpath("//*[@value='Применить купон']"); //кнопка применения купона
        var assertCouponActivationLocator = By.xpath("//*[@class='woocommerce-message']"); //проверка записи об активации купона
        var coupon500Locator = By.xpath("//*[@class='cart-discount coupon-sert500']"); //наличие купона в общей сумме
        var lastPriceTotalAmount = By.xpath("(//*[@class='woocommerce-Price-amount amount'])[last()]"); //Итоговая сумма
        //act
        driver.findElement(catalogMainPageLocator).click();
        driver.findElement(tvProductCatalogLocator).click();
        driver.findElement(addBasketTvLocator).click();
        driver.findElement(placinglocator).click();
        driver.findElement(authorizationLocator).click();
        driver.findElement(nameUserInputLocator).sendKeys("Vadim Valentin");
        driver.findElement(passwordInputLocator).sendKeys("12345qwert");
        driver.findElement(buttonLoginToAccount).click();
        driver.findElement(clickInEnterCoupon).click();
        driver.findElement(inputInEnterCoupon).sendKeys("sert500");
        driver.findElement(applyСouponButtonLocator).click();
        //assert
        Assert.assertTrue("Элемента об успешной активации купона нету", driver.findElement(assertCouponActivationLocator).isDisplayed());
        Assert.assertTrue("Купона - 500 нет.", driver.findElement(coupon500Locator).isDisplayed());
        Assert.assertEquals("Итоговая сумма не верна.","219490,00₽",driver.findElement(lastPriceTotalAmount).getText());
    }
    @Test
    public void placeAnOrderWithoutOrderDetails() { //в разделе оформить заказ не заполняем данные и нажимаем оформить заказ
        //arrange
        driver.navigate().to(url);
        var errorAllLocator = By.cssSelector(".woocommerce-NoticeGroup.woocommerce-NoticeGroup-checkout");//ошибка если поле не заполненно
        var nameLocator = By.cssSelector("[autocomplete='given-name']"); // имя(очистка данных)
        var emailLocator = By.cssSelector("[name='billing_email']"); //email(очистка данных)
        //act
        driver.findElement(catalogMainPageLocator).click();
        driver.findElement(tvProductCatalogLocator).click();
        driver.findElement(addBasketTvLocator).click();
        driver.findElement(placinglocator).click();
        driver.findElement(authorizationLocator).click();
        driver.findElement(nameUserInputLocator).sendKeys("Vadim Valentin");
        driver.findElement(passwordInputLocator).sendKeys("12345qwert");
        driver.findElement(buttonLoginToAccount).click();
        driver.findElement(nameLocator).clear();
        driver.findElement(emailLocator).clear();
        driver.findElement(buttonAllLocator).click();
        //assert
        Assert.assertTrue("Ошибки не заполнения пропущеных строк не появилось",driver.findElement(errorAllLocator).isDisplayed());
    }
    @Test
    public void orderReceivedDeliveryMethod () { //заказ получен и метод доставки:оплата при доставке
        //arrange
        driver.navigate().to(url);
        var payLocator = By.xpath("//*[@for='payment_method_cod']"); //способ оплаты
        var payMethodTextLocator = By.xpath("//*[contains(@class,'payment-method')]/*[text()='Оплата при доставке']");
        var elementOrderReceivedLocator = By.xpath("//*[@class='post-title']"); //заголовок заказ получен
        var phoneLocator = By.cssSelector("[name='billing_phone']"); //локатор сбить фокус
        //act
        driver.findElement(catalogMainPageLocator).click();
        driver.findElement(tvProductCatalogLocator).click();
        driver.findElement(addBasketTvLocator).click();
        driver.findElement(placinglocator).click();
        driver.findElement(authorizationLocator).click();
        driver.findElement(nameUserInputLocator).sendKeys("Vadim Valentin");
        driver.findElement(passwordInputLocator).sendKeys("12345qwert");
        driver.findElement(buttonLoginToAccount).click();
        driver.findElement(phoneLocator).sendKeys(Keys.TAB);
        driver.findElement(payLocator).click();
        driver.findElement(buttonAllLocator).click();
        //assert
        Assert.assertEquals("Способ оплаты остался прямым банковским переводом","Оплата при доставке",driver.findElement(payMethodTextLocator).getText());
        Assert.assertEquals("Заказ не получен","Заказ получен",driver.findElement(elementOrderReceivedLocator).getText());
    }
}

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

public class MyAccountRegistrationAndAuthorization { //мой аккаунт ругистрация и авторизация

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
    private By myAccountMainPageLocator = By.xpath("//*[contains(@class,'menu')]/*[contains(@class,'menu-item')]/a[text()='Мой аккаунт']");
    private By textMyAccountTitleElement = By.xpath("//*[@class='post-title'][text()='Мой аккаунт']"); //заголовок мой аккаунт
    private By buttonLoginLocator = By.xpath("//*[@class='custom-register-button']"); //кнопка зарегестрироваться
    private By textTitleRegistration = By.xpath("//*[@class='post-title'][text()='Регистрация']");
    private By finishButtonLoginLocator = By.xpath("//*[@value='Зарегистрироваться']"); // последняя кнопка зарегестрироватся
    private By errorFalseLocator = By.xpath("//*[@class='woocommerce-notices-wrapper']"); // ошибка при регистрации
    private By nameUserInputLocator = By.xpath("//*[@name='username']"); //ввод логина в авторизации
    private By passwordInputLocator = By.xpath("//*[@name='password']"); //ввод пароля

    @Test
    public void accauntLogin() { //авторизация аккаунта
        driver.navigate().to(url);

        var buttonVoyti = By.xpath("//*[@class='account']"); //кнопка войти на главное странице
        var buttonLoginToAccount = By.xpath("//*[@value='Войти']"); // кнопка входа в аккаунт
        var infoLogininPasswordAccount = By.xpath("//strong[text()='Vadim Valentinov']"); //проверка авторизации

        driver.findElement(buttonVoyti).click();
        driver.findElement(nameUserInputLocator).sendKeys("Vadim Valentin");
        driver.findElement(passwordInputLocator).sendKeys("12345qwert");
        driver.findElement(buttonLoginToAccount).click();

        Assert.assertTrue("Переход на мой аккаунт не произошел", driver.findElement(textMyAccountTitleElement).isDisplayed());
        Assert.assertEquals("Авторизации не произошло", "Vadim Valentinov", driver.findElement(infoLogininPasswordAccount).getText());
    }
        @Test
        public void registrLogin() { //Регистрация аккаунта
            driver.navigate().to(url);

            var emailInputLoctor = By.xpath("//*[@name='email']"); //ввод email
            var finishElementLocator = By.xpath("//*[@class='content-page']"); //сообщение об успешной регистрации

            driver.findElement(myAccountMainPageLocator).click();
            driver.findElement(buttonLoginLocator).click();
            driver.findElement(nameUserInputLocator).sendKeys("vadimValentinovvvv");
            driver.findElement(emailInputLoctor).sendKeys("vadim@testerrrr.by");
            driver.findElement(passwordInputLocator).sendKeys("qwert12345");
            driver.findElement(finishButtonLoginLocator).click();

            Assert.assertTrue("Перехода в регестраци/авторизацию не произошло",driver.findElement(textTitleRegistration).isDisplayed());
            Assert.assertEquals("","Регистрация завершена",driver.findElement(finishElementLocator).getText());
        }
        @Test
        public void errorInRegistration() { //Ошибка при регистрации
            driver.navigate().to(url);

            driver.findElement(myAccountMainPageLocator).click();
            Assert.assertTrue("Перехода и заголовка нету",driver.findElement(textMyAccountTitleElement).isDisplayed());

            driver.findElement(buttonLoginLocator).click();
            driver.findElement(finishButtonLoginLocator).click();

            Assert.assertTrue("Перехода и заголовка нету",driver.findElement(textTitleRegistration).isDisplayed());
            Assert.assertEquals("Текста ошибки нет","Error: Пожалуйста, введите корректный email.",driver.findElement(errorFalseLocator).getText());
    }
}

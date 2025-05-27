package data.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.NoSuchElementException;
import io.qameta.allure.Step;
import data.model.Item;

import java.time.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainPage {
    public WebDriver driver;

    public MainPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    /**
     * Кнопка закрытия всплывающего окна с приглашением войти в учетную запись
     */
    @FindBy(xpath = "//div[@data-auto='modal']//button[@data-auto='close-popup']")
    private WebElement modalCloseButton;

    /**
     * Вейтер сортировки
     */
    @FindBy(xpath = "//div[@data-auto='SerpStatic-loader']")
    private WebElement sortWaiter;

    /**
     * Поле ввода поискового запроса
     */
    @FindBy(xpath = "//input[@data-auto='search-input']")
    private WebElement searchInput;

    /**
     * Кнопка Найти
     */
    @FindBy(xpath = "//button[@data-auto='search-button']")
    private WebElement searchButton;

    /**
     * Список с результатами поиска
     */
    @FindBy(xpath = "//div[@data-auto-themename='listDetailed']")
    private List<WebElement> resultBlockList;

    /**
     * Проверка на присутствие объекта на странице
     * @param we объект WebElement
     * @return true / false
     */
    public boolean isElementPresent(WebElement we) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        try {
            we.getTagName();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        } finally {
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }
    }

    /**
     * Кнопка сортировки результатов по возрастанию цены
     */
    @FindBy(xpath = "//button[@data-autotest-id='aprice']")
    private WebElement sortByPriceButton;

    /**
     * Метод закрывает всплывающее окно с приглашением войти в учетную запись
     */
    public void closeModalIfPresent() {
        if (isElementPresent(modalCloseButton)) modalCloseButton.click();
    }

    /**
     * Метод передает в поисковое поле строку
     */
    @Step("Ввести в строку поиска название искомого товара")
    public void fillSearchInput(String searchString) {
        searchInput.sendKeys(searchString);
    }

    /**
     * Метод нажимает на кнопку Найти
     */
    @Step("Нажать кнопку Найти")
    public void pressSearchButton() {
        searchButton.click();
    }

    /**
     * Метод возвращает результаты поиска в виде списка объектов Item.
     * @param searchString Строка поискового запроса
     * @return Список объектов Item
     */
    @Step("Получение списка резульатов по строке поиска")
    public ArrayList<Item> search(String searchString) {
        closeModalIfPresent();
        fillSearchInput(searchString);
        pressSearchButton();
        ArrayList<Item> result = new ArrayList<>();
        for (WebElement resultBlock : resultBlockList) {
            String title = resultBlock.findElement(By.xpath(".//span[@data-auto='snippet-title']")).getText();
            String price = resultBlock.findElement(By.xpath(".//span[@data-auto='snippet-price-current']/span[1]")).getText();
            result.add(new Item(title, price, ""));
        }
        return result;
    }

    /**
     * Применяем на странице сортировку по возрастанию цены
     */
    @Step("Применить сортировку по цене и дождаться обновления результатов")
    public void clickSortByPriceButton() {
        sortByPriceButton.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // 10 секунд ожидания
        wait.until(ExpectedConditions.visibilityOf(sortWaiter));
        wait.until(ExpectedConditions.invisibilityOf(sortWaiter));
    }

    /**
     * Открываем товар соответствующий первому результату поиска
     */
    @Step("Нажать на самый первый результат")
    public void clickFirstResult() {
        String newUrl = resultBlockList.get(0).findElement(By.xpath(".//a[@data-auto='snippet-link']")).getAttribute("href");
        driver.get(newUrl);
    }
}
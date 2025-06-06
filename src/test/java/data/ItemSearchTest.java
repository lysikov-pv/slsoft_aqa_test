package data;

import data.utils.BrowserManager;
import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.*;
import io.qameta.allure.*;
import data.pages.ItemPage;
import data.pages.MainPage;
import data.model.Item;

import java.util.ArrayList;

import static data.constants.TestConstants.*;

@Feature("Тест поиска на Яндекс Маркете")
public class ItemSearchTest {
    public static WebDriver driver;
    public static MainPage mainPage;
    public static ItemPage itemPage;

    /**
     * Снятие и прикрепление скриншота для Allure
     */
    @Attachment(value = "{1}", type = "image/png")
    public byte[] saveScreenshot(WebDriver driver, String title) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    /**
     * Прикрепление текста лога для Allure
     */
    @Attachment(value = "{1}", type = "text/plain")
    public static String saveTextLog(String message, String title) {
        return message;
    }

    @BeforeClass(description = "Открываем в браузере страницу market.yandex.ru")
    public void before() {
        driver = BrowserManager.getDriver("chrome");
        mainPage = new MainPage(driver);
        driver.get(PAGE_URI);
        saveScreenshot(driver, "Скриншот после открытия страницы");
    }
    @AfterClass(description = "Закрываем браузер")
    public void after() {
        BrowserManager.quit();
    }

    @Test(description = "Искомый товар присутствует в результатах")
    @Description("В ответ на запрос, получаем список результатов и проверяем, что есть хотя бы один товар в названии" +
            " которого содержится искомая строка")
    public void resultAsExpected() {
        ArrayList<Item> items = new ArrayList<>(mainPage.search(SEARCH_STRING));
        saveScreenshot(driver, "Скриншот результатов поиска");
        StringBuilder titles = new StringBuilder();
        boolean isContains = false;
        int count = 1;
        for (Item item: items) {
            if (item.title.toLowerCase().contains(SEARCH_STRING.toLowerCase())) {
                isContains = true;
                titles.append(count).append(". ")
                        .append(item.title)
                        .append(" -- ")
                        .append(item.price)
                        .append("р. \n");
                count++;
            }
        }
        saveTextLog(titles.toString(),"Результаты поиска");
        Assert.assertTrue(isContains, "В результатах первой страницы не найдено вхождение поисковой строки");
    }

    @Test(dependsOnMethods = {"resultAsExpected"}, description = "Товар с наименьшей ценой соответствует запросу")
    @Description("Сортируем результаты поиска по возрастанию цены, затем открываем первый в результатах товар и" +
            " проверяем его на соответствие поисковой строке")
    public void cheapestItemAsExpected() {
        mainPage.clickSortByPriceButton();
        saveScreenshot(driver, "Скриншот результатов после сортировки по цене");
        mainPage.clickFirstResult();
        saveScreenshot(driver, "Скриншот карточки самого дешевого товара по поисковому запросу");
        itemPage = new ItemPage(driver);
        Item item = itemPage.getItemInfo();
        saveTextLog(item.toString(), "Самый дешевый товар по поисковому запросу");
        Assert.assertTrue(item.title.toLowerCase().contains(SEARCH_STRING.toLowerCase()),
                "Название товара \"" + item.title + "\" не содержит поисковую строку \"" + SEARCH_STRING + "\"");
    }
}
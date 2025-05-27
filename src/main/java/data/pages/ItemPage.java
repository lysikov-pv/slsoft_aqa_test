package data.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import data.model.Item;

public class ItemPage {
    public WebDriver driver;

    public ItemPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    /**
     * Название магазина
     */
    @FindBy(xpath = "//div[@data-auto='shop-info-container']//a")
    private WebElement shopNameBlock;

    /**
     * Название продукта
     */
    @FindBy(xpath = "//h1[@data-auto='productCardTitle']")
    private WebElement titleBlock;

    /**
     * Цена
     */
    @FindBy(xpath = "//h3[@data-auto='snippet-price-current']")
    private WebElement priceBlock;

    /**
     * Метод возвращает информацию со страницы товара в виде объекта Item
     */
    public Item getItemInfo() {
        String shopName = shopNameBlock.getText();
        String title = titleBlock.getText();
        String price = priceBlock.getText().replace("Цена с картой Яндекс Пэй:\n", "").replace(" ₽", "");
        return new Item(title, price, shopName);
    }
}
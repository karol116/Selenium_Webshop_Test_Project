package pages;

import methods.TestUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import javax.management.InstanceNotFoundException;
import java.util.List;

public class StorePage extends AbstractPage {

    TestUtils testUtils;
    Actions actions;

    public StorePage(WebDriver driver) {
        super(driver);
        testUtils = new TestUtils(driver);
    }

    @FindBy(xpath = "//input[@class='search-field']")
    private WebElement searchInput;

    @FindBy(xpath = "//button[@value='Search']")
    private WebElement searchButton;

    @FindBy(xpath = "//h1[contains(text(), 'Search results')]")
    private WebElement searchResultsTitle;

    @FindBy(xpath = "//*[@class='cart-container']")
    private WebElement cartIcon;

    @FindBy(xpath = "//*[@id='ast-desktop-header']//*[contains(@class, 'button') and text()='View cart']")
    private WebElement viewCart;

    @FindBy(xpath = "//*[@class='cart-container']//*[@class= 'count']")
    private WebElement cartCounter;

    @FindBy(xpath = "//*[text()='Add to cart']")
    public List<WebElement> addToCartButtons;

    public void confirmSearchButton() {
        testUtils.clickElement(searchButton);
    }

    public void searchInsertedText(String textToFind) {
        testUtils.clickElement(searchInput);
        searchInput.sendKeys(textToFind);
        confirmSearchButton();
    }

    public String getSearchResultsText() {
        return testUtils.getElementText(searchResultsTitle);
    }

    private void waitUntilAddedProductToCart(int cartCounterBeforeAddedProduct) throws InterruptedException, InstanceNotFoundException {
        final int halfSecondSleepTime = 500;
        String cartCounterText = "";
        int cartCounterNumber = 0;
        int attemptNumber = 6;


        for (int loopCounter = 0; loopCounter <= attemptNumber; loopCounter++) {
            cartCounterText = cartCounter.getText();
            cartCounterNumber = Integer.parseInt(cartCounterText);
            if (!(cartCounterNumber == cartCounterBeforeAddedProduct + 1)) {
                Thread.sleep(halfSecondSleepTime);
            } else {
                return;
            }
        }

        throw new InstanceNotFoundException("Liczba produktów w koszyku nie zwiększyła się po upływnie " + attemptNumber / 2 + " sekund od próby dodania produktu do koszyka.");
    }

    public void addSearchedProductToCart(int numberOfResultItem) throws InstanceNotFoundException, InterruptedException {
        String cartCounterText = cartCounter.getText();
        int cartCounterNumber = Integer.parseInt(cartCounterText);

        testUtils.clickElement(addToCartButtons.get(numberOfResultItem - 1));

        waitUntilAddedProductToCart(cartCounterNumber);
    }

    public StorePage addProductToCart(String productName) throws InstanceNotFoundException, InterruptedException {
        String cartCounterText = cartCounter.getText();
        int cartCounterNumber = Integer.parseInt(cartCounterText);
//        long seconds = driver.manage().timeouts().getPageLoadTimeout().getSeconds();
//        System.out.println("Loaded time: " + seconds);
        testUtils.clickElement(driver.findElement(By.xpath("//*[text()='" + productName + "']/../..//*[text()='Add to cart']")));
        waitUntilAddedProductToCart(cartCounterNumber);
        return this;
    }
    public StorePage addFirstDisplayedProductToCart() throws InstanceNotFoundException, InterruptedException {
        String cartCounterText = cartCounter.getText();
        int cartCounterNumber = Integer.parseInt(cartCounterText);
        testUtils.clickElement(addToCartButtons.get(0));
        waitUntilAddedProductToCart(cartCounterNumber);
        return this;
    }

    public CartPage goToCart() {
        actions = new Actions(driver);
        actions.moveToElement(cartIcon).click(viewCart).build().perform();
        return new CartPage(driver);
    }
}
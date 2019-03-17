package ru.timofeeva;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

public class Main {
    private static WebDriver driver;


    public static void main(String[] args) {
        try {
            System.out.println("ТEСТ ПО SELENIUM");
            System.setProperty("webdriver.chrome.driver", "drv/chromedriver.exe");


            driver = new ChromeDriver();
            String url = "https://www.rgs.ru/";
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.manage().window().maximize();
            driver.get(url);

            //2. Выбрать пункт меню - Страховани
            System.out.println("2. Выбрать пункт меню - Страховани");
            clickElement(By.xpath("//ol/li/a[contains(text(),'Страхование')]"));

            //3. Путешествия – Страхование выезжающих за рубеж
            System.out.println("3. Путешествия – Страхование выезжающих за рубеж");
            clickElement(By.xpath("//a[contains(text(),'Выезжающим за рубеж')]"));

            //4. Нажать рассчитать – Онлайн
            System.out.println("4. Нажать рассчитать – Онлайн");
            scrollToAndClickElement(By.xpath("//*[contains(text(),'Рассчитать')][contains(@class,'btn-attention')]"));

            //5. Проверить наличие заголовка – Страхование выезжающих за рубеж
            System.out.println("5. Проверить наличие заголовка – Страхование выезжающих за рубеж");
            String expectString = "Страхование выезжающих за рубеж";
          //  WebElement element = driver.findElement(By.xpath("//div/span[@class='h1']"));
            compareText("//div/span[@class='h1']", expectString);

           // Thread.sleep(2000);

            //14. Я согласен на обработку данных  - выбрать чекбокс
            System.out.println("5.5 Я согласен на обработку данных  - выбрать чекбокс");
            scrollToAndClickElement(By.xpath("//form[contains(@data-bind,'calculation')]" +
                    "/div[contains(@data-bind,'validationApply')]/adaptive-checkbox/label"));


            //6. Заполнить форму: Несколько поездок в течении года
            System.out.println("6. Заполнить форму: Несколько поездок в течении года");
            clickElement(By.xpath("//*[contains(text(),'Несколько')]"));

            //7.Куда едем – Шенген
            System.out.println("7.Куда едем – Шенген");
            WebElement element = driver.findElement(By.id("Countries"));
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            jse.executeScript("arguments[0].scrollIntoView()", element);
            element.sendKeys("Шен");
            element.sendKeys(Keys.TAB);

            // 8.Страна въезда – Испания
            System.out.println("8.Страна въезда – Испания");
            element = driver.findElement(By.name("ArrivalCountryList"));
            jse.executeScript("arguments[0].scrollIntoView()", element);
            new Select(element).selectByVisibleText("Испания");

            // 9.Дата первой поездки – 1 ноября
            System.out.println("9.Дата первой поездки");
            scrollToAndClickElement(By.xpath("//input[contains(@data-bind, 'FirstDepartureDate')]"));
            WebElement nextMonth = driver.findElement(By.xpath("//div[contains(@class, 'datepicker-days')]" +
                    "/table/thead/tr/th[contains(@class, 'next')]"));
            setFirstDateOfTrip(nextMonth);

            // 10.Сколько дней планируете пробыть за рубежом за год – не более 90
            System.out.println("10.Сколько дней планируете пробыть за рубежом за год – не более 90");
            scrollToAndClickElement(By.xpath("//*[contains(@data-bind,'btnRadioGroupValue: 90')]"));

            //11. ФИО
            System.out.println("11.ФИО");
            String fName = "Ivan Nikolaevich";
            element = driver.findElement(By.xpath("//input[contains(@class,'form-control')][@data-test-name='FullName']"));
            jse.executeScript("arguments[0].scrollIntoView()", element);
            element.click();
            element.sendKeys(fName);

            //12.Дата рождения
            System.out.println("12.Дата рождения");
            element = driver.findElement(By.xpath("//*[@data-test-name='BirthDate']"));
            jse.executeScript("arguments[0].scrollIntoView()", element);
            element.click();
            element.sendKeys("15.02.1966");


            //13.Планируется активный отдых
            System.out.println("13.Планируется активный отдых");
            scrollToAndClickElement(By.xpath("//div[contains(@data-bind,'activeRestOrSportsToggle')]/div[contains(@class,  'toggle-rgs')]"));


            //15. Нажать рассчитать
            System.out.println("15. Нажать рассчитать");
            scrollToAndClickElement(By.xpath("//*[@data-test-name='NextButton'][contains(@data-bind,'Misc.NextButton')]"));

            //16. Проверить значения:
            //Thread.sleep(2000);
            System.out.println("16.Проверить значения");

            //16.0 Условия страхования – Многократные поездки в течении года
            System.out.println("16.0 Условия страхования – Многократные поездки в течении года");
            String trips = "Многократные поездки в течение года";
            compareText("//span[contains(@class,'summary-value')][@data-bind='with: Trips']/span[@class='text-bold']", trips);

          //16.1 Территория – Шенген
            System.out.println("16.1 Территория – Шенген");
            compareText("//span/span[contains(@data-bind,'foreach: countries')]/strong", "Шенген");

           //16.2 Застрахованный
            System.out.println("16.2 Застрахованный");
            String fullName = "IVAN NIKOLAEVICH";
            compareText("//strong[contains(@data-bind,'LastName')]", fullName);

           //16.3 Дата рождения
            System.out.println("16.3 Дата рождения");
            compareText("//strong[contains(@data-bind,' text: BirthDay.')]", "15.02.1966");

           //16.4 Активный отдых - включен
            System.out.println("16.4 Активный отдых - включен");
            compareText("//div[contains(@data-bind, 'SelectedProgram.Options')]" +
                    "/div[contains(@data-bind, 'Активный')]/div[@class='summary-row']/span[@class='summary-value']/span", "Включен");

            //Thread.sleep(10000);

        } catch (Exception e) {
            System.out.println("The following exception was generated:");
            System.out.println(e.getMessage());
        } finally {
            driver.quit();
        }


    }

    public static void compareText(String xpath, String expect) throws Exception{

        WebElement s1 = (new WebDriverWait(driver,10))
               .until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].scrollIntoView()", s1);
        String s2 = s1.getText();
        if (s2.contains(expect)) {
            System.out.println("Исходный текст есть: " + expect);
        } else {
            throw new Exception("Исходного текста нет:" + expect);
        }
    }


    public static void printElement(WebElement element) {
        if (element == null) {
            System.out.println("Element is null");
        } else {
            System.out.println(element.getTagName() + " " + element.getText());
        }

    }

    public static void clickElement(By locator) {
        (new WebDriverWait(driver,10))
                .until(ExpectedConditions.elementToBeClickable(locator)).click();

    }

    public static void setFirstDateOfTrip(WebElement nextMonth) {
        LocalDate dateNow = LocalDate.now();
        LocalDate dateInTwoWeeks = dateNow.plusDays(14);
        DateFormat df = new SimpleDateFormat();
        int reportDay = dateInTwoWeeks.getDayOfMonth();
        if (dateNow.getMonth() != dateInTwoWeeks.getMonth()) {
            nextMonth.click();
        } else {
            driver.findElement(By.xpath("//div[contains(@class,'datepicker-days')]/table/tbody/tr/td[@class='day'][contains(text(), " + reportDay + ")]")).click();
        }
    }

    public static void scrollToAndClickElement(By locator) {

       WebElement element = (new WebDriverWait(driver,10)
                .until(ExpectedConditions.elementToBeClickable(locator)));
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].scrollIntoView()", element);
        printElement(element);
        element.click();
    }


}

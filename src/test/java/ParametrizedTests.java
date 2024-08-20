import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


@DisplayName("Набор параметризированных тестов для ИМ 123")
public class ParametrizedTests {

    @BeforeEach
    void setUp() {
        Configuration.baseUrl = "https://www.123.ru/";
        Configuration.browserSize = "1920x1080";
    }

    @DisplayName("Добавление товара в корзину из каталога")
    @ValueSource(strings = {
            "/kompyutery_noutbuki_soft/kibersport/manipulyatory-igrovye/igrovye-kovriki-dlya-myshek",
            "/kompyutery_noutbuki_soft/notebooks", "/smartfoni_plansheti_gadjeti/telefoniya"
    })
    @ParameterizedTest(name = "Добавление из категории {0}")
    @Tag("Smoke")
    @Tag("High")
    @Tag("Команда_1")
    void addToCartFromCategoriesTest(String category) {
        open(category);
        $(".buy-block").$(byText("в корзину")).click();
        $("#basket-preview").shouldBe(appear);
        $("#basket-preview").shouldHave(text("Товар добавлен в корзину"));
    }


    @DisplayName("Добавление товара в корзину из карточки товара")
    @CsvFileSource(resources = "/articuls.csv")

    @ParameterizedTest(name = "Добавление из КТ артикула {0}")
    @Tag("Smoke")
    @Tag("Medium")
    @Tag("Команда_1")
    void addToCardFromSearchTest(String articul) {
        $("#searchinput").setValue(articul).pressEnter();
        $(".pc-button-pay").click();
        $("#basket-preview").shouldBe(appear);
        $("#basket-preview").shouldHave(text("Товар добавлен в корзину"));
    }


    @DisplayName("Смена региона в хедере")
    @CsvSource(value = {"Санкт-Петербург, 8 (800) 333 9 123", "Москва, 8 (495) 225-9-123"
    })
    @ParameterizedTest(name = "В хедере регион {0} имеет телефон {1}")
    @Tag("Smoke")
    @Tag("Medium")
    @Tag("Команда_2")
    void checkRegionHeaderTest(String city, String number) {
        open("/");
        $(".select-city").click();
        $("#cities").$(".city-list").$(byText(city)).click();
        $(".select-city").shouldHave(text(city));
        $(".wr__tel").shouldHave(text(number));

    }

  @DisplayName("Подсказки в поиске")
    @MethodSource
    @ParameterizedTest(name = "При введенном запросе {0} есть подсказка {1}")
    @Tag("Smoke")
    @Tag("Medium")
    @Tag("Команда_4")
    void popularCaregorySearchTest(String query, String result) {
        open("/");
        $("#searchinput").setValue(query);
        $(".digi-autocomplete-container").shouldHave(text(result));

    }
 static Stream<Arguments>  popularCaregorySearchTest() {
        return Stream.of(
                Arguments.of("виде", "видеокарта"),
                Arguments.of("сма", "смартфоны")
                );
    }


}
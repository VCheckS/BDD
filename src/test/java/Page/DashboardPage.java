package Page;


import Data.DataHelper;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;


import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final String balancaStart = "баланс: ";
    private final String balancaEnd = " р.";

    private SelenideElement heading = $("[data-test-id=dashboard]");
    private static final ElementsCollection cards = $$(".list__item div");

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        var text = cards.findBy(attribute("data-test-id", cardInfo.getTestId())).getText();
        return extractBalance(text);
    }

    public TransferPage selectCardToTransfer(DataHelper.CardInfo cardInfo) {
        cards.findBy(attribute("data-test-id", cardInfo.getTestId())).$("button").click();
        return new TransferPage();
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balancaStart);
        var end = text.indexOf(balancaEnd);
        var value = text.substring(start + balancaStart.length(), end);
        return Integer.parseInt(value);
    }

}


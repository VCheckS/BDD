package Test;
import Page.DashboardPage;
import Page.LoginPageV1;
import Data.DataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static Data.DataHelper.*;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {
    LoginPageV1 loginPageV1;
    DashboardPage dashboardPage;

    @BeforeEach
    void setUp() {
        loginPageV1 =  open("http://localhost:9999", LoginPageV1.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPageV1.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor();
        dashboardPage = verificationPage.validVerify(verificationCode);
    }
    @Test
    void shouldTransferMoneyBetweenOwnCardsV1() {
        var firstCardInfo = getCard1Info();
        var secondCardInfo = getCard2Info();
        var firstBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = generateInvalidAmount(firstBalance);
        var expectedBalanceFirst = firstBalance - amount;
        var expectedBalanceSecond = secondBalance + amount;
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
        var actualBalanceFirst = dashboardPage.getCardBalance(firstCardInfo);
        var actualBalanceSecond = dashboardPage.getCardBalance(secondCardInfo);
        assertEquals(expectedBalanceFirst, actualBalanceFirst);
        assertEquals(expectedBalanceSecond, actualBalanceSecond);

    }

}

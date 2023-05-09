package test;

import data.DataHelper;
import page.DashboardPage;
import page.LoginPageV1;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static data.DataHelper.*;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {
    LoginPageV1 loginPageV1;
    DashboardPage dashboardPage;

    @BeforeEach
    void setUp() {
        loginPageV1 = open("http://localhost:9999", LoginPageV1.class);
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
        var amount = generateValidAmount(firstBalance);
        var expectedBalanceFirst = firstBalance - amount;
        var expectedBalanceSecond = secondBalance + amount;
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
        var actualBalanceFirst = dashboardPage.getCardBalance(firstCardInfo);
        var actualBalanceSecond = dashboardPage.getCardBalance(secondCardInfo);
        assertEquals(expectedBalanceFirst, actualBalanceFirst);
        assertEquals(expectedBalanceSecond, actualBalanceSecond);
    }

    @Test
    void shouldNotMakeTransfer() {
        Configuration.holdBrowserOpen = true;
        var firstCardInfo = getCard1Info();
        var secondCardInfo = getCard2Info();
        var firstBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = generateInvalidAmount(secondBalance);
        var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
        transferPage.makeTransfer(String.valueOf(amount), secondCardInfo);
        transferPage.findErrorMessage("На карте **** 0001 Недостаточно средств для перевода");
        var actualBalanceFirst = dashboardPage.getCardBalance(firstCardInfo);
        var actualBalanceSecond = dashboardPage.getCardBalance(secondCardInfo);
        assertEquals(firstBalance, actualBalanceFirst);
        assertEquals(secondBalance, actualBalanceSecond);
    }

}

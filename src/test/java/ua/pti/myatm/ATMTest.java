/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pti.myatm;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.*;

/**
 *
 * @author andrii
 */
public class ATMTest {



    @Test
    public void testGetMoneyInATM() {
        System.out.println("getMoneyInATM");
        ATM instance = new ATM(10.0);
        double expResult = 10.0;
        double result = instance.getMoneyInATM();
        assertEquals(expResult, result, 0.0);
        
    }

    @Test
    public void testATMStartBalanceZero() {
        ATM instance = new ATM(0.0);
        double expResult = 0;
        double result = instance.getMoneyInATM();
        assertEquals(result, expResult, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testATMWithLowerThanZeroStartBalance() {
        System.out.println("ATM with start balance lower than 0");
        ATM instance = new ATM(-1.0);
    }

    @Test(expected = ATM.NoCardInsertedException.class)
    public void testValidateCardWithoutInsertedCard() {
        System.out.println("ValidateCard without inserted card");
        ATM instance = new ATM(10.0);
        boolean expResult = false;
        boolean result = instance.validateCard(null, 1234);
        assertEquals(expResult, result);
    }

    @Test
    public void testValidateBlockedCard() {
        System.out.println("validateCard with blocked card");
        Card card = mock(Card.class);
        int pinCode = 1234;
        when(card.checkPin(pinCode)).thenReturn(true);
        when(card.isBlocked()).thenReturn(true);
        ATM instance = new ATM(10.0);
        boolean expResult = false;
        boolean result = instance.validateCard(card, pinCode);
        assertEquals(expResult, result);
    }

    @Test
    public void testValidateCardWithWrongPin() {
        System.out.println("validateCard with wrong pin");
        Card card = mock(Card.class);
        when(card.checkPin(anyInt())).thenReturn(false);
        when(card.isBlocked()).thenReturn(false);
        ATM instance = new ATM(10.0);
        boolean expResult = false;
        boolean result = instance.validateCard(card, 1234);
        assertEquals(expResult, result)
;    }


    @Test
    public void testValidateCard() {
        System.out.println("validateCard");
        Card card = mock(Card.class);
        int pinCode = 1234;
        when(card.checkPin(pinCode)).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        ATM instance = new ATM(10.0);
        boolean expResult = true;
        boolean result = instance.validateCard(card, pinCode);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckBalance() {
        System.out.println("checkBalance");
        Account account = mock(Account.class);
        double accountBalance = 1000.0;
        when(account.getBalance()).thenReturn(accountBalance);
        Card card = mock(Card.class);
        when(card.getAccount()).thenReturn(account);
        when(card.checkPin(anyInt())).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);

        double startBalanceInATM = 100;
        ATM instance = new ATM(startBalanceInATM);
        instance.validateCard(card,1234);
        double result = instance.checkBalance();
        assertEquals(accountBalance, result, 0.0);
    }

    @Test(expected=ATM.NoCardInsertedException.class)
    public void testCheckBalanceWithoutCard(){
    	System.out.println("checkBalance with no card inserted");
    	double ATMBalance =100.0;
    	ATM instance = new ATM(ATMBalance);
    	double result = instance.checkBalance();
    }

    @Test
    public void testGetCash() {
        System.out.println("getCash");
        double accountBalanceBefore = 1000.0;
        double amountForWithdraw = 100.0;
        double accountBalanceAfter = 900.0;
        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(accountBalanceBefore).thenReturn(accountBalanceAfter);
        when(account.withdraw(amountForWithdraw)).thenReturn(amountForWithdraw);
        
        Card card = mock(Card.class);
        when(card.getAccount()).thenReturn(account);
        when(card.checkPin(anyInt())).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        
        ATM instance = new ATM(1000.0);
        instance.validateCard(card,12345);
        double expResult = accountBalanceAfter;
        double result = instance.getCash(amountForWithdraw);
        assertEquals(expResult, result, 0.0);
    }

    @Test(expected = ATM.NoCardInsertedException.class)
    public void textGetCashWitoutCard() {
        System.out.println("getCash without inserted(valid) card");
        double ATMBalance = 200.0;
        ATM instance = new ATM(ATMBalance);
        double accountWithdraw = 1.0;
        double result = instance.getCash(accountWithdraw);
    }

    @Test(expected = ATM.NotEnoughMoneyInAccountException.class)
    public void testGetCashWithNotEnoughMoneyInAccount(){
    	System.out.println("getCash with not enough money in account");
    	double ATMBalance = 100.0;
    	ATM instance = new ATM(ATMBalance);
    	double accountBalance = 50.0;
        double accountWithdraw = 70.0;
        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(accountBalance);
        when(account.withdraw(accountWithdraw)).thenReturn(accountWithdraw);
        Card card = mock(Card.class);
        when(card.getAccount()).thenReturn(account);
        when(card.checkPin(anyInt())).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        instance.validateCard(card, 1234);
        double result = instance.getCash(accountWithdraw);
    }

    @Test(expected = ATM.NotEnoughMoneyInATMException.class)
    public void textGetCashWithNotEnoughMoneyInATM() {
        System.out.println("getCash with not enough money in ATM");
        double ATMBalance = 20.0;
        ATM instance = new ATM(ATMBalance);
        double accountBalance = 100.0;
        double accountWithdraw = 90.0;
        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(accountBalance);
        when(account.withdraw(accountWithdraw)).thenReturn(accountWithdraw);
        Card card = mock(Card.class);
        when(card.getAccount()).thenReturn(account);
        when(card.checkPin(anyInt())).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        instance.validateCard(card, 1234);
        double result = instance.getCash(accountWithdraw);
    }

    @Test
    public void testGetCashMethodsOrderCheck() {
        System.out.println("Method order check for getCash method");
        double ATMBalance = 200.0;
        double accountBalance = 100.0;
        double accountWithdraw = 90.0;
        double accountBalanceAfter = 10.0;
        
        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(accountBalance).thenReturn(accountBalanceAfter);
        when(account.withdraw(accountWithdraw)).thenReturn(accountWithdraw);
        
        Card card = mock(Card.class);
        when(card.getAccount()).thenReturn(account);
        when(card.checkPin(anyInt())).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        
        ATM instance = new ATM(ATMBalance);
        instance.validateCard(card, 1234);
        double result = instance.getCash(accountWithdraw);
        InOrder order = inOrder(account);
        order.verify(account).getBalance();
        order.verify(account).withdraw(accountWithdraw);
    }

    @Test
    public void testAddCash() {
        System.out.println("addCash");
        double accountBalanceBefore = 900.0;
        double amountForAdddraw = 100.0;
        double accountBalanceAfter = 1000.0;
        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(accountBalanceBefore).thenReturn(accountBalanceAfter);
        when(account.adddraw(amountForAdddraw)).thenReturn(amountForAdddraw);
        
        Card card = mock(Card.class);
        when(card.getAccount()).thenReturn(account);
        when(card.checkPin(anyInt())).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        
        ATM instance = new ATM(1000.0);
        instance.validateCard(card,12345);
        double expResult = accountBalanceAfter;
        double result = instance.addCash(amountForAdddraw);
        assertEquals(expResult, result, 0.0);
    }

    @Test(expected = ATM.IncorrectMoneyException.class)
    public void testAddCashWithIncorrectMoney(){
    	System.out.println("addCash with Incorrect Money");
    	double ATMBalance = 100.0;
    	ATM instance = new ATM(ATMBalance);
    	double accountBalance = 50.0;
        double accountAdddraw = -70.0;

        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(accountBalance);
        when(account.adddraw(accountAdddraw)).thenReturn(accountAdddraw);

        Card card = mock(Card.class);
        when(card.getAccount()).thenReturn(account);
        when(card.checkPin(anyInt())).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        instance.validateCard(card, 1234);

        double result = instance.addCash(accountAdddraw);
    }

}

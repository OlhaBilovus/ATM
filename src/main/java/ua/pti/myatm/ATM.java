package ua.pti.myatm;

public class ATM {
	
	private double moneyInATM;
	private Card currentCard = null;
	
	public class NoCardInsertedException extends RuntimeException {
		
	}
	public class NotEnoughMoneyInAccountException extends RuntimeException {

	}
	public class NotEnoughMoneyInATMException extends RuntimeException {

	}
        public class IncorrectMoneyException extends RuntimeException {
        
        }
        public class CardNotValidnessException extends RuntimeException {

        }
	
        
    //Можно задавать количество денег в банкомате 
    ATM(double moneyInATM){
    	if (moneyInATM  < 0.0)
    		throw new IllegalArgumentException("Start amount should not be less than zero");
    	this.moneyInATM = moneyInATM;
    }

    // Возвращает каоличестов денег в банкомате
    public double getMoneyInATM() {
    	return this.moneyInATM;
    }
        
    //С вызова данного метода начинается работа с картой
    //Метод принимает карту и пин-код, проверяет пин-код карты и не заблокирована ли она
    //Если неправильный пин-код или карточка заблокирована, возвращаем false. При этом, вызов всех последующих методов у ATM с данной картой должен генерировать исключение NoCardInserted
    public boolean validateCard(Card card, int pinCode){
    	if (card == null)
    		throw new NoCardInsertedException();
    	
    	if (!card.checkPin(pinCode) || card.isBlocked()){
    		return false;
    	}
        this.currentCard = card;
    	return true;
    }
    
    //Возвращает сколько денег есть на счету
    public double checkBalance(){
    	if (this.currentCard == null)
    		throw new NoCardInsertedException();
    	return this.currentCard.getAccount().getBalance();
    	
    }
    
    //Метод для снятия указанной суммы
    //Метод возвращает сумму, которая у клиента осталась на счету после снятия
    //Кроме проверки счета, метод так же должен проверять достаточно ли денег в самом банкомате
    //Если недостаточно денег на счете, то должно генерироваться исключение NotEnoughMoneyInAccount 
    //Если недостаточно денег в банкомате, то должно генерироваться исключение NotEnoughMoneyInATM 
    //При успешном снятии денег, указанная сумма должна списываться со счета, и в банкомате должно уменьшаться количество денег
    public double getCash(double amount){
    	if (this.currentCard == null) 	
    		throw new NoCardInsertedException();
    	if(this.currentCard.getAccount().getBalance() < amount)
    		throw new NotEnoughMoneyInAccountException();
    	if (this.moneyInATM < amount)
    		throw new NotEnoughMoneyInATMException();
    	this.currentCard.getAccount().withdraw(amount);
    	this.moneyInATM -= amount;
    	return this.currentCard.getAccount().getBalance();
    }

    public double addCash(double amount){
        
        if (amount <= 0.0){
            throw new IncorrectMoneyException();
        }
        if (this.currentCard == null){
            throw new NoCardInsertedException();
        }   		
           this.currentCard.getAccount().adddraw(amount);
    	   this.moneyInATM += amount;
    	   return this.currentCard.getAccount().getBalance();

    }
}

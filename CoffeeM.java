import java.util.HashMap;

public class CoffeeM {
//    커피자판기
//    속성-제품이름, 제품가격, 잔액, 잔여 제품 갯수

    HashMap<String, Integer> ItemList = new HashMap<>();
    int money = 20;

//  제품 등록 리턴: void  이름: insertItem  입력값: 제품이름, 제품가격
    public void insertItem(String item, int price) {
        ItemList.put(item, price);
    }

// 제품 수정: void 이름: deleteItem 입력값: 제품이름
    public void deleteItem(String item) {
        ItemList.remove(item);
    }

//  잔액 넣기
    public int insertMoney(int money) {
        return this.money += money;
    }

// 잔액 출금
    public int withdrawMoney(int money) {
        if(this.money >= money){
            this.money -= money;
            return money;
        } else {
            money = this.money;
            this.money = 0;
            return money;
        }
    }

//
    public void sellItem(String name, int inputMoney) {
    if(ItemList.containsKey(name)){
        int price = ItemList.get(name);
        if(inputMoney >= price) {
            int moneyleft = inputMoney-price;
            this.money += inputMoney;
            this.money -= inputMoney-price;

            System.out.printf("%s 을 구입하셨습니다. 제품의 금액은 %d 입니다. 잔돈은 %d입니다.%n",name, price, moneyleft);
            System.out.printf("현재 자판기의 잔액은" + this.money + "원 입니다.");
        } else {
          System.out.println("금액이 부족합니다.");
        }
    }
    }
}

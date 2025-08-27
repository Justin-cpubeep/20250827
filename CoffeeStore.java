import java.util.Scanner;

public class CoffeeStore {
    public static void main(String[] args) {
        CoffeeM coffeeM = new CoffeeM();
//        coffeeM.insertItem(CoffeeType.CAFE_LATTE.name(), 1000);
//        coffeeM.insertItem(CoffeeType.ICE_AMERICANO.name(), 1500);
//        coffeeM.insertItem(CoffeeType.AMERICANO.name(), 1200);
        for(String itemName : coffeeM.ItemList.keySet()){
            System.out.println(itemName + ":" + coffeeM.ItemList.get(itemName));
        }
//        System.out.println(coffeeM.insertMoney(10000));
////        System.out.println(coffeeM.withdrawMoney(15000));
//        coffeeM.sellItem(CoffeeType.CAFE_LATTE.name(), 3000);
    Scanner scanner = new Scanner(System.in);
    while(true){
        System.out.printf("\n[Menu] 현재 잔액 : %d 원입니다. %n",coffeeM.money);
        System.out.println("[1] 제품등록  [2]제품목록 [3]제품삭제 [4]입금 [5]잔액출금 [6]판매 [7]종료");
        System.out.print("메뉴를 선택하세요 :  ");
        String menu = scanner.next();
        switch (menu){
            case "1":
                System.out.println("[1] 제품을 등록하겠습니다.");
                System.out.print("제품이름을 입력해주세요");
                String name = scanner.next();
                System.out.print("가격을 입력해주세요");
                int price = scanner.nextInt();
                coffeeM.insertItem(name, price);
                break;

            case "2":
                System.out.println("[2] 제품의 목록을 확인하곘습니다.");
                for(String itemName : coffeeM.ItemList.keySet()){
                    System.out.println(itemName + " : " + coffeeM.ItemList.get(itemName) + "원");
                    }
                break;

            case "3":
                System.out.println("[3] 제품을 삭제하겠습니다.");
                break;
            case "4":
                System.out.println("[4] 입금을 하겠습니다.");
                break;
            case "5":
                System.out.println("[5] 출금을 하겠습니다..");
                //test
                System.out.println("[3] 제품을 삭제하겠습니다."); //임시문구
                System.out.println("[3] 제품을 삭제하겠습니다!!!."); //임시문구
                
                break;
            case "6":
                System.out.println("[6] 판매를 하겠습니다..");
                break;
            case "7":
                System.out.println("시스템을 종료하겠습니다.");
                System.exit(0);
                break;
            default:
                break;
        }
    }
    }
}


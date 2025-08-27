import java.util.HashMap;
import java.util.Scanner;

public class RamenM {
    // HashMap to store product number and Product object
    HashMap<Integer, Product> itemList = new HashMap<>();
    int money = 2000;

    // 제품 등록
    public void insertItem(int number, String item, int price) {
        itemList.put(number, new Product(item, price));
        System.out.printf("%s (%d번)가 제품 목록에 추가되었습니다. (가격: %d원)%n", item, number, price);
    }

    // 제품 삭제
    public void deleteItem(int number) {
        if (itemList.containsKey(number)) {
            Product product = itemList.remove(number);
            System.out.printf("%s (%d번)가 제품 목록에서 삭제되었습니다.%n", product.getName(), number);
        } else {
            System.out.println("해당 번호의 제품은 존재하지 않습니다.");
        }
    }

    // 입금
    public void insertMoney(int amount) {
        this.money += amount;
        System.out.printf("%d원이 입금되었습니다. 현재 자판기 잔액: %d원%n", amount, this.money);
    }

    // 출금
    public void withdrawMoney(int amount) {
        if (this.money >= amount) {
            this.money -= amount;
            System.out.printf("%d원이 출금되었습니다. 현재 자판기 잔액: %d원%n", amount, this.money);
        } else {
            System.out.println("자판기 잔액이 부족합니다.");
        }
    }

    // 판매
    public void sellItem(int number, int inputMoney) {
        if (itemList.containsKey(number)) {
            Product product = itemList.get(number);
            String name = product.getName();
            int price = product.getPrice();

            if (inputMoney >= price) {
                int change = inputMoney - price;
                this.money += price;
                System.out.printf("%s을 구입하셨습니다. 제품의 금액은 %d원입니다. 잔돈은 %d원입니다.%n", name, price, change);
                System.out.printf("현재 자판기의 잔액은 %d원입니다.%n", this.money);
            } else {
                System.out.println("금액이 부족합니다.");
            }
        } else {
            System.out.println("해당 번호의 제품은 존재하지 않습니다.");
        }
    }

    // 자판기 메뉴 실행 메소드
    public void runVendingMachine() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.printf("\n[라면 자판기] 현재 잔액: %d원입니다. %n", this.money);
            System.out.println("[1] 제품 등록  [2] 제품 목록  [3] 입출금 관리  [4] 제품 판매  [5] 종료");
            System.out.print("메뉴를 선택하세요: ");
            String menu = scanner.next();

            switch (menu) {
                case "1":
                    System.out.print("제품 번호: ");
                    int number = scanner.nextInt();
                    System.out.print("제품 이름: ");
                    String name = scanner.next();
                    System.out.print("가격: ");
                    int price = scanner.nextInt();
                    insertItem(number, name, price);
                    break;
                case "2":
                    if (itemList.isEmpty()) {
                        System.out.println("등록된 제품이 없습니다.");
                    } else {
                        System.out.println("\n--- 제품 목록 ---");
                        for (int productNumber : itemList.keySet()) {
                            Product p = itemList.get(productNumber);
                            System.out.printf("- %d번 %s: %d원%n", productNumber, p.getName(), p.getPrice());
                        }
                        System.out.println("-----------------");
                    }
                    break;
                case "3":
                    System.out.println("\n[입출금 관리] 메뉴를 선택하세요.");
                    System.out.println("[1] 입금  [2] 출금");
                    System.out.print("선택: ");
                    String moneyMenu = scanner.next();

                    if ("1".equals(moneyMenu)) {
                        System.out.print("입금할 금액: ");
                        int deposit = scanner.nextInt();
                        insertMoney(deposit);
                    } else if ("2".equals(moneyMenu)) {
                        System.out.print("출금할 금액: ");
                        int withdraw = scanner.nextInt();
                        withdrawMoney(withdraw);
                    } else {
                        System.out.println("잘못된 선택입니다.");
                    }
                    break;
                case "4":
                    System.out.print("판매할 제품 번호: ");
                    int sellNumber = scanner.nextInt();
                    System.out.print("투입할 금액: ");
                    int inputMoney = scanner.nextInt();
                    sellItem(sellNumber, inputMoney);
                    break;
                case "5":
                    System.out.println("라면 자판기를 종료합니다.");
                    return;
                default:
                    System.out.println("잘못된 메뉴 선택입니다.");
            }
        }
    }
}

// Product 클래스를 RamenM.java 파일 안에 포함시킴
class Product {
    private String name;
    private int price;

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
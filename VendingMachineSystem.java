import java.util.Scanner;

public class VendingMachineSystem {
    public static void main(String[] args) {
        // 커피 자판기 객체 생성
        CoffeeM coffeeMachine = new CoffeeM();
        // 라면 자판기 객체 생성
        RamenM ramenMachine = new RamenM();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n[전체 시스템] 사용할 자판기를 선택하세요.");
            System.out.println("[1] 라면자판기  [2] 시스템 종료 ");
            System.out.print("선택: ");
            String choice = scanner.next();

            switch (choice) {
                case "1":
                    ramenMachine.runVendingMachine(); // 라면 자판기 메뉴 실행
                    break;
                case "2":
                    System.out.println("시스템을 종료합니다.");
                    scanner.close();
                    return;
                default:
                    System.out.println("잘못된 선택입니다. 다시 선택해주세요.");
            }
        }
    }
}
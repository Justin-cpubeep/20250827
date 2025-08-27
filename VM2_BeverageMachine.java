import java.util.*;

/**
 * VM2: 음료수 자판기
 * - 모든 변수명은 VM2_ 접두사 사용
 * - 제품 등록/목록 관리/입출금/판매/종료(복귀) 기능 포함
 * - 초기 잔고 0
 * - Main에서 VM2_BeverageMachine.run(scanner) 호출로 진입
 */
public class VM2_BeverageMachine {

    // ===== 제품 클래스 =====
    private static class VM2_Product {
        String VM2_name;
        int VM2_price;
        int VM2_stock;

        VM2_Product(String VM2_name, int VM2_price, int VM2_stock) {
            this.VM2_name = VM2_name;
            this.VM2_price = VM2_price;
            this.VM2_stock = VM2_stock;
        }
    }

    // ===== 자판기 상태 변수 =====
    private static final List<VM2_Product> VM2_products = new ArrayList<>();
    private static int VM2_balance = 0;          // 투입된 현재 잔고
    private static int VM2_cashbox = 0;          // 자판기 내부 매출 누적
    private static final int VM2_MAX_PRODUCTS = 100;

    // ===== 외부에서 진입하는 실행 메서드 =====
    public static void run(Scanner VM2_sc) {
        // 샘플 제품(원하면 삭제 가능)
        if (VM2_products.isEmpty()) {
            VM2_products.add(new VM2_Product("콜라", 1500, 5));
            VM2_products.add(new VM2_Product("사이다", 1500, 5));
            VM2_products.add(new VM2_Product("이온음료", 1800, 3));
        }

        while (true) {
            System.out.println("\n[VM2 음료수 자판기]");
            System.out.println("1) 제품 판매 모드");
            System.out.println("2) 제품 등록");
            System.out.println("3) 제품 목록 관리(수정/삭제/조회)");
            System.out.println("4) 입출금 관리(투입/거스름돈/정산)");
            System.out.println("0) 종료(메인으로 돌아가기)");
            System.out.print("선택: ");
            String VM2_sel = VM2_sc.nextLine().trim();

            switch (VM2_sel) {
                case "1": VM2_saleMode(VM2_sc); break;
                case "2": VM2_addProduct(VM2_sc); break;
                case "3": VM2_manageProducts(VM2_sc); break;
                case "4": VM2_moneyMenu(VM2_sc); break;
                case "0":
                    System.out.println("VM2 종료. 메인으로 복귀합니다.");
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }

    // ===== 판매 모드 =====
    private static void VM2_saleMode(Scanner VM2_sc) {
        while (true) {
            System.out.println("\n[판매 모드] 현재 잔고: " + VM2_balance + "원");
            VM2_printSimpleList();
            System.out.println("a) 금액 투입");
            System.out.println("r) 거스름돈 반환(잔고 비움)");
            System.out.println("0) 이전 메뉴");
            System.out.print("구매할 제품 번호를 선택하거나 메뉴 선택: ");
            String VM2_in = VM2_sc.nextLine().trim();

            if (VM2_in.equals("0")) return;
            if (VM2_in.equalsIgnoreCase("a")) {
                VM2_deposit(VM2_sc);
                continue;
            }
            if (VM2_in.equalsIgnoreCase("r")) {
                VM2_refund();
                continue;
            }

            // 숫자 선택 → 구매 시도
            try {
                int VM2_idx = Integer.parseInt(VM2_in);
                if (VM2_idx < 1 || VM2_idx > VM2_products.size()) {
                    System.out.println("번호 범위를 확인하세요.");
                    continue;
                }
                VM2_buy(VM2_idx - 1);
            } catch (NumberFormatException e) {
                System.out.println("숫자 또는 메뉴 문자를 입력하세요.");
            }
        }
    }

    private static void VM2_buy(int VM2_idx) {
        VM2_Product VM2_p = VM2_products.get(VM2_idx);
        if (VM2_p.VM2_stock <= 0) {
            System.out.println("재고가 없습니다.");
            return;
        }
        if (VM2_balance < VM2_p.VM2_price) {
            System.out.println("잔고가 부족합니다. 금액을 더 투입하세요.");
            return;
        }
        VM2_p.VM2_stock -= 1;
        VM2_balance -= VM2_p.VM2_price;
        VM2_cashbox += VM2_p.VM2_price;
        System.out.println("구매 완료: " + VM2_p.VM2_name + " (-1). 남은 잔고: " + VM2_balance + "원");
    }

    // ===== 제품 등록 =====
    private static void VM2_addProduct(Scanner VM2_sc) {
        if (VM2_products.size() >= VM2_MAX_PRODUCTS) {
            System.out.println("제품 등록 한도에 도달했습니다.");
            return;
        }
        System.out.println("\n[제품 등록]");
        System.out.print("이름: ");
        String VM2_name = VM2_sc.nextLine().trim();
        if (VM2_name.isEmpty()) {
            System.out.println("이름은 비어 있을 수 없습니다.");
            return;
        }
        if (VM2_findByName(VM2_name) != -1) {
            System.out.println("이미 같은 이름의 제품이 있습니다.");
            return;
        }
        int VM2_price = VM2_inputPositiveInt(VM2_sc, "가격(원): ");
        int VM2_stock = VM2_inputNonNegativeInt(VM2_sc, "재고(개): ");

        VM2_products.add(new VM2_Product(VM2_name, VM2_price, VM2_stock));
        System.out.println("등록 완료.");
    }

    // ===== 제품 목록 관리(수정/삭제/조회) =====
    private static void VM2_manageProducts(Scanner VM2_sc) {
        while (true) {
            System.out.println("\n[제품 목록 관리]");
            VM2_printFullList();
            System.out.println("1) 가격 수정");
            System.out.println("2) 재고 수정(덧셈/설정)");
            System.out.println("3) 제품 삭제");
            System.out.println("0) 이전 메뉴");
            System.out.print("선택: ");
            String VM2_sel = VM2_sc.nextLine().trim();

            switch (VM2_sel) {
                case "1":
                    int VM2_idx1 = VM2_selectIndex(VM2_sc);
                    if (VM2_idx1 == -1) break;
                    int VM2_newPrice = VM2_inputPositiveInt(VM2_sc, "새 가격(원): ");
                    VM2_products.get(VM2_idx1).VM2_price = VM2_newPrice;
                    System.out.println("가격 수정 완료.");
                    break;
                case "2":
                    int VM2_idx2 = VM2_selectIndex(VM2_sc);
                    if (VM2_idx2 == -1) break;
                    System.out.print("모드 선택 (+ 덧셈 / = 설정): ");
                    String VM2_mode = VM2_sc.nextLine().trim();
                    if (VM2_mode.equals("+")) {
                        int VM2_add = VM2_inputNonNegativeInt(VM2_sc, "추가할 수량: ");
                        VM2_products.get(VM2_idx2).VM2_stock += VM2_add;
                        System.out.println("재고 추가 완료.");
                    } else if (VM2_mode.equals("=")) {
                        int VM2_set = VM2_inputNonNegativeInt(VM2_sc, "설정할 수량: ");
                        VM2_products.get(VM2_idx2).VM2_stock = VM2_set;
                        System.out.println("재고 설정 완료.");
                    } else {
                        System.out.println("잘못된 모드입니다.");
                    }
                    break;
                case "3":
                    int VM2_idx3 = VM2_selectIndex(VM2_sc);
                    if (VM2_idx3 == -1) break;
                    VM2_Product VM2_removed = VM2_products.remove(VM2_idx3);
                    System.out.println("삭제됨: " + VM2_removed.VM2_name);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }

    // ===== 입출금 관리 =====
    private static void VM2_moneyMenu(Scanner VM2_sc) {
        while (true) {
            System.out.println("\n[입출금 관리]");
            System.out.println("현재 잔고: " + VM2_balance + "원, 자판기 매출(금고): " + VM2_cashbox + "원");
            System.out.println("1) 금액 투입");
            System.out.println("2) 거스름돈 반환(잔고 비움)");
            System.out.println("3) 매출 정산(금고를 0으로)");
            System.out.println("0) 이전 메뉴");
            System.out.print("선택: ");
            String VM2_sel = VM2_sc.nextLine().trim();

            switch (VM2_sel) {
                case "1": VM2_deposit(VM2_sc); break;
                case "2": VM2_refund(); break;
                case "3":
                    System.out.println("정산 완료. " + VM2_cashbox + "원을 출금하여 0으로 초기화합니다.");
                    VM2_cashbox = 0;
                    break;
                case "0": return;
                default: System.out.println("잘못된 입력입니다.");
            }
        }
    }

    private static void VM2_deposit(Scanner VM2_sc) {
        int VM2_amount = VM2_inputPositiveInt(VM2_sc, "투입할 금액(원): ");
        VM2_balance += VM2_amount;
        System.out.println("투입 완료. 현재 잔고: " + VM2_balance + "원");
    }

    private static void VM2_refund() {
        if (VM2_balance <= 0) {
            System.out.println("반환할 잔고가 없습니다.");
            return;
        }
        System.out.println("거스름돈 반환: " + VM2_balance + "원");
        VM2_balance = 0;
    }

    // ===== 유틸 =====
    private static void VM2_printSimpleList() {
        if (VM2_products.isEmpty()) {
            System.out.println("(등록된 제품이 없습니다.)");
            return;
        }
        System.out.println("번호 | 이름 | 가격 | 재고");
        for (int VM2_i = 0; VM2_i < VM2_products.size(); VM2_i++) {
            VM2_Product VM2_p = VM2_products.get(VM2_i);
            System.out.printf("%d) %s | %d원 | %d개%n",
                    VM2_i + 1, VM2_p.VM2_name, VM2_p.VM2_price, VM2_p.VM2_stock);
        }
    }

    private static void VM2_printFullList() {
        System.out.println("=== 제품 목록 ===");
        VM2_printSimpleList();
    }

    private static int VM2_selectIndex(Scanner VM2_sc) {
        if (VM2_products.isEmpty()) {
            System.out.println("제품이 없습니다.");
            return -1;
        }
        VM2_printSimpleList();
        System.out.print("대상 제품 번호: ");
        try {
            int VM2_idx = Integer.parseInt(VM2_sc.nextLine().trim()) - 1;
            if (VM2_idx < 0 || VM2_idx >= VM2_products.size()) {
                System.out.println("번호 범위를 확인하세요.");
                return -1;
            }
            return VM2_idx;
        } catch (NumberFormatException e) {
            System.out.println("숫자를 입력하세요.");
            return -1;
        }
    }

    private static int VM2_findByName(String VM2_name) {
        for (int VM2_i = 0; VM2_i < VM2_products.size(); VM2_i++) {
            if (VM2_products.get(VM2_i).VM2_name.equalsIgnoreCase(VM2_name)) return VM2_i;
        }
        return -1;
    }

    private static int VM2_inputPositiveInt(Scanner VM2_sc, String VM2_msg) {
        while (true) {
            System.out.print(VM2_msg);
            String VM2_s = VM2_sc.nextLine().trim();
            try {
                int VM2_v = Integer.parseInt(VM2_s);
                if (VM2_v > 0) return VM2_v;
                System.out.println("0보다 큰 값을 입력하세요.");
            } catch (NumberFormatException e) {
                System.out.println("정수를 입력하세요.");
            }
        }
    }

    private static int VM2_inputNonNegativeInt(Scanner VM2_sc, String VM2_msg) {
        while (true) {
            System.out.print(VM2_msg);
            String VM2_s = VM2_sc.nextLine().trim();
            try {
                int VM2_v = Integer.parseInt(VM2_s);
                if (VM2_v >= 0) return VM2_v;
                System.out.println("0 이상 값을 입력하세요.");
            } catch (NumberFormatException e) {
                System.out.println("정수를 입력하세요.");
            }
        }
    }
}

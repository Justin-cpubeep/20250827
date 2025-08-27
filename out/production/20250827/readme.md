아래는 업로드하신 \*\*CoffeeM.java / CoffeeStore.java\*\*를 직접 확인한 뒤(소스: 업로드 파일) 정리한 \*\*현 상태 분석 + 보완·수정 설계 + 3인 업무분담안\*\*입니다.

요구사항(① 재고 필수 ② 잔돈은 즉시 반환 ③ 초기 목록 하드코딩 ④ 테스트 생략)을 반영했습니다.



---



\# 1) 현 상태 분석(현재 코드 기준)



\## 1.1 CoffeeM.java



\* 보유 필드



&nbsp; \* `HashMap<String, Integer> ItemList` : \*\*제품명 → 가격\*\*만 저장(재고 없음).

&nbsp; \* `int money = 20` : 자판기의 보유 현금(금고, cash box)처럼 사용하려 한 흔적.

\* 구현 함수



&nbsp; \* `insertItem(String item, int price)` : 제품/가격 등록.

&nbsp; \* `deleteItem(String item)` : \*\*미완성(본문에 `...`)\*\*.

&nbsp; \* 판매 관련 로직 일부 단편 존재(미완성):

&nbsp;   `inputMoney >= price` 비교 후 “잔돈” 메시지 출력, `this.money` 더했다 빼는 \*\*불일치 연산\*\* 존재.

\* 주요 문제



&nbsp; \* \*\*컴파일 불가\*\*: 중간에 `...`(ellipsis), 중괄호 정합성 불량.

&nbsp; \* \*\*재고(stock) 미구현\*\*: 요구사항과 괴리.

&nbsp; \* \*\*금고(cash box) vs 사용자 투입액(session balance) 구분 없음\*\* → 잔돈 처리 모호.

&nbsp; \* 캡슐화 미흡: `ItemList`가 패키지-프라이빗, 구조가 가격만 보유.



> 근거: 업로드된 CoffeeM.java 원문(중간 `...`, 가격만 보관, 잔돈 출력 로직 일부).



\## 1.2 CoffeeStore.java



\* `main`에서 `CoffeeM` 인스턴스 생성.

\* 과거 시도 흔적: `CoffeeType` enum 기반 등록/판매 주석(실제 enum 부재).

\* `Scanner` 기반 메뉴 루프 \*\*초안\*\*이 있으나, 중간에 `...`로 \*\*미완\*\*이며 각 메뉴 처리도 대부분 \*\*표시만 하고 동작 없음\*\*.

\* 프로그램 종료(메뉴 7)만 정상 동작.



> 근거: 업로드된 CoffeeStore.java 원문(메뉴 스켈레톤, 중간 `...`, 동작 미연결).



---



\# 2) 요구사항 반영 설계(추가·수정이 필요한 내용)



\## 2.1 자료구조/도메인



\* `Item`(제품) 도입 권장



&nbsp; \* 필드: `name`, `price`, `stock`

&nbsp; \* 자판기 내부 저장: `Map<String, Item> items`

\* \*\*세션 잔액(session balance)\*\* 과 \*\*자판기 금고(cash box)\*\* 분리



&nbsp; \* `int cashBox` : 자판기가 보유한 돈(판매로 증가).

&nbsp; \* `int sessionBalance` : 사용자가 “이번 구매 세션” 동안 투입한 금액.



\## 2.2 기능 정의



\* 제품 관리



&nbsp; \* `insertItem(String name, int price, int stock)` : 추가(초기 목록은 \*\*하드코딩\*\*으로 seed).

&nbsp; \* `updateItemPrice(String name, int newPrice)` : 가격 변경.

&nbsp; \* `restock(String name, int addQty)` : 재고 보충(+).

&nbsp; \* `removeItem(String name)` : 삭제.

&nbsp; \* `listItems()` : 이름/가격/재고 조회.

\* 결제/판매



&nbsp; \* `insertMoney(int amount)` : \*\*sessionBalance\*\* 증가(검증: 음수/0 불가).

&nbsp; \* `sell(String name)` : \*\*즉시 거스름돈 반환\*\* 정책



&nbsp;   1. `name` 등록 여부/재고>0/세션잔액≥가격 검증

&nbsp;   2. 판매 성공 → `stock - 1`, `cashBox += price`, `sessionBalance -= price`

&nbsp;   3. \*\*잔돈(change) = sessionBalance\*\* 즉시 계산·반환 후 `sessionBalance = 0`

&nbsp; \* (선택) `getSessionBalance()` : 잔액 조회(UX 메시지 용).

\* 조회/출력



&nbsp; \* `printMenu()` 수준의 UI는 `CoffeeStore`가 담당, `CoffeeM`는 로직 전담.

\* 예외/검증 (Validation)



&nbsp; \* 미등록/품절/부족금액/음수·0원 입력 금지/오버플로 방지 등.



\## 2.3 콘솔 UI 흐름(CoffeeStore)



\* 시작 시 \*\*하드코딩 seed\*\* 등록: 예) 아메리카노(1200/재고5), 라떼(1500/재고3) 등.

\* 메뉴(예시)



&nbsp; 1. 제품 등록

&nbsp; 2. 제품 목록 조회

&nbsp; 3. 제품 삭제

&nbsp; 4. 금액 투입

&nbsp; 5. 가격 변경/재고 보충(선택: 메뉴 분리 가능)

&nbsp; 6. 판매(제품 선택 → 즉시 잔돈 반환)

&nbsp; 7. 종료

\* 입력 검증: 숫자 이외 입력 시 재요청, 음수/0 거부, 존재하지 않는 제품명 방지.

\* 리소스: `Scanner` 마지막에 `close()`.



---



\# 3) 즉시 수정(Blocking) 체크리스트



\* \[ ] 두 파일의 `...`(ellipsis) 제거, \*\*중괄호 정합성\*\* 복구 → \*\*컴파일 우선 확보\*\*.

\* \[ ] `CoffeeM`에 `Item` 도입 및 \\\*\\\*Map\\<String, Item>\\\*\\\*로 교체.

\* \[ ] `cashBox` vs `sessionBalance` 분리(필드 추가).

\* \[ ] `sell()`에서 \*\*즉시 잔돈 반환\*\* 로직 완성(판매 후 `change=sessionBalance` 반환, `sessionBalance=0`).

\* \[ ] `insertItem(name, price, stock)` 서명 확정 및 검증(음수/0 금지).

\* \[ ] `CoffeeStore`의 메뉴 실제 동작 연결(각 case → `CoffeeM` 메서드 호출).

\* \[ ] `ItemList`(혹은 `items`) \*\*private\*\* 전환 및 캡슐화(접근은 메서드로).

\* \[ ] 콘솔 메시지(오류·성공) 일관성 정리(한글/영문 병기 선택).



---



\# 4) 팀 업무 분담(3인 기준) + Git/GitHub 운영



\## 4.1 브랜치 전략(수업 친화형)



\* `main`: 항상 실행 가능한 상태 유지

\* `feature/\*\*`: 작업 단위별 브랜치(작게 쪼개 PR)

&nbsp; 예) `feature/domain-item-and-stock`, `feature/sell-and-change`, `feature/cli-menu`



\## 4.2 역할 및 담당 업무



\### 👤 A — \*\*도메인/코어 로직(아이템·재고·판매)\*\*



\* \[ ] `Item` 클래스 추가(`name`, `price`, `stock`).

\* \[ ] `Map<String, Item>`로 자판기 내부 구조 교체.

\* \[ ] `insertItem/updateItemPrice/restock/removeItem` 완성(검증 포함).

\* \[ ] `sell(String name)` 구현: \*\*즉시 잔돈 반환\*\* 정책대로 cash 흐름 확정.

\* \[ ] `cashBox`, `sessionBalance` 필드 및 Getter·검증 로직.



브랜치: `feature/domain-core`



\### 👤 B — \*\*CLI/입력 검증/초기 시드\*\*



\* \[ ] `CoffeeStore` 메뉴 루프 완성(각 case → `CoffeeM` 호출).

\* \[ ] 사용자 입력 검증(숫자/문자, 범위, 존재 유효성).

\* \[ ] 시작 시 \*\*하드코딩 시드\*\* 등록(예: 3\\~5개 품목).

\* \[ ] 출력 메시지 통일(가격/재고/잔돈/에러 포맷).



브랜치: `feature/cli-ui`



\### 👤 C — \*\*정돈/리팩터링/품질(테스트 생략 기준)\*\*



\* \[ ] 캡슐화(필드 `private`, 필요 접근자 제공) 확인.

\* \[ ] 경계 케이스 수동 점검표 작성(품절/부족금액/미등록/음수/0).

\* \[ ] NPE·IllegalArgument 방지 가드 추가.

\* \[ ] 주석/Javadoc(간단) 정리, 메시지 한국어 기준 통일.



브랜치: `feature/refactor-quality`



\## 4.3 공통 Git 규칙(간단)



\* 이슈 생성 → 브랜치 파생 → 작은 커밋(무엇/왜 중심) → PR → 상호 리뷰 → 머지.

\* 커밋 메시지 예:



&nbsp; \* `feat: 판매 시 즉시 잔돈 반환 로직 구현`

&nbsp; \* `fix: 음수 가격/투입금 차단`

&nbsp; \* `refactor: Item 구조 도입 및 Map 교체`



---



\# 5) 상세 체크리스트(완료 기준)



\## 5.1 제품/재고



\* \[ ] `insertItem(name, price, stock)` (검증 포함)

\* \[ ] `updateItemPrice(name, newPrice)`

\* \[ ] `restock(name, addQty)`

\* \[ ] `removeItem(name)`

\* \[ ] `listItems()` (이름/가격/재고 표기)



\## 5.2 결제/판매(즉시 잔돈 정책)



\* \[ ] `insertMoney(amount)` → `sessionBalance += amount`

\* \[ ] `sell(name)` → 검증(등록·재고·잔액) → `stock--`, `cashBox+=price`, `sessionBalance-=price`

\* \[ ] \*\*잔돈 = sessionBalance\*\* 즉시 계산·출력 후 `sessionBalance=0`

\* \[ ] 에러 메시지: \*\*미등록/품절/부족금액/부적절 입력\*\* 모두 케이스별 안내



\## 5.3 UI/입출력



\* \[ ] 메뉴 1\\~7 완전 동작(문자/숫자 입력 검증)

\* \[ ] 시작 시 시드 3\\~5개 주입(하드코딩)

\* \[ ] 종료 시 스캐너 `close()`



---



\# 6) 권장 구현 순서(마일스톤)



1\. \*\*컴파일 복구\*\*: `...` 제거, 중괄호/임포트 정리(두 파일 전부).

2\. \*\*도메인 확정\*\*: `Item` + `Map<String, Item>`, `cashBox/sessionBalance` 분리.

3\. \*\*판매/잔돈(즉시 반환) 로직\*\* 완성.

4\. \*\*CLI 연결\*\*: 하드코딩 시드 + 메뉴 액션 전부 연결.

5\. \*\*정리\*\*: 예외/메시지/주석/간단 리팩터링.



---



\## 참고(용어 병기)



\* 재고: \*\*stock\*\*

\* 금고: \*\*cash box\*\*

\* 세션 잔액: \*\*session balance\*\*

\* 즉시 잔돈 반환: \*\*immediate change return\*\*

\* 캡슐화: \*\*encapsulation\*\*

\* 입력 검증: \*\*input validation\*\*



---



필요하시면 위 체크리스트를 이슈 템플릿/PR 템플릿으로 바로 변환해 드릴 수 있습니다.

또, 다음 단계에서 ‘메서드 시그니처 표(파라미터/반환/예외)’와 ‘메뉴 동작 흐름도(ASCII)’도 제공해 드릴게요.




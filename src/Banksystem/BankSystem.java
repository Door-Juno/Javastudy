import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class Account {
    private String accountNumber; // 계좌번호 추가
    private String owner;
    private double balance;

    // 🔹 계좌 생성 시 계좌번호 자동 생성
    public Account(String owner, double initialDeposit) {
        this.accountNumber = generateAccountNumber();
        this.owner = owner;
        this.balance = initialDeposit;
    }

    // 🔹 계좌번호 포함하는 생성자 (파일에서 불러올 때 사용)
    public Account(String accountNumber, String owner, double balance) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = balance;
    }

    // 🔹 계좌번호 자동 생성 (랜덤 8자리 숫자)
    private String generateAccountNumber() {
        Random rand = new Random();
        return String.format("%08d", rand.nextInt(100000000)); // 8자리 숫자로 생성
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        System.out.println(amount + "원이 입금되었습니다.");
    }

    public void withdraw(double amount) {
        if (amount > balance) {
            System.out.println("잔액이 부족합니다.");
        } else {
            balance -= amount;
            System.out.println(amount + "원이 출금되었습니다.");
        }
    }

    public void showBalance() {
        System.out.println("계좌번호: " + accountNumber + " | " + owner + "님의 잔액: " + balance + "원");
    }

    // 🔹 계좌 정보를 파일에 저장할 문자열 형태로 변환
    public String toFileString() {
        return accountNumber + "," + owner + "," + balance;
    }

    // 🔹 파일에서 불러올 때 문자열을 Account 객체로 변환
    public static Account fromFileString(String data) {
        String[] parts = data.split(",");
        return new Account(parts[0], parts[1], Double.parseDouble(parts[2]));
    }
}

public class BankSystem {
    private static final String FILE_PATH = "/Users/juno/Documents/GitHub/Javastudy/src/Banksystem/"; // 원하는 저장 경로
    private static final String FILE_NAME = FILE_PATH + "accounts.txt"; // 파일 경로

    private static ArrayList<Account> accounts = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadAccountsFromFile(); // 프로그램 시작 시 계좌 불러오기

        while (true) {
            System.out.println("\n1. 계좌 개설  2. 입금  3. 출금  4. 잔액 조회  5. 종료");
            System.out.print("선택: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    depositMoney();
                    break;
                case 3:
                    withdrawMoney();
                    break;
                case 4:
                    checkBalance();
                    break;
                case 5:
                    saveAccountsToFile(); // 종료 전에 파일 저장
                    System.out.println("프로그램 종료");
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }

    // ✅ 계좌 개설 (이름 입력 → 계좌번호 자동 생성)
    private static void createAccount() {
        System.out.print("이름: ");
        String name = scanner.next();
        System.out.print("초기 입금액: ");
        double deposit = scanner.nextDouble();

        Account newAccount = new Account(name, deposit);
        accounts.add(newAccount);

        System.out.println(name + "님의 계좌가 개설되었습니다.");
        System.out.println("계좌번호: " + newAccount.getAccountNumber());
    }

    // ✅ 계좌번호로 계좌 찾기
    private static Account findAccountByNumber(String accountNumber) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accountNumber)) {
                return acc;
            }
        }
        return null;
    }

    // ✅ 입금
    private static void depositMoney() {
        System.out.print("계좌번호: ");
        String accountNumber = scanner.next();
        Account acc = findAccountByNumber(accountNumber);
        if (acc != null) {
            System.out.print("입금할 금액: ");
            double amount = scanner.nextDouble();
            acc.deposit(amount);
        } else {
            System.out.println("계좌를 찾을 수 없습니다.");
        }
    }

    // ✅ 출금
    private static void withdrawMoney() {
        System.out.print("계좌번호: ");
        String accountNumber = scanner.next();
        Account acc = findAccountByNumber(accountNumber);
        if (acc != null) {
            System.out.print("출금할 금액: ");
            double amount = scanner.nextDouble();
            acc.withdraw(amount);
        } else {
            System.out.println("계좌를 찾을 수 없습니다.");
        }
    }

    // ✅ 잔액 조회
    private static void checkBalance() {
        System.out.print("계좌번호: ");
        String accountNumber = scanner.next();
        Account acc = findAccountByNumber(accountNumber);
        if (acc != null) {
            acc.showBalance();
        } else {
            System.out.println("계좌를 찾을 수 없습니다.");
        }
    }

    // ✅ 파일 저장
    private static void saveAccountsToFile() {
        File directory = new File(FILE_PATH);
        if (!directory.exists()) {
            directory.mkdirs(); // 폴더 없으면 생성
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Account acc : accounts) {
                writer.write(acc.toFileString());
                writer.newLine();
            }
            System.out.println("계좌 정보가 파일에 저장되었습니다.");
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류 발생: " + e.getMessage());
        }
    }

    // ✅ 파일 불러오기
    private static void loadAccountsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("저장된 계좌 정보가 없습니다. 새로 시작합니다.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                accounts.add(Account.fromFileString(line));
            }
            System.out.println("파일에서 계좌 정보를 불러왔습니다.");
        } catch (IOException e) {
            System.out.println("파일 읽기 중 오류 발생: " + e.getMessage());
        }
    }
}

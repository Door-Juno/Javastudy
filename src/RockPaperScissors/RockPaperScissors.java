import java.util.Random;
import java.util.Scanner;

public class RockPaperScissors {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int result[] = {0,0,0};

        String choice[] = {"가위","바위","보"};

        while (true){
            System.out.println("가위,바위,보 중 하나 내세요.(exit로 나가기) : ");
            String user = scanner.next();

            if(user.equals("exit")){
                break;
            }

            int computerIndex = random.nextInt(3);
            String computer = choice[computerIndex];

            System.out.println("컴퓨터는 "+ computer + "를 냈습니다.");

            if(user.equals(computer)){
                System.out.println("무승부입니다.");
                result[1]++;
            }
            else if (
                (user.equals("가위") && computer.equals("보")) ||
                (user.equals("바위") && computer.equals("가위")) ||
                (user.equals("보") && computer.equals("바위"))
                ){
                    System.out.println("당신이 이겼습니다! ");
                    result[0] ++;
            }
            else{
                System.out.println("당신이 패배했습니다!");
                result[2] ++ ;
            }
            System.out.println("당신의 전적은 "+result[0]+"승 "+result[1] + "무 "+ result[2] + "패 입니다.");
        }
        scanner.close();
    }
}
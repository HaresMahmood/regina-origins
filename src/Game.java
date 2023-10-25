import java.util.Scanner;

public class Game {
    // needs a board at some point


    public void play(){
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("Where would you like to move next? (Enter quit to exit): ");
            String userInput = scanner.nextLine().toLowerCase();
            
            if(userInput.equals("quit")){
                break;
            }
        }

        scanner.close();
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}

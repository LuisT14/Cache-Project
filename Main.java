import java.util.Scanner;

/**
 * Cache Project CSCE 312
 * @author Luis Trejo
 * @author Jared Miscisin <- that's me
*/
class Main { 
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        String command;
        String command_substring;
        boolean invalid_input = false;

        do {
            System.out.println("*** Cache simulator menu ***");
            System.out.println("type one command:");
            System.out.println("1. cache-read");
            System.out.println("2. cache-write");
            System.out.println("3. cache-flush");
            System.out.println("4. cache-view");
            System.out.println("5. memory-view");
            System.out.println("6. cache-dump");
            System.out.println("7. memory-dump");
            System.out.println("8. quit");
            System.out.println("****************************");

            command = userInput.next();

            switch (command) {
                case "quit":
                    System.out.println("Exiting program");
                    break;
                case "memory-dump":
                    System.out.println("temporary memory-dump");
                    break;
                case "cache-dump":
                    System.out.println("temporary cache-dump");
                    break;
                case "memory-view":
                    System.out.println("temporary memory-view");
                    break;
                case "cache-view":
                    System.out.println("temporary cache-view");
                    break;
                case "cache-flush":
                    System.out.println("temporary cache-flush");
                    break;
                default:
                    if (command.length() >= 11) {
                        command_substring = command.substring(0, 11);
                        if (command_substring.equals("cache-read ")) {
                            System.out.println("temporary cache-read");
                            break;
                        } else if (command_substring.equals("cache-write")) {
                            System.out.println("temporary cache-write");
                            break;
                        }
                    }
                    break;
            }

            if (invalid_input) {
                System.out.println("Invalid input");
                invalid_input = false;
            }

        } while (!command.equals("quit"));
    }
}

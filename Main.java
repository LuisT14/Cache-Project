import java.util.Scanner;

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

// bruh bruh so wanna make it object oriented to the max?
// how so 
// templates for each one of policies/stuff? althout some are simple, wait give me a couple of min
// templates scare me but if you think it'll make some of the stuff easier then go ahead
// looking at the req, most likely not. it will be simple.
// Hmm, im not sure how to approach this, i think it will build as we build 
// I saw that we had to implement everything that's in the menu i already made but that's about it for now. so we can start with that because that doesn't look that bad
// I FORGOT about doxygen. F, i think for that we must document as we go
// what does that even mean. i didn't understand that stuff lol
// hmm, i hope i dont kill anything
// explain
// no idea i tried to download packages, idk what does

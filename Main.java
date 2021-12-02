import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;

/**
 * Cache Project CSCE 312
 * 
 * @author Luis Trejo
 * @author Jared Miscisin
 */
class Main {
  public static void main(String[] args) {
    boolean hasfile = false;

    // Command line input
    //System.out.println("*** Welcome to the cache simulator ***");
    //System.out.println("initialize the RAM:");
    String filename = "";
    if (args.length > 0) {
      filename = args[0];
      File f = new File(filename);
      if (f.exists()) {
        hasfile = true;
      } else
        System.out.println("error: file does not exist");
    } else
      System.err.println("error: no file name in commmand line");

    if (hasfile) {
      CacheConfigObj config = new CacheConfigObj();
      config.start();
      CacheController CC = new CacheController(config, filename);

      // Menu Loop
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
            CC.MemoryDump();
            break;
          case "cache-dump":
            System.out.println("temporary cache-dump");
            break;
          case "memory-view":
            CC.MemoryView();
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
}

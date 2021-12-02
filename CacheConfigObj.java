import java.util.Scanner;

/**
 * CacheConfigObj Class Sets up the Cache options at the beginning of the
 * program
 */
public class CacheConfigObj {

    protected int cacheSizeBytes;
    protected int blockBytes;
    protected int associativity;
    protected int replacementPolicy;
    protected int writeHitPolicy;
    protected int writeMissPolicy;
    protected int RAM;

    /**
     * Default Constructor for {@link CacheConfigObj}
     */
    public CacheConfigObj() {
    }

    /**
     * Collect Data from user to parse and check
     */
    public void start() {
        Scanner theSetup = new Scanner(System.in);
        System.out.println("*** Welcome to the cache simulator ***\ninitialize the RAM:");
        String ramString = theSetup.nextLine();
        parseRam(ramString);
        System.out.println("configure the cache: ");
        System.out.print("cache size: ");
        cacheSizeBytes = theSetup.nextInt();
        System.out.print("data block size: ");
        blockBytes = theSetup.nextInt();
        System.out.print("associativity: ");
        associativity = theSetup.nextInt();
        System.out.print("replacement policy: ");
        replacementPolicy = theSetup.nextInt();
        System.out.print("write hit policy: ");
        writeHitPolicy = theSetup.nextInt();
        System.out.print("write miss policy: ");
        writeMissPolicy = theSetup.nextInt();
        System.out.println("cache successfully configured!");
    }

    /**
     * Parse the ram string given by the user. Will save resuts to RAM in object
     * 
     * @param String input string given by the user
     */
    public void parseRam(String s) {
        String[] splitoff = s.split(" ");
        if (splitoff.length == 3) {
            if (splitoff[0].equals("ram-init")) {
                int s1 = Integer.parseInt(splitoff[1].substring(2), 16);
                int s2 = Integer.parseInt(splitoff[2].substring(2), 16);
                RAM = (s2 - s1)+1;
                System.out.println("RAM successfully initialized!");
            }
        }
    }

    /**
     * Getter for Cache Size Bytes
     * 
     * @return int (bytes)
     */
    public int GetCacheSizeBytes() {
        return cacheSizeBytes;
    }

    /**
     * Getter for Block Bytes
     * 
     * @return int (bytes)
     */
    public int GetBlockBytes() {
        return blockBytes;
    }

    /**
     * Getter for Associativity
     * 
     * @return int (1-way, 2-way, 4-way)
     */
    public int GetAssociativity() {
        return associativity;
    }

    /**
     * Getter for replacement Policy
     * 
     * @return int (1 or 2)
     */
    public int GetReplacementPolicy() {
        return replacementPolicy;
    }

    /**
     * Getter for write hit policy
     * 
     * @return int (1 or 2)
     */
    public int GetWriteHitPolicy() {
        return writeHitPolicy;
    }

    /**
     * Getter for write miss policy
     * 
     * @return int (1 or 2)
     */
    public int GetWriteMissPolicy() {
        return writeMissPolicy;
    }

    /**
     * Getter for RAM size
     * 
     * @return int RAM
     */
    public int GetRamSize() {
        return RAM;
    }
}
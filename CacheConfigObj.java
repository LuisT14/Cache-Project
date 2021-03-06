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
        while(!parseRam(ramString)) {ramString = theSetup.nextLine();}
        System.out.println("configure the cache: ");

        System.out.print("cache size: ");
        cacheSizeBytes = Integer.parseInt(theSetup.nextLine());

        System.out.print("data block size: ");
        blockBytes =  Integer.parseInt(theSetup.nextLine());

        System.out.print("associativity: ");
        associativity = Integer.parseInt(theSetup.nextLine());
        while(associativity !=1 && associativity !=2 && associativity !=4) {System.out.print("ERROR: invalid associativity\nassociativity: "); associativity =  Integer.parseInt(theSetup.nextLine());}

        System.out.print("replacement policy: ");
        replacementPolicy = Integer.parseInt(theSetup.nextLine());
        while(replacementPolicy > 2 || replacementPolicy < 1) {System.out.print("ERROR: invalid replacement Policy\nreplacement policy: "); replacementPolicy =  Integer.parseInt(theSetup.nextLine());}

        System.out.print("write hit policy: ");
        writeHitPolicy =  Integer.parseInt(theSetup.nextLine());
        while(writeHitPolicy > 2 || writeHitPolicy < 1) {System.out.print("ERROR: invalid write hit Policy\nrwrite hit policy: "); writeHitPolicy =  Integer.parseInt(theSetup.nextLine());}

        System.out.print("write miss policy: ");
        writeMissPolicy =  Integer.parseInt(theSetup.nextLine());
        while(writeMissPolicy > 2 || writeMissPolicy < 1) {System.out.print("ERROR: invalid write miss Policy\nwrite miss policy: "); writeMissPolicy =  Integer.parseInt(theSetup.nextLine());}

        System.out.println("cache successfully configured!");
    }

    /**
     * Parse the ram string given by the user. Will save resuts to RAM in object
     * 
     * @param String input string given by the user
     */
    public boolean parseRam(String s) {
        String[] splitoff = s.split(" ");
        if (splitoff.length == 3) {
            if (splitoff[0].equals("init-ram")) {
                int s1 = Integer.parseInt(splitoff[1].substring(2), 16);
                int s2 = Integer.parseInt(splitoff[2].substring(2), 16);
                RAM = (s2 - s1)+1;
                if(RAM <= 256){
                  System.out.println("RAM successfully initialized!");
                  return true;
                }
            }
        }
        System.out.println("ERROR: invalid RAM argument");
        return false;
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
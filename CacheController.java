import java.io.File; 
import java.io.FileNotFoundException;  
import java.util.Scanner;
import java.io.FileInputStream;  
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Cache Controller does all the actions It uses the {@link CacheConfigObj} to
 * initilize all values
 */
public class CacheController {

    /**
     * Internal Cache
     * <p>
     * The internal data structured used by the controller to manage the cache data
     */
    private class internalCache {
        public class CacheLine {
            public CacheLine(int num){
              Block = new String[num];
            }
            public boolean validBit;
            public String tag;
            public String[] Block;
        }

        public CacheLine[][] Set;

        /**
         * Constructor for Internal Cache
         * 
         * @param S number of Sets
         * @param E number of Lines
         * @param B number of Block (bytes)
         */
        public internalCache(int S, int E, int B) {
            Set = new CacheLine[S][E];
            for(int i = 0; i < S; i++){
              for(int j = 0; j < E; j++){
                Set[i][j] = new CacheLine(B);
              }
            }
        }
    }

    /**
     * Enum for different options
     */
    private enum ReplacmentOption {
        NONE, RANDOM_REPLACEMENT, LEAST_RECENTLY_USE
    }

    private enum WriteHitOption {
        NONE, WRITE_THROUGH, WRITE_BACK
    }

    private enum WriteMissOption {
        NONE, WRITE_ALLOCATE, NO_WRITE_ALLOCATE
    }

    // Various Variables
    private int cacheSize;
    private int dataBlockSize;
    private int associativity;
    private int ramSize;
    private int setSize;
    private ReplacmentOption replacementPolicy;
    private WriteHitOption writeHitPolicy;
    private WriteMissOption writeMissPolicy;
    private internalCache theCache;
    private String[] ramData;

    /**
     * Constructor for CacheController
     * 
     * @param CacheConfigObj the start {@link CacheConfigObj} method must have been
     *                       called before passing to {@link CacheController}
     * @param String         ramFile location of file to load into ram
     */
    public CacheController(CacheConfigObj ccfig, String ramFile) {
        // Save Config to vars
        cacheSize = ccfig.GetCacheSizeBytes();
        dataBlockSize = ccfig.GetBlockBytes();
        associativity = ccfig.GetAssociativity();
        replacementPolicy = ReplacmentOption.values()[ccfig.GetReplacementPolicy()];
        writeHitPolicy = WriteHitOption.values()[(ccfig.GetWriteHitPolicy())];
        writeMissPolicy = WriteMissOption.values()[(ccfig.GetWriteMissPolicy())];
        ramSize = ccfig.GetRamSize();

        // Some Setup :)
        setSize = cacheSize / (dataBlockSize * associativity);
        ramData = new String[ramSize];
        theCache = new internalCache(setSize, associativity, dataBlockSize);
        parseRam(ramFile);
    }

    /**
     * Ram Parse
     * <p>
     * Will parse the ram and add it to the data structure
     * 
     * @param String ramLocation the name and location of the file for the ram
     */
    public void parseRam(String ramLocation) {
        try{
            FileInputStream in = null;
            in = new FileInputStream(ramLocation);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line;
            int i = 0;
            try{
                while ((line = br.readLine()) != null) {
                    ramData[i] = line;
                    i++;
                }
            }catch(Exception e){
                System.out.println("An error occured while reading the file!");
            }
            
            System.out.println("RAM successfully initialized!");
            in.close();
        }catch(Exception e){
            System.out.println("File could not be read!");
        }
    }

    /**
     * Cache Read
     * <p>
     * function Returns the Set, tag, hit, eviction_line, ram_address, data
     * 
     * @param String Memory adress in hex
     */
    public void CacheRead(String hexAddress) {
      
    }

    /**
     * Cache Write
     * <p>
     * function Returns set, tag, hit, eviction_line, ram_address, data, dirty_bit
     * 
     * @param String Adress (8-bit)
     * @param String data (1 byte)
     */
    public void CacheWrite(String address, String data) {

    }

    /**
     * Cache Flush
     * <p>
     * function makes the cache empty
     */
    public void CacheFlush() {
        int S = theCache.Set.length;
        int E = theCache.Set[0].length;
        int B = theCache.Set[0][0].Block.length;
        int tag_length = theCache.Set[0][0].tag.length();
        int block_length = theCache.Set[0][0].Block[0].length();
        for (int i=0; i<S; i++) {
            for (int j=0; i<E; j++) {
                theCache.Set[i][j].tag = "";
                for (int k=0; k<tag_length; k++) theCache.Set[i][j].tag += "0";
                theCache.Set[i][j].validBit = false;
                for (int l=0; l<B; l++) {
                    theCache.Set[i][j].Block[l] = "";
                    for (int m=0; m<block_length; m++) theCache.Set[i][j].Block[l] += "0";
                }
            }
        }
        System.out.println("cache-cleared");
    }

    /**
     * Memory View
     * <p>
     * Takes a peek at ram and displays along with size
    */
    public void MemoryView(){
      System.out.println("memory-view");
      System.out.println("memory_size:" + ramSize);
      System.out.print("memory_content:");
      for(int i = 0; i < ramData.length; i++){
        if(i % 8 == 0){
          System.out.print("\n"+ Int2Hex(i) + " ");
        }
        System.out.print(ramData[i] + " ");
      } 
      System.out.println();
    }

    /**
     * Memory Dump
     * <p>
     * Dump raw contents of memory stored in the object
    */
    public void MemoryDump(){
      System.out.print("memory-dump");
      for(int i = 0; i < ramData.length; i++){
        System.out.println(ramData[i]);
      } 
    }

    /**
     * Decimal to Hex
     * Accepts an integer and returns a hex in string from 
     * @param decimal Int
     * @return String Hex
    */
    static String Int2Hex(int num){
      String h = Integer.toHexString(num);
      if(h.length() < 2){
        return "0x0"+h;
      }
      return "0x"+h;
    }
}
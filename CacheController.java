import java.io.File; 
import java.io.FileNotFoundException;  
import java.util.Scanner;
import java.io.FileInputStream;  
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Math;
import java.util.Random;
import java.util.LinkedList;
import java.io.FileWriter;
import java.io.Writer;

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
    @SuppressWarnings("unchecked") 
    private class internalCache {
        public class CacheLine {
            public CacheLine(int num){
              Block = new String[num];
              validBit =false;
              dirtyBit = false;
              tag = "";
            }
            public boolean validBit;
            public boolean dirtyBit; // new
            public int address;
            public String tag;
            public String[] Block;
        }

        public CacheLine[][] Set;
        public LinkedList<Integer>[] order;

        /**
         * Constructor for Internal Cache
         * 
         * @param S number of Sets
         * @param E number of Lines
         * @param B number of Block (bytes)
         */
        public internalCache(int S, int E, int B, ReplacementOption r) {
            Set = new CacheLine[S][E];
            for(int i = 0; i < S; i++){
              for(int j = 0; j < E; j++){
                Set[i][j] = new CacheLine(B);
              }
            }
            if(r == ReplacementOption.LEAST_RECENTLY_USED){
              order = new LinkedList[S];
              for(int i = 0; i < S; i++){
                order[i] = new LinkedList<Integer>();
              }
            }
        }
    }

    /**
     * Enum for different options
     */
    private enum ReplacementOption {
        NONE, RANDOM_REPLACEMENT, LEAST_RECENTLY_USED
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
    private int tagBits;
    private int blockOffsetBits;
    private int setIndex;
    private ReplacementOption replacementPolicy;
    private WriteHitOption writeHitPolicy;
    private WriteMissOption writeMissPolicy;
    private internalCache theCache;
    private String[] ramData;
    private int cacheHit = 0;
    private int cacheMiss = 0;
    //Random randGen = new Random(123123123);

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
        replacementPolicy = ReplacementOption.values()[ccfig.GetReplacementPolicy()];
        writeHitPolicy = WriteHitOption.values()[(ccfig.GetWriteHitPolicy())];
        writeMissPolicy = WriteMissOption.values()[(ccfig.GetWriteMissPolicy())];
        ramSize = ccfig.GetRamSize();

        // Some Setup
        setSize = cacheSize / (dataBlockSize * associativity);
        setIndex = (int)(Math.log(setSize)/Math.log(2));
        blockOffsetBits = (int)( Math.log(dataBlockSize) / Math.log(2));
        tagBits = ((int)(Math.log(ramSize)/Math.log(2))) - (setIndex + blockOffsetBits);
        ramData = new String[ramSize];
        theCache = new internalCache(setSize, associativity, dataBlockSize, replacementPolicy);
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
                while (((line = br.readLine()) != null) && (i < ramSize)) {
                    ramData[i] = line;
                    i++;
                }
            }catch(Exception e){
                System.out.println("An error occured while reading the file!");
            }
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
      int addressInt = Integer.parseInt(hexAddress.substring(2), 16);
      String addressBin = Int2Bin(addressInt);
      String tagS = ((tagBits == 0) ? "0" : addressBin.substring(0,tagBits)  );
      String setS = ((setIndex == 0) ? "0" : addressBin.substring(tagBits, tagBits+setIndex));
      String blockS =((blockOffsetBits == 0) ? "0" : addressBin.substring(tagBits+setIndex));
      int setI = Integer.parseInt(setS,2);
      String tagH = Int2Hex(Integer.parseInt(tagS,2),false);
      int offset = Integer.parseInt(blockS,2);

      System.out.println("set:"+setI);
      System.out.println("tag:"+Hex2Hex(tagH));


      for(int i = 0; i < associativity; i++){
        if(tagH.equals(theCache.Set[setI][i].tag)){
          cacheHit++;
          System.out.println("write_hit: yes");
          System.out.println("eviction_line:-1");
          System.out.println("ram_address:-1");
          System.out.println("data:0x" +theCache.Set[setI][i].Block[offset]);
          CacheLineUpdate(setI, i);
          return;
        }
      }
      int victim = ReplaceLine(setI, tagH, addressInt);
      cacheMiss++;
      System.out.println("write_hit: no");
      System.out.println("eviction_line:" + victim);
      System.out.println("ram_address:"+hexAddress);
      System.out.println("data:0x"+ theCache.Set[setI][victim].Block[offset]);
      

    }

    /**
     * Cache Write
     * <p>
     * function Returns set, tag, hit, eviction_line, ram_address, data, dirty_bit
     * 
     * @param String Adress (8-bit)
     * @param String data (1 byte)
     */
    public void CacheWrite(String hexAddress, String data) {

      int addressInt = Integer.parseInt(hexAddress.substring(2), 16);
      String addressBin = Int2Bin(addressInt);
      String tagS = ((tagBits == 0) ? "0" : addressBin.substring(0,tagBits)  );
      String setS = ((setIndex == 0) ? "0" : addressBin.substring(tagBits, tagBits+setIndex));
      String blockS =((blockOffsetBits == 0) ? "0" : addressBin.substring(tagBits+setIndex));
      int setI = Integer.parseInt(setS,2);
      String tagH = Int2Hex(Integer.parseInt(tagS,2),false);
      int offset = Integer.parseInt(blockS,2);
      int CacheHitLine = -1;
      data = data.substring(2);

      for(int i = 0; i < associativity; i++){
          if(tagH.equals(theCache.Set[setI][i].tag)){
            CacheHitLine = i;
            break;
          }
      }

      System.out.println("set:"+setI);
      System.out.println("tag:"+Hex2Hex(tagH));
      System.out.println("write_hit:"+((CacheHitLine == -1) ? "no": "yes"));

      if(CacheHitLine == -1){
        cacheMiss++;
        if(writeMissPolicy == WriteMissOption.WRITE_ALLOCATE){
          CacheHitLine =ReplaceLine(setI, tagS, addressInt);
          theCache.Set[setI][CacheHitLine].Block[offset] = data;
          System.out.println("ram_address:"+hexAddress);
          System.out.println("data:0x"+data);

          if(writeHitPolicy == WriteHitOption.WRITE_THROUGH){
            System.out.println("dirty_bit:0");
            ramData[addressInt] = data;
            theCache.Set[setI][CacheHitLine].dirtyBit = false;

          }else{
            System.out.println("dirty_bit:1");
            theCache.Set[setI][CacheHitLine].dirtyBit = true;
          }

        }else{
          ramData[addressInt] = data;
          System.out.println("ram_address:"+hexAddress);
          System.out.println("data:0x"+data);
          System.out.println("dirty_bit:0");
        }
      }else{
        cacheHit++;
        if(writeHitPolicy == WriteHitOption.WRITE_THROUGH){
          System.out.println("ram_address:"+"-1");
          System.out.println("data:0x"+data);
          System.out.println("dirty_bit:"+Bool2Int(theCache.Set[setI][CacheHitLine].dirtyBit));
          ramData[addressInt] = data;
          theCache.Set[setI][CacheHitLine].Block[offset] = data;
          //theCache.Set[setI][CacheHitLine].dirtyBit = true;
          CacheLineUpdate(setI, CacheHitLine);
        }else{
          System.out.println("ram_address:"+"-1");
          System.out.println("data:0x"+data);
          System.out.println("dirty_bit:1");
          theCache.Set[setI][CacheHitLine].Block[offset] = data;
          theCache.Set[setI][CacheHitLine].dirtyBit = true;
          CacheLineUpdate(setI, CacheHitLine);
        }
      }
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
        for (int i=0; i<S; i++) {
            for (int j=0; j<E; j++) {
                theCache.Set[i][j].validBit = false;
                theCache.Set[i][j].dirtyBit = false;
                theCache.Set[i][j].tag = "";
                for (int k=0; k<tag_length; k++) theCache.Set[i][j].tag += "0";
                for (int l=0; l<B; l++) {
                    theCache.Set[i][j].Block[l] = null;
                }
            }
            if(replacementPolicy == ReplacementOption.LEAST_RECENTLY_USED){
              theCache.order[i].clear();
            }
        }
        System.out.println("cache-cleared");
    }

    /**
     * Cache View
     * <p>
     *
    */
    public void CacheView() {
        System.out.println("cache_size:" + cacheSize);
        System.out.println("data_block_size:" + dataBlockSize);
        System.out.println("associativity:" + associativity);
        System.out.print("replacement_policy:");
            if (replacementPolicy == ReplacementOption.NONE) System.out.println("none");
            if (replacementPolicy == ReplacementOption.RANDOM_REPLACEMENT) System.out.println("random_replacement");
            if (replacementPolicy == ReplacementOption.LEAST_RECENTLY_USED) System.out.println("least_recently_used");
        System.out.print("write_hit_policy:");
            if (writeHitPolicy == WriteHitOption.NONE) System.out.println("none");
            if (writeHitPolicy == WriteHitOption.WRITE_THROUGH) System.out.println("write_through");
            if (writeHitPolicy == WriteHitOption.WRITE_BACK) System.out.println("write_back");
        System.out.print("write_miss_policy:");
            if (writeMissPolicy == WriteMissOption.NONE) System.out.println("none");
            if (writeMissPolicy == WriteMissOption.WRITE_ALLOCATE) System.out.println("write_allocate");
            if (writeMissPolicy == WriteMissOption.NO_WRITE_ALLOCATE) System.out.println("no_write_allocate");
        System.out.println("number_of_cache_hits:"+cacheHit); 
        System.out.println("number_of_cache_misses:"+cacheMiss);
        System.out.println("cache_content:");
            int S = theCache.Set.length;
            int E = theCache.Set[0].length;
            int B = theCache.Set[0][0].Block.length;
            for (int i=0; i<S; i++) {
                for (int j=0; j<E; j++) {
                    System.out.print(Bool2Int(theCache.Set[i][j].validBit) + " ");
                    System.out.print(Bool2Int(theCache.Set[i][j].dirtyBit) + " ");
                    System.out.print(Hex2Hex(theCache.Set[i][j].tag) + " ");
                    for (int l=0; l<B; l++) {
                        System.out.print( ((theCache.Set[i][j].Block[l] == null ) ? "00" : theCache.Set[i][j].Block[l]) );
                        if (l != B-1) System.out.print(" ");
                    }
                    if (j == E-1) System.out.print("\n");
                    else System.out.print("  ");
                }
            }
    }

    /**
     * Memory View
     * <p>
     * Takes a peek at ram and displays along with size
    */
    public void MemoryView(){
      System.out.println("memory_size:" + ramSize);
      System.out.print("memory_content:");
      for(int i = 0; i < ramData.length; i++){
        if(i % dataBlockSize == 0){
          System.out.print("\n"+ Int2Hex(i) + " ");
        }
        System.out.print(ramData[i] + " ");
      } 
      System.out.println();
    }

    /**
     * Cache Dump
     * <p>
     *
    */
    public void CacheDump(){
        String filename = "cache.txt";
        try{
            Writer fileWriter = new FileWriter(filename, false);
            for(int i =0; i < setSize; i++){
              for(int j = 0; j < associativity; j++ ){
                for(int z = 0; z < dataBlockSize; z++){
                  System.out.print(((theCache.Set[i][j].Block[z]==null)? "00": theCache.Set[i][j].Block[z]) + " ");
                  fileWriter.write(((theCache.Set[i][j].Block[z]==null)? "00": theCache.Set[i][j].Block[z]) + " ");
                }
                System.out.println();
                fileWriter.write("\n");
              }
            }
            fileWriter.close();
        }catch(Exception e){
          System.out.println("Could not write to file");
        }
    }

    /**
     * Memory Dump
     * <p>
     * Dump raw contents of memory stored in the object
    */
    public void MemoryDump(){
        String filename = "ram.txt";
        try{
            Writer fileWriter = new FileWriter(filename, false);
            for(int i = 0; i < ramSize; i++){
                System.out.println(ramData[i]);
                fileWriter.write(ramData[i]);
                if (i != 255) fileWriter.write("\n");
            }
            for(int i= ramSize; i < 256;i++){
              System.out.println("00");
              fileWriter.write("00");
              if (i != 255) fileWriter.write("\n");
            }
            fileWriter.close();
        }catch(Exception e){
          System.out.println("Could not Write to file");
        }
    }

    /**
     * Cache Line Update
     * @param set number
     * @param line number
    */
    public void CacheLineUpdate(int set, int line){
      if(replacementPolicy == ReplacementOption.RANDOM_REPLACEMENT){
        return;
      }
      theCache.order[set].removeFirstOccurrence(line);
      theCache.order[set].addFirst(line);
    }


    /**
     * Cache Line Victim 
     * @param set number
     * @return victim
    */
    public int CacheLineVictim(int set){
      int victim;
      if(replacementPolicy == ReplacementOption.RANDOM_REPLACEMENT){
        for(int i = 0;i < associativity;i++){
          if(theCache.Set[set][i].tag.equals("")){
            victim = i;
            return victim;
          }
        }
        victim = 0;
      } else {
        if(theCache.order[set].size() < associativity){
          victim = theCache.order[set].size();
          theCache.order[set].addFirst(victim);
        } else {
          victim = theCache.order[set].getLast();
          theCache.order[set].removeLast();
          theCache.order[set].addFirst(victim);
        }
      }
      return victim;
    }

    /**
     * Save to Ram
     * Saves the given Set and Cacheline to the Ram
     * @param set (int)
     * @param line (int)
    */
    private void SaveToRam(int set, int line){
      int j =0;
      for(int i = theCache.Set[set][line].address; i < (theCache.Set[set][line].address + dataBlockSize);i++ ){
        ramData[i] = theCache.Set[set][line].Block[j];
        j++;
      }
    }

    /**
     * Replace Line
     * replaces the line based on the replacement policy
     * @param set int
     * @param tag String
     * @param ram address
     * @return line evicted
    */
    public int ReplaceLine(int set, String tag, int address){
      int victim = CacheLineVictim(set);

      if(theCache.Set[set][victim].validBit && theCache.Set[set][victim].dirtyBit){
        SaveToRam(set, victim);
      }

      theCache.Set[set][victim].tag = tag;
      theCache.Set[set][victim].dirtyBit = false;
      theCache.Set[set][victim].validBit = true;

      int starter = ((int)Math.floor(1.0 * address / dataBlockSize))*dataBlockSize;
      theCache.Set[set][victim].address = starter;
      int j = 0;
      for(int i = starter; i < starter+dataBlockSize; i++ ){
        theCache.Set[set][victim].Block[j] = ramData[i];
        j++;
      }

      return victim;
    }

    /**
     * Hex to Nice Hex 
     * formats the Hex number with 0z
     * @param Hex String
     * @return Hex String
    */
    static String Hex2Hex(String hexString){
      return ((hexString.length() == 2) ? hexString : ((hexString.length() == 1) ? "0" + hexString : "00") );
    }

    /**
     * Decimal to Hex
     * <p>
     * Accepts an integer and returns a hex in string from 
     * @param decimal Int
     * @return String Hex
    */
    static String Int2Hex(int num){
      return Int2Hex(num, true);
    }

    /**
     * Decimal to Hex
     * <p>
     * Accepts an integer and returns a hex in string from 
     * @param decimal Int
     * @param boolean add "0x" to the beginning
     * @return String Hex
    */
    static String Int2Hex(int num,boolean id){
      String h = Integer.toHexString(num);
      if(h.length() < 2){
        return ((id) ? "0x0" : "")+h;
      }
      return ((id) ? "0x" : "")+h;
    }

    /**
     * Decimal to Binary
     * <p>
     * Accepts an Integer and returns a binary string in the right format 
     * @param num Int
     * @return binary String 
    */
    static String Int2Bin(int num){
      return String.format("%8s", Integer.toBinaryString(num)).replace(' ', '0');
    }

    /**
     * Boolean to Integer
     * <p>
     * Accepts a boolean and returns the value as an integer
     * @param boolean 
     * @return num Int
    */
    static int Bool2Int(boolean b) {
        if (b == false) return 0;
        else return 1;
    }
}
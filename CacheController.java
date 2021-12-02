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
            public boolean validBit;
            public String tag;
            public String[] Block = new String[B];
        }

        public CacheLine[] Set[][];

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
                Set[i][j] = new 
              }
            }
        }
    }

    /**
     * Enum for different options
     */
    private enum ReplacmentOption {
        NONE(0), RANDOM_REPLACEMENT(1), LEAST_RECENTLY_USE(2)
    }

    private enum WriteHitOption {
        NONE(0), WRITE_THROUGH(1), WRITE_BACK(2)
    }

    private enum WriteMissOption {
        NONE(0), WRITE_ALLOCATE(1), NO_WRITE_ALLOCATE(2)
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
        cacheSize = ccfig.GetCacheByte();
        dataBlockSize = ccifg.GetBlockBytes();
        associativity = ccfig.GetAssociativity();
        replacementPolicy = ReplacementOption.valueOf(ccfig.GetReplacementPolicy());
        writeHitPolicy = WriteHitOption.valueOf(ccfig.GetWriteHitPolicy());
        writeMissPolicy = WriteMissOption.valueOf(ccfig.GetWriteMissPolicy());
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
        FileInputStream in = null;
        in = new FileInputStream(ramLocation);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line;
        int i = 0;
        while ((line = br.readLine()) != null) {
            ramData[i] = line;
            i++;
        }

        System.out.println("RAM successfully initialized!");
        in.close();
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
}
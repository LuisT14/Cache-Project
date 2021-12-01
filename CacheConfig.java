import java.util.Scanner;


/**
 * CacheConfig Class
 * Sets up the Cache options at the beginning of the program 
*/
public class CacheConfig {

  private int cacheSizeBytes;
  private int blockBytes;
  private int associativty;
  private int replacementPolicy;
  private int writeHitPolicy;
  private int writeMissPolicy;

  /**
   * Default Constructor for {@link CacheConfig}
  */
  public CacheConfig(){}

  public void start(){
    System.out.println("configure the cache:");
    System.out.print("cache size: ");
    System.out.print("data block size: ");
    System.out.print("associativty: ");
    System.out.print("replacementPolicy: ");
    System.out.print("write hit policy: ");
    System.out.print("write miss policy: ");
    System.out.print("cache successfully configured!");
  }


}
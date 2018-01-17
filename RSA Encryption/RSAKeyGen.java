import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;


public class RSAKeyGen {


    private BigInteger P;
    private BigInteger Q;
    private BigInteger N;
    private BigInteger E;
    private BigInteger D;

    BigInteger PhiN;
    private RSAKeyGen(){
        int bitLength = 512; // 512 bits gets *2 for N to get 1024
        int certainty = Integer.MAX_VALUE; // 1 - 1/2(2147483647)certainty of prime num generated (increases runtime the higher the value)
        SecureRandom rnd1 = new SecureRandom();
        SecureRandom rnd2 = new SecureRandom();
        P = new BigInteger(bitLength, certainty, rnd1);
        Q = new BigInteger(bitLength, certainty, rnd2);
        N = P.multiply(Q);
        PhiN = (P.subtract(BigInteger.ONE)).multiply(Q.subtract(BigInteger.ONE));
        do {
            E = new BigInteger(1024, new SecureRandom());
        } while((E.compareTo(PhiN) != 1) || (E.gcd(PhiN).compareTo(BigInteger.valueOf(1)) != 0));
        D = E.modInverse(PhiN);
        writeKeys();
    }
    private void writeKeys(){
        try {
            FileOutputStream out1 = new FileOutputStream("pubkey.rsa");
            ObjectOutputStream oout1 = new ObjectOutputStream(out1);
            oout1.writeObject(E);
            oout1.writeObject(N);
            oout1.close();
            out1.close();
            System.out.println("pubkey.rsa\t\t-- Created --");

            FileOutputStream out2 = new FileOutputStream("privkey.rsa");
            ObjectOutputStream oout2 = new ObjectOutputStream(out2);
            oout2.writeObject(D);
            oout2.writeObject(N);
            oout2.close();
            out2.close();
            System.out.println("privkey.rsa\t\t-- Created --");


        }catch (Exception e){

        }

    }
    public static void main(String[] args){
        new RSAKeyGen();
    }
}

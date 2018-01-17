import java.io.*;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RSASign {
    private static int mode;
    private static final int sign = 0;
    private static final int verify = 1;
    private File file;
    private File sFile;
    private BigInteger N;
    private BigInteger E;
    private BigInteger D;

    RSASign(String fileName){
        try {
            if(mode == sign){
                URL f1Path = getClass().getResource(fileName.trim());
                if(f1Path == null) errorExit("\n--> There is no file associated with "+fileName.trim()+"\n");
                file = new File(f1Path.toURI());
            }
            else {
                String[] f = fileName.trim().split("\\.");
                if(f.length == 0 || !f[f.length-1].equals("sig")) errorExit("\nIt appears you are not using the .sig file extension to verify the file\n");

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < f.length-1; i++){
                    sb.append(f[i]);
                    if(i != f.length-2) sb.append(".");
                }
                URL f1Path = getClass().getResource(sb.toString());
                URL f2Path = getClass().getResource(fileName.trim());
                if(f1Path == null) errorExit("\n--> There is no file associated with "+sb.toString()+"\n");
                else file = new File(f1Path.toURI());
                if(f2Path == null) errorExit("\n--> There is no file associated with "+fileName.trim()+"\n");
                else sFile = new File(f2Path.toURI());
            }
        }catch (URISyntaxException e){
                e.printStackTrace();
        }
        try {
            if (mode == sign) {
                Path path = Paths.get(file.toURI());
                byte[] data = Files.readAllBytes(path);
                String d = new String(data);
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(data);
                byte[] digest = md.digest();
                BigInteger hash = new BigInteger(1, digest);
                initPrivateKey();
                BigInteger decryptedHash = decrypt(hash);
                OutputStream out = new FileOutputStream(path.getFileName() + ".sig");
                ObjectOutputStream outputStream = new ObjectOutputStream(out);
                outputStream.writeObject(d);
                outputStream.writeObject(decryptedHash);
                out.close();
                System.out.println("\nSuccesfully Signed : "+file.getName()+"\n");
            }
            else {
                ObjectInputStream in = null;
                String d = null;
                BigInteger decryptedHash = null;
                try{
                    in = new ObjectInputStream(new FileInputStream(sFile));
                }catch(Exception e){
                    System.out.println("\n\n -- Signature not evaluated -- \n\n");
                    errorExit("Header from signed file is corrupted, file was illegally modified\n\n");
                }
                try{
                    d = (String) in.readObject();
                }catch(Exception e){
                    System.out.println("\n\n -- Signature not evaluated -- \n\n");
                    errorExit("Orginal Text from signed file is corrupted, String object was modified\n\n");
                }
                byte[] data = d.getBytes();
                try{
                    decryptedHash = (BigInteger)in.readObject();
                }catch(Exception e){
                    System.out.println("\n\n -- Signature not evaluated -- \n\n");
                    errorExit("Decrypted Hash from signed file is corrupted, Big Integer object was modified\n\n");
                }
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(data);
                byte[] digest = md.digest();
                BigInteger hash = new BigInteger(1, digest);
                initPublicKey();
                BigInteger encryptedHash = encrypt(decryptedHash);
                if(encryptedHash.equals(hash))System.out.println("\n\n -- Signature is valid-- \n\n");
                else System.out.println("\n\n -- Signature is invalid-- \n\n");
            }

        }catch (IOException e){
            System.out.println("--> There was an issue loading your file IO, please try again");
            e.printStackTrace();
        }catch (NoSuchAlgorithmException e){
            System.out.println("--> There was an issue loading your file NSA, please try again");
            e.printStackTrace();
        }
    }

    public static void errorExit(String error){
        System.out.println(error);
        System.exit(0);
    }


    public void initPrivateKey(){
        try {
            URL priv = getClass().getResource("privkey.rsa");
            if(priv == null){
                System.out.println("\n--> There is no privkey.rsa in the current directory\n");
                System.exit(0);
            }
            else {
                FileInputStream inputStream = new FileInputStream("privkey.rsa");
                ObjectInputStream oIn = new ObjectInputStream(inputStream);
                D = (BigInteger) oIn.readObject();
                N = (BigInteger) oIn.readObject();
                inputStream.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void initPublicKey(){
        try {
            URL pub = getClass().getResource("pubkey.rsa");
            if(pub == null){
                System.out.println("\n--> There is no pubkey.rsa in the current directory\n");
                System.exit(0);
            }
            else {
                FileInputStream inputStream = new FileInputStream("pubkey.rsa");
                ObjectInputStream oIn = new ObjectInputStream(inputStream);
                E = (BigInteger) oIn.readObject();
                N = (BigInteger) oIn.readObject();
                inputStream.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public BigInteger encrypt(BigInteger fingerPrint) {
        return fingerPrint.modPow(E, N);
    }

    public BigInteger decrypt(BigInteger fingerPrint) {
        return fingerPrint.modPow(D, N);
    }

    public static void main(String[] args){
        int code = -1;
        if(args.length != 2) errorExit("\n -- Invalid Argument -- \n");
        if(args[0].equals("s")) mode = sign;
        else if (args[0].equals("v"))mode = verify;
        else errorExit("\n -- Invalid Argument -- \n");
        new RSASign(args[1]);
    }

}

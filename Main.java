package encryptdecrypt;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
 
interface AlgEncDec{
    String encrypt(String stringToEncrypt,int key);
 
    String decrypt(String stringToEncrypt,int key);
}
 
class Unicode implements AlgEncDec{
    @Override
    public String encrypt(String stringToEncrypt,int key){
        StringBuilder res = new StringBuilder();
        for (char currentCh : stringToEncrypt.toCharArray()){
            res.append((char) (currentCh + key));
        }
        return res.toString();
    }
    @Override
    public String decrypt(String stringToEncrypt,int key){
        StringBuilder res = new StringBuilder();
        for (char currentCh : stringToEncrypt.toCharArray()){
            res.append((char) (currentCh - key));
        }
        return res.toString();
    }
}
 
class Shift implements AlgEncDec{
    @Override
    public String encrypt(String stringToEncrypt, int key) {
        StringBuilder res = new StringBuilder();
        for(char currentCh : stringToEncrypt.toCharArray()){
            if(currentCh>='a'&&currentCh<='z'){
                currentCh = (char)(currentCh+key);
                currentCh = currentCh > 'z' ? (char)(currentCh-26):currentCh;
            }else if(currentCh>='A'&&currentCh<='Z'){
                currentCh = (char)(currentCh+key);
                currentCh = currentCh > 'Z' ? (char)(currentCh-26):currentCh;
            }
            res.append(currentCh);
        }
        return res.toString();
    }
    @Override
    public String decrypt(String stringToEncrypt,int key){
        StringBuilder res = new StringBuilder();
        for(char currentCh : stringToEncrypt.toCharArray()){
            if(currentCh>='a'&&currentCh<='z'){
                currentCh = (char)(currentCh-key);
                currentCh = currentCh < 'a' ? (char)(currentCh+26):currentCh;
            }else if(currentCh>='A'&&currentCh<='Z'){
                currentCh = (char)(currentCh-key);
                currentCh = currentCh < 'A' ? (char)(currentCh+26):currentCh;
            }
            res.append(currentCh);
        }
        return res.toString();
    }
}
 
public class Main {
    private static String logFileName = "log.txt";
 
    private static void appendLogFile(String message) {
        try (FileWriter fr = new FileWriter(logFileName, true)) {
            fr.write(message);
            fr.close();
        } catch (IOException e) {
            System.out.println("Error loging file " + e);
        }
    }
 
    private static AlgEncDec algEncDecBuilder(String alg) {
        switch (alg) {
            case "shift":
                return new Shift();
            case "unicode":
                return new Unicode();
            default:
                return null;
        }
    }
 
    private static String getArg(String[] args, String agrName, String defaultValue) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(agrName)) {
                return args[i + 1];
            }
        }
        return defaultValue;
    }
 
    public static void main(String[] args) {
        String mode = getArg(args, "-mode", "enc");
        String data = getArg(args, "-data", "");
        int key = Integer.parseInt(getArg(args, "-key", "0"));
        String in = getArg(args, "-in", null);
        String out = getArg(args, "-out", null);
        String alg = getArg(args, "-alg", "shift");
        if (in != null && data.equals("")) {
            try {
                File file = new File(in);
                FileInputStream fis = new FileInputStream(file);
                Scanner scanner = new Scanner(fis);
                StringBuilder sb = new StringBuilder();
                while (scanner.hasNextLine()) {
                    sb.append(scanner.nextLine());
                }
                data = sb.toString();
                scanner.close();
            } catch (IOException e) {
                System.out.println("Wrong fileName " + in);
                System.out.println(e.getMessage());
            }
        }
        AlgEncDec algorithm = algEncDecBuilder(alg);
        String handledData = "";
        if (mode.equals("enc")) {
            handledData = algorithm.encrypt(data, key);
        } else {
            handledData = algorithm.decrypt(data, key);
        }
 
        if (out != null) {
            try (FileWriter fw = new FileWriter(out)) {
                fw.write(handledData);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println(handledData);
        }
    }
}

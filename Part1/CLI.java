import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class CLI extends ellipticCurve{
    public static void main(String[] args) throws IOException {
        boolean keepGoing = true;
        Scanner input = new Scanner(System.in);
        while (keepGoing) {
            System.out.println("Welcome to our encryption software");
            System.out.println("Enter '1' to compute a plain cryptographic hash of a given file");
            System.out.println("Enter '2' to encrypt a data file");
            System.out.println("Enter '3' to decrypt a data file");
            System.out.println("Enter '4' to generate an elliptic key pair and write it to a file");
            System.out.println("Enter '5' to encrypt a data file under a given elliptic key pair");
            System.out.println("Enter '6' to decrypt an elliptic-encrypted data file");
            System.out.println("Enter '7' to sign a given file from a given password and write the signature to a file");
            System.out.println("Enter '8' to verify a given data file and its signature file under a given public key file");
            System.out.println("Enter '9' to Exit");


            Path root = Paths.get("");

            System.out.println();
            System.out.print("Select option: ");
            String userInput = input.nextLine();


            switch (userInput) {
                case "1":

                    System.out.println();
                    System.out.println("Chose 1");
                    boolean chose1or2 = false;
                    while (!chose1or2) {

                        System.out.print("Would you like to hash from a file or CLI? '1' for file, '2' for CLI: ");
                        String choice = input.nextLine();

                        if ("1".equals(choice)) {
                            System.out.println("Hashing from file");
                            Path path = Paths.get(root.toAbsolutePath() + "/Part1/Storage/testFile.txt");
                            byte[] data = Files.readAllBytes(path);

                            byte[] hash = hash(data);

                            Path path2 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/hash.txt");
                            Files.write(path2, hash);
                            chose1or2 = true;
                        } else if ("2".equals(choice)) {

                            System.out.print("Type a message you would like to encrypt: ");
                            String textToBeHashed = input.nextLine();

                            byte[] toBeHashedBytes = new byte[userInput.length()];

                            for (int i = 0; i < userInput.length(); i++) {
                                toBeHashedBytes[i] = (byte) userInput.charAt(i);
                            }

                            byte[] hash = hash(toBeHashedBytes);
                            Path path2 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/hash.txt");
                            Files.write(path2, hash);

                            System.out.println("text has been hashed to a file");

                            chose1or2 = true;
                        } else {
                            System.out.println("Please select 1 or 2");
                        }
                    }
                    break;
                case "2":

                    System.out.println();
                    System.out.println("Chose 2");

                    Path path = Paths.get(root.toAbsolutePath() + "/Part1/Storage/testFile.txt");
                    byte[] data = Files.readAllBytes(path);

                    Path path2 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/pw.txt");
                    byte[] pw = Files.readAllBytes(path2);

                    byte[][] encryptedData = encrypt(data, pw);

                    Path path3 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/encrypted/Z.txt");
                    Files.write(path3, encryptedData[0]);

                    Path path4 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/encrypted/c.txt");
                    Files.write(path4, encryptedData[1]);

                    Path path5 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/encrypted/t.txt");
                    Files.write(path5, encryptedData[2]);

                    System.out.println("File has been encrypted to encrypted folder");
                    System.out.println();

                    break;
                case "3":

                    System.out.println();
                    System.out.println("Chose 3");

                    Path path6 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/encrypted/Z.txt");
                    byte[] Z = Files.readAllBytes(path6);

                    Path path7 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/encrypted/c.txt");
                    byte[] c = Files.readAllBytes(path7);

                    Path path8 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/encrypted/t.txt");
                    byte[] t = Files.readAllBytes(path8);

                    Path path9 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/pw2.txt");
                    byte[] pw2 = Files.readAllBytes(path9);

                    byte[][] decrypted = decrypt(pw2, Z, c, t);

                    byte[] m = decrypted[0];
                    byte[] tPrime = decrypted[1];

                    boolean acceptMesssage = acceptMessage(t, tPrime);

                    if (acceptMesssage) {
                        Path path10 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/decrypted/message.txt");
                        Files.write(path10, m);
                        System.out.println("Message accepted and written to file");
                        System.out.println("Message written to file: ");
                        String mString = new String(m, "UTF-8");
                        System.out.println(mString);

                    } else {
                        System.out.println("Message not accepted, something went wrong");
                    }
                    System.out.println();

                    break;
                case "4":

                    System.out.println();
                    System.out.println("Chose 4");

                    generatePairToFiles();

                    break;
                case "5":

                    System.out.println();
                    System.out.println("Chose 5");

                    generatePairToFiles();

                    Path path11 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/testFile.txt");
                    byte[] data2 = Files.readAllBytes(path11);

                    Path path13 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/ECPoint/x.txt");
                    byte[] xBytes = Files.readAllBytes(path13);
                    BigInteger x = new BigInteger(xBytes);

                    Path path14 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/ECPoint/y.txt");
                    byte[] yBytes = Files.readAllBytes(path14);
                    BigInteger y = new BigInteger(yBytes);

                    ECPoint retrievedPoint = new ECPoint(x, y);

                    Cryptogram crypt = encryptEC(data2, retrievedPoint);


                    ECPoint Z2 = crypt.Z;

                    BigInteger x2 = Z2.x;
                    byte[] xArr2 = x2.toByteArray();
                    BigInteger y2 = Z2.y;
                    byte[] yArr2 = y2.toByteArray();

                    byte[] c2 = crypt.c;
                    byte[] t2 = crypt.t;

                    Path path16 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/encryptedEC/ECPoint/x.txt");
                    Files.write(path16, xArr2);

                    Path path17 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/encryptedEC/ECPoint/y.txt");
                    Files.write(path17, yArr2);

                    Path path18 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/encryptedEC/c.txt");
                    Files.write(path18, c2);

                    Path path19 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/encryptedEC/t.txt");
                    Files.write(path19, t2);

                    System.out.println("Cryptogram has been stored into files");
                    System.out.println();

                    break;
                case "6":

                    System.out.println();
                    System.out.println("Chose 6");

                    Path path20 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/encryptedEC/ECPoint/x.txt");
                    byte[] x3 = Files.readAllBytes(path20);
                    BigInteger x3Big = new BigInteger(x3);

                    Path path21 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/encryptedEC/ECPoint/y.txt");
                    byte[] y3 = Files.readAllBytes(path21);
                    BigInteger y3Big = new BigInteger(y3);

                    Path path22 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/encryptedEC/c.txt");
                    byte[] c3 = Files.readAllBytes(path22);

                    Path path23 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/encryptedEC/t.txt");
                    byte[] t3 = Files.readAllBytes(path23);

                    ECPoint currentPoint = new ECPoint(x3Big, y3Big);

                    Cryptogram currentCrypt = new Cryptogram(currentPoint, c3, t3);

                    Path path24 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/pw2.txt");
                    byte[] pwTwo = Files.readAllBytes(path24);

                    ReturnMessage returnMessage = decryptEC(currentCrypt, pwTwo);

                    byte[] m2 = returnMessage.m;
                    boolean verified = returnMessage.verified;

                    if (!verified) {
                        Path path25 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/decryptedEC/message.txt");
                        Files.write(path25, m2);
                        System.out.println("Message accepted and written to file");
                        System.out.println("Message written to file: ");
                        String mString = new String(m2, "UTF-8");
                        System.out.println(mString);

                    } else {
                        System.out.println("Message not accepted, something went wrong");
                        System.out.println("Message recieved was: ");
                        String mString = new String(m2, "UTF-8");
                        System.out.println(mString);
                    }

                    break;
                case "7":
                    System.out.println("Chose 7");
                    break;
                case "8":
                    System.out.println("Chose 8");
                    break;
                case "9":
                    System.out.println("Chose 9");
                    keepGoing = false;
                    input.close();
                    break;
            }


        }
        System.out.println("Thanks for trying our encryption program, dont come again");
    }
    public static void generatePairToFiles() throws IOException {

        Path root = Paths.get("");

        Path path10 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/pw.txt");
        byte[] pw3 = Files.readAllBytes(path10);

        ECPoint pair = generatePair(pw3);

        byte[] s = pair.s;
        BigInteger x = pair.x;
        byte[] xArr = x.toByteArray();
        BigInteger y = pair.y;
        byte[] yArr = y.toByteArray();

        Cryptogram crypt = encryptEC(s, pair);

        ECPoint Z = crypt.Z;
        BigInteger x2 = Z.x;
        byte[] x2Bytes = x2.toByteArray();
        BigInteger y2 = Z.y;
        byte[] y2Bytes = y2.toByteArray();
        byte[] c = crypt.c;
        byte[] t = crypt.t;

        Path path = Paths.get(root.toAbsolutePath() + "/Part1/Storage/ECPoint/EncryptedS/ECPoint/x.txt");
        Files.write(path, x2Bytes);
        Path path2 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/ECPoint/EncryptedS/ECPoint/y.txt");
        Files.write(path2, y2Bytes);
        Path path3 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/ECPoint/EncryptedS/ECPoint/c.txt");
        Files.write(path3, c);
        Path path4 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/ECPoint/EncryptedS/ECPoint/t.txt");
        Files.write(path4, t);

//                    Path path13 = Paths.get("C:/temp/ECPoint/x.txt");
//                    byte[] returnBytes = Files.readAllBytes(path13);
//                    BigInteger returnBig = new BigInteger(returnBytes);
//                    System.out.println("xBig      = " + x);
//                    System.out.println("returnBig = " + returnBig);


        Path path11 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/ECPoint/s.txt");
        Files.write(path11, s);

        Path path12 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/ECPoint/x.txt");
        Files.write(path12, xArr);

        Path path13 = Paths.get(root.toAbsolutePath() + "/Part1/Storage/ECPoint/y.txt");
        Files.write(path13, yArr);

        System.out.println("Pair has been generated and stored in file");
        System.out.println();
    }
}

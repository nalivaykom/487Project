import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
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

                    System.out.println("Chose 1");
                    boolean chose1or2 = false;
                    while (!chose1or2) {

                        System.out.println();
                        System.out.print("Would you like to hash from a file or CLI? '1' for file, '2' for CLI: ");
                        String choice = input.nextLine();

                        if ("1".equals(choice)) {
                            System.out.println("Please make sure that your message is saved into inputFile.txt, then press enter:");
                            input.nextLine();
                            System.out.println("Hashing from file...");
                            Path path = Paths.get(root.toAbsolutePath() + "/Default/Storage/inputFile.txt");
                            byte[] data = Files.readAllBytes(path);

                            byte[] hash = hash(data);

                            Path path2 = Paths.get(root.toAbsolutePath() + "/Default/Storage/hash.txt");
                            Files.write(path2, hash);
                            System.out.println("Finished hashing");
                            System.out.println();
                            chose1or2 = true;

                        } else if ("2".equals(choice)) {

                            System.out.print("Type a message you would like to encrypt: ");

                            byte[] toBeHashedBytes = new byte[userInput.length()];

                            for (int i = 0; i < userInput.length(); i++) {
                                toBeHashedBytes[i] = (byte) userInput.charAt(i);
                            }

                            byte[] hash = hash(toBeHashedBytes);
                            Path path2 = Paths.get(root.toAbsolutePath() + "/Default/Storage/hash.txt");
                            Files.write(path2, hash);

                            System.out.println("Text has been hashed to a file");
                            System.out.println();

                            chose1or2 = true;
                        } else {
                            System.out.println("Please select 1 or 2");
                        }
                    }

                    boolean validR1 = false;
                    while (!validR1) {
                        System.out.print("Would you like to continue using using the program? ('y' or 'n') : ");
                        String response1 = input.nextLine().toLowerCase(Locale.ROOT);
                        if ("y".equals(response1)) {
                            validR1 = true;
                        } else if ("n".equals(response1)) {
                            keepGoing = false;
                            validR1 = true;
                            input.close();
                        } else {
                            System.out.println("please enter 'y' or 'n'");
                        }
                    }
                    break;
                case "2":

                    System.out.println();
                    System.out.println("Chose 2");

                    System.out.println("Please make sure that your message is saved into inputFile.txt");
                    System.out.println("and your passphrase is stored in pw.txt, then press enter:");
                    input.nextLine();

                    Path path = Paths.get(root.toAbsolutePath() + "/Default/Storage/inputFile.txt");
                    byte[] data = Files.readAllBytes(path);

                    Path path2 = Paths.get(root.toAbsolutePath() + "/Default/Storage/pw.txt");
                    byte[] pw = Files.readAllBytes(path2);

                    byte[][] encryptedData = encrypt(data, pw);

                    Path path3 = Paths.get(root.toAbsolutePath() + "/Default/Storage/encrypted/Z.txt");
                    Files.write(path3, encryptedData[0]);

                    Path path4 = Paths.get(root.toAbsolutePath() + "/Default/Storage/encrypted/c.txt");
                    Files.write(path4, encryptedData[1]);

                    Path path5 = Paths.get(root.toAbsolutePath() + "/Default/Storage/encrypted/t.txt");
                    Files.write(path5, encryptedData[2]);

                    System.out.println("File has been encrypted to encrypted folder");
                    System.out.println();

                    boolean validR2 = false;
                    while (!validR2) {
                        System.out.print("Would you like to continue using using the program? ('y' or 'n') : ");
                        String response2 = input.nextLine().toLowerCase(Locale.ROOT);
                        if ("y".equals(response2)) {
                            validR2 = true;
                        } else if ("n".equals(response2)) {
                            keepGoing = false;
                            validR2 = true;
                            input.close();
                        } else {
                            System.out.println("please enter 'y' or 'n'");
                        }
                    }

                    break;
                case "3":

                    System.out.println("Chose 3");
                    System.out.println();

                    System.out.println("Please make sure that your passphrase is stored in pw2.txt, then press enter:");
                    input.nextLine();

                    Path path6 = Paths.get(root.toAbsolutePath() + "/Default/Storage/encrypted/Z.txt");
                    byte[] Z = Files.readAllBytes(path6);

                    Path path7 = Paths.get(root.toAbsolutePath() + "/Default/Storage/encrypted/c.txt");
                    byte[] c = Files.readAllBytes(path7);

                    Path path8 = Paths.get(root.toAbsolutePath() + "/Default/Storage/encrypted/t.txt");
                    byte[] t = Files.readAllBytes(path8);

                    Path path9 = Paths.get(root.toAbsolutePath() + "/Default/Storage/pw2.txt");
                    byte[] pw2 = Files.readAllBytes(path9);

                    byte[][] decrypted = decrypt(pw2, Z, c, t);

                    byte[] m = decrypted[0];
                    byte[] tPrime = decrypted[1];

                    boolean acceptMessage = acceptMessage(t, tPrime);

                    if (acceptMessage) {
                        Path path10 = Paths.get(root.toAbsolutePath() + "/Default/Storage/decryptedMessage.txt");
                        Files.write(path10, m);
                        System.out.println("Message accepted and written to 'decryptedMessage.txt'");
                        System.out.println("Message written to file: ");
                        String mString = new String(m, "UTF-8");
                        System.out.println(mString);

                    } else {
                        System.out.println("Message not accepted, something went wrong");
                        System.out.println("Message received was: ");
                        String mString = new String(m, "UTF-8");
                        System.out.println(mString);
                    }
                    System.out.println();

                    byte[] tag = generateTag(m, pw2);
                    Path path100 = Paths.get(root.toAbsolutePath() + "/Default/Storage/tag.txt");
                    Files.write(path100, tag);

                    boolean validR3 = false;
                    while (!validR3) {
                        System.out.print("Would you like to continue using using the program? ('y' or 'n') : ");
                        String response3 = input.nextLine().toLowerCase(Locale.ROOT);
                        if ("y".equals(response3)) {
                            validR3 = true;
                        } else if ("n".equals(response3)) {
                            keepGoing = false;
                            validR3 = true;
                            input.close();
                        } else {
                            System.out.println("please enter 'y' or 'n'");
                        }
                    }

                    break;
                case "4":

                    System.out.println();
                    System.out.println("Chose 4");

                    generatePairToFiles();

                    System.out.println("Pair has been generated and stored in file");
                    System.out.println();

                    boolean validR4 = false;
                    while (!validR4) {
                        System.out.print("Would you like to continue using using the program? ('y' or 'n') : ");
                        String response4 = input.nextLine().toLowerCase(Locale.ROOT);
                        if ("y".equals(response4)) {
                            validR4 = true;
                        } else if ("n".equals(response4)) {
                            keepGoing = false;
                            validR4 = true;
                            input.close();
                        } else {
                            System.out.println("please enter 'y' or 'n'");
                        }
                    }

                    break;
                case "5":

                    System.out.println("Chose 5");
                    System.out.println();

                    System.out.print("Would you like to use file input or console input? ('1' for file, '2' for console) ");
                    String choice = input.nextLine();

                    byte[] data2 = {};
                    boolean choseValid = false;
                    while (!choseValid) {
                        if ("1".equals(choice)) {
                            System.out.println("chose 1");
                            System.out.println("Please make sure that your message is saved into inputFile.txt");
                            System.out.println("and your passphrase is stored in pw.txt, then press enter:");
                            input.nextLine();
                            Path path11 = Paths.get(root.toAbsolutePath() + "/Default/Storage/inputFile.txt");
                            data2 = Files.readAllBytes(path11);
                            choseValid = true;
                        } else if ("2".equals(choice)) {
                            System.out.println("chose 2");
                            System.out.print("Please enter your message: ");
                            String inputString = input.nextLine();
                            data2 = inputString.getBytes();
                            System.out.println("Please make sure that your passphrase is stored in pw.txt, then press enter:");
                            input.nextLine();
                            choseValid = true;
                        } else {
                            System.out.println("please choose a valid option");
                            System.out.println();
                            System.out.print("Would you like to use file input or console input? ('1' for file, '2' for console) ");
                            choice = input.nextLine();
                        }

                    }

                    generatePairToFiles();

                    Path path13 = Paths.get(root.toAbsolutePath() + "/Default/Storage/ECPoint/x.txt");
                    byte[] xBytes = Files.readAllBytes(path13);
                    BigInteger x = new BigInteger(xBytes);

                    Path path14 = Paths.get(root.toAbsolutePath() + "/Default/Storage/ECPoint/y.txt");
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

                    Path path16 = Paths.get(root.toAbsolutePath() + "/Default/Storage/encryptedEC/ECPoint/x.txt");
                    Files.write(path16, xArr2);

                    Path path17 = Paths.get(root.toAbsolutePath() + "/Default/Storage/encryptedEC/ECPoint/y.txt");
                    Files.write(path17, yArr2);

                    Path path18 = Paths.get(root.toAbsolutePath() + "/Default/Storage/encryptedEC/c.txt");
                    Files.write(path18, c2);

                    Path path19 = Paths.get(root.toAbsolutePath() + "/Default/Storage/encryptedEC/t.txt");
                    Files.write(path19, t2);

                    System.out.println("Cryptogram has been stored into files");
                    System.out.println();

                    boolean validR5 = false;
                    while (!validR5) {
                        System.out.print("Would you like to continue using using the program? ('y' or 'n') : ");
                        String response5 = input.nextLine().toLowerCase(Locale.ROOT);
                        if ("y".equals(response5)) {
                            validR5 = true;
                        } else if ("n".equals(response5)) {
                            keepGoing = false;
                            validR5 = true;
                            input.close();
                        } else {
                            System.out.println("please enter 'y' or 'n'");
                        }
                    }

                    break;
                case "6":

                    System.out.println("Chose 6");
                    System.out.println();

                    System.out.println("Please make sure that your passphrase is stored in pw2.txt, then press enter:");
                    input.nextLine();

                    Path path20 = Paths.get(root.toAbsolutePath() + "/Default/Storage/encryptedEC/ECPoint/x.txt");
                    byte[] x3 = Files.readAllBytes(path20);
                    BigInteger x3Big = new BigInteger(x3);

                    Path path21 = Paths.get(root.toAbsolutePath() + "/Default/Storage/encryptedEC/ECPoint/y.txt");
                    byte[] y3 = Files.readAllBytes(path21);
                    BigInteger y3Big = new BigInteger(y3);

                    Path path22 = Paths.get(root.toAbsolutePath() + "/Default/Storage/encryptedEC/c.txt");
                    byte[] c3 = Files.readAllBytes(path22);

                    Path path23 = Paths.get(root.toAbsolutePath() + "/Default/Storage/encryptedEC/t.txt");
                    byte[] t3 = Files.readAllBytes(path23);

                    ECPoint currentPoint = new ECPoint(x3Big, y3Big);

                    Cryptogram currentCrypt = new Cryptogram(currentPoint, c3, t3);

                    Path path24 = Paths.get(root.toAbsolutePath() + "/Default/Storage/pw2.txt");
                    byte[] pwTwo = Files.readAllBytes(path24);

                    ReturnMessage returnMessage = decryptEC(currentCrypt, pwTwo);

                    byte[] m2 = returnMessage.m;
                    boolean verified = returnMessage.verified;
                    if (verified) {
                        Path path25 = Paths.get(root.toAbsolutePath() + "/Default/Storage/decryptedMessage.txt");
                        Files.write(path25, m2);
                        System.out.println("Message accepted and written to decryptedMessage.txt");
                        System.out.println("Message written to file: ");
                        String mString = new String(m2, "UTF-8");
                        System.out.println(mString);
                        System.out.println();
                    } else {
                        System.out.println("Message not accepted, something went wrong");
                        System.out.println("Message received was: ");
                        String mString = new String(m2, "UTF-8");
                        System.out.println(mString);
                        System.out.println();
                    }

                    boolean validR6 = false;
                    while (!validR6) {
                        System.out.print("Would you like to continue using using the program? ('y' or 'n') : ");
                        String response6 = input.nextLine().toLowerCase(Locale.ROOT);
                        if ("y".equals(response6)) {
                            validR6 = true;
                        } else if ("n".equals(response6)) {
                            keepGoing = false;
                            validR6 = true;
                            input.close();
                        } else {
                            System.out.println("please enter 'y' or 'n'");
                        }
                    }

                    break;
                case "7":
                    System.out.println("Chose 7");

                    Path path26 = Paths.get(root.toAbsolutePath() + "/Default/Storage/decryptedMessage.txt");
                    byte[] inputText = Files.readAllBytes(path26);

                    Path path27 = Paths.get(root.toAbsolutePath() + "/Default/Storage/pw.txt");
                    byte[] firstPW = Files.readAllBytes(path27);

                    Signature signature = genSignature(inputText, firstPW);

                    Path path28 = Paths.get(root.toAbsolutePath() + "/Default/Storage/FileSignature/h.txt");
                    Files.write(path28, signature.h.toByteArray());

                    Path path29 = Paths.get(root.toAbsolutePath() + "/Default/Storage/FileSignature/z.txt");
                    Files.write(path29, signature.z.toByteArray());

                    System.out.println();
                    System.out.println("Signature of decryptedMessage.txt has been stored to files");
                    System.out.println();

                    boolean validR7 = false;
                    while (!validR7) {
                        System.out.print("Would you like to continue using using the program? ('y' or 'n') : ");
                        String response7 = input.nextLine().toLowerCase(Locale.ROOT);
                        if ("y".equals(response7)) {
                            validR7 = true;
                        } else if ("n".equals(response7)) {
                            keepGoing = false;
                            validR7 = true;
                            input.close();
                        } else {
                            System.out.println("please enter 'y' or 'n'");
                        }
                    }

                    break;
                case "8":
                    System.out.println("Chose 8");

                    Path path30 = Paths.get(root.toAbsolutePath() + "/Default/Storage/FileSignature/h.txt");
                    byte[] h = Files.readAllBytes(path30);
                    BigInteger bigH = new BigInteger(h);

                    Path path31 = Paths.get(root.toAbsolutePath() + "/Default/Storage/FileSignature/z.txt");
                    byte[] z = Files.readAllBytes(path31);
                    BigInteger bigZ = new BigInteger(z);

                    Signature signature1 = new Signature(bigH, bigZ);


                    Path path32 = Paths.get(root.toAbsolutePath() + "/Default/Storage/decryptedMessage.txt");
                    byte[] decryptedMessage = Files.readAllBytes(path32);


                    Path path33 = Paths.get(root.toAbsolutePath() + "/Default/Storage/ECPoint/x.txt");
                    byte[] xByteArr = Files.readAllBytes(path33);
                    BigInteger xBig = new BigInteger(xByteArr);

                    Path path34 = Paths.get(root.toAbsolutePath() + "/Default/Storage/ECPoint/y.txt");
                    byte[] yByteArr = Files.readAllBytes(path34);
                    BigInteger yBig = new BigInteger(yByteArr);

                    ECPoint V = new ECPoint(xBig, yBig);

                    boolean verifySignature = verifySignature(signature1, decryptedMessage, V);

                    if (verifySignature) {
                        System.out.println();
                        System.out.println("Signature of decryptedMessage.txt verified, you may accept the message");
                        System.out.println();
                    } else {
                        System.out.println();
                        System.out.println("Signature verification of decryptedMessage failed, do not accept the message");
                    }

                    boolean validR8 = false;
                    while (!validR8) {
                        System.out.print("Would you like to continue using using the program? ('y' or 'n') : ");
                        String response8 = input.nextLine().toLowerCase(Locale.ROOT);
                        if ("y".equals(response8)) {
                            validR8 = true;
                        } else if ("n".equals(response8)) {
                            keepGoing = false;
                            validR8 = true;
                            input.close();
                        } else {
                            System.out.println("please enter 'y' or 'n'");
                        }
                    }

                    break;
                case "9":
                    System.out.println("Chose 9");
                    keepGoing = false;
                    input.close();
                    break;
            }


        }
        System.out.println();
        System.out.println("Thanks for trying our encryption program!");
    }
    public static void generatePairToFiles() throws IOException {

        Path root = Paths.get("");

        Path path10 = Paths.get(root.toAbsolutePath() + "/Default/Storage/pw.txt");
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

        Path path = Paths.get(root.toAbsolutePath() + "/Default/Storage/ECPoint/EncryptedS/ECPoint/x.txt");
        Files.write(path, x2Bytes);
        Path path2 = Paths.get(root.toAbsolutePath() + "/Default/Storage/ECPoint/EncryptedS/ECPoint/y.txt");
        Files.write(path2, y2Bytes);
        Path path3 = Paths.get(root.toAbsolutePath() + "/Default/Storage/ECPoint/EncryptedS/ECPoint/c.txt");
        Files.write(path3, c);
        Path path4 = Paths.get(root.toAbsolutePath() + "/Default/Storage/ECPoint/EncryptedS/ECPoint/t.txt");
        Files.write(path4, t);

        Path path11 = Paths.get(root.toAbsolutePath() + "/Default/Storage/ECPoint/s.txt");
        Files.write(path11, s);

        Path path12 = Paths.get(root.toAbsolutePath() + "/Default/Storage/ECPoint/x.txt");
        Files.write(path12, xArr);

        Path path13 = Paths.get(root.toAbsolutePath() + "/Default/Storage/ECPoint/y.txt");
        Files.write(path13, yArr);

    }
}

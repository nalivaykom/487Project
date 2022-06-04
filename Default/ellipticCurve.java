import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.SQLOutput;
import java.util.BitSet;
import java.util.Scanner;

public class ellipticCurve extends KMACXOF256 {

    //private static byte[] s;

    private static int iteration;
    public static void main(String[] args) throws IOException {
//        BigInteger[] testBig1 = new BigInteger[2];
////        testBig1[0] = new BigInteger("9");
////        testBig1[1] = new BigInteger("4");
//        testBig1[0] = new BigInteger("3");
//        testBig1[1] = new BigInteger("3");
//
//        BigInteger[] testBig2 = new BigInteger[2];
////        testBig2[0] = new BigInteger("3");
////        testBig2[1] = new BigInteger("7");
//        testBig2[0] = new BigInteger("2");
//        testBig2[1] = new BigInteger("5");
//
//        BigInteger[] returnBig = addPoints(testBig1, testBig2);
//        System.out.println("returnBig[0]: " + returnBig[0]);
//        System.out.println("returnBig[1]: " + returnBig[1]);


//
//        int[] returnBytes = scalarMultiply(testBytes, testBig1);
//        for (int i = 0; i < returnBytes.length; i++) {
//            System.out.println("returnBytes[" + i + "] = " + returnBytes[i]);
//        }


        String abc = "1000100101001110111010100000010010010010010001010111011001000111000011000100010000001111010110101110111111001111100011001011111101000000111001100100000000111001110101110011101001111110111011101010111010111101101010100100011010111100111001110011010101001010000011101001100111111101001100011100100000110011100000100111110010001111001011111001101010111001101011010100001101001110001101011001101101111001101100000111111001101001101011001001111001100011001010010100010010101111000010001111111100110010011100000000000100";
        System.out.println("abc length = " + abc.length());

        byte[] testBytes = {1,2,3,4,56,23,12,5,6};
        ECPoint results = generatePair(testBytes);
        System.out.println("results[0][0] = " + results.getS());
        System.out.println("results[1][0] = " + results.getX());
        System.out.println("results[1][1] = " + results.getY());



        Scanner input = new Scanner(System.in);
        System.out.print("Type a message you would like to encrypt: ");
        String userInput = input.nextLine();

        //String userInput = "hello my man, jennifer is an odd human being";
        System.out.println("Message before encryption: " + userInput);

        byte[] testInt = new byte[userInput.length()];

        for (int i = 0; i < userInput.length(); i++) {
            testInt[i] = (byte) userInput.charAt(i);
            System.out.println("testInt[" + i + "] = " + testInt[i]);
        }

//        byte[] key = {1,2,3,4,75};
//        byte[] key2 = {1,2,3,4,75};
        Cryptogram testCrypt = encryptEC(testInt, results);
        ReturnMessage testRM = decryptEC(testCrypt, testBytes);
        System.out.println(testRM.verified);
        String finalOutput = "";
        try {
            finalOutput = new String(testRM.m, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("got to where finalOutput should be printed");
        System.out.println(finalOutput);


//        BigInteger p = new BigInteger("2");
//        p = p.pow(521);
//        p.subtract(new BigInteger("1"));
//
//
//        BigInteger[] z = new BigInteger[2];
//        z[0] = new BigInteger("5");
//        z[1] = new BigInteger("1");
//
//        BigInteger[] z2 = new BigInteger[2];
//        z2[0] = new BigInteger("81");
//        z2[1] = new BigInteger("27");
//
//        BigInteger[] zz = addPoints(z, z);
//        System.out.println("zz[0] = " + zz[0]);
//        System.out.println("zz[1] = " + zz[1]);

//        BigInteger[] zzz = addPoints(z, zz);
//        System.out.println("zzz[0] = " + zz[0]);
//        System.out.println("zzz[1] = " + zz[1]);





//        ECPoint G = new ECPoint(BigInteger.valueOf(4L));
//        String name = "G";
//        printECPoint(name, G);
//
//        ECPoint G2 = addPoints(G, G);
//        String name2 = "G2";
//        printECPoint(name2, G2);
//
//        ECPoint G3 = scalarMultiply(BigInteger.valueOf(2L), G);
//        String name3 = "G3";
//        printECPoint(name3, G3);



    }

    public static Signature genSignature(byte[] m, byte[] pw) {
        byte[] s = KMACXOF256(pw, new byte[] {}, 512, "K");
        byte[] newS = new byte[65];
        System.arraycopy(s, 0, newS, 1, s.length);
        newS[0] = (byte) 0;
        BigInteger newSBig = new BigInteger(newS);

        byte[] k = KMACXOF256(newS, m, 512, "N");
        byte[] newK = new byte[65];
        System.arraycopy(k, 0, newK, 1, k.length);
        newK[0] = (byte) 0;

        ECPoint G = new ECPoint(BigInteger.valueOf(4L));
        BigInteger newKBig = new BigInteger(newK);

        ECPoint U = scalarMultiply(newKBig, G);
        BigInteger Ux = U.x;
        byte[] UxBytes = Ux.toByteArray();

        byte[] h = KMACXOF256(UxBytes, m, 512, "T");
        byte[] newH = new byte[65];
        System.arraycopy(h, 0, newH, 1, h.length);
        newH[0] = (byte) 0;
        BigInteger newHBig = new BigInteger(newH);
        //BigInteger r = new


        return new Signature(new byte[] {}, BigInteger.ONE);
    }

    public static ReturnMessage decryptEC(Cryptogram Crypt, byte[] pw) {

        byte[] s = KMACXOF256(pw, new byte[] {}, 512, "K");
        byte[] newS = new byte[65];
        System.arraycopy(s, 0, newS, 1, s.length);
        newS[0] = (byte) 0;

//        String myBinaryS = byteArrayToBinaryString(newS);
//        System.out.println("myBinaryS = " + myBinaryS);
//        BigInteger tempBig = new BigInteger(myBinaryS, 2);
//        System.out.println(tempBig);

        BigInteger bigNewS = new BigInteger(newS); //this here be the spot
//        System.out.println(sInt2);
        bigNewS = bigNewS.multiply(BigInteger.valueOf(4L));
//        System.out.println(sInt2);

        ECPoint W = scalarMultiply(bigNewS, Crypt.Z);
        BigInteger Wx = W.x;
        byte[] WxBytes = Wx.toByteArray();

        byte[] ke_ka = KMACXOF256(WxBytes, new byte[] {}, 1024, "P");
        byte[] ke = new byte[ke_ka.length / 2];
        byte[] ka = new byte[ke_ka.length / 2];

        System.arraycopy(ke_ka, 0, ke, 0, ke.length);
        System.arraycopy(ke_ka, ke.length, ka, 0, ka.length);

        byte[] mTemp = KMACXOF256(ke, new byte[] {}, Crypt.c.length * 8, "PKE");
        byte[] m = new byte[mTemp.length];

        for (int i = 0; i < m.length; i++) {
            long mTempLong = (long) mTemp[i];
            long testIntLong = (long) Crypt.c[i];
            m[i] = (byte) (mTempLong ^ testIntLong);
        }

        byte[] tPrime = KMACXOF256(ka, m, 512, "PKA");

        boolean verified = acceptMessage(Crypt.t, tPrime);
        for (int i = 0; i < m.length; i++) {
            System.out.println("m[" + i + "] = " + m[i]);
        }
        return new ReturnMessage(m, verified);
    }

    public static Cryptogram encryptEC(byte[] testInt, ECPoint V) {
        SecureRandom random = new SecureRandom();
        byte[] k = new byte[65];
        random.nextBytes(k);
        k[0] = (byte) 0;
        for (int g = 0; g < k.length; g++) {
            System.out.println("k[" + g + "] = " + k[g]);
        }

        //String bigStringBinary = byteArrayToBinaryString(k);
//        BigInteger bigK = new BigInteger(k);
        //BigInteger bigK = new BigInteger(bigStringBinary, 2);

        BigInteger bigK = new BigInteger(k);

        System.out.println("bigK = " + bigK);
        BigInteger bigKx4 = bigK.multiply(BigInteger.valueOf(4L));

        ECPoint W = scalarMultiply(bigKx4, V);

        ECPoint G = new ECPoint(BigInteger.valueOf(4L));
        ECPoint Z = scalarMultiply(bigKx4, G);

        BigInteger Wx = W.x;
        byte[] WxBytes = Wx.toByteArray();

        byte[] ke_ka = KMACXOF256(WxBytes, new byte[] {}, 1024, "P");
        byte[] ke = new byte[ke_ka.length / 2];
        byte[] ka = new byte[ke_ka.length / 2];

        System.arraycopy(ke_ka, 0, ke, 0, ke.length);
        System.arraycopy(ke_ka, ke.length, ka, 0, ka.length);

        byte[] cTemp = KMACXOF256(ke, new byte[] {}, testInt.length * 8, "PKE");
        byte[] c = new byte[cTemp.length];

        for (int i = 0; i < c.length; i++) {
            long cTempLong = (long) cTemp[i];
            long testIntLong = (long) testInt[i];
            c[i] = (byte) (cTempLong ^ testIntLong);
        }

        byte[] t = KMACXOF256(ka, testInt, 512, "PKA");

        return new Cryptogram(Z, c, t);
    }

    public static ECPoint generatePair(byte[] pw) {
        //creates points to global variable
        byte[] s = KMACXOF256(pw, new byte[] {}, 512, "K");
        byte[] newS = new byte[65];
        System.arraycopy(s, 0, newS, 1, s.length);
        newS[0] = (byte) 0;

        System.out.println(newS[0]);
        System.out.println(newS[1]);

        BigInteger sInt2 = new BigInteger(newS);
//        System.out.println(sInt2);
        sInt2 = sInt2.multiply(BigInteger.valueOf(4L));
//        System.out.println(sInt2);


//        BigInteger[] G = new BigInteger[2];
//        G[0] = new BigInteger("4");
//        G[1] = new BigInteger("16");

        ECPoint G = new ECPoint(BigInteger.valueOf(4L));

        //BigInteger[][] return2dBig = new BigInteger[2][];
        //return2dBig[0] = sArr;
        //return2dBig[1] = scalarMultiply(sInt2, G);

        ECPoint returnPoint = scalarMultiply(sInt2, G);
        returnPoint.s = s;
        return returnPoint;
    }
     public static ECPoint scalarMultiply(BigInteger big, ECPoint P) {
         // s = (sk sk-1 … s1 s0)2, sk = 1.

         int k = big.bitLength();
         ECPoint V = new ECPoint(BigInteger.ZERO, BigInteger.ONE); // initialize with sk*P, which is simply P
         for (int i = k - 1; i >= 0; i--) { // scan over the k bits of s
             V = addPoints(V, V); //V = V.add(V); // invoke the Edwards point addition formula
             if (big.testBit(i)) { //if (resultBig.charAt(i) == '1') { // test the i-th bit of s
                 V = addPoints(V, P); //V = V.add(P); // invoke the Edwards point addition formula
             }
         }
         return V; // now finally V = s*P
     }

//    public static ECPoint scalarMultiply(BigInteger big, ECPoint P) {
//        // s = (sk sk-1 … s1 s0)2, sk = 1.
//
//        String resultBig = big.toString(2);
//        System.out.println("resultBig = " + resultBig);
//        if ('-' == resultBig.charAt(0)) {
//            resultBig = resultBig.substring(1);
//        }
////         System.out.println(resultBig);
//
//        int k = resultBig.length();
//        ECPoint res = new ECPoint(BigInteger.ZERO, BigInteger.ONE);
//        ECPoint temp = P; // initialize with sk*P, which is simply P
//        for (int i = 0; i < k; i++) { // scan over the k bits of s
//            temp = addPoints(temp, temp); //V = V.add(V); // invoke the Edwards point addition formula
//            if (resultBig.charAt(i) == '1') { // test the i-th bit of s
//                //System.out.println("////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
//                res = addPoints(res, temp); //V = V.add(P); // invoke the Edwards point addition formula
//            }
////            temp = addPoints(temp, temp); //V = V.add(V); // invoke the Edwards point addition formula
//        }
//        return res; // now finally V = s*P
//    }

    //assuming done
    public static ECPoint addPoints(ECPoint p1, ECPoint p2) {
        BigInteger p = new BigInteger("2");
        p = p.pow(521);
        p = p.subtract(new BigInteger("1"));

//        System.out.println("p = " + p);
//        BigInteger p = new BigInteger("31");
        
        BigInteger x1 = p1.x;
        BigInteger y1 = p1.y;

        BigInteger x2 = p2.x;
        BigInteger y2 = p2.y;

        BigInteger x3a = (x1.multiply(y2)).add(y1.multiply(x2));
//        System.out.println("x3a = " + x3a);
        BigInteger x3b = ( new BigInteger("1") ).add( (new BigInteger("-376014")).multiply( (x1.multiply(x2.multiply(y1.multiply(y2))))));
//        System.out.println("x3b = " + x3b);
//        System.out.println("x3b.modinvere(p) = " + x3b.modInverse(p));
//        System.out.println("x3a.multiply(x3b.modInverse(p)) = " + x3a.multiply(x3b.modInverse(p)));
//        System.out.println("x3a.multiply(x3b.modInverse(p)).mod(p) = " + x3a.multiply(x3b.modInverse(p)).mod(p));
        BigInteger x3 = x3a.multiply(x3b.modInverse(p)).mod(p); //wtf is p
        //BigInteger x3 = x3a.divide(x3b);
        //System.out.println("x3 = " + x3);

        BigInteger y3a = (y1.multiply(y2)).subtract(x1.multiply(x2));
//        System.out.println("y3a = " + y3a);
        BigInteger y3b = ( new BigInteger("1") ).subtract( (new BigInteger("-376014")).multiply( (x1.multiply(x2.multiply(y1.multiply(y2))))));
//        System.out.println("y3b = " + y3b);
        BigInteger y3 = y3a.multiply(y3b.modInverse(p)).mod(p); //wtf is p
        //BigInteger y3 = y3a.divide(y3b);

        ECPoint returnPoint = new ECPoint(x3, y3);

//        System.out.println("returnBig[0] = " + returnBig[0]);
//        System.out.println("returnBig[1] = " + returnBig[1]);
//        System.out.println("this was iteration: " + iteration);
//        iteration++;
        return returnPoint;
    }

    public static String byteArrayToBinaryString(byte[] bArr) {
        String returnString = "";

        for (int i = 0; i < bArr.length; i++) {
            String tempStr;
            int tempInt;
            tempInt = bArr[i];
            tempStr = Integer.toBinaryString(tempInt);
            while (tempStr.length() < 8) {
                tempStr = "0" + tempStr;
            }
            if (tempStr.length() > 8) {
                tempStr = tempStr.substring(tempStr.length() - 8, tempStr.length());
            }
            System.out.println("tempInt[" + i + "] = " + tempInt);
            System.out.println("tempStr[" + i + "] = " + tempStr);
            returnString = returnString + tempStr;
        }

        return returnString;
    }

    public static void printECPoint(String str, ECPoint point) {
        System.out.print(str + " = ");
        System.out.println("{" + point.x + ", " + point.y + "}");
    }

























//    int numBytes = s.length;
//    int k = (numBytes * 8);
//    int[] bits = new int[k];
//         System.out.println("numBytes = " + numBytes);
//         for (int j = 0; j < numBytes; j++) {
//        byte temp = s[j];
//        //System.out.println("temp = " + temp);
//        int tempInt = (int) temp;
//        //System.out.println("tempInt = " + tempInt);
//        String tempStr = Integer.toBinaryString(tempInt);
//        //System.out.println("tempStr = " + tempStr);
//        while (tempStr.length() < 8) {
//            tempStr = "0" + tempStr;
//        }
//        if (tempStr.length() > 8) {
//            tempStr = tempStr.substring(tempStr.length() - 8, tempStr.length());
//        }
//        System.out.println("binary string: " + tempStr);
//        for (int a = 0; a < 8; a++) {
//            if (tempStr.charAt(a) == 48) {
//                bits[(j * 8) + a] = 0;
//            } else if (tempStr.charAt(a) == 49) {
//                bits[(j * 8) + a] = 1;
//            }
//        }
//    }
}

import java.math.BigInteger;
public class ellipticCurve extends KMACXOF256 {

    //private static byte[] s;

    private static int iteration;
    public static void main(String[] args) {
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

        byte[] testBytes = {1,2,3,4,56,23,12,5,6};
        BigInteger[][] results = generatePair(testBytes);
        System.out.println("results[0][0] = " + results[0][0]);
        System.out.println("results[1][0] = " + results[1][0]);
        System.out.println("results[1][1] = " + results[1][1]);



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



    }



    public static BigInteger[][] generatePair(byte[] pw) {
        //creates points to global variable
        byte[] s = KMACXOF256(pw, new byte[] {}, 512, "K");

        BigInteger sInt2 = new BigInteger(s);
        System.out.println(sInt2);
        sInt2 = sInt2.multiply(BigInteger.valueOf(4L));
        System.out.println(sInt2);
        BigInteger[] sArr = new BigInteger[1];
        sArr[0] = sInt2;

        BigInteger[] G = new BigInteger[2];
        G[0] = new BigInteger("4");
        G[1] = new BigInteger("16");

        BigInteger[][] return2dBig = new BigInteger[2][];
        return2dBig[0] = sArr;
        return2dBig[1] = scalarMultiply(sInt2, G);
        return return2dBig;
    }
     public static BigInteger[] scalarMultiply(BigInteger big, BigInteger[] P) {
         // s = (sk sk-1 … s1 s0)2, sk = 1.

         String resultBig = big.toString(2);
         System.out.println(resultBig);
         if ('-' == resultBig.charAt(0)) {
             resultBig = resultBig.substring(1);
         }
         System.out.println(resultBig);

         int k = resultBig.length();

         BigInteger[] V = P; // initialize with sk*P, which is simply P
         for (int i = k - 1; i >= 0; i--) { // scan over the k bits of s
             V = addPoints(V, V); //V = V.add(V); // invoke the Edwards point addition formula
             if (resultBig.charAt(i) == '1') { // test the i-th bit of s
                 System.out.println("////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
                 V = addPoints(V, P); //V = V.add(P); // invoke the Edwards point addition formula
             }
         }
         return V; // now finally V = s*P
     }

    //assuming done
    public static BigInteger[] addPoints(BigInteger[] X, BigInteger[] Y) {
        BigInteger p = new BigInteger("2");
        p = p.pow(521);
        p = p.subtract(new BigInteger("1"));

        System.out.println("p = " + p);
//        BigInteger p = new BigInteger("31");
        
        BigInteger x1 = X[0];
        BigInteger y1 = X[1];

        BigInteger x2 = Y[0];
        BigInteger y2 = Y[1];

        BigInteger x3a = (x1.multiply(y2)).add(y1.multiply(x2));
        System.out.println("x3a = " + x3a);
        BigInteger x3b = ( new BigInteger("1") ).add( (new BigInteger("-376014")).multiply( (x1.multiply(x2.multiply(y1.multiply(y2))))));
        System.out.println("x3b = " + x3b);
        System.out.println("x3b.modinvere(p) = " + x3b.modInverse(p));
        System.out.println("x3a.multiply(x3b.modInverse(p)) = " + x3a.multiply(x3b.modInverse(p)));
        System.out.println("x3a.multiply(x3b.modInverse(p)).mod(p) = " + x3a.multiply(x3b.modInverse(p)).mod(p));
        BigInteger x3 = x3a.multiply(x3b.modInverse(p)).mod(p); //wtf is p
        //BigInteger x3 = x3a.divide(x3b);
        //System.out.println("x3 = " + x3);

        BigInteger y3a = (y1.multiply(y2)).subtract(x1.multiply(x2));
        System.out.println("y3a = " + y3a);
        BigInteger y3b = ( new BigInteger("1") ).subtract( (new BigInteger("-376014")).multiply( (x1.multiply(x2.multiply(y1.multiply(y2))))));
        System.out.println("y3b = " + y3b);
        BigInteger y3 = y3a.multiply(y3b.modInverse(p)).mod(p); //wtf is p
        //BigInteger y3 = y3a.divide(y3b);

        BigInteger[] returnBig = new BigInteger[2];
        returnBig[0] = x3;
        returnBig[1] = y3;
        System.out.println("returnBig[0] = " + returnBig[0]);
        System.out.println("returnBig[1] = " + returnBig[1]);
        System.out.println("this was iteration: " + iteration);
        iteration++;
        return returnBig;
    }
    public static BigInteger sqrt(BigInteger v, BigInteger p, boolean lsb) {
        assert (p.testBit(0) && p.testBit(1)); // p = 3 (mod 4)
        if (v.signum() == 0) {
            return BigInteger.ZERO;
        }
        BigInteger r = v.modPow(p.shiftRight(2).add(BigInteger.ONE), p);
        if (r.testBit(0) != lsb) {
            r = p.subtract(r); // correct the lsb
        }
        return (r.multiply(r).subtract(v).mod(p).signum() == 0) ? r : null;
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

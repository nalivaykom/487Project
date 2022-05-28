import java.math.BigInteger;

public class ellipticCurve extends KMACXOF256{
    public static void main(String[] args) {
        BigInteger[] testBig1 = new BigInteger[2];
        testBig1[0] = new BigInteger("9");
        testBig1[1] = new BigInteger("3");

        BigInteger[] testBig2 = new BigInteger[2];
        testBig2[0] = new BigInteger("4");
        testBig2[1] = new BigInteger("7");

        BigInteger[] returnBig = addPoints(testBig1, testBig2);
        System.out.println("returnBig[0]: " + returnBig[0]);
        System.out.println("returnBig[1]: " + returnBig[1]);

    }
             
    public static void generatePair(byte[] pw) {
        //creates points to global variable
        byte[] s = KMACXOF256(pw, new byte[] {}, 512, "K");
        
    }
    // public static BigInteger[] scalarMultiply(byte[] s, BigInteger[] P) {
    //     // s = (sk sk-1 … s1 s0)2, sk = 1.
    //     BigInteger[] V = P; // initialize with sk*P, which is simply P
    //     for (int i = k - 1; i >= 0; i--) { // scan over the k bits of s
    //         //V = V.add(V); // invoke the Edwards point addition formula
    //         if (si == 1) { // test the i-th bit of s
    //             //V = V.add(P); // invoke the Edwards point addition formula
    //         }
    //     }
    //     return V; // now finally V = s*P
    // }
    public static BigInteger[] addPoints(BigInteger[] X, BigInteger[] Y) {
        BigInteger p = new BigInteger("2");
        p = p.pow(521);
        p.subtract(new BigInteger("1"));
        System.out.println("p = " + p);
        BigInteger temp = new BigInteger("11");
        
        BigInteger x1 = X[0];
        BigInteger x2 = X[1];

        BigInteger y1 = Y[0];
        BigInteger y2 = Y[1];

        BigInteger x3a = (x1.multiply(y2)).add(y1.multiply(x2));
        System.out.println("x3a = " + x3a);
        BigInteger x3b = ( new BigInteger("1") ).add( (new BigInteger("-376014")).multiply( (x1.multiply(x2.multiply(y1.multiply(y2))))));
        System.out.println("x3b = " + x3b);
        System.out.println("x3b.modinvere(p) = " + x3b.modInverse(p));
        BigInteger x3 = x3a.multiply(x3b.modInverse(p)).mod(p); //wtf is p
        System.out.println("x3 = " + x3);

        BigInteger y3a = (y1.multiply(y2)).subtract(x1.multiply(x2));
        BigInteger y3b = ( new BigInteger("1") ).subtract( (new BigInteger("-376014")).multiply( (x1.multiply(x2.multiply(y1.multiply(y2))))));
        BigInteger y3 = y3a.multiply(y3b.modInverse(p)).mod(p); //wtf is p

        BigInteger[] returnBig = new BigInteger[2];
        returnBig[0] = x3;
        returnBig[1] = y3;

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
}

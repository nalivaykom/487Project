import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class ellipticCurve extends KMACXOF256 {

    public static boolean verifySignature(Signature signature, byte[] m,ECPoint V) {

        ECPoint G = new ECPoint(BigInteger.valueOf(4L));

        ECPoint zG = scalarMultiply(signature.z, G);
        ECPoint hV = scalarMultiply(signature.h, V);

        ECPoint U = addPoints(zG, hV);
        BigInteger Ux = U.x;
        byte[] UxBytes = Ux.toByteArray();

        byte[] hPrime = KMACXOF256(UxBytes, m, 512, "T");
        byte[] h = signature.h.toByteArray();

        boolean returnBool = acceptMessage(h, hPrime);

        return returnBool;
    }

    public static Signature genSignature(byte[] m, byte[] pw) {
        byte[] s = KMACXOF256(pw, new byte[] {}, 512, "K");
        byte[] newS = new byte[65];
        System.arraycopy(s, 0, newS, 1, s.length);
        newS[0] = (byte) 0;
        BigInteger newSBig = new BigInteger(newS);
        newSBig = newSBig.multiply(BigInteger.valueOf(4L));

        byte[] k = KMACXOF256(newS, m, 512, "N");
        byte[] newK = new byte[65];
        System.arraycopy(k, 0, newK, 1, k.length);
        newK[0] = (byte) 0;

        ECPoint G = new ECPoint(BigInteger.valueOf(4L));
        BigInteger newKBig = new BigInteger(newK);
        newKBig = newKBig.multiply(BigInteger.valueOf(4L));

        ECPoint U = scalarMultiply(newKBig, G);
        BigInteger Ux = U.x;
        byte[] UxBytes = Ux.toByteArray();

        byte[] h = KMACXOF256(UxBytes, m, 512, "T");
        byte[] newH = new byte[65];
        System.arraycopy(h, 0, newH, 1, h.length);
        newH[0] = (byte) 0;
        BigInteger newHBig = new BigInteger(newH);
        BigInteger r = new BigInteger("2");
        r = r.pow(519);
        r = r.subtract(new BigInteger("337554763258501705789107630418782636071904961214051226618635150085779108655765"));

        BigInteger hs = newHBig.multiply(newSBig);
        BigInteger kMinusHS = newKBig.subtract(hs);
        BigInteger z = kMinusHS.mod(r);

        Signature returnSignature = new Signature(newHBig, z);

        return returnSignature;
    }

    public static ReturnMessage decryptEC(Cryptogram Crypt, byte[] pw) {

        byte[] s = KMACXOF256(pw, new byte[] {}, 512, "K");
        byte[] newS = new byte[65];
        System.arraycopy(s, 0, newS, 1, s.length);
        newS[0] = (byte) 0;

        BigInteger bigNewS = new BigInteger(newS); //this here be the spot
        bigNewS = bigNewS.multiply(BigInteger.valueOf(4L));

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

        return new ReturnMessage(m, verified);
    }

    public static Cryptogram encryptEC(byte[] testInt, ECPoint V) {
        SecureRandom random = new SecureRandom();
        byte[] k = new byte[65];
        random.nextBytes(k);
        k[0] = (byte) 0;

        BigInteger bigK = new BigInteger(k);

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
        byte[] s = KMACXOF256(pw, new byte[] {}, 512, "K");
        byte[] newS = new byte[65];
        System.arraycopy(s, 0, newS, 1, s.length);
        newS[0] = (byte) 0;

        BigInteger sInt2 = new BigInteger(newS);
        sInt2 = sInt2.multiply(BigInteger.valueOf(4L));

        ECPoint G = new ECPoint(BigInteger.valueOf(4L));

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

    public static ECPoint addPoints(ECPoint p1, ECPoint p2) {
        BigInteger p = new BigInteger("2");
        p = p.pow(521);
        p = p.subtract(new BigInteger("1"));
        
        BigInteger x1 = p1.x;
        BigInteger y1 = p1.y;

        BigInteger x2 = p2.x;
        BigInteger y2 = p2.y;

        BigInteger x3a = (x1.multiply(y2)).add(y1.multiply(x2));
        BigInteger x3b = ( new BigInteger("1") ).add( (new BigInteger("-376014")).multiply( (x1.multiply(x2.multiply(y1.multiply(y2))))));
        BigInteger x3 = x3a.multiply(x3b.modInverse(p)).mod(p);

        BigInteger y3a = (y1.multiply(y2)).subtract(x1.multiply(x2));
        BigInteger y3b = ( new BigInteger("1") ).subtract( (new BigInteger("-376014")).multiply( (x1.multiply(x2.multiply(y1.multiply(y2))))));
        BigInteger y3 = y3a.multiply(y3b.modInverse(p)).mod(p);

        ECPoint returnPoint = new ECPoint(x3, y3);

        return returnPoint;
    }
}
import java.math.BigInteger;

public class ECPoint {

    public byte[] s; //only used sometimes
    public BigInteger x;
    public BigInteger y;


    public ECPoint() {
        this.x = BigInteger.ZERO;
        this.y = BigInteger.ONE;
    }

    public ECPoint(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }

    public ECPoint(byte[] s, BigInteger x, BigInteger y) {
        this.s = s;
        this.x = x;
        this.y = y;
    }

    public ECPoint(BigInteger x) {
        this.x = x;
        BigInteger p = new BigInteger("2");
        p = p.pow(521);
        p = p.subtract(new BigInteger("1"));

        BigInteger one = BigInteger.ONE;

        BigInteger xSquared = x.multiply(x);
        BigInteger oneMinusXSquared = (BigInteger.ONE).subtract(xSquared);
        BigInteger d = (BigInteger.valueOf(376014L));
        BigInteger dXSquared = d.multiply(xSquared);
        BigInteger onePlusDXSquared = (BigInteger.ONE).add(dXSquared);

        BigInteger wholeThing = (oneMinusXSquared.multiply(onePlusDXSquared.modInverse(p)).mod(p));

        this.y = sqrt(wholeThing, p, false).mod(p);
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

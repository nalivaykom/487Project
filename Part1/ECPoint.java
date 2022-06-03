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
        System.out.println("p = " + p);

        BigInteger one = BigInteger.ONE;
        System.out.println("one: " + one);

        BigInteger xSquared = x.multiply(x);
        System.out.println("xSquared = " + xSquared);
        BigInteger oneMinusXSquared = (BigInteger.ONE).subtract(xSquared);
        System.out.println("oneMinusXSquared = " + oneMinusXSquared);
        BigInteger d = (BigInteger.valueOf(376014L));
        System.out.println("d = " + d);
        BigInteger dXSquared = d.multiply(xSquared);
        System.out.println("dXSquared = " + dXSquared);
        BigInteger onePlusDXSquared = (BigInteger.ONE).add(dXSquared);
        System.out.println("onePlusDXSquared = " + onePlusDXSquared);

//        BigInteger bMIp = onePlusDXSquared.modInverse(p);
//        BigInteger aMbMIp = oneMinusXSquared.multiply(bMIp);
//        BigInteger aMbMIpmodp = aMbMIp.mod(p);
//        System.out.println("wholeThing different = " + aMbMIpmodp);

        BigInteger wholeThing = (oneMinusXSquared.multiply(onePlusDXSquared.modInverse(p)).mod(p));
        System.out.println("wholeThing = " + wholeThing);

        this.y = sqrt(wholeThing, p, false).mod(p);
        //this.y = new BigInteger("3832365545844557255247407452492276489648660861956878854823428409796421008857878243727634210686791507168493732364650567836638782015285562571568855063494635788");
    }

    public void setS(byte[] s) {
        this.s = s;
    }
    public void setX(BigInteger x) {
        this.x = x;
    }
    public void setY(BigInteger y) {
        this.y = y;
    }

    public byte[] getS() {
        return this.s;
    }
    public BigInteger getX() {
        return this.x;
    }
    public BigInteger getY() {
        return this.y;
    }

    public static BigInteger sqrt(BigInteger v, BigInteger p, boolean lsb) {
        assert (p.testBit(0) && p.testBit(1)); // p = 3 (mod 4)
        if (v.signum() == 0) {
            return BigInteger.ZERO;
        }
        BigInteger r = v.modPow(p.shiftRight(2).add(BigInteger.ONE), p);
        System.out.println("r = " + r);
        if (r.testBit(0) != lsb) {
            r = p.subtract(r); // correct the lsb
        }
        return (r.multiply(r).subtract(v).mod(p).signum() == 0) ? r : null;
    }
}

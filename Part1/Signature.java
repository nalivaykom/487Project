import java.math.BigInteger;

public class Signature {

    public byte[] h;
    BigInteger z;

    public Signature(byte[] h, BigInteger z) {
        this.h = h;
        this.z = z;
    }

}

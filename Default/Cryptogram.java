public class Cryptogram {

    public ECPoint Z;
    public byte[] c;
    public byte[] t;

    public Cryptogram(ECPoint Z, byte[] c, byte[] t) {
        this.Z = Z;
        this.c = c;
        this.t = t;
    }

}

//Comment added by Cliff
//Another one
//Another nother one
public class KMACXOF256 {

    private String KEY = "11110000";
    private enum ENCODE {Left, Right}
    
    public static void main(String[] args) {
        
        //System.out.println("hello world");
        /*
        String str = "Hi my name is Michael and my partner is Cliff";
        byte[] bArr = str.getBytes();
        int temp = bArr.length;
        int i = 0;
        StringBuilder totalString = new StringBuilder();
        //System.out.println(bArr.length);
        while (temp > 0) {
            System.out.println("bArr[" + i + "]: " + bArr[i]);
            System.out.println("bArr[" + i + "] in binary: " + Integer.toString(bArr[i], 2));
            totalString.append(Integer.toString(bArr[i], 2));
            temp--;
            i++;
        }
        System.out.println("totalString: " + totalString);
        */
        
        
        byte[] temp = lrEncode(136, ENCODE.Left);
        for (int i = 0; i < temp.length; i++) {
            System.out.println(temp[i]);
        }
        
    }

    public byte[] KMAC256(byte[] key, byte[] input, int outLength, String str) {
        
        byte[] newX = new byte[bytepad(encodeString(key), 136).length + input.length + lrEncode(outLength, ENCODE.Right).length];
        System.arraycopy(bytepad(encodeString(key), 136), 0, newX, 0, bytepad(encodeString(key), 136).length);
        System.arraycopy(input, 0, newX, bytepad(encodeString(key), 136).length, input.length);
        System.arraycopy(lrEncode(outLength, ENCODE.Right), 0, newX, bytepad(encodeString(key), 136).length + input.length, lrEncode(outLength, ENCODE.Right).length);
        //bytepad(encodeString(key), 136) + input + lrEncode(outLength, ENCODE.Right);

        
  
        //byte[] returnArray = {};
        //return returnArray;
        return cShake256(newX, outLength);
    }

    

    public byte[] cShake256(byte[] input, int outLength) {
        /*
        byte[] returnArray = {};
        return returnArray;
        */
        return KECCAK[512](bytepad(encode_string(N) || encode_string(S), 136) || X || 00, L).
    }

    /**
    * Apply the NIST bytepad primitive to a byte array X with encoding factor w.
    * @param X the byte array to bytepad
    * @param w the encoding factor (the output length must be a multiple of w)
    * @return the byte-padded byte array X with encoding factor w.
    */
    private byte[] bytepad(byte[] X, int outLen) {

        if (outLen > 0) {
            //byte[] wenc = leftEncode(outLen);
            byte[] wenc = lrEncode(outLen, ENCODE.Left);
            byte[] z = new byte[outLen*((wenc.length + X.length + outLen - 1)/outLen)];

            System.arraycopy(wenc, 0, z, 0, wenc.length);
            System.arraycopy(X, 0, z, wenc.length, X.length);
            for (int i = wenc.length + X.length; i < z.length; i++) {
                z[i] = (byte)0;
            }
 
            return z;
        }
        return X;
    }

    private byte[] encodeString(byte[] s) {
        /*
        byte[] returnArray = {};
        return returnArray;
        */
        byte[] returnArray = new byte[lrEncode(s.length, ENCODE.Left).length + s.length];
        System.arraycopy(lrEncode(s.length, ENCODE.Left), 0, returnArray, 0, lrEncode(s.length, ENCODE.Left).length);
        System.arraycopy(s, 0, returnArray, lrEncode(s.length, ENCODE.Left).length, s.length);
        

        return returnArray;
    }

    private byte[] leftEncode(int outLen) {

        byte[] returnArray = {};
        for (int i = 0; i < outLen; i++) {
            returnArray[0] = (byte) 0;
        }
        return returnArray;
    }

    private byte[] rightEncode() {

        byte[] returnArray = {};
        return returnArray;
    }



    private static byte[] lrEncode(long len, ENCODE dir) {
        if (len==0) return dir == ENCODE.Left ? new byte[] {1, 0} : new byte[] {0, 1};
        byte[] buf = new byte[8];
        long l = len;
        int cnt = 0;
        while (l > 0) {
            byte b = (byte) (l & 0xffL);
            l = l>>>(8);
            buf[7 - cnt++] = b; // reverse for appropriate ordering
        }
        byte[] out = new byte[cnt + 1];
        System.arraycopy(buf, 8 - cnt, out, dir == ENCODE.Left ? 1 : 0, cnt);
        out[dir == ENCODE.Left ? 0 : out.length - 1] = (byte) cnt;
        return out;
    }    
/*
    KECCAK[512]() {

    }
*/
}


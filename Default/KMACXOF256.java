import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.*;

//Comment added by Cliff
//Another one
//Another nother one
public class KMACXOF256 {

    private static final long[] roundConstant = {
        0x0000000000000001L, 0x0000000000008082L, 0x800000000000808aL,
        0x8000000080008000L, 0x000000000000808BL, 0x0000000080000001L,
        0x8000000080008081L, 0x8000000000008009L, 0x000000000000008aL,
        0x0000000000000088L, 0x0000000080008009L, 0x000000008000000aL,
        0x000000008000808bL, 0x800000000000008bL, 0x8000000000008089L,
        0x8000000000008003L, 0x8000000000008002L, 0x8000000000000080L,
        0x000000000000800aL, 0x800000008000000aL, 0x8000000080008081L,
        0x8000000000008080L, 0x0000000080000001L, 0x8000000080008008L
    };

    private static final int[][] offset = {
        {25, 39, 3, 10, 43},
        {55, 20, 36, 44, 6},
        {28, 27, 0, 1, 62},
        {56, 14, 18, 2, 61},
        {21, 8, 41, 45, 15}
    };

    private static byte[] KEY = {1, 2, 3, 4};
    private enum ENCODE {Left, Right}

    public static byte[] hash(byte[] m) {
        return KMACXOF256(new byte[] {}, m, 512, "D");
    }

    public static byte[] generateTag(byte[] m, byte[] pw) { return KMACXOF256(pw, m, 512, "T"); }

    public static byte[][] encrypt(byte[] testInt, byte[] key) {

        byte[][] output = new byte[3][];

        SecureRandom random = new SecureRandom();
        byte[] z = new byte[64];
        random.nextBytes(z);

        byte[] z_key = new byte[z.length + key.length]; 
        System.arraycopy(z, 0, z_key, 0, z.length);
        System.arraycopy(key, 0, z_key, z.length, key.length);

        byte[] ke_ka = KMACXOF256(z_key, new byte[] {}, 1024, "S");
        byte[] ke = new byte[ke_ka.length / 2];
        byte[] ka = new byte[ke_ka.length / 2];
        
        System.arraycopy(ke_ka, 0, ke, 0, ke.length);
        System.arraycopy(ke_ka, ke.length, ka, 0, ka.length);

        byte[] cTemp = KMACXOF256(ke, new byte[] {}, testInt.length * 8, "SKE");
        byte[] c = new byte[cTemp.length];

        for (int i = 0; i < c.length; i++) {
            long cTempLong = (long) cTemp[i];
            long testIntLong = (long) testInt[i];
            c[i] = (byte) (cTempLong ^ testIntLong);
        }

        byte[] t = KMACXOF256(ka, testInt, 512, "SKA");

        output[0] = z;
        output[1] = c;
        output[2] = t;
        return output;
    }

    public static byte[][] decrypt(byte[] key2, byte[] z, byte[] c, byte[] t) {
        byte[][] output = new byte[2][];

        byte[] z_key2 = new byte[z.length + key2.length]; 
        System.arraycopy(z, 0, z_key2, 0, z.length);
        System.arraycopy(key2, 0, z_key2, z.length, key2.length);

        byte[] ke_ka2 = KMACXOF256(z_key2, new byte[] {}, 1024, "S");
        byte[] ke2 = new byte[ke_ka2.length / 2];
        byte[] ka2 = new byte[ke_ka2.length / 2];
        
        System.arraycopy(ke_ka2, 0, ke2, 0, ke2.length);
        System.arraycopy(ke_ka2, ke2.length, ka2, 0, ka2.length);

        byte[] mTemp = KMACXOF256(ke2, new byte[] {}, c.length * 8, "SKE");
        byte[] m = new byte[mTemp.length];
        for (int i = 0; i < m.length; i++) {
            long mTempLong = (long) mTemp[i];
            long cLong = (long) c[i];
            m[i] = (byte) (mTempLong ^ cLong);
        }

        byte[] tPrime = KMACXOF256(ka2, m, 512, "SKA");

        output[0] = m;
        output[1] = tPrime;

        return output;

    }

    public static boolean acceptMessage(byte[] t, byte[] tPrime) {
        boolean returnBool = true;
        for (int i = 0; i < t.length; i++) {
            if (t[i] != tPrime[i]) {
                returnBool = false;
            }
        }
        return returnBool;
    }

    public static byte[] KMACXOF256(byte[] key, byte[] input, int outLength, String str) {
        
        byte[] newX = new byte[bytepad(encodeString(key), 136).length + input.length + rightEncode(0).length];
        System.arraycopy(bytepad(encodeString(key), 136), 0, newX, 0, bytepad(encodeString(key), 136).length);
        System.arraycopy(input, 0, newX, bytepad(encodeString(key), 136).length, input.length);
        System.arraycopy(rightEncode(0), 0, newX, bytepad(encodeString(key), 136).length + input.length, rightEncode(0).length);

        return cShake256(newX, outLength, "KMAC", str);
    }

    

    public static byte[] cShake256(byte[] input, int outLength, String functionName, String customString) {

        byte[] inFun = concatByteArray(encodeString(functionName.getBytes()), encodeString(customString.getBytes()));
        inFun = concatByteArray(bytepad(inFun, 136), input);
        inFun = concatByteArray((inFun), new byte[] {0x04});

        return sponge(inFun, outLength, 512);

    }

    public static byte[] sponge(byte[] input, int outLength, int capacity) {

        int rate = 1600 - capacity;
        byte[] padded = input;
        if ((padded.length % (rate / 8)) != 0) {
            padded = pad10_1(padded);
        }
        long[][] states = byteArrayToStates(padded, capacity);
        long[] state = new long[25];
        for (int i = 0; i < states.length; i++) {
            state = keccakp(xorLongs(state, states[i]), 1600, 24);
        }

        long[] out = {};
        int offset = 0;
        do {
            out = Arrays.copyOf(out, offset + rate/64);
            System.arraycopy(state, 0, out, offset, rate/64);
            offset += rate/64;
            state = keccakp(state, 1600, 24);
        } while (out.length*64 < outLength);

        return stateToByteArray(out, outLength);
    }

    
    /**
     * start of keccak function
     */

    public static long[] keccakp(long[] input,int outLength, int rounds) {
        long[] output = input;
        for (int i = 0; i < rounds; i++) {
            output = iota(chi(rhoPie(theta(output))), i);
        }
        return output;
    }

    public static long[] theta(long[] input) {

        long[] C = new long[25];
        long[] D = new long[25];
        long[] output = new long[25];

        for (int x = 0; x < 5; x++) {
            C[x] = input[x] ^ input[x + 5] ^ input[x + 10] ^ input[x + 15] ^ input[x + 20];
        }

        for (int x = 0; x < 5; x++) {
            long tempWord = C[(x + 1) % 5];
            D[x] = C[(x + 4) % 5] ^ (tempWord << 1 | (tempWord >>>(Long.SIZE - 1)));
        }

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                output[x + (5 * y)] = input[x + (5 * y)] ^ D[x];
            }
        }

        return output;
    }

    public static long[] rhoPie(long[] input) {
        long[] output = new long [25];
        
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                output[x + (5 * (((2 * x) + (3 * y)) % 5))] = (input[x + (5 * y)] 
                << offset[x][y] | (input[x + (5 * y)] >>>(Long.SIZE - offset[x][y])));
            }
        }
        return output;
    }    

     public static long[] chi(long[] input) {
        long[] output = new long [25];

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {   
                output[x + (5 * y)] = input[x + (5 * y)] ^ (( ~input[((x + 1) % 5) + (5 * y)]) & input[((x + 2) % 5) + (5 * y)]); 
            }        
        }
        return output;
     }

    public static long[] iota(long[] input, int round) {
        long[] output = new long [25];
        output = input;
        output[0] = output[0] ^ roundConstant[round]; 
        return output;
    }


    /**
    * Apply the NIST bytepad primitive to a byte array X with encoding factor w.
    * @param X the byte array to bytepad
    * @return the byte-padded byte array X with encoding factor w.
    */
    private static byte[] bytepad(byte[] X, int outLen) {

        if (outLen > 0) {
            byte[] wenc = leftEncode(outLen);
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

    private static byte[] encodeString(byte[] s) {
        
        byte[] returnArray = new byte[leftEncode(s.length).length + s.length];
        System.arraycopy(leftEncode(s.length), 0, returnArray, 0, leftEncode(s.length).length);
        System.arraycopy(s, 0, returnArray, leftEncode(s.length).length, s.length);

        return returnArray;
    }
    
    private static byte[] leftEncode(long length) {
        if (length == 0) return new byte[] {1, 0};
        
        long lengthCopy = length;
        byte[] tempArray = new byte[8]; 
        int i = 0;
        while (lengthCopy > 0) {
            byte lengthByte = (byte) lengthCopy;
            lengthCopy = lengthCopy>>>8;
            tempArray[7 - i++] = lengthByte;
        }
        byte[] returnArray = new byte[i + 1];
        returnArray[0] = (byte) i;
        System.arraycopy(tempArray, 8 - i, returnArray, 1, i);

        return returnArray;
    }
    
    private static byte[] rightEncode(long length) {

        if (length == 0) return new byte[] {0, 1};
        
        long lengthCopy = length;
        byte[] tempArray = new byte[8]; 
        int i = 0;
        while (lengthCopy > 0) {
            byte lengthByte = (byte) lengthCopy;
            lengthCopy = lengthCopy>>>8;
            tempArray[7 - i++] = lengthByte;
        }
        byte[] returnArray = new byte[i + 1];
        returnArray[returnArray.length - 1] = (byte) i;
        System.arraycopy(tempArray, 8 - i, returnArray, 0, i);

        return returnArray;
    }
    
    public static byte[] concatByteArray(byte[] a1, byte[] a2) {
        byte[] returnArray = Arrays.copyOf(a1, a1.length + a2.length);
        System.arraycopy(a2, 0, returnArray, a1.length, a2.length);
        return returnArray;
    }

    private static long bytesToWord(int offset, byte[] in) {        
        long word = 0L;
        for (int i = 0; i < 8; i++) {
            word += (((long) in[offset + i])) << (8*i);
        }
        return word;
    }

    public static long[] xorLongs(long[] l1, long[] l2) {
        long[] returnLong = new long[25];
        for (int i = 0; i < l1.length; i++) {
            returnLong[i] = l1[i] ^ l2[i];
        }
        return returnLong;
    }

    public static long [][] byteArrayToStates(byte[] input, int capacity) {
        long[][] states = new long[(input.length * 8) / (1600 - capacity)][25];
        int ofs = 0;
        for (int i = 0; i < states.length; i++) {
            long[] state = new long[25];
            for (int j = 0; j < (1600 - capacity) / 64; j++) {
                long word = bytesToWord(ofs, input);
                state[j] = word;
                ofs = ofs + 8;
            }
            states[i] = state;
        }
        return states;
    }

    public static byte[] pad10_1(byte[] input) {
        int numToPad = 136 - (input.length % 136);
        byte[] output = new byte[input.length + numToPad];
        System.arraycopy(input, 0, output, 0, input.length);
        for (int i = 0; i < numToPad; i++) {
            output[input.length + i] = 0;
        }
        output[input.length + numToPad - 1] = (byte) 0x80;
        return output;
    }

    private static byte[] stateToByteArray(long[] state, int bitLen) {

        byte[] out = new byte[bitLen/8];
        int wordIndex = 0;
        while (wordIndex * 64 < bitLen) {
            long word = state[wordIndex++];
            int fill = 0;
            if ((wordIndex * 64) > bitLen) {
                fill = (bitLen - (wordIndex - 1) * 64) / 8;
            } else {
                fill = 8;
            }
            for (int i = 0; i < fill; i++) {
                byte uByte = (byte) (word>>>(8 * i) & 0xFF);
                out[(wordIndex - 1) * 8 + i] = uByte;
            }
        }
        return out;
    }
}
public class Zadanie01 {
    private static final int BLAD = -2_000_000_000; // Error code if result is invalid
    private StringBuilder bits; // Stores bits of the input
    private Integer wynik; // Holds the result of the last operation
    private boolean wynikOdebrany; // Tracks if result was already retrieved

    public Zadanie01() {
        bits = new StringBuilder();
        wynik = null;
        wynikOdebrany = true;
    }

    public void input(int bit) {
        // input error handling
        if (bit != 0 && bit != 1) 
        {
            throw new IllegalArgumentException("Bit musi być 0 lub 1"); 
        }

        // Append the bit to the buffer
        bits.append(bit);

        // 7 bits are needed
        if (bits.length() >= 7)
        {
            String segmentBits = bits.substring(0, 4); // Get segment length part
            int segmentLen = Integer.parseInt(segmentBits, 2); // Convert to integer
            int expectedLength = 4 + segmentLen * 2 + 2; // Total bits needed for a full operation
            
            if (bits.length() == expectedLength) // Check if we reached expected length
            { 
                calculateWynik(); // Process the result
                bits = new StringBuilder(); // Reset the bits buffer 
            }
        }
    }

    public int wynik() {
        if (wynik == null) {
            return BLAD; 
        }
        if (wynikOdebrany) {
            return BLAD; // Result is currently marked as retrieved
        }
        wynikOdebrany = true; // Mark result as retrieved
        return wynik;
    }

    private void calculateWynik() {
        String segmentBits = bits.substring(0, 4); // Bits for segment length
        int segmentLen = Integer.parseInt(segmentBits, 2); 
        String opBits = bits.substring(4 + segmentLen, 6 + segmentLen); // Operation code

        int opCode;
        if (opBits.equals("00")) {
            opCode = 0;
        } 
        else if (opBits.equals("01")) {
            opCode = 1;
        } 
        else if (opBits.equals("10")) {
            opCode = 2;
        } 
        else if (opBits.equals("11")) {
            opCode = 3;
        } 
        else {
            opCode = -1; // if invalid
        }

        // Decode first and second argument separately
        int arg1 = decodeArgument(bits.substring(4, 4 + segmentLen));
        int arg2 = decodeArgument(bits.substring(6 + segmentLen, 6 + 2 * segmentLen));

        // Perform operation based on decoded opCode
        if (opCode == 0) {
              wynik = arg1 + arg2;
        }    else if (opCode == 1) {
                wynik = arg1 - arg2;
        }   else if (opCode == 2) {
                wynik = arg1 * arg2;
        }   else if (opCode == 3) {
                wynik = arg1 / arg2;
        }   else {
                wynik = BLAD; // Error 
        }
        wynikOdebrany = false; // New result ready to be retrieved
    }

    private int decodeArgument(String bitString) {
        if (bitString.length() == 0) {
            return 0; // Empty 
        }
        
        // is argument positive or negative
        int sign = bitString.charAt(0) == '1' ? -1 : 1;
        
        // Parse remaining bits as a positive integer
        String magnitudeBits = bitString.substring(1);
        int magnitude = 0;
        
        // Conversion from binary to integer 
        for (int i = 0; i < magnitudeBits.length(); i++) {
            char bit = magnitudeBits.charAt(i);
            if (bit == '1') {
                magnitude += Math.pow(2, magnitudeBits.length() - 1 - i);
            }
        }
        
        return sign * magnitude;
    }
}

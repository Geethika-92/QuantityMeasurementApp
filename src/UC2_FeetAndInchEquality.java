import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UC2_FeetAndInchEquality {

    // Feet class
    public static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Feet feet = (Feet) obj;
            return Double.compare(this.value, feet.value) == 0;
        }
    }

    // Inches class
    public static class Inches {
        private final double value;

        public Inches(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Inches inch = (Inches) obj;
            return Double.compare(this.value, inch.value) == 0;
        }
    }

    // Static methods (as per UC2 requirement)
    public static boolean compareFeet(double v1, double v2) {
        Feet f1 = new Feet(v1);
        Feet f2 = new Feet(v2);
        return f1.equals(f2);
    }

    public static boolean compareInches(double v1, double v2) {
        Inches i1 = new Inches(v1);
        Inches i2 = new Inches(v2);
        return i1.equals(i2);
    }

    // Main method
    public static void main(String[] args) {
        System.out.println("Feet Equality: " + compareFeet(1.0, 1.0));
        System.out.println("Inch Equality: " + compareInches(1.0, 1.0));
    }

    // Test cases
    public static class UC2_Test {

        @Test
        void testEquality_SameValue_Feet() {
            assertTrue(compareFeet(1.0, 1.0));
        }

        @Test
        void testEquality_DifferentValue_Feet() {
            assertFalse(compareFeet(1.0, 2.0));
        }

        @Test
        void testEquality_SameValue_Inches() {
            assertTrue(compareInches(1.0, 1.0));
        }

        @Test
        void testEquality_DifferentValue_Inches() {
            assertFalse(compareInches(1.0, 2.0));
        }

        @Test
        void testEquality_NullComparison() {
            Inches i1 = new Inches(1.0);
            assertFalse(i1.equals(null));
        }

        @Test
        void testEquality_SameReference() {
            Feet f1 = new Feet(1.0);
            assertTrue(f1.equals(f1));
        }

        @Test
        void testEquality_NonNumericInput() {
            Feet f1 = new Feet(1.0);
            assertFalse(f1.equals("string"));
        }
    }
}
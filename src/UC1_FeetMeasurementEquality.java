import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UC1_FeetMeasurementEquality {

    // Inner class
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

    // Main method
    public static void main(String[] args) {
        Feet f1 = new Feet(1.0);
        Feet f2 = new Feet(1.0);
        System.out.println("UC1 Result: " + f1.equals(f2));
    }

    // Test cases
    public static class UC1_Test {

        @Test
        void testEquality_SameValue() {
            Feet f1 = new Feet(1.0);
            Feet f2 = new Feet(1.0);
            assertTrue(f1.equals(f2));
        }

        @Test
        void testEquality_DifferentValue() {
            Feet f1 = new Feet(1.0);
            Feet f2 = new Feet(2.0);
            assertFalse(f1.equals(f2));
        }

        @Test
        void testEquality_NullComparison() {
            Feet f1 = new Feet(1.0);
            assertFalse(f1.equals(null));
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
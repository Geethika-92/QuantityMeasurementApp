import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UC3_QuantityLength {

    // Enum for units
    enum LengthUnit {
        FEET(1.0),
        INCH(1.0 / 12.0);

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        public double toFeet(double value) {
            return value * toFeetFactor;
        }
    }

    // Generic Quantity class
    public static class Quantity {
        private final double value;
        private final LengthUnit unit;

        public Quantity(double value, LengthUnit unit) {
            if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
            this.value = value;
            this.unit = unit;
        }

        private double toFeet() {
            return unit.toFeet(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            Quantity other = (Quantity) obj;

            return Double.compare(this.toFeet(), other.toFeet()) == 0;
        }
    }

    // Main method
    public static void main(String[] args) {
        Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
        Quantity q2 = new Quantity(12.0, LengthUnit.INCH);

        System.out.println("Feet vs Inch Equality: " + q1.equals(q2));
    }

    // Test cases
    public static class UC3_Test {

        @Test
        void testEquality_FeetToFeet_SameValue() {
            Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
            Quantity q2 = new Quantity(1.0, LengthUnit.FEET);
            assertTrue(q1.equals(q2));
        }

        @Test
        void testEquality_InchToInch_SameValue() {
            Quantity q1 = new Quantity(1.0, LengthUnit.INCH);
            Quantity q2 = new Quantity(1.0, LengthUnit.INCH);
            assertTrue(q1.equals(q2));
        }

        @Test
        void testEquality_FeetToInch_Equivalent() {
            Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
            Quantity q2 = new Quantity(12.0, LengthUnit.INCH);
            assertTrue(q1.equals(q2));
        }

        @Test
        void testEquality_InchToFeet_Equivalent() {
            Quantity q1 = new Quantity(12.0, LengthUnit.INCH);
            Quantity q2 = new Quantity(1.0, LengthUnit.FEET);
            assertTrue(q1.equals(q2));
        }

        @Test
        void testEquality_DifferentValues() {
            Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
            Quantity q2 = new Quantity(2.0, LengthUnit.FEET);
            assertFalse(q1.equals(q2));
        }

        @Test
        void testEquality_SameReference() {
            Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
            assertTrue(q1.equals(q1));
        }

        @Test
        void testEquality_NullComparison() {
            Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
            assertFalse(q1.equals(null));
        }

        @Test
        void testEquality_InvalidUnit() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Quantity(1.0, null);
            });
        }
    }
}
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UC4_ExtendedUnits {

    // Enum with all units (base = FEET)
    enum LengthUnit {
        FEET(1.0),
        INCH(1.0 / 12.0),
        YARD(3.0),
        CENTIMETER(0.0328084); // 1 cm = 0.0328084 feet

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
        Quantity q1 = new Quantity(1.0, LengthUnit.YARD);
        Quantity q2 = new Quantity(3.0, LengthUnit.FEET);
        Quantity q3 = new Quantity(36.0, LengthUnit.INCH);
        Quantity q4 = new Quantity(1.0, LengthUnit.CENTIMETER);
        Quantity q5 = new Quantity(0.393701, LengthUnit.INCH);

        System.out.println("Yard vs Feet: " + q1.equals(q2));
        System.out.println("Yard vs Inch: " + q1.equals(q3));
        System.out.println("CM vs Inch: " + q4.equals(q5));
    }

    // Test cases
    public static class UC4_Test {

        @Test
        void testEquality_YardToYard_SameValue() {
            assertTrue(new Quantity(1.0, LengthUnit.YARD)
                    .equals(new Quantity(1.0, LengthUnit.YARD)));
        }

        @Test
        void testEquality_YardToFeet_Equivalent() {
            assertTrue(new Quantity(1.0, LengthUnit.YARD)
                    .equals(new Quantity(3.0, LengthUnit.FEET)));
        }

        @Test
        void testEquality_YardToInch_Equivalent() {
            assertTrue(new Quantity(1.0, LengthUnit.YARD)
                    .equals(new Quantity(36.0, LengthUnit.INCH)));
        }

        @Test
        void testEquality_CmToInch_Equivalent() {
            assertTrue(new Quantity(1.0, LengthUnit.CENTIMETER)
                    .equals(new Quantity(0.393701, LengthUnit.INCH)));
        }

        @Test
        void testEquality_DifferentValues() {
            assertFalse(new Quantity(1.0, LengthUnit.YARD)
                    .equals(new Quantity(2.0, LengthUnit.FEET)));
        }

        @Test
        void testEquality_SameReference() {
            Quantity q = new Quantity(1.0, LengthUnit.YARD);
            assertTrue(q.equals(q));
        }

        @Test
        void testEquality_NullComparison() {
            Quantity q = new Quantity(1.0, LengthUnit.YARD);
            assertFalse(q.equals(null));
        }

        @Test
        void testEquality_InvalidUnit() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Quantity(1.0, null);
            });
        }
    }
}
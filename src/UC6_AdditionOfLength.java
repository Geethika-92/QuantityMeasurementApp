import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UC6_AdditionOfLength {

    // Enum (base = FEET)
    enum LengthUnit {
        FEET(1.0),
        INCH(1.0 / 12.0),
        YARD(3.0),
        CENTIMETER(0.0328084);

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        public double toFeet(double value) {
            return value * toFeetFactor;
        }

        public double fromFeet(double valueInFeet) {
            return valueInFeet / toFeetFactor;
        }
    }

    // Quantity class
    public static class Quantity {
        private final double value;
        private final LengthUnit unit;

        public Quantity(double value, LengthUnit unit) {
            if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
            if (!Double.isFinite(value)) throw new IllegalArgumentException("Invalid value");
            this.value = value;
            this.unit = unit;
        }

        private double toFeet() {
            return unit.toFeet(value);
        }

        // ADD METHOD (core UC6)
        public Quantity add(Quantity other) {
            if (other == null) throw new IllegalArgumentException("Other cannot be null");

            double sumInFeet = this.toFeet() + other.toFeet();

            double result = this.unit.fromFeet(sumInFeet);

            return new Quantity(result, this.unit); // result in first operand unit
        }

        // Static add (optional)
        public static Quantity add(Quantity q1, Quantity q2) {
            return q1.add(q2);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            Quantity other = (Quantity) obj;
            return Double.compare(this.toFeet(), other.toFeet()) == 0;
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    // Main method
    public static void main(String[] args) {
        Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
        Quantity q2 = new Quantity(12.0, LengthUnit.INCH);

        Quantity result = q1.add(q2);

        System.out.println("Result: " + result);
    }

    // Tests
    public static class UC6_Test {

        double EPS = 1e-6;

        @Test
        void testAddition_FeetPlusFeet() {
            Quantity r = new Quantity(1.0, LengthUnit.FEET)
                    .add(new Quantity(2.0, LengthUnit.FEET));
            assertEquals(3.0, r.value, EPS);
        }

        @Test
        void testAddition_InchPlusInch() {
            Quantity r = new Quantity(6.0, LengthUnit.INCH)
                    .add(new Quantity(6.0, LengthUnit.INCH));
            assertEquals(12.0, r.value, EPS);
        }

        @Test
        void testAddition_FeetPlusInch() {
            Quantity r = new Quantity(1.0, LengthUnit.FEET)
                    .add(new Quantity(12.0, LengthUnit.INCH));
            assertEquals(2.0, r.value, EPS);
        }

        @Test
        void testAddition_InchPlusFeet() {
            Quantity r = new Quantity(12.0, LengthUnit.INCH)
                    .add(new Quantity(1.0, LengthUnit.FEET));
            assertEquals(24.0, r.value, EPS);
        }

        @Test
        void testAddition_YardPlusFeet() {
            Quantity r = new Quantity(1.0, LengthUnit.YARD)
                    .add(new Quantity(3.0, LengthUnit.FEET));
            assertEquals(2.0, r.value, EPS);
        }

        @Test
        void testAddition_CmPlusInch() {
            Quantity r = new Quantity(2.54, LengthUnit.CENTIMETER)
                    .add(new Quantity(1.0, LengthUnit.INCH));
            assertEquals(5.08, r.value, 1e-2);
        }

        @Test
        void testAddition_Zero() {
            Quantity r = new Quantity(5.0, LengthUnit.FEET)
                    .add(new Quantity(0.0, LengthUnit.INCH));
            assertEquals(5.0, r.value, EPS);
        }

        @Test
        void testAddition_Negative() {
            Quantity r = new Quantity(5.0, LengthUnit.FEET)
                    .add(new Quantity(-2.0, LengthUnit.FEET));
            assertEquals(3.0, r.value, EPS);
        }

        @Test
        void testAddition_Null() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Quantity(1.0, LengthUnit.FEET).add(null);
            });
        }
    }
}
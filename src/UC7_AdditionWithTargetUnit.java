import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UC7_AdditionWithTargetUnit {

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

        // UC6 method (default → first operand unit)
        public Quantity add(Quantity other) {
            return add(other, this.unit);
        }

        // UC7 method (target unit specified)
        public Quantity add(Quantity other, LengthUnit targetUnit) {
            if (other == null) throw new IllegalArgumentException("Other cannot be null");
            if (targetUnit == null) throw new IllegalArgumentException("Target unit cannot be null");

            double sumInFeet = this.toFeet() + other.toFeet();

            double result = targetUnit.fromFeet(sumInFeet);

            return new Quantity(result, targetUnit);
        }

        // Static version
        public static Quantity add(Quantity q1, Quantity q2, LengthUnit targetUnit) {
            return q1.add(q2, targetUnit);
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

        System.out.println(q1.add(q2, LengthUnit.FEET));      // 2 FEET
        System.out.println(q1.add(q2, LengthUnit.INCH));      // 24 INCH
        System.out.println(q1.add(q2, LengthUnit.YARD));      // ~0.667 YARD
    }

    // Tests
    public static class UC7_Test {

        double EPS = 1e-3;

        @Test
        void testAddition_TargetFeet() {
            Quantity r = new Quantity(1.0, LengthUnit.FEET)
                    .add(new Quantity(12.0, LengthUnit.INCH), LengthUnit.FEET);
            assertEquals(2.0, r.value, EPS);
        }

        @Test
        void testAddition_TargetInch() {
            Quantity r = new Quantity(1.0, LengthUnit.FEET)
                    .add(new Quantity(12.0, LengthUnit.INCH), LengthUnit.INCH);
            assertEquals(24.0, r.value, EPS);
        }

        @Test
        void testAddition_TargetYard() {
            Quantity r = new Quantity(1.0, LengthUnit.FEET)
                    .add(new Quantity(12.0, LengthUnit.INCH), LengthUnit.YARD);
            assertEquals(0.667, r.value, EPS);
        }

        @Test
        void testAddition_TargetCm() {
            Quantity r = new Quantity(1.0, LengthUnit.INCH)
                    .add(new Quantity(1.0, LengthUnit.INCH), LengthUnit.CENTIMETER);
            assertEquals(5.08, r.value, 1e-2);
        }

        @Test
        void testAddition_Commutativity() {
            Quantity a = new Quantity(1.0, LengthUnit.FEET);
            Quantity b = new Quantity(12.0, LengthUnit.INCH);

            double r1 = a.add(b, LengthUnit.YARD).value;
            double r2 = b.add(a, LengthUnit.YARD).value;

            assertEquals(r1, r2, EPS);
        }

        @Test
        void testAddition_NullTarget() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Quantity(1.0, LengthUnit.FEET)
                        .add(new Quantity(1.0, LengthUnit.FEET), null);
            });
        }

        @Test
        void testAddition_Zero() {
            Quantity r = new Quantity(5.0, LengthUnit.FEET)
                    .add(new Quantity(0.0, LengthUnit.INCH), LengthUnit.YARD);
            assertEquals(1.667, r.value, EPS);
        }

        @Test
        void testAddition_Negative() {
            Quantity r = new Quantity(5.0, LengthUnit.FEET)
                    .add(new Quantity(-2.0, LengthUnit.FEET), LengthUnit.INCH);
            assertEquals(36.0, r.value, EPS);
        }
    }
}
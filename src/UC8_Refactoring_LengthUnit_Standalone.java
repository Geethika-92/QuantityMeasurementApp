import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Standalone Enum (UC8 main change)
enum LengthUnit {

    FEET(1.0),
    INCH(1.0 / 12.0),
    YARD(3.0),
    CENTIMETER(1.0 / 30.48);

    private final double toFeetFactor;

    LengthUnit(double toFeetFactor) {
        this.toFeetFactor = toFeetFactor;
    }

    public double convertToBaseUnit(double value) {
        return value * toFeetFactor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / toFeetFactor;
    }
}

// Main class
public class UC8_Refactoring_LengthUnit_Standalone {

    static class Quantity {

        private final double value;
        private final LengthUnit unit;

        public Quantity(double value, LengthUnit unit) {
            if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
            if (!Double.isFinite(value)) throw new IllegalArgumentException("Invalid value");
            this.value = value;
            this.unit = unit;
        }

        private double toBase() {
            return unit.convertToBaseUnit(value);
        }

        public Quantity convertTo(LengthUnit targetUnit) {
            if (targetUnit == null) throw new IllegalArgumentException("Target unit null");

            double base = this.toBase();
            double result = targetUnit.convertFromBaseUnit(base);

            return new Quantity(result, targetUnit);
        }

        public Quantity add(Quantity other, LengthUnit targetUnit) {
            if (other == null) throw new IllegalArgumentException("Other null");
            if (targetUnit == null) throw new IllegalArgumentException("Target null");

            double sumBase = this.toBase() + other.toBase();
            double result = targetUnit.convertFromBaseUnit(sumBase);

            return new Quantity(result, targetUnit);
        }

        public Quantity add(Quantity other) {
            return add(other, this.unit);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            Quantity other = (Quantity) obj;
            return Double.compare(this.toBase(), other.toBase()) == 0;
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    // Main
    public static void main(String[] args) {

        Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
        Quantity q2 = new Quantity(12.0, LengthUnit.INCH);

        System.out.println(q1.convertTo(LengthUnit.INCH));     // 12
        System.out.println(q1.add(q2, LengthUnit.FEET));       // 2
        System.out.println(new Quantity(36, LengthUnit.INCH)
                .equals(new Quantity(1, LengthUnit.YARD)));   // true
    }

    // Tests
    public static class UC8_Test {

        double EPS = 1e-3;

        @Test
        void testConvert() {
            Quantity q = new Quantity(1.0, LengthUnit.FEET)
                    .convertTo(LengthUnit.INCH);
            assertEquals(12.0, q.value, EPS);
        }

        @Test
        void testAdd() {
            Quantity q = new Quantity(1.0, LengthUnit.FEET)
                    .add(new Quantity(12.0, LengthUnit.INCH), LengthUnit.FEET);
            assertEquals(2.0, q.value, EPS);
        }

        @Test
        void testEquals() {
            assertTrue(new Quantity(36, LengthUnit.INCH)
                    .equals(new Quantity(1, LengthUnit.YARD)));
        }
    }
}
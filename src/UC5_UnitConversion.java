import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UC5_UnitConversion {

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

    // Quantity class (immutable)
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

        // Instance conversion
        public Quantity convertTo(LengthUnit target) {
            double base = toFeet();
            double converted = target.fromFeet(base);
            return new Quantity(converted, target);
        }

        // Static conversion API
        public static double convert(double value, LengthUnit source, LengthUnit target) {
            if (source == null || target == null)
                throw new IllegalArgumentException("Unit cannot be null");
            if (!Double.isFinite(value))
                throw new IllegalArgumentException("Invalid value");

            double base = source.toFeet(value);
            return target.fromFeet(base);
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

    // Demo methods (overloading)
    public static void demonstrateLengthConversion(double value, LengthUnit from, LengthUnit to) {
        double result = Quantity.convert(value, from, to);
        System.out.println(value + " " + from + " = " + result + " " + to);
    }

    public static void demonstrateLengthConversion(Quantity q, LengthUnit to) {
        Quantity result = q.convertTo(to);
        System.out.println(q + " = " + result);
    }

    // Main method
    public static void main(String[] args) {
        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCH);
        demonstrateLengthConversion(3.0, LengthUnit.YARD, LengthUnit.FEET);
        demonstrateLengthConversion(36.0, LengthUnit.INCH, LengthUnit.YARD);
        demonstrateLengthConversion(1.0, LengthUnit.CENTIMETER, LengthUnit.INCH);

        Quantity q = new Quantity(1.0, LengthUnit.YARD);
        demonstrateLengthConversion(q, LengthUnit.INCH);
    }

    // Tests
    public static class UC5_Test {

        double EPS = 1e-6;

        @Test
        void testConversion_FeetToInches() {
            assertEquals(12.0, Quantity.convert(1.0, LengthUnit.FEET, LengthUnit.INCH), EPS);
        }

        @Test
        void testConversion_InchesToFeet() {
            assertEquals(2.0, Quantity.convert(24.0, LengthUnit.INCH, LengthUnit.FEET), EPS);
        }

        @Test
        void testConversion_YardToInch() {
            assertEquals(36.0, Quantity.convert(1.0, LengthUnit.YARD, LengthUnit.INCH), EPS);
        }

        @Test
        void testConversion_CmToInch() {
            assertEquals(1.0, Quantity.convert(2.54, LengthUnit.CENTIMETER, LengthUnit.INCH), 1e-3);
        }

        @Test
        void testConversion_Zero() {
            assertEquals(0.0, Quantity.convert(0.0, LengthUnit.FEET, LengthUnit.INCH), EPS);
        }

        @Test
        void testConversion_Negative() {
            assertEquals(-12.0, Quantity.convert(-1.0, LengthUnit.FEET, LengthUnit.INCH), EPS);
        }

        @Test
        void testConversion_SameUnit() {
            assertEquals(5.0, Quantity.convert(5.0, LengthUnit.FEET, LengthUnit.FEET), EPS);
        }

        @Test
        void testConversion_InvalidUnit() {
            assertThrows(IllegalArgumentException.class, () -> {
                Quantity.convert(1.0, null, LengthUnit.FEET);
            });
        }

        @Test
        void testConversion_InvalidValue() {
            assertThrows(IllegalArgumentException.class, () -> {
                Quantity.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCH);
            });
        }

        @Test
        void testRoundTrip() {
            double v = 5.0;
            double res = Quantity.convert(
                    Quantity.convert(v, LengthUnit.FEET, LengthUnit.INCH),
                    LengthUnit.INCH,
                    LengthUnit.FEET
            );
            assertEquals(v, res, EPS);
        }
    }
}
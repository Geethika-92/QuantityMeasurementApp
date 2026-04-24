public class UC9_QuantityMeasurementApp {
}
// WeightUnit.java
enum WeightUnit {
    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double factor;

    WeightUnit(double factor) {
        this.factor = factor;
    }

    public double toBase(double value) {
        return value * factor; // to kilograms
    }

    public double fromBase(double baseValue) {
        return baseValue / factor;
    }
}

// QuantityWeight.java
class QuantityWeight {
    private final double value;
    private final WeightUnit unit;
    private static final double EPSILON = 1e-6;

    public QuantityWeight(double value, WeightUnit unit) {
        if (unit == null || !Double.isFinite(value)) {
            throw new IllegalArgumentException("Invalid input");
        }
        this.value = value;
        this.unit = unit;
    }

    // Convert to another unit
    public QuantityWeight convertTo(WeightUnit target) {
        if (target == null) throw new IllegalArgumentException("Target unit null");
        double base = unit.toBase(value);
        return new QuantityWeight(target.fromBase(base), target);
    }

    // Add (default → first unit)
    public QuantityWeight add(QuantityWeight other) {
        return add(other, this.unit);
    }

    // Add (explicit target unit)
    public QuantityWeight add(QuantityWeight other, WeightUnit target) {
        if (other == null || target == null) {
            throw new IllegalArgumentException("Invalid input");
        }
        double sumBase = this.unit.toBase(this.value) + other.unit.toBase(other.value);
        return new QuantityWeight(target.fromBase(sumBase), target);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof QuantityWeight)) return false;

        QuantityWeight other = (QuantityWeight) obj;
        double a = this.unit.toBase(this.value);
        double b = other.unit.toBase(other.value);

        return Math.abs(a - b) < EPSILON;
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}

// Main App
public class QuantityMeasurementApp {
    public static void main(String[] args) {

        QuantityWeight w1 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight w2 = new QuantityWeight(1000.0, WeightUnit.GRAM);
        QuantityWeight w3 = new QuantityWeight(2.20462, WeightUnit.POUND);

        // Equality
        System.out.println(w1.equals(w2)); // true
        System.out.println(w1.equals(w3)); // true

        // Conversion
        System.out.println(w1.convertTo(WeightUnit.GRAM)); // 1000 g
        System.out.println(w3.convertTo(WeightUnit.KILOGRAM)); // ~1 kg

        // Addition
        System.out.println(w1.add(w2)); // 2 kg
        System.out.println(w1.add(w2, WeightUnit.GRAM)); // 2000 g
        System.out.println(w1.add(w3, WeightUnit.POUND)); // ~4.409 lb
    }
}
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Field {

    private static final Map<String, Integer> LETTERS_TO_NUMBERS;

    static {
        LETTERS_TO_NUMBERS = new HashMap<>();
        LETTERS_TO_NUMBERS.put("A", 0);
        LETTERS_TO_NUMBERS.put("B", 1);
        LETTERS_TO_NUMBERS.put("C", 2);
        LETTERS_TO_NUMBERS.put("D", 3);
        LETTERS_TO_NUMBERS.put("E", 4);
        LETTERS_TO_NUMBERS.put("F", 5);
        LETTERS_TO_NUMBERS.put("G", 6);
        LETTERS_TO_NUMBERS.put("H", 7);
        LETTERS_TO_NUMBERS.put("I", 8);
        LETTERS_TO_NUMBERS.put("J", 9);
    }

    private static final Map<Integer, String> NUMBERS_TO_LETTERS;

    static {
        NUMBERS_TO_LETTERS = new HashMap<>();
        NUMBERS_TO_LETTERS.put(0, "A");
        NUMBERS_TO_LETTERS.put(1, "B");
        NUMBERS_TO_LETTERS.put(2, "C");
        NUMBERS_TO_LETTERS.put(3, "D");
        NUMBERS_TO_LETTERS.put(4, "E");
        NUMBERS_TO_LETTERS.put(5, "F");
        NUMBERS_TO_LETTERS.put(6, "G");
        NUMBERS_TO_LETTERS.put(7, "H");
        NUMBERS_TO_LETTERS.put(8, "I");
        NUMBERS_TO_LETTERS.put(9, "J");
    }


    private int x;
    private int y;

    public Field(String field) {

        Objects.requireNonNull(field, "Field can not be null");
        field = field.trim();

        if (field.length() < 2 || field.length() > 3) {
            throw new IllegalArgumentException("Illegal field format: " + field);
        }

        String letter = field.substring(0, 1);
        String number = field.substring(1);

        if (!LETTERS_TO_NUMBERS.containsKey(letter.toUpperCase())) {
            throw new IllegalArgumentException("Letter out of range: " + letter);
        }

        try {
            this.y = Integer.parseInt(number);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Illegal number format: " + number);
        }

        if (this.y > Engine.BOARD_SIZE || this.y <= 0) {
            throw new IllegalArgumentException("Number out of range: " + this.y);
        }

        this.y --;
        this.x = LETTERS_TO_NUMBERS.get(letter);

    }

    public Field(int x, int y) {
        if(x < 0 || x >= Engine.BOARD_SIZE) {
            throw new IllegalArgumentException("Number out of range: " + this.x);
        }

        if(y < 0 || y >= Engine.BOARD_SIZE) {
            throw new IllegalArgumentException("Number out of range: " + this.x);
        }

        this.x = x;
        this.y = y;
    }

    public int getXCoordinate() {
        return x;
    }

    public int getYCoordinate() {
        return y;
    }

    public String getAsString() {
        return NUMBERS_TO_LETTERS.get(x) + (y+1);
    }

    public FieldRange toRange() {
        return new FieldRange(this);
    }

    public Field nextVerticalField() {
        return new Field(x, y + 1);
    }

    public Field nextHorizontalField() {
        return new Field(x + 1, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return x == field.x &&
                y == field.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

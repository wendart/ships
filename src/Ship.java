import java.util.List;

public class Ship {

    private FieldRange placement;


    public Ship(FieldRange shipPlacement) {

        placement = shipPlacement;
        if (!placement.isOneFieldWide()) {
            throw new IllegalArgumentException("Ship must be one field wide");
        }
    }

    public boolean isCollision(Ship otherShip) {
        List<Field> placementFields = placement.getRangeFields();
        FieldRange extend = otherShip.placement.extend();
        for (Field field : extend.getRangeFields()) {
            if (placementFields.contains(field)) {
                return true;
            }
        }
        return false;
    }

    public int getSize() {
        return placement.getRangeFields().size();
    }

    public FieldRange getPlacement() {
        return placement;
    }
}

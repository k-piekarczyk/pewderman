package pewderman;

import java.awt.*;
import java.util.Random;


class Field {
    Point cord;

    public enum TypeFamily {WALL, POWER_UP}

    public enum Type {BREAKABLE_WALL, UNBREAKABLE_WALL, NO_WALL, FIRE, RANGE, BOMBS, LIVES, CUBA_LIBRE, GHOST, BOOTS, TRUMP_BLESSING}

    private Type fieldType;
    private TypeFamily fieldTypeFamily;

    Field(Type fieldType, TypeFamily fieldTypeFamily, int x, int y) {
        this.fieldType = fieldType;
        this.fieldTypeFamily = fieldTypeFamily;
        cord = new Point(x, y);

        System.out.printf("pewderman.Field [x: " + cord.x + ", y: " + cord.y + " ]: created as %s%n", fieldType);
    }

    Type getFieldType() {
        return fieldType;
    }

    boolean isAWall() {
        return fieldTypeFamily == TypeFamily.WALL;
    }

    boolean isEmpty() {
        return fieldType == Type.NO_WALL;
    }

    TypeFamily getFieldTypeFamily() {
        return fieldTypeFamily;
    }

    void setOnFire(Type postExplosion) {
        this.fieldType = Type.FIRE;

        new Thread(() -> {
            try {
                Thread.sleep(250);
                this.fieldType = postExplosion;
            } catch (InterruptedException e) {
                this.fieldType = postExplosion;
                e.printStackTrace();
            }
        }).start();
    }

    void buildAWall(){
        fieldType = Type.BREAKABLE_WALL;
    }

    private void destroyAllTypes() {
        fieldType = Type.NO_WALL;
        fieldTypeFamily = TypeFamily.WALL;
        System.out.printf("pewderman.Field [x: " + cord.x + ", y: " + cord.y + " ]: changed state from %s to NO_WALL\n", fieldType);
    }

    private void destroyDestroyableWall() {
        double probability;
        Random generator = new Random();
        probability = generator.nextDouble();

        if (probability * 10 < 4) {
            setOnFire(PowerUp.getPowerUp());
            fieldTypeFamily = TypeFamily.POWER_UP;
            System.out.printf("pewderman.Field [x: " + cord.x + ", y: " + cord.y + " ]: changed state from DESTROYABLE_WALL to %s%n", fieldType);
        } else {
            setOnFire(Type.NO_WALL);
            System.out.println("pewderman.Field [x: " + cord.x + ", y: " + cord.y + " ]: changed state from DESTROYABLE_WALL to NO_WALL");
        }
    }

    void destroy(TypeFamily typeFamily) {
        switch (typeFamily) {
            case WALL: {
                if (fieldType == Type.BREAKABLE_WALL) destroyDestroyableWall();
                else return;
                return;
            }
            case POWER_UP: {
                destroyAllTypes();
            }
        }
    }
}

package nl.fontys.scope.controls;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.mappings.Xbox;

class XBoxControllerSupport extends ControllerSupport {

    public static class Buttons {
        public static final int A = Xbox.A;
        public static final int B = Xbox.B;
        public static final int X = Xbox.X;
        public static final int Y = Xbox.Y;
        public static final int LB = Xbox.L_BUMPER;
        public static final int RB = Xbox.R_BUMPER;
        public static final int BACK = Xbox.BACK;
        public static final int START = Xbox.START;
        public static final int T_TRIGGER_CODE = 4;
        public static final int LEFT_STICK_CODE_Y = Xbox.L_STICK_VERTICAL_AXIS;
        public static final int LEFT_STICK_CODE_X = Xbox.L_STICK_HORIZONTAL_AXIS;
        public static final int RIGHT_STICK_CODE_Y = Xbox.R_STICK_VERTICAL_AXIS;
        public static final int RIGHT_STICK_CODE_X = Xbox.R_STICK_HORIZONTAL_AXIS;
    }

    public XBoxControllerSupport(Moveable moveable) {
        super(moveable);
    }

    @Override
    protected void onUpdate() {
        float value = getAxisValue(Buttons.T_TRIGGER_CODE);
        final float TOLERANCE = 0.02f;
        if (value > TOLERANCE) {
            MoveableAction.SHOOT.act(moveable);
        } else {
            act(MoveableAction.BOOST, Buttons.T_TRIGGER_CODE, TOLERANCE);
        }
        act(MoveableAction.RISE, Buttons.RIGHT_STICK_CODE_Y, TOLERANCE);

        final float ROTATION_SPEED = 1.5f;
        value = getAxisValue(Buttons.LEFT_STICK_CODE_X);
        if (value > 0.2f || value < -0.2f) {
            MoveableAction.ROTATE.act(moveable, value * ROTATION_SPEED, 0f, 0f);
        }
        value = getAxisValue(Buttons.LEFT_STICK_CODE_Y);
        if (value > 0.2f || value < -0.2f) {
            MoveableAction.ROTATE.act(moveable, 0f, 0f, value * ROTATION_SPEED);
        }
        value = getAxisValue(Buttons.RIGHT_STICK_CODE_X);
        if (value > 0.2f || value < -0.2f) {
            MoveableAction.ROTATE.act(moveable, 0f, -value * ROTATION_SPEED, 0f);
        }
    }

    @Override
    protected boolean isSupported(Controller controller) {
        return Xbox.isXboxController(controller);
    }

    private void act(MoveableAction action, int code, float tolerance) {
        float value = getAxisValue(code);
        if (value > tolerance || value < -tolerance) {
            action.act(moveable, -value);
        }
    }
}

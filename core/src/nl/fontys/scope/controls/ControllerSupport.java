package nl.fontys.scope.controls;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;
import java.util.Map;

abstract class ControllerSupport implements ControllerListener {

    private static final int BUTTON_COUNT = 24;

    private boolean button[];

    protected Moveable moveable;

    private Map<Integer, MoveableAction> actionMapping;

    private Map<Integer, Float> axisMove;

    public ControllerSupport(Moveable moveable) {
        actionMapping = new HashMap<Integer, MoveableAction>();
        axisMove = new HashMap<Integer, Float>();
        button = new boolean[BUTTON_COUNT];
        this.moveable = moveable;
    }

    public final void update(float delta) {
        if (moveable != null) {
            for (Map.Entry<Integer, MoveableAction> action : actionMapping.entrySet()) {
                int button = action.getKey();
                if (isButtonPressed(button)) {
                    action.getValue().act(moveable);
                }
            }
        }
        onUpdate();
    }

    protected void register(int buttonCode, MoveableAction action) {
        if (validButtonCode(buttonCode)) {
            actionMapping.put(buttonCode, action);
        }
    }

    @Override
    public final void connected(Controller controller) {
        System.out.println("Connected " + controller.getName());
    }

    @Override
    public final void disconnected(Controller controller) {
        System.out.println("Disconnected " + controller.getName());
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        if (!isSupported(controller)) {
            return false;
        }
        onButtonClick(axisCode, value);
        axisMove.put(axisCode, value);
        return false;
    }

    @Override
    public final boolean buttonDown(Controller controller, int buttonCode) {
        if (!isSupported(controller)) {
            return false;
        }
        if (validButtonCode(buttonCode)) {
            button[buttonCode] = true;
            onButtonClick(buttonCode, 0f);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public final boolean buttonUp(Controller controller, int buttonCode) {
        if (!isSupported(controller)) {
            return false;
        }
        if (validButtonCode(buttonCode)) {
            button[buttonCode] = false;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public final boolean povMoved(Controller controller, int povCode, PovDirection value) {
        if (!isSupported(controller)) {
            return false;
        }
        povMoved(value);
        return false;
    }

    @Override
    public final boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        if (!isSupported(controller)) {
            return false;
        }
        return false;
    }

    @Override
    public final boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        if (!isSupported(controller)) {
            return false;
        }
        return false;
    }

    @Override
    public final boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        if (!isSupported(controller)) {
            return false;
        }
        return false;
    }

    private boolean validButtonCode(int code) {
        return code >= 0 && button.length > code;
    }

    private boolean isButtonPressed(int code) {
        return button[code];
    }

    protected abstract void onUpdate();

    protected float getAxisValue(int axisCode) {
        Float value = axisMove.get(axisCode);
        return value != null ? value : 0f;
    }

    protected void onButtonClick(int code, float strength) {
        // noOp
    }

    protected abstract boolean isSupported(Controller controller);

    protected void povMoved(PovDirection direction) {
        // noOp
    }
}

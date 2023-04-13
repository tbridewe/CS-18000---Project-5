import javax.swing.*;
import java.awt.*;

public class TestMethods {
    public static Component getComponentByName(Component component, String name) {
        if (component.getName().equals(name))
            return component;

        if (component instanceof Container) {
            Component[] components;

            if (component instanceof JMenu) {
                components = ((JMenu) component).getMenuComponents();
            } else if (component instanceof JFrame) {
                components = ((JFrame) component).getComponents();
            } else {
                components = ((Container) component).getComponents();
            }

            for (int i = 0; i < components.length; i++) {
                Component newComponent = getComponentByName(component, name);

                if (newComponent != null) {
                    return newComponent;
                }
            }
        }

        return null;
    }
}

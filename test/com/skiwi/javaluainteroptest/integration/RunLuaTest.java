
package com.skiwi.javaluainteroptest.integration;

import com.skiwi.javaluainteroptest.JavaLuaInteropTestController;
import java.io.IOException;
import java.io.UncheckedIOException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import static org.junit.Assert.*;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

/**
 *
 * @author Frank van Heeswijk
 */
public class RunLuaTest extends GuiTest {
    @Override
    protected Parent getRootNode() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(JavaLuaInteropTestController.class.getResource("JavaLuaInteropTest.fxml"));
            return fxmlLoader.load();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
    
    @Test
    public void testRunLua() {
        TextArea codeTextArea = lookup(find("#splitPane"), "#codeTextArea", TextArea.class);
        Platform.runLater(() -> {
            codeTextArea.clear();
            codeTextArea.appendText("function applyFunction(value)");
            codeTextArea.appendText(System.lineSeparator());
            codeTextArea.appendText("return value + 1");
            codeTextArea.appendText(System.lineSeparator());
            codeTextArea.appendText("end");
        });
        
        TextField valueTextField = find("#valueTextField");
        Platform.runLater(() -> valueTextField.setText("5"));
        
        click("#runButton");
        
        TextField resultTextField = find("#resultTextField");
        waitUntil(resultTextField, textField -> textField.getText().equals("6"), 3);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T lookup(final Node parent, final String id, final Class<T> clazz) {
        for (Node node : parent.lookupAll(id)) {
            if (node.getClass().isAssignableFrom(clazz)) {
                return (T)node;
            }
        }
        throw new IllegalArgumentException("Parent " + parent + " doesn't contain node with id " + id);
    }
}
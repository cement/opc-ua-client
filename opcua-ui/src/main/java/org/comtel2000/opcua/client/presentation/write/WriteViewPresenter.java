package org.comtel2000.opcua.client.presentation.write;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import org.comtel2000.opcua.client.service.OpcUaClientConnector;
import org.comtel2000.opcua.client.service.PersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.digitalpetri.opcua.stack.core.types.builtin.DataValue;
import com.digitalpetri.opcua.stack.core.types.builtin.Variant;
import com.digitalpetri.opcua.stack.core.types.structured.ReferenceDescription;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class WriteViewPresenter implements Initializable {

    @Inject
    PersistenceService session;

    @Inject
    OpcUaClientConnector connection;

    @FXML
    private Button cancelButton;

    @FXML
    private Button writeButton;

    @FXML
    private TextField variable;

    @FXML
    private TextField value;

    protected static Executor FX_PLATFORM_EXECUTOR = Platform::runLater;

    private final ObjectProperty<ReferenceDescription> reference = new SimpleObjectProperty<>(this, "reference");

    private final static Logger logger = LoggerFactory.getLogger(WriteViewPresenter.class);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
	reference.addListener((l, a, b) -> update(b));

    }

    private void update(ReferenceDescription b) {
	if (b == null) {
	    return;
	}

	variable.setText(b.getDisplayName().getText());

    }

    @FXML
    void write(ActionEvent event) {
	logger.info("try to write: {}", reference);
	if (reference.get() == null) {
	    return;
	}
	Variant v = new Variant(value.getText());
	DataValue dv = new DataValue(v);
	connection.writeValue(reference.get().getNodeId().local().get(), dv);

    }

    private Object convertValue(ReferenceDescription b, String data) {
	if (b == null || data == null) {
	    return null;
	}

	System.err.println(b.getTypeDefinition());
	return data;
    }

    @FXML
    void cancel(ActionEvent event) {
	reference.set(null);
	cancelButton.getScene().getWindow().hide();
    }
}
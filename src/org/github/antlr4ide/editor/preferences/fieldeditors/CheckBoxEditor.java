package org.github.antlr4ide.editor.preferences.fieldeditors;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * Wrapper class allowing the CheckBox control to be returned.
 */
public class CheckBoxEditor extends BooleanFieldEditor {
	private Button theCheckBox;

	public CheckBoxEditor(String propertyName, String labelText, Composite parent) {
		super(propertyName,labelText,parent);
	}

	public Button getCheckBox() { return theCheckBox; }
	
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		super.doFillIntoGrid(parent, numColumns);
		theCheckBox=getChangeControl(parent);
	}
}


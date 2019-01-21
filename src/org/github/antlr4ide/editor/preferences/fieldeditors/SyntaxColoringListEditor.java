package org.github.antlr4ide.editor.preferences.fieldeditors;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.widgets.Composite;

public class SyntaxColoringListEditor extends FieldEditor {
    /**
     * The button box containing the Foreground, Background Color Dialog
     * Style and Font Selection buttons;
     * <code>null</code> if none (before creation or after disposal).
     */
    private Composite colorActionBox;

	
	
	public SyntaxColoringListEditor(String propertyName, String labelText, Composite parent) {
		super(propertyName,labelText,parent);
		System.out.println("SyntaxColoringListEditor ("+propertyName+","+labelText+")" );
	}		
	
	@Override
	protected void init(String name, String text) {
		super.init(name, text);
		
	}
	
	
	
	@Override
	protected void adjustForNumColumns(int numColumns) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doLoadDefault() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doStore() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getNumberOfControls() {
		// TODO Auto-generated method stub
		return 0;
	}

}

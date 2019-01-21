package org.github.antlr4ide.editor.preferences.fieldeditors;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;

public class StringLabel extends StringFieldEditor {
	
    /**
     * Creates a string field editor of unlimited width.
     * Use the method <code>setTextLimit</code> to limit the text.
     *
     * @param name the name of the preference this field editor works on
     * @param labelText the label text of the field editor
     * @param parent the parent of the field editor's control
     */
    public StringLabel(String name, String labelText, Composite parent) {
        super(name, labelText, 0, parent);
    }

    @Override
    public int getNumberOfControls() { return 1; };
    
    @Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
        getLabelControl(parent);
    }
    
    @Override
    protected boolean checkState() { return true; }

    @Override
	protected void doLoadDefault() {}
	
	@Override
	protected void doLoad() {} 
	
	@Override
	protected void doStore() {}
    
}

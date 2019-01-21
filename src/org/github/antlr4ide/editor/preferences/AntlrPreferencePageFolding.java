package org.github.antlr4ide.editor.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.github.antlr4ide.editor.preferences.fieldeditors.CheckBoxEditor;
import org.github.antlr4ide.editor.preferences.fieldeditors.StringLabel;

public class AntlrPreferencePageFolding extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	private FieldEditor fields[];
	public AntlrPreferencePageFolding() {
		// System.out.println("AntlrPreferencePageFolding");
	}
	@Override
	public void init(IWorkbench workbench) { 
		// System.out.println("AntlrPreferencePageFolding - init " );
		setPreferenceStore(PlatformUI.getPreferenceStore());
		setDescription("ANTLR Folding Configuration");
	}

	@Override
	protected void createFieldEditors() {
		// System.out.println("AntlrPreferencePageFolding - createFieldEditors " );
		fields= new FieldEditor[] { 
		 new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_ENABLED, "Enable folding", getFieldEditorParent())
		,new StringLabel(AntlrPreferenceConstants.P_FOLDING_LABEL_01, "Initially fold these elements:", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_COMMENTS, "Comments (tbd)", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_OPTIONS, "Options (tbd)", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_TOKENS, "Tokens (tbd)", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_GRAMMAR_ACTION, "Actions (tbd)", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_PARSER_RULE, "Parser Rules", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_LEXER_RULE, "Lexer Rules (tbd)", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_LEXER_MODE, "Lexer Modes", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_RULE_ACTION, "Rule actions (tbd)", getFieldEditorParent())
		};
		
		for(FieldEditor f: fields) { addField(f); }
		
		boolean val=getPreferenceStore().getBoolean(AntlrPreferenceConstants.P_FOLDING_ENABLED);
		updateFields(val);
		
		/*
		 * Add SelectionAdaptor to the main checkbox to control whether sub controls are enabled or not 
		 */
		Button b=(Button) ((CheckBoxEditor) fields[0]).getCheckBox();
		b.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean val=((Button) e.getSource()).getSelection();
				updateFields(val);
			}
		});
	}

	public void updateFields(boolean val) {
		// only enable/disable checkboxes
		for (int i = 2; i < fields.length; i++) {
			((CheckBoxEditor) fields[i]).getCheckBox().setEnabled(val);
		}
	}
	
}

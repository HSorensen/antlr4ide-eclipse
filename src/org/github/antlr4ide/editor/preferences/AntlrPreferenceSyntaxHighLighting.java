package org.github.antlr4ide.editor.preferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.antlr.parser.antlr4.ANTLRv4Lexer;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.github.antlr4ide.editor.preferences.fieldeditors.CheckBoxEditor;
import org.github.antlr4ide.editor.preferences.fieldeditors.SyntaxElementListEditor;

public class AntlrPreferenceSyntaxHighLighting extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
//	private FieldEditor fields[];
	
	private static Map<Integer,String> hiliteElements ;  // default from ANTLRv4Scanner.java
	static {
		   hiliteElements = new HashMap<Integer, String>();
		   // Statements
		   hiliteElements.put(ANTLRv4Lexer.FRAGMENT, "Lexer Fragment");
		   hiliteElements.put(ANTLRv4Lexer.CHANNELS, "Lexer Channels");
		   hiliteElements.put(ANTLRv4Lexer.PARSER,   "Parser");
		   hiliteElements.put(ANTLRv4Lexer.LEXER,    "Lexer");
		   hiliteElements.put(ANTLRv4Lexer.OPTIONS,  "Options");
		   hiliteElements.put(ANTLRv4Lexer.TOKENS,   "Tokens");
		   hiliteElements.put(ANTLRv4Lexer.GRAMMAR,  "Grammar");
		   hiliteElements.put(ANTLRv4Lexer.IMPORT,   "Import");

		   // Constants
		   hiliteElements.put(ANTLRv4Lexer.INT,            "Integer");
		   hiliteElements.put(ANTLRv4Lexer.STRING_LITERAL, "Strings");
		   
		   // Comments
		   hiliteElements.put(ANTLRv4Lexer.DOC_COMMENT,   "Doc Comment");
		   hiliteElements.put(ANTLRv4Lexer.BLOCK_COMMENT, "Block Comment");
		   hiliteElements.put(ANTLRv4Lexer.LINE_COMMENT,  "Line Comment");
		   
		   // References
		   hiliteElements.put(ANTLRv4Lexer.TOKEN_REF, "Token");
		   hiliteElements.put(ANTLRv4Lexer.RULE_REF,  "Rule");

		}
	
	
	
	public AntlrPreferenceSyntaxHighLighting() {
		// System.out.println("AntlrPreferenceSyntaxHighLighting");
	}
	@Override
	public void init(IWorkbench workbench) { 
		// System.out.println("AntlrPreferenceSyntaxHighLighting - init " );
		setPreferenceStore(PlatformUI.getPreferenceStore());
		setDescription("ANTLR Syntax Coloring");
	}

	@Override
	protected void createFieldEditors() {
		// System.out.println("AntlrPreferenceSyntaxHighLighting - createFieldEditors " );
//		fields= new FieldEditor[] { 
//		 new CheckBoxEditor(AntlrPreferenceConstants.P_SYNTAXCOLOR_ENABLED, "Enable Syntax Coloring", getFieldEditorParent())
/*		,new StringLabel(AntlrPreferenceConstants.P_FOLDING_LABEL_01, "Initially fold these elements:", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_COMMENTS, "Comments (tbd)", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_OPTIONS, "Options (tbd)", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_TOKENS, "Tokens (tbd)", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_GRAMMAR_ACTION, "Actions (tbd)", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_PARSER_RULE, "Parser Rules", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_LEXER_RULE, "Lexer Rules (tbd)", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_LEXER_MODE, "Lexer Modes", getFieldEditorParent())
		,new CheckBoxEditor(AntlrPreferenceConstants.P_FOLDING_RULE_ACTION, "Rule actions (tbd)", getFieldEditorParent())
*///		};
		
		FieldEditor fieldEnabled = new CheckBoxEditor(AntlrPreferenceConstants.P_SYNTAXCOLOR_ENABLED, "Enable Syntax Coloring", getFieldEditorParent());
//	    addField(fieldEnabled);
		
		SyntaxElementListEditor syntaxElements = new SyntaxElementListEditor("property", "label", getFieldEditorParent());
//		syntaxElements.loadDefault();   //createList(getHiliteArray());
		addField(syntaxElements);
		
		// populate list
		List list=syntaxElements.getTheList();
		for (Integer ix: hiliteElements.keySet()) {
			list.add(hiliteElements.get(ix));
		}

//	    add(group);
	    
	    
//		for(FieldEditor f: fields) { addField(f); }
		
/*
 * See AntlrToolLauncherTabGroup for sample using Group and Buttons
 *  
 * 			    final Group group = new Group(parent, SWT.NONE);
			    group.setText(title);
			    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			    group.setLayoutData(gd);
			    final GridLayout layout = new GridLayout();
			    layout.numColumns = 3;
			    group.setLayout(layout);
			    group.setFont(font);
			    final Text text = new Text(group, style);
			    GridData _gridData = new GridData(GridData.FILL_HORIZONTAL);
			    gd = _gridData;
			    int _lineHeight = text.getLineHeight();
			    int _multiply = (rows * _lineHeight);
			    gd.heightHint = _multiply;
			    text.setLayoutData(gd);
			    text.setFont(font);
			    text.addModifyListener(this.modifyListener);

 * 		
 */
		
		boolean val=getPreferenceStore().getBoolean(AntlrPreferenceConstants.P_SYNTAXCOLOR_ENABLED);
		updateFields(val);
		
		/*
		 * Add SelectionAdaptor to the main checkbox to control whether sub controls are enabled or not 
		 */
		Button b=(Button) ((CheckBoxEditor) fieldEnabled).getCheckBox();
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
//		for (int i = 2; i < fields.length; i++) {
//			((CheckBoxEditor) fields[i]).getCheckBox().setEnabled(val);
//		}
	}
	

public String[] getHiliteArray() {
	java.util.List<String> out=new ArrayList<>();
	
	for (Integer ix: hiliteElements.keySet()) {
		out.add(hiliteElements.get(ix)+";");
	}
	
	return out.toArray(new String[0]);
}
	
}

package org.github.antlr4ide.editor.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.github.antlr4ide.editor.preferences.fieldeditors.AntlrVersionListEditor;

public class AntlrPreferencePageTool extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private FieldEditor fieldStoreParseTree; 
	
	
	public AntlrPreferencePageTool() {
		super(GRID);
//		setPreferenceStore(activator.getDefault().getPreferenceStore());
		setDescription("ANTLR Tool Configuration");
	}
	
	
	@Override
	public void init(IWorkbench workbench) { 
	    setPreferenceStore(PlatformUI.getPreferenceStore());
}

	@Override
	protected void createFieldEditors() {
		// ------------ Original skeleton:
		//v ANTLR Tool 
		//  [ ] Tool enabled
		//        List of available distributions
		//        Version, Tool Jar path
		//        [ ] Version, Tool Jar path
		//        [ ] Version, Tool Jar path
		//v Options
		//    Directory (-o) _______
		//    Library (-lib) _______
		//    [ ] Generate parse tree listener (-listner)
		//    [ ] Generate parse tree visitors (-visitor)
		//    [ ] Delete generated files when clean build is triggered
		//    [ ] Mark generated files as derived
		//    Encoding (-encoding) _______
		//v VM Arguments
		//    ________________________________________
		// -------

		
		addField(new BooleanFieldEditor(AntlrPreferenceConstants.P_TOOL_ENABLED,	"Tool Enabled",	getFieldEditorParent()));
		addField(new AntlrVersionListEditor(AntlrPreferenceConstants.P_TOOL_DISTRIBUTIONS, "Distributions", getFieldEditorParent()));
		addField(new StringFieldEditor(AntlrPreferenceConstants.P_TOOL_OUTDIRECTORY,"Directory (-o)", getFieldEditorParent()));
		addField(new StringFieldEditor(AntlrPreferenceConstants.P_TOOL_LIB, "Library (-lib)", getFieldEditorParent()));
		
		addField(new BooleanFieldEditor(AntlrPreferenceConstants.P_TOOL_GENLISTENER,    "Generate parse tree listener (-listener)",   getFieldEditorParent()));
		addField(new BooleanFieldEditor(AntlrPreferenceConstants.P_TOOL_GENVISITOR,     "Generate parse tree visitors (-visitor)",    getFieldEditorParent()));
		addField(new BooleanFieldEditor(AntlrPreferenceConstants.P_CLEANUPDERIVED,      "Delete generated files when clean build is triggered",	getFieldEditorParent()));
		addField(new BooleanFieldEditor(AntlrPreferenceConstants.P_MARKDERIVED,         "Mark generated files as derived",	getFieldEditorParent()));
		addField(new StringFieldEditor(AntlrPreferenceConstants.P_TOOL_ENCODING,        "Encoding (-encoding)", getFieldEditorParent()));
		
		addField(new StringFieldEditor(AntlrPreferenceConstants.P_TOOL_VMARGS, "VM Arguments", getFieldEditorParent()));
		// initialize fieldStoreParseTree because AntlrVersionListEditor will enable/disable the field
		fieldStoreParseTree=new StringFieldEditor(AntlrPreferenceConstants.P_TOOL_STORE_PARSETREE, "Folder for stored parsetree (if supported)", getFieldEditorParent());
		fieldStoreParseTree.setEnabled(false, getFieldEditorParent()); // only enable if Tool supports serialization of parseTree
		addField(fieldStoreParseTree);
		}
	
	

}

package org.github.antlr4ide.editor.preferences.fieldeditors;

import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

public class SyntaxElementListEditor extends ListEditor {
	private List list;

	public SyntaxElementListEditor(String propertyName, String labelText, Composite parent) {
		super(propertyName,labelText,parent);
		System.out.println("SyntaxElementListEditor ("+propertyName+","+labelText+")" );
		this.list=super.getListControl(parent);
	}		
	
	public List getTheList() { return this.list; }
	
	@Override
	protected void init(String name, String text) {
		System.out.println("SyntaxElementListEditor - init ("+name+","+text+")" );
		super.init(name, text);
	}
	
	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		System.out.println("SyntaxElementListEditor - doFillIntoGrid " );
		super.doFillIntoGrid(parent, numColumns);
	}
	
	@Override
	protected void doLoad() {
		System.out.println("SyntaxElementListEditor - doLoad " );
		super.doLoad();
	}
	
	@Override
	protected void doStore() {
		System.out.println("SyntaxElementListEditor - doStore " );
		super.doStore();
	}
	
	@Override
	protected void doLoadDefault() {
		System.out.println("SyntaxElementListEditor - doLoadDefault " );
		super.doLoadDefault();
	}
	
	@Override
	protected String createList(String[] items) {
		StringBuilder out=new StringBuilder();
		if(items.length==0) return "";
		out.append(items[0]);
		for(int i=1;i<items.length;i++)
		{
			out.append(";");
			out.append(items[i]);
		}
		
		return out.toString();
	}

	@Override
	protected String getNewInputObject() {
		return "new line";
	}

	@Override
	protected String[] parseString(String stringList) {
		return stringList.split(";");
	}
}

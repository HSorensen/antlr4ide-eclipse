package org.github.antlr4ide.editor.preferences.fieldeditors;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.antlr.parser.antlr4.ANTLRv4Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.github.antlr4ide.editor.ANTLRv4Scanner;


public class SyntaxColoringPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	private static final String ANTLRIDE_PREFERENCE_SYNTAX = "antlride.preference.syntax.";
	private Map<String,ItemStyle>hiliteItemStyle;
	private ItemStyle selectedItemStyle;
	
	/*   Style Editors   */
	Composite stylesComposite;
	private CheckBoxEditor styleEnabled;
	private ColorFieldEditor colorForegroundField;
	private ColorFieldEditor colorBackgroundField;
	private CheckBoxEditor styleBoldEnabled;
	private CheckBoxEditor styleItalicEnabled;
	private CheckBoxEditor styleUnderlineEnabled;
	private CheckBoxEditor styleStrikethruEnabled;
	/* ------------------ */

	/*  Preview Editor */
	private StyledText previewText;
	
	
	private static Map<Integer,String> hiliteElements ;  // default from ANTLRv4Scanner.java
	static {
		   hiliteElements = new LinkedHashMap<Integer, String>();
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
	
	private static String previewInitText = 
			  "// COMMENT \n"
			+ "parser grammar GrammarName;"
			+ "this: IS A rule; "
			+ "rule: RULE;"
			;
	
	
	@Override
	public void init(IWorkbench workbench) {
		System.out.println("SyntaxColoringPreferencePage - init" );

		setPreferenceStore(PlatformUI.getPreferenceStore());
		setDescription("ANTLR Syntax Coloring");		
		
	}
	
	public boolean performOk() {
		storePreferences();
		return super.performOk();
	}

	
	@Override
	protected void performApply() {
		storePreferences();
		updatePreviewTextHighlight();
		super.performApply();
	}
	
	@Override
	protected void performDefaults() {
		setDefaults();
		updatePreviewTextHighlight();
		super.performDefaults();
	}
	
	@Override
	protected Control createContents(Composite parent) {
		System.out.println("SyntaxColoringPreferencePage - createControl" );
		initializeDialogUnits(parent);
		Control out=createSyntaxPage(parent);
		updatePreviewTextHighlight();
		return out;
	}
	
	// Lot of inspiration from the CDT project : https://git.eclipse.org/c/cdt/org.eclipse.cdt.git/tree/core/org.eclipse.cdt.ui/src/org/eclipse/cdt/internal/ui
	private Control createSyntaxPage(Composite parent) {
		System.out.println("SyntaxColoringPreferencePage - createSyntaxPage" );
		Composite colorComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		colorComposite.setLayout(layout);

		/*
		 * Header: Syntax Elements
		 */
		Label label;
		label = new Label(colorComposite, SWT.LEFT);
		label.setText("Syntax Elements");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		/* 
		 * Composite for List of elements and style selector/editor
		 */
		Composite editorComposite = new Composite(colorComposite, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		editorComposite.setLayout(layout);
		GridData gd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		editorComposite.setLayoutData(gd);
		
		/*
		 * List of elements 
		 */
		org.eclipse.swt.widgets.List elementList=new org.eclipse.swt.widgets.List(editorComposite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		SelectionListener elementListListener= new ElementListListener();
		elementList.addSelectionListener(elementListListener);
		initElementList(elementList);
		//elementList.
		
		/*
		 * Style editor
		 */
		stylesComposite = new Composite(editorComposite, SWT.NONE);
		layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 2;
		stylesComposite.setLayout(layout);
		stylesComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		styleEnabled = new CheckBoxEditor(pn("enabled"),"Enable",stylesComposite) ;
		
		colorForegroundField = new ColorFieldEditor(pn("enabledFgRgb"),"Foreground",stylesComposite) ;
		colorBackgroundField = new ColorFieldEditor(pn("enabledBgRgb"),"Background",stylesComposite) ;

		styleBoldEnabled = new CheckBoxEditor(pn("enabledBold"),"Bold",stylesComposite) ;
		styleItalicEnabled = new CheckBoxEditor(pn("enabledItalic"),"Italic",stylesComposite) ;
		styleUnderlineEnabled = new CheckBoxEditor(pn("enabledUnderline"),"Underline",stylesComposite) ;
		styleStrikethruEnabled = new CheckBoxEditor(pn("enabledStrikethru"),"Strikethrough",stylesComposite) ;

		MyStylePropertyChangeListener styleChangeListener=new MyStylePropertyChangeListener();
		styleEnabled.setPropertyChangeListener(styleChangeListener);
		colorForegroundField.setPropertyChangeListener(styleChangeListener);
		colorBackgroundField.setPropertyChangeListener(styleChangeListener);
		styleBoldEnabled.setPropertyChangeListener(styleChangeListener);
		styleItalicEnabled.setPropertyChangeListener(styleChangeListener);
		styleUnderlineEnabled.setPropertyChangeListener(styleChangeListener);
		styleStrikethruEnabled.setPropertyChangeListener(styleChangeListener);

		/*
		 * PREVIEW
		 */
		gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = convertWidthInCharsToPixels(20);
		gd.heightHint = convertHeightInCharsToPixels(5);
		gd.horizontalSpan = 2;
		
		
		label = new Label(editorComposite, SWT.LEFT);
		label.setText("Preview");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		

		//http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/SettingthefontstyleforegroundandbackgroundcolorsofStyledText.htm
		previewText = new StyledText(editorComposite,SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		previewText.setLayoutData(gd);
		previewText.setText(previewInitText);

		return colorComposite;
	}
	
	private void updatePreviewTextHighlight() {
		System.out.println("SyntaxColoringPreferencePage.ElementListListner - updatePreviewTextHighlight "); 
		CharStream stream=CharStreams.fromString(previewText.getText());
		ANTLRv4Lexer lexer = new ANTLRv4Lexer(stream);
		previewText.setStyleRange(null); // clear all styles
		for(Token antlrToken: lexer.getAllTokens()) {
	    	applyPreviewTextItemStyle(hiliteItemStyle.get(hiliteElements.get(antlrToken.getType()))
	    			,antlrToken.getStartIndex()
	    			,antlrToken.getStopIndex());
	    }
		
//		previewText.layout(true,true);
//		previewText.replayout(true,true);
	}
	
	
	private void applyPreviewTextItemStyle(ItemStyle itemStyle,int start, int stop) {
//		System.out.println("SyntaxColoringPreferencePage.ElementListListner - applyPreviewTextItemStyle "+itemStyle+" "+start + " " + stop); 
		if(itemStyle==null) return;
		// public void setStyleRanges(int start, int length, int[] ranges, StyleRange[] styles)
		// int[] ranges: ranges[n]: start, ranges[n+1]: length, styles[n/2]: style
	    StyleRange style1 = new StyleRange(); //StyleRange(int start, int length, Color foreground, Color background, int fontStyle)
	    style1.start = start;
	    style1.length = stop-start+1;
	    style1.fontStyle = itemStyle.toFontStyle();
	    style1.foreground = new Color(Display.getCurrent(),itemStyle.getFg());
	    style1.background = new Color(Display.getCurrent(),itemStyle.getBg());
	    previewText.setStyleRange(style1);
	}

	/**
	 * When an list item is selected shows associated style attributes
	 */
	private class ElementListListener implements SelectionListener {


		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// invoked when double clicking on an item
			//System.out.println("SyntaxColoringPreferencePage.ElementListListner - widgetDefaultSelected "+e );
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			List list=(List) e.getSource();
			String selected[]=list.getSelection();
			for(String item:selected) {
			System.out.println("SyntaxColoringPreferencePage.ElementListListner - widgetSelected " 
		    + " selection "+item);
		    
		    selectedItemStyle=hiliteItemStyle.get(item);
		    updateColorStyle(selectedItemStyle);
		}
		}
		
	}

	private void updateColorStyle(ItemStyle style) {
		styleEnabled.getCheckBox().setSelection(style.isEnabled());
		
		colorForegroundField.getColorSelector().setColorValue(style.getFg());
		colorBackgroundField.getColorSelector().setColorValue(style.getBg());

		styleBoldEnabled.getCheckBox().setSelection(style.isBold());
		styleItalicEnabled.getCheckBox().setSelection(style.isItalic());
		styleUnderlineEnabled.getCheckBox().setSelection(style.isUnderlined());
		styleStrikethruEnabled.getCheckBox().setSelection(style.isStrikethru());
	}
	
	private void initElementList(List elementList) {
		System.out.println("SyntaxColoringPreferencePage - initElementList" );
		hiliteItemStyle=new HashMap<>();

		for(Integer ix: hiliteElements.keySet()) {
			String element=hiliteElements.get(ix);
			elementList.add(element);
			// set initial map of styles for each elements
			hiliteItemStyle.put(element,getItemStyleProperty(ix));
		}
	}
	
	private void storePreferences() {
		for(Integer ix: hiliteElements.keySet()) {
			String element=hiliteElements.get(ix);
			// store map of styles for each elements
			ItemStyle style = hiliteItemStyle.get(element);
			setItemStyleProperty(ix,style);
		}
	}
	
	private void setDefaults() {
		// contains attribute map<Integer, IToken> and IToken.getData contains the TextAttribute.
		ANTLRv4Scanner scanner=new ANTLRv4Scanner(false);
		Map<Integer, IToken> scannerHilite=ANTLRv4Scanner.getHilite();
		for(Integer ix: scannerHilite.keySet()) {
			ItemStyle is=new ItemStyle((TextAttribute) scannerHilite.get(ix).getData());
			hiliteItemStyle.put(hiliteElements.get(ix),is);
		}
	}

	private void setItemStyleProperty(Integer ix, ItemStyle style) {
		IPreferenceStore ps = getPreferenceStore();
		ps.setValue(pn(ix,"styleEnabled"),style.isEnabled());
		ps.setValue(pn(ix,"styleBold"),style.isBold());
		
		ps.setValue(pn(ix,"styleItalic"),style.isItalic());
		ps.setValue(pn(ix,"styleUnderline"),style.isUnderlined());
		ps.setValue(pn(ix,"styleStrikethru"),style.isStrikethru());
		ps.setValue(pn(ix,"styleFgRGB"),style.getFg().toString());
		ps.setValue(pn(ix,"styleBgRGB"),style.getBg().toString());
	}

	private ItemStyle getItemStyleProperty(Integer ix) {
		IPreferenceStore ps = getPreferenceStore();
		ItemStyle is=new ItemStyle(); // default Item Style
		
		is.setEnabled(ps.getBoolean(pn(ix,"styleEnabled")));
		is.setBold(ps.getBoolean(pn(ix,"styleBold")));
		is.setItalic(ps.getBoolean(pn(ix,"styleItalic")));
		is.setUnderlined(ps.getBoolean(pn(ix,"styleUnderline")));
		is.setStrikethru(ps.getBoolean(pn(ix,"styleStrikethru")));
		is.setFg(ps.getString(pn(ix,"styleFgRGB")));
		is.setBg(ps.getString(pn(ix,"styleBgRGB")));

		return is;
	}

	/**
	 * Return preference name for a style attribute
	 * @param ix: Unique index to hilite element map
	 * @param suffix suffix for style preference name
	 * @return ANTLRIDE_PREFERENCE_SYNTAX+ix+"."+suffix
	 */
	private String pn(Integer ix, String suffix) {
		return ANTLRIDE_PREFERENCE_SYNTAX+ix+"."+suffix;
	}

	/**
	 * Return preference name for a style attribute
	 * @param suffix suffix for style preference name
	 * @return ANTLRIDE_PREFERENCE_SYNTAX+"."+suffix
	 */
	private String pn(String suffix) {
		return ANTLRIDE_PREFERENCE_SYNTAX+"."+suffix;
	}
	
	private class MyStylePropertyChangeListener implements IPropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			String pref=((FieldEditor) event.getSource()).getPreferenceName();
			System.out.println("SyntaxColoringPreferencePage - MyStylePropertyChangeListener "
					+ " preference >" + pref +"<"
					+ " new " + event.getNewValue()
					+ " old " + event.getOldValue()
					);
			
			 if(pref.equals(pn("enabled"))) selectedItemStyle.setEnabled((Boolean) event.getNewValue());
		else if(pref.equals(pn("enabledBold"))) selectedItemStyle.setBold((Boolean) event.getNewValue());
		else if(pref.equals(pn("enabledItalic"))) selectedItemStyle.setItalic((Boolean) event.getNewValue());
		else if(pref.equals(pn("enabledUnderline"))) selectedItemStyle.setUnderlined((Boolean) event.getNewValue());
		else if(pref.equals(pn("enabledStrikethru"))) selectedItemStyle.setStrikethru((Boolean) event.getNewValue());
		else if(pref.equals(pn("enabledFgRgb"))) selectedItemStyle.setFg((RGB) event.getNewValue());
		else if(pref.equals(pn("enabledBgRgb"))) selectedItemStyle.setBg((RGB) event.getNewValue());
			 
        // tip from https://stackoverflow.com/questions/4368533/updating-swt-objects-from-another-thread
        // Update preview field with the changed attributes
		Display.getDefault().asyncExec(new Runnable() {
		    public void run() {
		        updatePreviewTextHighlight();
		    }
		});

		}
	}
	
}
